
// TimeBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Modified version, BPG 18-3-07

/* Update the alien sprite every timeDelay ms by calling
   its update() method.
   This class is the Java 3D version of a Timer.
*/

import java.util.Enumeration;
import javax.media.j3d.*;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;


public class TimeBehavior extends Behavior
{
  private final static double MOVERATE = 0.3;

  private TransformGroup objectTG;  // the object's TG
  private Transform3D t3d, toMove;  // used to affect viewerTG;
  private Vector3d currMove;	// current move

  private float collRad;	// radius for collision detection
  private Bounds obsBounds;	// bounds of obstacle

  private WakeupCondition timeOut;

  public TimeBehavior(int timeDelay, TransformGroup oTG, float orad, Bounds obsBnds)
  // oTG is transform group of object to be controlled
  // orad is radius of object (for collision detection)
  // obsBnds is bounds of obstacle (for collision detection)
  {
    objectTG = oTG;
    collRad = orad;
    obsBounds = obsBnds;
    t3d = new Transform3D();
    toMove = new Transform3D();

    currMove = new Vector3d(0, 0, -MOVERATE);

    timeOut = new WakeupOnElapsedTime(timeDelay);
  }

  public void initialize()
  { wakeupOn( timeOut );
  }

  public void processStimulus( Enumeration criteria )
  { // ignore criteria
    currMove = doMove( currMove );
    wakeupOn( timeOut );
  } // end of processStimulus()


  private Vector3d doMove(Vector3d theMove)
  // Move the sprite by the amount in theMove
  {
    objectTG.getTransform( t3d );
    toMove.setTranslation(theMove);    // overwrite previous trans
    t3d.mul(toMove);
    Vector3d trans = new Vector3d();
    t3d.get( trans );	// get translational component of transform
    Point3d newLoc = new Point3d( trans.x, trans.y, trans.z);	// next location
    BoundingSphere testBnds = new BoundingSphere(newLoc, collRad);
    // only allow move if does not intersect with obstacle
    if ( !obsBounds.intersect(testBnds) )
    	objectTG.setTransform(t3d);
    return theMove;
  }  // end of doMove()


}  // end of TimeBehavior class
