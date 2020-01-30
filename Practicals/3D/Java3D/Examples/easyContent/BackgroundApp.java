/*
 *      @(#)BackgroundApp.java 1.1 00/09/22 14:03
 *
 * Copyright (c) 1996-2000 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.event.*;
import java.awt.AWTEvent;
import java.util.Enumeration;
import com.sun.j3d.utils.behaviors.keyboard.*;


/*   BackgroundApp is an example of creating a geometric background
 *   in a Java 3D program.
 *
 *   This program creates a geometric background of stars and one
 *   constellation.  Also created in the scene is a grid of green
 *   lines.  These represent a horizontal surface (the ground) in
 *   in the local area.  It is provided for a reference for movement
 *   in the scene.
 *
 *   The viewer can move in this virtual world using the arrow keys.
 *   The movement is provided by the KeyNavigator class.
 *
 *   This example also uses a BoundingLeaf object for the application
 *   bounds for the KeyNavigatorBehavior class.  There is a
 *   bug kludge in this code.  See below for more details.
 *
 *   This program was created as a companion to the Java 3D API
 *   tutorial available from the Java 3D API homepage.
 *
 *   Background and BoundingLeaf classes are documented in Chapter 3.
 *   Interaction, in general, and KeyNavigator class, specifically,
 *   are documented in Chapter 4.
 *
 *   The first 200 lines (or so) of code simply create the geometry
 *   for the background and the rest of the view.
 *
 *   There is no guarantee that any of the stars nor the constellation
 *   is an accurate representation of the the view from earth.
 */


public class BackgroundApp extends Applet {

    Shape3D createLand(){
        LineArray landGeom = new LineArray(44, GeometryArray.COORDINATES
                                            | GeometryArray.COLOR_3);
        float l = -50.0f;
        for(int c = 0; c < 44; c+=4){

            landGeom.setCoordinate( c+0, new Point3f( -50.0f, 0.0f,  l ));
            landGeom.setCoordinate( c+1, new Point3f(  50.0f, 0.0f,  l ));
            landGeom.setCoordinate( c+2, new Point3f(   l   , 0.0f, -50.0f ));
            landGeom.setCoordinate( c+3, new Point3f(   l   , 0.0f,  50.0f ));
            l += 10.0f;
        }

        Color3f c = new Color3f(0.1f, 0.8f, 0.1f);
        for(int i = 0; i < 44; i++) landGeom.setColor( i, c);

        return new Shape3D(landGeom);
    }


    public BranchGroup createSceneGraph(SimpleUniverse su) {
	// Create the root of the branch graph
        BranchGroup objRootBG = new BranchGroup();

        Vector3f translate = new Vector3f();
        Transform3D T3D = new Transform3D();

        translate.set( 0.0f, -0.3f, 0.0f);
        T3D.setTranslation(translate);
        TransformGroup objRoot = new TransformGroup(T3D);
        objRootBG.addChild(objRoot);

        objRoot.addChild(createLand());

        BoundingLeaf boundingLeaf = new BoundingLeaf(new BoundingSphere());

        PlatformGeometry platformGeom = new PlatformGeometry();
        platformGeom.addChild(boundingLeaf);
        platformGeom.compile();
        su.getViewingPlatform().setPlatformGeometry(platformGeom);


        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(
                       su.getViewingPlatform().getViewPlatformTransform());
        keyNavBeh.setSchedulingBoundingLeaf(boundingLeaf);
        objRootBG.addChild(keyNavBeh);

         Background background = new Background();
         background.setApplicationBounds(new BoundingSphere(new Point3d(), 1000.0));
         background.setGeometry(createBackGraph());
         objRoot.addChild(background); 

         AmbientLight ambientLight = new AmbientLight();
         ambientLight.setInfluencingBounds(new BoundingSphere());
         objRootBG.addChild(ambientLight);
 
         return objRootBG;
     } // end of CreateSceneGraph method

/////////////////////////////////////////////////////////

     public BranchGroup createBackGraph() {

         // Create the root of the branch graph
         BranchGroup objRoot = new BranchGroup();

         PointArray starGeom1 = new PointArray(7, PointArray.COORDINATES);                 
         Appearance starAppear1 = new Appearance();
         starGeom1.setCoordinate( 0, new Point3f( 0.79483311f, -0.58810995f,  0.14955615f));
         starGeom1.setCoordinate( 1, new Point3f( 0.44430932f, -0.55736839f, -0.70137505f));
         starGeom1.setCoordinate( 2, new Point3f( 0.94901367f, -0.30404968f,  0.08322775f));
         starGeom1.setCoordinate( 3, new Point3f( 0.68060123f, -0.43044807f,  0.59287173f));
         starGeom1.setCoordinate( 4, new Point3f(-0.11641672f,  0.47273532f,  0.87348049f));
         starGeom1.setCoordinate( 5, new Point3f(-0.10399289f, -0.98059412f,  0.16619437f));
         starGeom1.setCoordinate( 6, new Point3f( 0.08024400f, -0.96944100f, -0.23182900f));
         PointAttributes point1 = new PointAttributes(4.0f, false);
         starAppear1.setPointAttributes(point1);
         objRoot.addChild(new Shape3D(starGeom1, starAppear1));

         PointArray starGeom2 = new PointArray(18, PointArray.COORDINATES);
         starGeom2.setCoordinate( 0, new Point3f( 0.050844f, -0.992329f,  0.112678f));
         starGeom2.setCoordinate( 1, new Point3f(-0.063091f, -0.997672f,  0.025869f));
         starGeom2.setCoordinate( 2, new Point3f( 0.096706f, -0.980384f,  0.171736f));
         starGeom2.setCoordinate( 3, new Point3f(-0.562384f,  0.073568f,  0.823595f));
         starGeom2.setCoordinate( 4, new Point3f(-0.863904f,  0.059045f,  0.500180f));
         starGeom2.setCoordinate( 5, new Point3f(-0.727033f,  0.304149f,  0.615559f));
         starGeom2.setCoordinate( 6, new Point3f(-0.724850f,  0.535590f,  0.433281f));
         starGeom2.setCoordinate( 7, new Point3f( 0.185904f, -0.976907f, -0.105311f));
         starGeom2.setCoordinate( 8, new Point3f( 0.738028f, -0.531886f, -0.415221f));
         starGeom2.setCoordinate( 9, new Point3f(-0.402152f,  0.392690f, -0.827085f));
         starGeom2.setCoordinate(10, new Point3f(-0.020020f, -0.999468f, -0.025724f));
         starGeom2.setCoordinate(11, new Point3f(-0.384103f, -0.887075f,  0.256050f));
         starGeom2.setCoordinate(12, new Point3f(-0.224464f, -0.968946f, -0.103720f));
         starGeom2.setCoordinate(13, new Point3f(-0.828880f, -0.397932f, -0.393203f));
         starGeom2.setCoordinate(14, new Point3f(-0.010557f, -0.998653f,  0.050797f));
         starGeom2.setCoordinate(15, new Point3f(-0.282122f,  0.258380f, -0.923930f));
         starGeom2.setCoordinate(16, new Point3f(-0.941342f, -0.030364f,  0.336082f));
         starGeom2.setCoordinate(17, new Point3f( 0.00057f, -0.99651f,  -0.08344f));
         Appearance starAppear2 = new Appearance();
         PointAttributes point2 = new PointAttributes(2.0f, false);
         starAppear2.setPointAttributes(point2);
         objRoot.addChild(new Shape3D(starGeom2, starAppear2));

         PointArray starGeom3 = new PointArray(20, PointArray.COORDINATES);
         starGeom3.setCoordinate( 0, new Point3f( 0.07292f, -0.98862f,  -0.13153f));
         starGeom3.setCoordinate( 1, new Point3f( 0.23133f, -0.87605f,  -0.42309f));
         starGeom3.setCoordinate( 2, new Point3f(-0.08215f, -0.64657f,   0.75840f));
         starGeom3.setCoordinate( 3, new Point3f(-0.84545f,  0.53398f,   0.00691f));
         starGeom3.setCoordinate( 4, new Point3f(-0.49365f, -0.83645f,  -0.23795f));
         starGeom3.setCoordinate( 5, new Point3f( 0.06883f, -0.99319f,  -0.09396f));
         starGeom3.setCoordinate( 6, new Point3f( 0.87582f, -0.40662f,   0.25997f));
         starGeom3.setCoordinate( 7, new Point3f(-0.09095f, -0.99555f,   0.02467f));
         starGeom3.setCoordinate( 8, new Point3f( 0.45306f, -0.81575f,  -0.35955f));
         starGeom3.setCoordinate( 9, new Point3f( 0.17669f, -0.97939f,   0.09776f));
         starGeom3.setCoordinate(10, new Point3f( 0.27421f, -0.83963f,   0.46884f));
         starGeom3.setCoordinate(11, new Point3f( 0.32703f, -0.94013f,  -0.09584f));
         starGeom3.setCoordinate(12, new Point3f(-0.01615f, -0.99798f,  -0.06132f));
         starGeom3.setCoordinate(13, new Point3f(-0.76665f,  0.45998f,  -0.44791f));
         starGeom3.setCoordinate(14, new Point3f(-0.91025f, -0.07102f,   0.40791f));
         starGeom3.setCoordinate(15, new Point3f(-0.00240f, -0.97104f,  -0.23887f));
         starGeom3.setCoordinate(16, new Point3f( 0.91936f, -0.39244f,   0.02740f));
         starGeom3.setCoordinate(17, new Point3f( 0.18290f, -0.97993f,   0.07920f));
         starGeom3.setCoordinate(18, new Point3f(-0.48755f,  0.61592f,   0.61884f));
         starGeom3.setCoordinate(19, new Point3f(-0.89375f,  0.36087f,  -0.26626f));
         objRoot.addChild(new Shape3D(starGeom3));                              

         int[] stripCount = {10};
         LineStripArray orion = new LineStripArray(10, LineStripArray.COORDINATES, stripCount);
         orion.setCoordinate( 0, new Point3f(0.978330f, -0.033900f, 0.204426f));
         orion.setCoordinate( 1, new Point3f(0.968007f, -0.167860f, 0.186506f));
         orion.setCoordinate( 2, new Point3f(0.981477f, -0.142660f, 0.127873f));
         orion.setCoordinate( 3, new Point3f(0.983764f, -0.005220f, 0.179391f));
         orion.setCoordinate( 4, new Point3f(0.981112f,  0.110597f, 0.158705f));
         orion.setCoordinate( 5, new Point3f(0.967377f,  0.172516f, 0.185523f));
         orion.setCoordinate( 6, new Point3f(0.961385f,  0.128845f, 0.243183f));
         orion.setCoordinate( 7, new Point3f(0.978330f, -0.033900f, 0.204426f));
         orion.setCoordinate( 8, new Point3f(0.981293f, -0.020980f, 0.191375f));
         orion.setCoordinate( 9, new Point3f(0.983764f, -0.005220f, 0.179391f));
         objRoot.addChild(new Shape3D(orion));
         
 
         objRoot.compile();
         return objRoot;
     } // end of CreateBackGraph method


/////////////////////BackgroundApp//////////////////////
     public BackgroundApp() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
            SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setStereoEnable(false);
        add("Center", canvas3D);

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        BranchGroup scene = createSceneGraph(simpleU);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
        new OtherView(simpleU.getLocale()); /* see note below */
    } // end of BackgroundApp (constructor)

/*
 *  This class was created to make the boundingleaf
 *  work for this example program.  One OtherView
 *  object is created just a couple of lines above.
 *  Inserting a second frame in the scene makes the
 *  BoundingLeaf object work as desired.
 */

    public class OtherView extends Object {
        public TransformGroup  vpTrans;

        public OtherView( Locale locale) {
           GraphicsConfiguration config =
               SimpleUniverse.getPreferredConfiguration();

           Canvas3D canvas3D = new Canvas3D(config);
           canvas3D.setStereoEnable(false);
           PhysicalBody body = new PhysicalBody();
           PhysicalEnvironment environment = new PhysicalEnvironment();
           View view = new View();
           view.setPhysicalBody(body);
           view.setPhysicalEnvironment(environment);
           BranchGroup vpRoot = new BranchGroup();
           Transform3D viewT3D = new Transform3D();
           viewT3D.set(new Vector3f(0.0f, 0.0f, 2.0f));
           ViewPlatform vp = new ViewPlatform();
           vpTrans = new TransformGroup(viewT3D);
           vpTrans.addChild(vp);
           vpRoot.addChild(vpTrans);
           view.attachViewPlatform(vp);
           locale.addBranchGraph(vpRoot);
        }
     } // end of OtherView class



    //  The following allows this to be run as an application
    //  as well as an applet

    public static void main(String[] args) {
        System.out.println("BackgroundApp.java - a demonstration of placing geometry");
        System.out.println("in the background of a Java 3D scene.");
        System.out.println("When the app loads, you can use the arrow keys to move.");
        System.out.println("The Java 3D Tutorial is available on the web at:");
        System.out.println("http://java.sun.com/products/java-media/3D/collateral");
        Frame frame = new MainFrame(new BackgroundApp(), 256, 256);
    } // end of main (method of BackgroundApp)

} // end of class BackgroundApp
