import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Articulate {

	private static Display display;
	private static Shell shell;
	private static Browser browser;
	private static JFrame frame;
	private static Process child;
	private static AudioEngine ae;

	private static final String URL = new File(".\\html\\articulate.html").getAbsolutePath();
	public static final String MP3 = "mp3";
	public static final String WAV = "wav";
	public static final String MIDI = "midi";

	public Articulate(Dimension size) {
		frame = new JFrame();
		frame.pack();

		display = new Display();
		shell = new Shell(display, SWT.NO_TRIM | SWT.ON_TOP);
		shell.setSize(size.width, size.height);
		shell.setLayout(new FillLayout());
		browser = new Browser(shell, SWT.NONE);

		browser.setUrl(URL);
		shell.setLocation(0, 0);
		shell.setFullScreen(true);
		shell.open();

		addHtmlListener(browser, "javabuttonclick");
		addConfigListener(browser, "obtainconfiguration");
		addReadyListener(browser, "ready");		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
	
	private static void addReadyListener(final Browser browser, String name) {
		final BrowserFunction function = new OnReady(browser, name);
		
		browser.addProgressListener(new ProgressAdapter() {
			public void completed(ProgressEvent event) {
				browser.addLocationListener(new LocationAdapter() {
					public void changed(LocationEvent event) {
						browser.removeLocationListener(this);
						function.dispose();
					}
				});
			}
		});
	}

	private static void addHtmlListener(final Browser browser, String name) {
		final BrowserFunction function = new StartFunction(browser, name);
		
		browser.addProgressListener(new ProgressAdapter() {
			public void completed(ProgressEvent event) {
				browser.addLocationListener(new LocationAdapter() {
					public void changed(LocationEvent event) {
						browser.removeLocationListener(this);
						function.dispose();
					}
				});
			}
		});
	}
	
	private static void addConfigListener(final Browser browser, String name) {
		final BrowserFunction function = new ObtainConfiguration(browser, name);
		
		browser.addProgressListener(new ProgressAdapter() {
			public void completed(ProgressEvent event) {
				browser.addLocationListener(new LocationAdapter() {
					public void changed(LocationEvent event) {
						browser.removeLocationListener(this);
						function.dispose();
					}
				});
			}
		});
	}
	
	private static class OnReady extends BrowserFunction {

		public OnReady(Browser browser, String name) {
			super(browser, name);
		}
		
		public Object function(Object[] arguments) {
			loadDefaultSounds();
			return null;
		}
		
		private void loadDefaultSounds() {
			File file = new File("Sounds\\");
			
			for (final File fileEntry : file.listFiles()) {
				Track t = new Track(fileEntry.getName(), fileEntry.getAbsolutePath());
				browser.execute("loadtrack(" + t.toString() + ");");
				System.out.println(fileEntry.getAbsolutePath());
			}
		}
	}
	
	private static class ObtainConfiguration extends BrowserFunction {

		public ObtainConfiguration(Browser browser, String name) {
			super(browser, name);
		}
		
		public Object function(Object[] arguments) {
			System.out.println("Configuration = " + arguments[0]);
			return null;
		}
	}
	
	private static class StartFunction extends BrowserFunction {
		JFileChooser ch;

		StartFunction(Browser browser, String name) {
			super(browser, name);
			ch = new JFileChooser();
			ch.setAcceptAllFileFilterUsed(true);
			ch.addChoosableFileFilter(new MusicFilter());
		}
		
		public Object function(Object[] arguments) {
			String btn = arguments[0].toString();
			System.out.println("Id: " + arguments[0]);

			if (btn.equals("start")) {
				start();
			} else if (btn.equals("upload")) {
				upload();
			} else if (btn.equals("stop")) {
				ae.dispose();
				child.destroy();
			} else if(btn.equals("exit")) {
				exit();
			}
			
			return null;
		}
		
		private void start(){
			String command = "pd AudioEngine/pdserver.pd";
			browser.execute("loaded()");
		    try {
				child = Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    Thread t = new Thread(){
		        public void run() {
		            ae = new AudioEngine();
				    ae.write("Test Data;");
		        }
		    };
		    t.start();
			//browser.execute("obtain()");
		}
		
		private void exit(){
			shell.close();
			try {
				if(child != null) {
					child.destroy();
				}
				ae.dispose();
			} catch(Exception e) {
				//meh
				//Don't know why we would be here
			} finally {
				System.exit(0);
			}
		}
		
		private void upload() {
			if (ch.isShowing()) {
				return;
			}
			frame.setAlwaysOnTop(true);
			frame.setSize(0, 0);
			frame.setVisible(true);

			int option = ch.showOpenDialog(frame);
			frame.setVisible(false);
			switch (option) {
			case JFileChooser.APPROVE_OPTION:
				Track track = new Track(ch.getSelectedFile().getName(), ch.getSelectedFile().getAbsolutePath());
				browser.execute("loadtrack(" + track.toString() + ");");
				break;
			case JFileChooser.CANCEL_OPTION:
				break;
			}
		}
	}

	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	private static class MusicFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}

			String extension = getExtension(f);

			if (extension != null) {
				if (extension.equals(MP3) || extension.equals(WAV)
						|| extension.equals(MIDI)) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

		@Override
		public String getDescription() {
			return "(.mp3, .wav, .midi) Audio Format Files";
		}
	}
	
	private static class Track {
		String name;
		String absolutelocation;
		
		public Track(String n, String a) {
			name = n;
			absolutelocation = a;
		}
		
		public String toString() {
			return "{ \"name\": \"" + name + "\", \"absolutelocation\":\"" + absolutelocation.replace("\\", "\\\\") + "\"}";
		}
	}
}
