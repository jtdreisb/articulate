package ActionListeners;

import java.awt.FileDialog;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
		JFrame parent = (JFrame) SwingUtilities.getRoot((JButton) arg0.getSource());
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(parent);
	}

}
