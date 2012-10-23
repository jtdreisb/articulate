package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

import Windows.ArticuSoft;

public class BrowseActionListener implements ActionListener {
	private ArticuSoft mainframe;
	private JFileChooser filechooser;
	
	public BrowseActionListener(ArticuSoft frame) {
		mainframe = frame;
		filechooser = new JFileChooser();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		filechooser.showOpenDialog(mainframe);
	}

}
