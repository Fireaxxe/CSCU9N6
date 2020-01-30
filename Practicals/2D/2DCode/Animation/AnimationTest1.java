import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class AnimationTest1 {

    public static void main(String args[]) {

        DisplayMode displayMode = new DisplayMode(1920,1080,16,60);
		// DisplayMode dm = new DisplayMode(1920,1080,16,DisplayMode.REFRESH_RATE_UNKNOWN);
        AnimationTest1 test = new AnimationTest1();
        test.run(displayMode);
    }

    private static final long DEMO_TIME = 5000;

    private SimpleScreenManager screen;
    private Image bgImage;
    private Animation anim;


    public void loadImages() {
        // load images
        bgImage = loadImage("images/background.jpg");
        Image player1 = loadImage("images/player1.png");
        Image player2 = loadImage("images/player2.png");
        Image player3 = loadImage("images/player3.png");

        // create animation
        anim = new Animation();
        anim.addFrame(player1, 250);
        anim.addFrame(player2, 150);
        anim.addFrame(player1, 150);
        anim.addFrame(player2, 150);
        anim.addFrame(player3, 200);
        anim.addFrame(player2, 150);
    }


    private Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }


    public void run(DisplayMode displayMode) {
        screen = new SimpleScreenManager();
        try {
            screen.setFullScreen(displayMode, new JFrame());
            loadImages();
            animationLoop();
        }
        finally {
            screen.restoreScreen();
        }
    }


    public void animationLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        while (currTime - startTime < DEMO_TIME) {
            long elapsedTime =
                System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // update animation
            anim.update(elapsedTime);

            // draw to screen
            Graphics g =
                screen.getFullScreenWindow().getGraphics();
            draw(g);
            g.dispose();

            // take a nap
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }
        }

    }


    public void draw(Graphics g) {

    	g.fillRect(0,0,1920,1080);

        // draw background
        g.drawImage(bgImage, 0, 0, null);

        // draw image
        g.drawImage(anim.getImage(), 0, 0, null);
    }

}