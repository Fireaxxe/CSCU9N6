
// LaserShot.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Modified BPG 19-4-07
//   - code in makeBeam() needs to be completed.

/*  Creates the scene subgraph:

      beamTG --> beamSW --> beamDir TG --> beam cylinder
                       |
                        --> explShape (explosion anim)

    The beamTG is for moving the beam and explosion. The Switch is
    for visibility (nothing, beam, or explosion), and the beamDir TG
    is for tweaking the position of the cylinder and rotating it.

    The KeyBehavior object calls fireBeam() in AmmoManager, which
    then calls requestFiring() in each of its LaserShot objects.

    If the LaserShot is not in use (inUse == false) then LaserShot starts
    an AnimBeam thread which calls LaserShot's moveBeam(). inUse is set
    to true.

    moveBeam() incrementally moves the beam forward, starting from the
    current viewer position (steerTG). If the beam gets close
    to the target then the explosion is shown, otherwise the beam disappears
    after reaching a ceratin MAX_RANGE from the gun. inUse is set to false
    again.
*/

import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;
import javax.vecmath.*;


public class LaserShot
{
  private static final double MAX_RANGE = 6.0;  // max distance travelled
  private static final double STEP = 1.0;       // distance travelled each increment
  private static final Vector3d INCR_VEC = new Vector3d(0, 0, -STEP);
  private static final long SLEEP_TIME = 100;   // delay between moves of beam
  private static final double HIT_RANGE = 1.0;  // how close to count as a hit


  private TransformGroup steerTG;   // the TG for the user's viewpoint

  private TransformGroup beamTG;    // moving the beam/explosion
  private Switch beamSW;            // for beam/explosion visibility
  private Cylinder beam;            // the beam is a cylinder
  private ImageCsSeries explShape;  // holds the explosion

  private boolean inUse;        // whether beam is in flight or not
  private Vector3d targetVec;   // location of target

  // for repeated calcs
  private Transform3D tempT3d = new Transform3D();
  private Transform3D toMove = new Transform3D();
  private Transform3D localT3d = new Transform3D();
  private Vector3d currVec = new Vector3d();



  public LaserShot(TransformGroup steerTG,
				ImageComponent2D[] exploIms, Vector3d tarVec)
  {
    this.steerTG = steerTG;
    makeBeam(exploIms);
    targetVec = tarVec;
    inUse = false;
   }  // end of LaserShot()


  private void makeBeam(ImageComponent2D[] exploIms)
  // Create the beam and explosion subgraph detailed above.
  {
    // beam position
    Transform3D beamT3d = new Transform3D();
    beamT3d.rotX(Math.PI/2);   // 90 degrees, so pointing into scene
    beamT3d.setTranslation(new Vector3d(0, -0.3, -0.25));  // down and in
    TransformGroup beamDir = new TransformGroup();
    beamDir.setTransform(beamT3d);

    // code to complete the Beam scene graph required here (BPG 19-4-07)
    beamTG = new TransformGroup();

  } // end of makeBeam()


  private Appearance makeRedApp()
  {
    Color3f medRed = new Color3f(0.8f, 0.4f, 0.3f);
    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
    Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);

    Material redMat = new Material(medRed, black, medRed, specular, 80.0f);
    redMat.setLightingEnable(true);

    Appearance redApp = new Appearance();
    redApp.setMaterial(redMat);
    return redApp;
  } // end of makeRedApp()


  public TransformGroup getTG()
  // accessed by AmmoManager
  {  return beamTG;  }



  // ------------------------ shot methods ----------------


  public boolean requestFiring()
  // If the beam is not being used then fire it
  { if (inUse)
      return false;
    else {
      inUse = true;
      new AnimBeam(this).start(); // calls moveBeam() inside a thread
      return true;
    }
  }  // end of fireBeam()


  public void moveBeam()
  /* Move the beam with a delay between each move.
     If the beam gets close to the target then show the explosion
     otherwise the beam disappears when it reaches MAX_RANGE
  */
  { // place the beam at the current gun hand position
    steerTG.getTransform( tempT3d );
    beamTG.setTransform( tempT3d);
    showBeam(true);

    double currDist = 0.0;
    boolean hitTarget = closeToTarget();
    while ((currDist < MAX_RANGE) && (!hitTarget)) {
      doMove(INCR_VEC);
      hitTarget = closeToTarget();
      currDist += STEP;
      try {
        Thread.sleep(SLEEP_TIME);
      }
      catch (Exception ex) {}
    }

    showBeam(false);     // make beam invisible
    if (hitTarget)
      showExplosion();   // if a hit, show explosion
    inUse = false;       // shot is finished
  }  // end of moveBeam()


  private void doMove(Vector3d mv)
  {
    beamTG.getTransform( tempT3d );
    toMove.setTranslation( mv );
    tempT3d.mul(toMove);
    beamTG.setTransform( tempT3d );
  } // end of doMove()


  private boolean closeToTarget()
  /* The beam is close if its current position (currVec)
     is a short distance from the target position (targetVec).
  */
  {
    beam.getLocalToVworld( localT3d );    // get beam's TG in world coords
    localT3d.get(currVec);                // get (x,y,z) component

    currVec.sub(targetVec);    // distance between two positions
    double sqLen = currVec.lengthSquared();
    if (sqLen < HIT_RANGE*HIT_RANGE)
      return true;
    return false;
  }  // end of closeToTarget()


  private void showBeam(boolean toVisible)
  // make beam visible/invisible
  {
    if (toVisible)
      beamSW.setWhichChild(0);   // make visible
    else
      beamSW.setWhichChild( Switch.CHILD_NONE );   // make invisible
  }  // end of showBeam()


  private void showExplosion()
  // start the explosion
  {
    beamSW.setWhichChild(1);   // make visible
    explShape.showSeries();    // boom!
    beamSW.setWhichChild( Switch.CHILD_NONE );   // make invisible
  }  // end of showExplosion()


}  // end of LaserShot class
