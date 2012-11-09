package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import Panels.MusicPanel;

public class BrowseActionListener implements ActionListener {
	private JPanel mainframe;
	private JFileChooser filechooser;
	
	public BrowseActionListener(JPanel frame) {
		mainframe = frame;
		filechooser = new JFileChooser();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int action = filechooser.showOpenDialog(mainframe);
		
		if(action == JFileChooser.APPROVE_OPTION) {
			((MusicPanel)mainframe).addTrack(filechooser.getSelectedFile().getAbsolutePath(), filechooser.getSelectedFile().getName());
			new PlayMusicThread(filechooser.getSelectedFile()).run();
		}
	}
}
