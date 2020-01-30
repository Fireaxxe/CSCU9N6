import java.awt.*;

import game2D.*;

// GCTester demonstrates how we can override the GameCore class
// to create our own 'game'. We usually need to implement at
// least 'draw' and 'update' (not including any local event handling)
// to begin the process. You should also write your own 'init'
// method that will initialise event handlers etc. By default GameCore
// will handle the 'Escape' key to quit the game but you should
// override this with your own event handler.

public class GCTester extends GameCore {

    long total;         // Total time elapsed
 
    // The obligatory main method that creates
    // an instance of our GCTester class and
    // starts it running
    public static void main(String[] args) {
        
        GCTester gct = new GCTester();
        gct.init();
        // Start in windowed mode with a 800x600 screen
        gct.run(false,800,600);
    }

    // Initialise the class, e.g. set up variables
    // animations, register event handlers
    public void init()
    {
        total = 0;
    }

    // Draw the current frame
    public void draw(Graphics2D g)
    {
        // A simple demo - note that this is not 
        // very efficient since we fill the screen
        // on every frame.
        g.setColor(Color.black);
        g.fillRect(0,0,800,600);
        g.setColor(Color.yellow);
        g.drawString("Time Expired:" + total,30,50);
    }

    // Update any sprites and check for collisions
    public void update(long elapsed)
    {
        total += elapsed;
        if (total > 5000) stop();
    }
}
