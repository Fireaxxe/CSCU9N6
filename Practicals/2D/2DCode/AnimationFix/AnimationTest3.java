import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class AnimationTest3 {

    public static void main(String args[]) {
        AnimationTest3 test = new AnimationTest3();
        test.run();
    }

    private static final DisplayMode POSSIBLE_MODES[] = {
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(640, 480, 16, 0)
    };

    private static final long DEMO_TIME = 10000;

    private ScreenManager screen;
    private Image bgImage;
    private Animation anim;
    private BufferedImage buffer=null;
    private Graphics2D bg=null;


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


    public void run() {
        screen = new ScreenManager();
        try {
 	       	DisplayMode displayMode = new DisplayMode(1920,1080,16,60);
            //    screen.findFirstCompatibleMode(POSSIBLE_MODES);
            screen.setFullScreen(displayMode);
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

//          If we havenÕt set up the buffer yet
            if (buffer == null)
            {
               // Create our own buffer
               buffer = new BufferedImage(screen.getWidth(), screen.getHeight(), BufferedImage.TYPE_INT_ARGB);
               bg = (Graphics2D)buffer.createGraphics();
            }

		    // draw to the buffer graphics, not the screen
            draw(bg);

            // Now get the screen graphics device
            Graphics2D g = screen.getGraphics();
            // and draw our completed image on that
            g.drawImage(buffer,0,0,null);
            screen.update();
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
