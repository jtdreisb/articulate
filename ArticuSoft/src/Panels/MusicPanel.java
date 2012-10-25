package Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Windows.ArticuSoft;

import ActionListeners.BrowseActionListener;

public class MusicPanel extends Panel {
	private static final String[] tableColumns = {"#", "Track Name"};
	private static final long serialVersionUID = 7132247492093531199L;
	
	private ArticuSoft mainframe;
	
	private JButton addNewBtn;
	private JTable trackTable;
	private DefaultTableModel tableModel;
	
	private Object[][] tracks;
	
	private BrowseActionListener addBtnListener;

	public MusicPanel(ArticuSoft frame) {
		mainframe = frame;
		setLayout(new BorderLayout());
		
		addNewBtn = new JButton("Add new Sound");
		addBtnListener = new BrowseActionListener(mainframe);
		addNewBtn.addActionListener(addBtnListener);
		addNewBtn.setMaximumSize(new Dimension(50, 50));
		
		add(addNewBtn, BorderLayout.NORTH);
		
		tracks = new Object[][] { };
		tableModel = new DefaultTableModel(tracks, tableColumns);
		trackTable = new JTable(tableModel);
		trackTable.setSize(new Dimension(200, 100));
		
		Panel tablepanel = new Panel();
		tablepanel.setLayout(new BorderLayout());
		tablepanel.add(trackTable.getTableHeader(), BorderLayout.PAGE_START);
		tablepanel.add(trackTable, BorderLayout.CENTER);
		add(tablepanel, BorderLayout.CENTER);
	}
	
	public void addTrack(String file) {
		Object[] newRow = { 1, file };
		tableModel.addRow(newRow);
		trackTable.repaint();
	}
}
