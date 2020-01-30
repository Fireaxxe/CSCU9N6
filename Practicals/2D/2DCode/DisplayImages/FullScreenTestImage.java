import java.awt.*;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

public class FullScreenTestImage extends JFrame {

	private Image background;
	private Image pic;
	
    public static void main(String[] args) {      
        DisplayMode dm = new DisplayMode(1920,1080,16,60);
        FullScreenTestImage test = new FullScreenTestImage();
        test.go(dm);
    }

    public void go(DisplayMode displayMode) {

    	background = new ImageIcon("images/background.jpg").getImage();
    	pic = new ImageIcon("images/translucent.png").getImage();
    	
        SimpleScreenManager screen = new SimpleScreenManager();
        try {
            screen.setFullScreen(displayMode, this);
            try { Thread.sleep(5000); }
            catch (InterruptedException ex) { }
        }
        finally { screen.restoreScreen(); }
    }

    public void paint(Graphics g) {
    	g.drawImage(background,0,0,null);
    	g.drawImage(pic,20,20,null);
    	g.drawImage(pic,120,120,null);
    }
}
