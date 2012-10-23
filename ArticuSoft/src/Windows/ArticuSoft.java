package Windows;

import java.awt.Dimension;

import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import Panels.MusicPanel;
import Panels.PortsPanel;

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
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(DIMENSIONS);
		this.setLocation(WindowsInfrastructureMethods.getCenterPoint(DIMENSIONS));
		
		this.setTitle(TITLE);
		
		MenuBar windowMenu = new MenuBar();
		Menu homeMenu = new Menu("Home");
		MenuItem exitLink = new MenuItem("Exit");
		exitLink.addActionListener(new ExitActionListener());
		homeMenu.addSeparator();
		homeMenu.add(exitLink);
		
		windowMenu.add(homeMenu);
		this.setMenuBar(windowMenu);
		
		setLayout(new BorderLayout(10, 10));
		this.add(new PortsPanel(), BorderLayout.NORTH);
		this.add(new MusicPanel(this), BorderLayout.WEST);
	}
	
	private class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}
	}
}
