import java.awt.*;
import javax.swing.JFrame;

public class FullScreenTest extends JFrame {

    public static void main(String[] args) {      
        DisplayMode dm = new DisplayMode(1920,1080,16,60);
        FullScreenTest test = new FullScreenTest();
        test.go(dm);
    }

    public void go(DisplayMode displayMode) {
        setBackground(Color.blue);
        setForeground(Color.white);
        setFont(new Font("Dialog", 0, 24));

        SimpleScreenManager screen = new SimpleScreenManager();
        try {
            screen.setFullScreen(displayMode, this);
            try { Thread.sleep(5000); }
            catch (InterruptedException ex) { }
        }
        finally { screen.restoreScreen(); }
    }

    public void paint(Graphics g) {
    	g.drawString("Hello World!", 20, 50);
    	g.drawOval(100,100,30,40);
    }
}
