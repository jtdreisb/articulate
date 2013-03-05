import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

public class Main {

	private static Toolkit toolkit = Toolkit.getDefaultToolkit();
	private static Dimension dim = toolkit.getScreenSize();

	public static void main(String[] args) throws IOException, InterruptedException {
		new Articulate(dim);
	}
}