package Panels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8816013753164036149L;
	
	private BufferedImage image;
	private String cwd = System.getProperty("user.dir");
	
	public ImagePanel() {
		System.out.println(cwd + "\\src\\Images\\articulate-side.png");
		try {
			image = ImageIO.read(new File(cwd + "\\src\\Images\\articulate-side.png"));
		} catch(IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image,  0,  0, null);
	}

}
