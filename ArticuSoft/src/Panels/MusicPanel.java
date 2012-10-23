package Panels;

import java.awt.BorderLayout;
import java.awt.Panel;

import javax.swing.JButton;

public class MusicPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7132247492093531199L;

	public MusicPanel() {
		setLayout(new BorderLayout());
		
		JButton addNewTrackbtn = new JButton("Add new Sound");
		add(addNewTrackbtn, BorderLayout.NORTH);
	}
}
