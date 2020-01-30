/*
 *      @(#)Text2DApp.java 1.1 00/09/22 14:03
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
import java.awt.event.*;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

/*   Text2DApp renders a single, Text2D object.  The Text2D object spins
 *   revealing that the backside does not render by default.  The code
 *   to make the Text2D object double-sided is commented out in this
 *   program.
 */

public class Text2DApp extends Applet {

 public BranchGroup createSceneGraph() {

         // Create the root of the branch graph
         BranchGroup objRoot = new BranchGroup();
 
         // Create the transform group node and initialize it to the 
         // identity. Add it to the root of the subgraph.
         TransformGroup objSpin = new TransformGroup();
         objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         objRoot.addChild(objSpin);
 
         // Create a simple Text2D leaf node, add it to the scene graph.
         Text2D text2d = new Text2D("2D text in Java 3D", 
                                      new Color3f(0.9f, 1.0f, 1.0f), 
                                      "Helvetica", 24, Font.ITALIC);

         objSpin.addChild(text2d);

         Appearance textAppear = text2d.getAppearance();

// The following 4 lines of code (commented out) make the Text2D object
// 2-sided.
// This code depends on the line of code above that sets TextAppear.
//         PolygonAttributes polyAttrib = new PolygonAttributes();
//         polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
//         polyAttrib.setBackFaceNormalFlip(true);
//         textAppear.setPolygonAttributes(polyAttrib);


// The following 12 lines of code (commented out) use the texture from
// the previous Text2D object on another object.
// This code depends on the line of code above that sets TextAppear.
//         QuadArray qa = new QuadArray(4, QuadArray.COORDINATES |
//                                         QuadArray.TEXTURE_COORDINATE_2);
//         qa.setCoordinate(0, new Point3f(-10.0f, 1.0f, -5.0f));
//         qa.setCoordinate(1, new Point3f(-10.0f, -1.0f, -5.0f));
//         qa.setCoordinate(2, new Point3f(10.0f, -1.0f, -5.0f));
//         qa.setCoordinate(3, new Point3f(10.0f, 1.0f, -5.0f));
//         qa.setTextureCoordinate(0, new Point2f(0.0f, 1.0f));
//         qa.setTextureCoordinate(1, new Point2f(0.0f, 0.0f));
//         qa.setTextureCoordinate(2, new Point2f(1.0f, 0.0f));
//         qa.setTextureCoordinate(3, new Point2f(1.0f, 1.0f));
//
//         objRoot.addChild(new Shape3D(qa, textAppear));
 
         // Create a new Behavior object that will perform the desired
         // operation on the specified transform object and add it into
         // the scene graph.
         Alpha rotationAlpha = new Alpha(-1, 8000);
  
         RotationInterpolator rotator =
                 new RotationInterpolator(rotationAlpha, objSpin);
 
         // a bounding sphere specifies a region a behavior is active
         // create a sphere centered at the origin with radius of 100
         BoundingSphere bounds = new BoundingSphere();
         rotator.setSchedulingBounds(bounds);
         objSpin.addChild(rotator);

         Background backg = new Background();
         backg.setColor(0.4f, 0.4f, 1.0f);
         backg.setApplicationBounds(new BoundingSphere());
         objRoot.addChild(backg);
 
         return objRoot;
     } // end of CreateSceneGraph method


     public Text2DApp() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
            SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setStereoEnable(false);
        add("Center", canvas3D);

        BranchGroup scene = createSceneGraph();

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

	// This will move the ViewPlatform back a bit so the
	// objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
    } // end of Text2DApp (constructor)

    //  The following allows this to be run as an application
    //  as well as an applet

    public static void main(String[] args) {
        System.out.println("Text2DApp.java - a demonstration of Text2D in Java 3D");
        System.out.println("The scene is of a rotating Text2D object.");
        System.out.print("If you don't have a 3D card, it may take a few seconds ");
        System.out.println("before you see anything.");
        System.out.println("The Java 3D Tutorial is available on the web at:");
        System.out.println("http://java.sun.com/products/java-media/3D/collateral");
        Frame frame = new MainFrame(new Text2DApp(), 256, 32);
    } // end of main (method of Text2DApp)

} // end of class Text2DApp
