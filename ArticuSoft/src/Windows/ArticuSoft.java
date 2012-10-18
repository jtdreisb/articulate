package Windows;

import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class ArticuSoft extends JFrame {

	/**
	 * Generated UID
	 */
	
	private static final String TITLE = "Articulate Wearable Dance Software";
	
	private static final int WINDOW_HEIGHT = 600;
	private static final int WINDOW_WIDTH = 600;
	private static final Dimension DIMENSIONS = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
	
	private static final long serialVersionUID = 6565821055076397067L;
	
	/*
	 * Components 
	 */

	public ArticuSoft() {
		this.setSize(DIMENSIONS);
		this.setLocation(WindowsInfrastructureMethods.getCenterPoint(DIMENSIONS));
		
		this.setTitle(TITLE);
		
		MenuBar windowMenu = new MenuBar();
		Menu homeMenu = new Menu("Home");
		MenuItem exitLink = new MenuItem("Exit");
		exitLink.addActionListener(new ExitActionListener());
		homeMenu.add(new MenuItem("Exit"));
		
		windowMenu.add(homeMenu);
		this.setMenuBar(windowMenu);
	}
	
	private class ExitActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("Action Performed");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			System.exit(0);
		}
		
	}
}
