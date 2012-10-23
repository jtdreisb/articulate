package Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;

import Windows.ArticuSoft;

import ActionListeners.BrowseActionListener;

public class MusicPanel extends Panel {
	private static final String[] tableColumns = {"#", "Track Name"};
	private static final long serialVersionUID = 7132247492093531199L;
	
	private ArticuSoft mainframe;
	
	private JButton addNewBtn;
	private JTable trackTable;
	
	private Object[][] tracks;
	
	private BrowseActionListener addBtnListener;

	public MusicPanel(ArticuSoft frame) {
		mainframe = frame;
		setLayout(new GridLayout(3, 2));
		
		addNewBtn = new JButton("Add new Sound");
		addBtnListener = new BrowseActionListener(mainframe);
		addNewBtn.addActionListener(addBtnListener);
		addNewBtn.setSize(100, 50);
		
		add(addNewBtn);
		
		tracks = new Object[0][0];
		trackTable = new JTable(tracks, tableColumns);
		trackTable.setSize(new Dimension(200, 100));
		
		add(trackTable.getTableHeader());
		add(trackTable);
	}
}
