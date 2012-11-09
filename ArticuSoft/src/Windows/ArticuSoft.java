package Windows;

import gnu.io.CommPortIdentifier;

import java.awt.Dimension;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import Panels.ActionButtons;
import Panels.ConsolePanel;
import Panels.ImagePanel;
import Panels.MusicPanel;

public class ArticuSoft extends JFrame {

	/**
	 * Generated UID
	 */
	
	private static final String TITLE = "Articulate Wearable Dance Software";
	
	private static final long serialVersionUID = 6565821055076397067L;
	
	private Toolkit tk = Toolkit.getDefaultToolkit();
	
	/*
	 * Components 
	 */

	public ArticuSoft() {		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle(TITLE);
		this.setFullScreen();
		this.initializeMenuBar();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx = 0.1;
		c.weighty = 0.8;
		c.gridy = 0;
		c.gridx = 0;
		c.fill = GridBagConstraints.BOTH;
		ImagePanel imgpanel = new ImagePanel();
		imgpanel.setBorder(BorderFactory.createLoweredBevelBorder());
		this.add(imgpanel, c);
		c.weightx = 0.6;
		c.gridx = 1;
		this.add(new MusicPanel(), c);
		
		c.gridx = 2;
		this.add(new ConsolePanel(), c);
		
		c.gridy = 2;
		c.gridx = 0;
		c.gridwidth = 3;
		c.weighty = 0.1;
		this.add(new ActionButtons(this), c);
	}
	
	private class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}
	}
	
	private void setFullScreen() {
		int height, width;
		height = tk.getScreenSize().height;
		width = tk.getScreenSize().width;
		this.setSize(new Dimension(width, height));
	}
	
	private void initializeMenuBar() {
		//Menu Bar
		JMenuBar windowMenu = new JMenuBar();
		createHomeMenu(windowMenu);
		createToolsMenu(windowMenu);
	}
	
	private void createHomeMenu(JMenuBar windowMenu) {
		JMenu homeMenu = new JMenu("Home");
		
		//Home menu components
		JMenuItem exitLink = new JMenuItem("Exit");
		exitLink.addActionListener(new ExitActionListener());
		
		homeMenu.add(exitLink);			//Add Exit link
		
		windowMenu.add(homeMenu);
	}
	
	private void createToolsMenu(JMenuBar windowMenu) {
		JMenu toolsMenu = new JMenu("Tools");
		windowMenu.add(toolsMenu);
		
		JMenu serialPortMenu = new JMenu("Serial Port");
		ArrayList<JRadioButtonMenuItem> portItems = new ArrayList<JRadioButtonMenuItem>();
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> commPorts = CommPortIdentifier.getPortIdentifiers();
		ArrayList<CommPortIdentifier> ports = new ArrayList<CommPortIdentifier>();
		
		while(commPorts.hasMoreElements()) {
			CommPortIdentifier port = commPorts.nextElement();
			if(port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				ports.add(port);
			}
		}
		
		ButtonGroup group = new ButtonGroup();
		
		for(CommPortIdentifier port : ports) {
			portItems.add(new JRadioButtonMenuItem(port.getName()));
		}
		
		if(portItems.size() > 0) {
			portItems.get(0).setSelected(true);
		} else {
			portItems.add(new JRadioButtonMenuItem("(none)"));
		}
		
		for(JRadioButtonMenuItem port : portItems) {
			group.add(port);
			serialPortMenu.add(port);
		}
		
		toolsMenu.add(serialPortMenu);
		this.setJMenuBar(windowMenu);
	}
}
