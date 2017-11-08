import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.awt.Robot;

public class TakeScreenshot {

	public static void main (String args[]) throws Exception {
		Thread.sleep(2000);
		BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(image, "png", new File("screenshot"+System.currentTimeMillis()+".png"));
	}
}