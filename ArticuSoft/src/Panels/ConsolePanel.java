package Panels;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ConsolePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7393015714803395708L;

	public ConsolePanel() {
		JTextArea console = new JTextArea(45, 40);
		console.setBackground(Color.BLACK);
		console.setForeground(Color.green);
		add(console);
	}
}
