import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;

public class MoveSprite implements KeyListener {

    public static void main(String args[]) {
        MoveSprite test = new MoveSprite();
        test.run();
    }

    private static final long DEMO_TIME = 10000;

    private ScreenManager screen;
    private Image bgImage;
    private Sprite sprite;
    private boolean stop;

    public void loadImages() {
        // load images
        bgImage = loadImage("images/background.jpg");
        Image player1 = loadImage("images/player1.png");
        Image player2 = loadImage("images/player2.png");
        Image player3 = loadImage("images/player3.png");

        // create sprite
        Animation anim = new Animation();
        anim.addFrame(player1, 250);
        anim.addFrame(player2, 150);
        anim.addFrame(player1, 150);
        anim.addFrame(player2, 150);
        anim.addFrame(player3, 200);
        anim.addFrame(player2, 150);
        sprite = new Sprite(anim);

        // start the sprite off moving down and to the right
        sprite.setVelocityX(0.0f);
        sprite.setVelocityY(0.0f);
    }


    private Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }


    public void run() {
        screen = new ScreenManager();
        
        try {
            DisplayMode displayMode = new DisplayMode(1920,1080,16,60);
            screen.setFullScreen(displayMode);
   	  		
   	  		Window win = screen.getFullScreenWindow();
		    win.addKeyListener(this);			   
            
            loadImages();
            animationLoop();
        }
        finally {
            screen.restoreScreen();
        }
    }


    public void animationLoop() {
        
        long currTime = System.currentTimeMillis();
        
        stop = false;

        while (!stop) {
            long elapsedTime = System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // update the sprites
            update(elapsedTime);

            // draw and update the screen
            Graphics2D g = screen.getGraphics();
            draw(g);
            g.dispose();
            screen.update();

            // take a nap
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }
        }
    }


    public void update(long elapsedTime) {
        // check sprite bounds
        if (sprite.getX() < 0) {
            sprite.setVelocityX(Math.abs(sprite.getVelocityX()));
        }
        else if (sprite.getX() + sprite.getWidth() >= screen.getWidth())
        {
            sprite.setVelocityX(-Math.abs(sprite.getVelocityX()));
        }
        if (sprite.getY() < 0) {
            sprite.setVelocityY(Math.abs(sprite.getVelocityY()));
        }
        else if (sprite.getY() + sprite.getHeight() >= screen.getHeight())
        {
            sprite.setVelocityY(-Math.abs(sprite.getVelocityY()));
        }

        // update sprite
        sprite.update(elapsedTime);
    }


    public void draw(Graphics g) {
        // draw background
        // g.drawImage(bgImage, 0, 0, null);

		// Fills background with current foreground colour
		g.fillRect(0,0,screen.getWidth(),screen.getHeight());

        // draw sprite
        g.drawImage(sprite.getImage(),
            Math.round(sprite.getX()),
            Math.round(sprite.getY()),
            null);
    }
    
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // exit the program
        if (keyCode == KeyEvent.VK_ESCAPE) stop = true;
        if (keyCode == KeyEvent.VK_RIGHT) sprite.setVelocityX(sprite.getVelocityX()+0.1f);
        
        e.consume();
    }


    public void keyReleased(KeyEvent e) { e.consume(); }

    public void keyTyped(KeyEvent e) { e.consume(); }

    

}
