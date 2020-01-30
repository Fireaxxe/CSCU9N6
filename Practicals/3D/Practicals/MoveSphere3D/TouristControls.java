
// TouristControls.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Stripped-down version, BPG 13-3-07

/* Arrow keys to move and rotate 3D sprite.

   We have restricted the functionality to XZ plane movement
   and Y rotation in the sprite, so not all movements
   and rotations need to have keys here.

*/

import java.awt.event.*;
import java.awt.AWTEvent;
import java.util.Enumeration;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.text.DecimalFormat;


public class TouristControls extends Behavior
{
  private WakeupCondition keyPress;

  private final static int forwardKey = KeyEvent.VK_DOWN;
  private final static int backKey = KeyEvent.VK_UP;
  private final static int leftKey = KeyEvent.VK_LEFT;
  private final static int rightKey = KeyEvent.VK_RIGHT;

  private final static double MOVERATE = 0.3;

  private TransformGroup objectTG;  // the object's TG
  private Transform3D t3d, toMove;  // used to affect viewerTG;

  private float collRad;	// radius for collision detection
  private Bounds obsBounds;	// bounds of obstacle


  public TouristControls(TransformGroup oTG)
  // oTG is transform group of object to be controlled
  {
    objectTG = oTG;	// store local reference to transform group
    t3d = new Transform3D();
    toMove = new Transform3D();

    keyPress = new WakeupOnAWTEvent( KeyEvent.KEY_PRESSED );
  } // end of TouristControls()


  public void initialize( )
  {
    wakeupOn( keyPress );
  }

  public void processStimulus(Enumeration criteria)
  {
    WakeupCriterion wakeup;
    AWTEvent[] event;

    while( criteria.hasMoreElements() ) {
      wakeup = (WakeupCriterion) criteria.nextElement();
      if( wakeup instanceof WakeupOnAWTEvent ) {
        event = ((WakeupOnAWTEvent)wakeup).getAWTEvent();
        for( int i = 0; i < event.length; i++ ) {
          if( event[i].getID() == KeyEvent.KEY_PRESSED )
            processKeyEvent((KeyEvent)event[i]);
        }
      }
    }
    wakeupOn( keyPress );
  } // end of processStimulus()


  private void processKeyEvent(KeyEvent eventKey)
  {
    int keyCode = eventKey.getKeyCode();

    if(keyCode == forwardKey )
      doMove( new Vector3d(0, 0, MOVERATE) );

  } // end of processKeyEvent()


  private void doMove(Vector3d theMove)
  // Move the sprite by the amount in theMove
  {
    objectTG.getTransform( t3d );
    toMove.setTranslation(theMove);    // overwrite previous trans
    t3d.mul(toMove);
    objectTG.setTransform(t3d);		// make the move
  }  // end of doMove()

} // end of TouristControls class
