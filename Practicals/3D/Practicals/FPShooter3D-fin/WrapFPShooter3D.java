
// WrapFPShooter3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Modified BPG 19-4-07
//   - code from TexturedPlane incorporated explicitly into gunHand()

/* A FPS example in the checkerboard world.

  The target is loaded using PropManager in createSceneGraph().

  initUserControls() does most of the FPS-specific stuff:
    * sets up the user's gun-in-hand image
    * moves the user's viewpoint
    * calls AmmoManager to create the beams and explosions
    * creates a KeyBehavior object to process keyboard input
*/

import javax.swing.*;
import java.awt.*;

import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;



public class WrapFPShooter3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
  private static final int PWIDTH = 512;   // size of panel
  private static final int PHEIGHT = 512;
  // info used to position initial viewpoint
  private final static double Z_START = 9.0;

  private static final int BOUNDSIZE = 100;  // larger than world

  private static final String TARGET = "Coolrobo.3ds";  // 3DS robot
  private static final String GUN_PIC = "images/gunDoom.gif";


  private SimpleUniverse su;
  private BranchGroup sceneBG;
  private BoundingSphere bounds;   // for environment nodes


  public WrapFPShooter3D()
  // construct the 3D canvas
  {
    setLayout( new BorderLayout() );
    setOpaque( false );
    setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

    GraphicsConfiguration config =
					SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);

    canvas3D.setFocusable(true);
    canvas3D.requestFocus();    // the canvas now has focus, so receives key events

    su = new SimpleUniverse(canvas3D);

    createSceneGraph();
    su.addBranchGraph( sceneBG );
  } // end of WrapFPShooter3D()


  private void createSceneGraph()
  // initilise the scene
  {
    sceneBG = new BranchGroup();
    bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE);

    lightScene();         // add the lights
    addBackground();      // add the sky
    sceneBG.addChild( new CheckerFloor().getBG() );  // add the floor

    // add the target
    PropManager propMan = new PropManager(TARGET, true);
    sceneBG.addChild( propMan.getTG() );
    Vector3d targetVec = propMan.getLoc();
    System.out.println("Location of target: " + targetVec );

    initUserControls(targetVec);

    sceneBG.compile();   // fix the scene
  } // end of createSceneGraph()



  private void lightScene()
  /* One ambient light, 2 directional lights */
  {
    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    // Set up the ambient light
    AmbientLight ambientLightNode = new AmbientLight(white);
    ambientLightNode.setInfluencingBounds(bounds);
    sceneBG.addChild(ambientLightNode);

    // Set up the directional lights
    Vector3f light1Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);
       // left, down, backwards
    Vector3f light2Direction  = new Vector3f(1.0f, -1.0f, 1.0f);
       // right, down, forwards

    DirectionalLight light1 =
            new DirectionalLight(white, light1Direction);
    light1.setInfluencingBounds(bounds);
    sceneBG.addChild(light1);

    DirectionalLight light2 =
        new DirectionalLight(white, light2Direction);
    light2.setInfluencingBounds(bounds);
    sceneBG.addChild(light2);
  }  // end of lightScene()


  private void addBackground()
  // A blue sky
  { Background back = new Background();
    back.setApplicationBounds( bounds );
    back.setColor(0.17f, 0.65f, 0.92f);    // sky colour
    sceneBG.addChild( back );
  } // end of addBackground()



// --------------------- user controls ----------------------------------


  private void initUserControls(Vector3d targetVec)
  /* Set up the user's gun-in-hand image. Fix the viewpoint.
     Call AmmoManager to create the beams and explosions.
     Create a KeyBehavior object to process keyboard input.
  */
  {
    // add a 'gun in hand' image to the viewpoint
    ViewingPlatform vp = su.getViewingPlatform();
    PlatformGeometry pg = gunHand();
    vp.setPlatformGeometry(pg);

    // position viewpoint
    TransformGroup steerTG = vp.getViewPlatformTransform();
    Transform3D t3d = new Transform3D();
    steerTG.getTransform( t3d );
    t3d.setTranslation( new Vector3d(0, 1, Z_START) );
    steerTG.setTransform(t3d);

    // create ammo (beams and explosions)
    AmmoManager ammoMan = new AmmoManager(steerTG, sceneBG, targetVec);

    // set up keyboard controls
    KeyBehavior keyBeh = new KeyBehavior( ammoMan );
         // keyBeh can ask the ammoManager to fire a beam
    keyBeh.setSchedulingBounds(bounds);
    vp.setViewPlatformBehavior(keyBeh);
  }  // end of initUserControls()


  private PlatformGeometry gunHand()
  // Add a 'gun in hand' image to the platform geometry
  {
    PlatformGeometry pg = new PlatformGeometry();

    // create geometry to contain gun image

    Point3f p1 = new Point3f(-0.1f, -0.3f, -0.7f);
    Point3f p2 = new Point3f(0.1f, -0.3f, -0.7f);
    Point3f p3 = new Point3f(0.1f, -0.1f, -0.7f);
    Point3f p4 = new Point3f(-0.1f, -0.1f, -0.7f);

    QuadArray plane = new QuadArray(4,
			GeometryArray.COORDINATES |
			GeometryArray.TEXTURE_COORDINATE_2 );

    // anti-clockwise from bottom left
    plane.setCoordinate(0, p1);
    plane.setCoordinate(1, p2);
    plane.setCoordinate(2, p3);
    plane.setCoordinate(3, p4);

    TexCoord2f q = new TexCoord2f();
    q.set(0.0f, 0.0f);
    plane.setTextureCoordinate(0, 0, q);
    q.set(1.0f, 0.0f);
    plane.setTextureCoordinate(0, 1, q);
    q.set(1.0f, 1.0f);
    plane.setTextureCoordinate(0, 2, q);
    q.set(0.0f, 1.0f);
    plane.setTextureCoordinate(0, 3, q);

    Shape3D gun = new Shape3D();
    gun.setGeometry(plane);

    // texture the geometry with the gun image
    TextureLoader loader = new TextureLoader(GUN_PIC, this);
    Appearance app = new Appearance();
    app.setTexture(loader.getTexture());

    // blended transparency so texture can be irregular
    TransparencyAttributes tra = new TransparencyAttributes();
    tra.setTransparencyMode( TransparencyAttributes.BLENDED );
    app.setTransparencyAttributes( tra );

    gun.setAppearance(app);

    pg.addChild( gun );
    return pg;
  } // end of gunHand()


} // end of WrapFPShooter3D class