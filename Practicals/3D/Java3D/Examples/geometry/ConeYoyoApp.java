/*
 *      @(#)ConeYoyoApp.java 1.1 00/09/22 15:57
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

/*
 * Getting Started with the Java 3D API
 * written in Java 3D 
 *
 * This program demonstrates:
 *   1. writing a visual object class
 *      In this program, ConeYoyo class defines a visual object
 *      independent of all other classes.
 *   2. Using Cone to create surfaces.
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;


public class ConeYoyoApp extends Applet {

    /////////////////////////////////////////////////
    //
    // create scene graph branch group
    //
    public class ConeYoyo{

        private BranchGroup yoyoBG;

	////////////////////////////////////////////
	//
	// create Shape3D with geometry and appearance
        // the geometry is created in method yoyoGeometry
        // the appearance is created in method yoyoAppearance
	//
	public ConeYoyo() {
	
	    yoyoBG = new BranchGroup();

		Appearance app = new Appearance();

	    // rotate object has composited transformation matrix
	    Transform3D rotate = new Transform3D();
	    Transform3D translate = new Transform3D();

	    translate.set(new Vector3f(0.1f, 0.0f, 0.0f));
	    TransformGroup yoyoTGT1 = new TransformGroup(translate);
	    yoyoBG.addChild(yoyoTGT1);

            rotate.rotZ(Math.PI/2.0d);
	    TransformGroup yoyoTGR1 = new TransformGroup(rotate);
	    Cone cone1 = new Cone(0.6f, 0.2f);
	    cone1.setAppearance(app);
	    yoyoTGR1.addChild(cone1);
	    yoyoTGT1.addChild(yoyoTGR1);
	
	    translate.set(new Vector3f(-0.1f, 0.0f, 0.0f));
	    TransformGroup yoyoTGT2 = new TransformGroup(translate);
	    yoyoBG.addChild(yoyoTGT2);

            rotate.rotZ(-Math.PI/2.0d);
	    TransformGroup yoyoTGR2 = new TransformGroup(rotate);
	    Cone cone2 = new Cone(0.6f, 0.2f);
	    cone2.setAppearance(app);
	    yoyoTGR2.addChild(cone2);
	    yoyoTGT2.addChild(yoyoTGR2);
    
	    yoyoBG.compile();
	    
	} // end of ConeYoyo constructor	

	public BranchGroup getBG(){
	    return yoyoBG;
	}

    } // end of class ConeYoyo

    /////////////////////////////////////////////////
    //
    // create scene graph branch group
    //
    public BranchGroup createSceneGraph() {

	BranchGroup objRoot = new BranchGroup();

      // Create the transform group node and initialize it to the
      // identity.  Enable the TRANSFORM_WRITE capability so that
      // our behavior code can modify it at runtime.  Add it to the
      // root of the subgraph.
      TransformGroup objSpin = new TransformGroup();
      objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

      objRoot.addChild(objSpin);
      objSpin.addChild(new ConeYoyo().getBG());

      // Create a new Behavior object that will perform the desired
      // operation on the specified transform object and add it into
      // the scene graph.
      Transform3D yAxis = new Transform3D();
      Alpha rotationAlpha = new Alpha(-1, 4000);

      RotationInterpolator rotator =
          new RotationInterpolator(rotationAlpha, objSpin);
      BoundingSphere bounds =
          new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
      rotator.setSchedulingBounds(bounds);
      objSpin.addChild(rotator);

	// Let Java 3D perform optimizations on this scene graph.
        objRoot.compile();

	return objRoot;
    } // end of CreateSceneGraph method of yoyo1

    // Create a simple scene and attach it to the virtual universe

    public ConeYoyoApp() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);

        BranchGroup scene = createSceneGraph();

        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
    } // end of ConeYoyoApp constructor

    //  The following allows this to be run as an application
    //  as well as an applet

    public static void main(String[] args) {
        Frame frame = new MainFrame(new ConeYoyoApp(), 256, 256);
    } // end of main method of coneyoyo

} // end of class ConeYoyoApp
