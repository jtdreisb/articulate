import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import gnu.io.*;

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
	private static Process child;
	private static AudioEngine ae;

	private static final String URL = new File(".\\html\\articulate.html").getAbsolutePath();
	public static final String MP3 = "mp3";
	public static final String WAV = "wav";
	public static final String MIDI = "midi";
	
	public static boolean isReady = false;
	
	public static ArrayList<AudioEngineConfiguration> configurations;

	public Articulate(Dimension size) {		
		configurations = new ArrayList<AudioEngineConfiguration>();
		
		display = new Display();
		shell = new Shell(display, SWT.NO_TRIM | SWT.ON_TOP);
		shell.setSize(size.width, size.height);
		shell.setLayout(new FillLayout());
		browser = new Browser(shell, SWT.NONE);

		browser.setUrl(URL);
		shell.setLocation(0, 0);
		shell.setFullScreen(true);
		browser.setJavascriptEnabled(true);
		shell.open();

		addHtmlListener(browser, "javabuttonclick");
		addConfigListener(browser, "sendconfig");
		addReadyListener(browser, "ready");
		//addViewConfigListener(browser, "viewconfig");
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
	
	/*private static void addViewConfigListener(final Browser browser, String name) {
final BrowserFunction function = new ConfigListener(browser, name);
		
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
	
	private static class ConfigListener extends BrowserFunction {
		public ConfigListener(Browser browser, String name) {
			super(browser, name);
		}
		
		public Object function(Object[] arguments) {
			String obj = "[";
			
			for(int n = 0; n < configurations.size(); n++) {
				obj += configurations.get(n).toString();
				
				if(n < configurations.size() - 1) {
					obj += ",";
				}
			}
			
			obj += "]";
			
			browser.execute("showconfigs('" + obj + "');");
			
			return null;
		}
	}*/
	
	private static class OnReady extends BrowserFunction {

		public OnReady(Browser browser, String name) {
			super(browser, name);
		}
		
		public Object function(Object[] arguments) {
			if(!isReady) {
				loadDefaultSounds();
			}
			
			Enumeration<?> portlist = CommPortIdentifier.getPortIdentifiers();
			CommPortIdentifier portid;
			while(portlist.hasMoreElements()) {
				portid = (CommPortIdentifier) portlist.nextElement();
				
				if(portid.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					browser.execute("addport('" + portid.getName() + "');");
				}
			}
			
			isReady = true;
			return null;
		}
		
		private void loadDefaultSounds() {
			File file = new File("Sounds\\");
			
			for (final File fileEntry : file.listFiles()) {
				Track t = new Track(fileEntry.getName(), fileEntry.getAbsolutePath());
				browser.execute("loadtrack(" + t.toString() + ");");
			}
		}
	}
	
	private static class ObtainConfiguration extends BrowserFunction {
		public ObtainConfiguration(Browser browser, String name) {
			super(browser, name);
		}
		
		public Object function(Object[] arguments) {
			Handstate left = Handstate.values()[Integer.parseInt((String)arguments[0])];
			Handstate right = Handstate.values()[Integer.parseInt((String)arguments[1])];
			String audio = (String) arguments[2];
			Mode mode = Mode.ENTER;
			
			if(!audio.equals("-1")) {
				AudioEngine.SoundConfiguration sc = new AudioEngine.SoundConfiguration(left, right, mode, audio);
				configurations.add(sc);
			}
			
			String lefteffect = (String) arguments[4];
			String righteffect = (String) arguments[6];
			
			if(!lefteffect.equals("-1")) {
				String lefteffectname = getEffectName(lefteffect);
				int lefteffectvalue = getEffectValue(lefteffect);
				AccelerometerDirection leftdir = AccelerometerDirection.valueOf((String) arguments[3]);
				AudioEngine.EffectConfiguration lec = new AudioEngine.EffectConfiguration(right, left, Sensor.ACC, Side.L, leftdir, lefteffectname + " " + lefteffectvalue);
				configurations.add(lec);
			}
			
			if(!righteffect.equals("-1")) {
				String righteffectname = getEffectName(righteffect);
				int righteffectvalue = getEffectValue(righteffect);
				AccelerometerDirection rightdir = AccelerometerDirection.valueOf((String) arguments[5]);
				AudioEngine.EffectConfiguration rec = new AudioEngine.EffectConfiguration(right, left, Sensor.ACC, Side.R, rightdir, righteffectname + " " + righteffectvalue);
				configurations.add(rec);
			}
			
			return null;
		}
		
		private String getEffectName(String name) {
			return name.split("-")[1];
		}
		
		private int getEffectValue(String effect) {
			int value = -1;
			String effectname = getEffectName(effect);
			effect = effect.split("-")[2];
			
			if(effectname.equals("BitCrush")) {
				if(effect.equals("bitdepth")) value = 0;
				if(effect.equals("samplerate")) value = 1;
				if(effect.equals("gain")) value = 2;
			} else if(effectname.equals("Vibrato")) {
				if(effect.equals("speed")) value = 0;
				if(effect.equals("depth")) value = 1;
				if(effect.equals("feedback")) value = 2;
				if(effect.equals("gain")) value = 3;
			} else if(effectname.equals("PitchShift")) {
				if(effect.equals("transpose")) value = 0;
				if(effect.equals("window")) value = 1;
				if(effect.equals("delay")) value = 2;
				if(effect.equals("gain")) value = 3;
			} else if(effectname.equals("Reverb")) {
				if(effect.equals("dry")) value = 0;
				if(effect.equals("wet")) value = 1;
				if(effect.equals("time")) value = 2;
				if(effect.equals("gain")) value = 3;
			}
			
			return value;
		}
	}
	
	private static class StartFunction extends BrowserFunction {
		StartFunction(Browser browser, String name) {
			super(browser, name);
		}
		
		public Object function(Object[] arguments) {
			String btn = arguments[0].toString();
			System.out.println("Id: " + arguments[0]);

			if (btn.equals("start")) {
				if(!((String)arguments[2]).equals("-1")) {
					AudioEngine.LoadConfiguration lc = new AudioEngine.LoadConfiguration((String) arguments[2]);
					configurations.add(lc);
				}
				start((String) arguments[1]);
			}else if (btn.equals("stop")) {
				ae.dispose();
				child.destroy();
			} else if(btn.equals("exit")) {
				exit();
			}
			
			return null;
		}
		
		private void start(String port){
			String command = "pd AudioEngine/pdserver.pd " + port;
			browser.execute("loaded()");
		    try {
				child = Runtime.getRuntime().exec(command);
				Thread.sleep(1000);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    Thread t = new Thread(){
		        public void run() {
		            ae = new AudioEngine();
		            for(AudioEngineConfiguration aec : configurations) {
		            	ae.write(aec.toString());
		            }
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