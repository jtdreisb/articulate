package Panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import ActionListeners.BrowseActionListener;

public class MusicPanel extends JPanel {
	private static final String[] tableColumns = {"#", "Track Name", "Function"};
	private static final long serialVersionUID = 7132247492093531199L;
	
	private JButton addNewBtn;
	private JTable trackTable;
	private DefaultTableModel tableModel;
	
	private Object[][] tracks;
	
	private BrowseActionListener addBtnListener;

	@SuppressWarnings("serial")
	public MusicPanel() {
		this.setBackground(Color.LIGHT_GRAY);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.weightx = 0.5;
		c.weighty = 0.1;
		c.gridy = 0;
		
		JLabel title = new JLabel("Sound Controls");
		title.setFont(new Font("Segoe UI", Font.PLAIN, 26));
		//title.setBorder(BorderFactory.createEmptyBorder(30, 20, 0, 20));
		add(title, c);
		
		c.weighty = 0.1;
		c.gridy = 1;
		addNewBtn = createSimpleButton("Add new Sound");
		addBtnListener = new BrowseActionListener(this);
		addNewBtn.addActionListener(addBtnListener);
		addNewBtn.setMaximumSize(new Dimension(50, 50));
		
		add(addNewBtn, c);
		
		c.weighty = 0.5;
		c.fill = GridBagConstraints.BOTH;
		c.gridy = 2;
		
		tracks = new Object[][] { };
		tableModel = new DefaultTableModel();
		for(String col : tableColumns) {
			tableModel.addColumn(col);
		}
		
		trackTable = new JTable(tableModel){
			  public boolean isCellEditable(int rowIndex, int colIndex) {
				  return false; //Disallow the editing of any cell
			  }
		};
		
		JPanel tablepanel = new JPanel(new BorderLayout());
		tablepanel.add(trackTable.getTableHeader(), BorderLayout.PAGE_START);
		tablepanel.add(trackTable, BorderLayout.CENTER);
		add(tablepanel, c);
	}
	
	private static JButton createSimpleButton(String text) {
		  JButton button = new JButton(text);
		  button.setForeground(Color.BLACK);
		  button.setBackground(Color.WHITE);
		  Border line = new LineBorder(Color.BLACK);
		  Border margin = new EmptyBorder(5, 15, 5, 15);
		  Border compound = new CompoundBorder(line, margin);
		  button.setBorder(compound);
		  return button;
		}
	
	public void addTrack(String filelocation, String file) {
		Object[] newRow = { tableModel.getRowCount() + 1, file };
		tableModel.addRow(newRow);
		trackTable.repaint();
	}
}
