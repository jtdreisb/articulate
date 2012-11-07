package Panels;

import java.awt.Panel;

import javax.swing.JButton;

import Windows.ArticuSoft;

public class ActionButtons extends Panel {
	private static final long serialVersionUID = 5910466852981502089L;
	
	private ArticuSoft mainframe;
	
	private JButton startBtn;
	private JButton stopBtn;
	
	public ActionButtons(ArticuSoft frame) {
		mainframe = frame;
		
		startBtn = new JButton("Start");
		stopBtn = new JButton("Stop");
		
		add(startBtn);
		add(stopBtn);
		
		mainframe.repaint();
	}
}
