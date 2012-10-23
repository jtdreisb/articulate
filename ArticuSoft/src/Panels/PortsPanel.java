package Panels;

import gnu.io.CommPortIdentifier;

import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JComboBox;

public class PortsPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2240444273271524543L;
	
	public PortsPanel() {
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> commPorts = CommPortIdentifier.getPortIdentifiers();
		ArrayList<CommPortIdentifier> ports = new ArrayList<CommPortIdentifier>();
		
		while(commPorts.hasMoreElements()) {
			CommPortIdentifier port = commPorts.nextElement();
			if(port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				ports.add(port);
			}
		}
		
		int n = 0;
		String[] portsStr = new String[ports.size()];
		for(CommPortIdentifier port : ports) {
			portsStr[n++] = port.getName();
		}
		
		setLayout(new FlowLayout());
		
		Label connection1 = new Label("Wearable suit on port ");
		JComboBox portCombo = new JComboBox(portsStr);
		Label connection2 = new Label(" is ");
		Label connectionState = new Label("Connected!");
		
		add(connection1);
		add(portCombo);
		add(connection2);
		add(connectionState);
	}

}
