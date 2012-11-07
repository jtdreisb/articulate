package Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import ActionListeners.BrowseActionListener;

public class MusicPanel extends JPanel {
	private static final String[] tableColumns = {"#", "Track Name"};
	private static final long serialVersionUID = 7132247492093531199L;
	
	private JButton addNewBtn;
	private JTable trackTable;
	private DefaultTableModel tableModel;
	
	private Object[][] tracks;
	
	private BrowseActionListener addBtnListener;

	public MusicPanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx = 0.5;
		c.weighty = 0.2;
		c.gridy = 0;
		
		JLabel title = new JLabel("Sound Controls");
		title.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		//title.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));
		add(title, c);
		
		//c.weighty = 0.5;
		c.gridy = 1;
		addNewBtn = new JButton("Add new Sound");
		addBtnListener = new BrowseActionListener(this);
		addNewBtn.addActionListener(addBtnListener);
		addNewBtn.setMaximumSize(new Dimension(50, 50));
		
		add(addNewBtn, c);
		
		//c.weighty = 1;
		c.gridy = 2;
		c.gridheight = 2;
		
		tracks = new Object[][] { };
		tableModel = new DefaultTableModel(tracks, tableColumns);
		trackTable = new JTable(tableModel);
		
		JPanel tablepanel = new JPanel(new BorderLayout());
		tablepanel.add(trackTable.getTableHeader(), BorderLayout.PAGE_START);
		tablepanel.add(trackTable, BorderLayout.CENTER);
		add(tablepanel, c);
	}
	
	public void addTrack(String file) {
		Object[] newRow = { 1, file };
		tableModel.addRow(newRow);
		trackTable.repaint();
	}
}
