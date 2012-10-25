package Windows;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

public final class WindowsInfrastructureMethods {

	public static Point getCenterPoint(Dimension windowDimensions) {
		Point location = new Point();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		location.x = (screenSize.width / 2) - (windowDimensions.width / 2);
		location.y = (screenSize.height / 2) - (windowDimensions.height / 2);
				
		return location;
	}
}
