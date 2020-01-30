/*
 *      @(#)BlueLitTwistApp.java 1.1 00/09/24 22:37
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
 * BlueLitTwistApp.java is intended to demonstrate lighting complex surfaces
 * A Twist strip is a continuous surface with a complete twist in it.
 * (As opposed to a half-twist which would make a Mobius Strip.)
 * The Class Twist creates a surface using a TriangleStripArray.
 * A Twist strip is placed in a scene graph with a RotationInterpolator
 * so the strip spins.
 *
 * One Twist strip is created using the inner class (Twist).
 * This visual object is rendered as filled polygons.
 *
 * This application (or a version of it) generated one or more
 * of the images in Chapter 6 of Getting Started with the Java 3D API.
 * The Java 3D Turtorial.
 *
 * See http://www.sun.com/desktop/java3d/collateral for more information.
 *
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;


public class BlueLitTwistApp extends Applet {

    /////////////////////////////////////////////////
    //
    // create Twist Strip visual object
    //
    public class Twist extends Shape3D{

	////////////////////////////////////////////
	//
        // create twisted strip subgraph
	//
        public Twist() {
	
            this.setGeometry(createGeometry());
            this.setAppearance(createAppearance());
        } // end of Twist constructor 

        Geometry createGeometry(){

            TriangleStripArray twistStrip; 

            // create triangle strip for Twist
            int N = 180;
            int stripCounts[] = {N};
            twistStrip = new TriangleStripArray(N,
              TriangleStripArray.COORDINATES
              | TriangleStripArray.NORMALS
              | TriangleStripArray.COLOR_3,
              stripCounts
            );

            double a;
            int v;
            Vector3f norm = new Vector3f();
            Color3f color = new Color3f();
            for(v = 0, a=0.0; v < N; v+=2, a=v*2.0*Math.PI/(N-2)){
                twistStrip.setCoordinate(v, new Point3d(
                        0.65*Math.cos(a)+0.2*Math.sin(a),
                                       -0.3*Math.cos(a),
                        0.65*Math.sin(a)+0.2*Math.sin(a)));
                twistStrip.setCoordinate(v+1, new Point3d(
                        0.65*Math.cos(a)-0.2*Math.sin(a),
                                        0.3*Math.cos(a),
                        0.65*Math.sin(a)-0.2*Math.sin(a)));
                norm.set((float) (Math.abs(Math.cos(a))),
                         (float) Math.abs(Math.sin(a)),
                         (float) (Math.cos(a)*Math.sin(a)));
                norm.normalize();
                twistStrip.setNormal(v, norm);
                twistStrip.setNormal(v+1, norm);
                color.set((float)(Math.cos(a)*Math.cos(a)),
                          (float)(Math.sin(2*a)*Math.sin(2*a)),
                          (float)Math.abs(Math.cos(a+1.5)*Math.cos(a+1.5)));
                twistStrip.setColor (v,color);
                twistStrip.setColor (v,color);
	    }

            return twistStrip;

            }
	    	    
        // create Appearance for Twist Strip
        //
        // this method creates the default Appearance for the
        // twist strip.  The commented line of code containting
        // the setCullFace will fix the problem of half of the
        // Twist Strip disappearing.

        Appearance createAppearance(){

            Appearance twistAppear = new Appearance();
            PolygonAttributes polyAttrib = new PolygonAttributes();
            polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
            polyAttrib.setBackFaceNormalFlip(true);
            twistAppear.setPolygonAttributes(polyAttrib);

            Material material = new Material();
            twistAppear.setMaterial(material);

            return twistAppear;
        }

    } // end of class Twist

    /////////////////////////////////////////////////
    //
    // create scene graph branch group
    //
    public BranchGroup createSceneGraph() {

        BranchGroup contentRoot = new BranchGroup();

        // Create the transform group node and initialize it to the
        // identity. Add it to the root of the subgraph.
        TransformGroup objSpin = new TransformGroup();
        objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        contentRoot.addChild(objSpin);

        Shape3D twist = new Twist();
        objSpin.addChild(twist);

        Alpha rotationAlpha = new Alpha(-1, 80000);
  
        RotationInterpolator rotator =
                 new RotationInterpolator(rotationAlpha, objSpin);
 
        // a bounding sphere specifies a region a behavior is active
        // create a sphere centered at the origin with radius of 1.5
        BoundingSphere bounds = new BoundingSphere();
        bounds.setRadius(1.5);
        rotator.setSchedulingBounds(bounds);
        objSpin.addChild(rotator);

        DirectionalLight lightD = new DirectionalLight();
        lightD.setInfluencingBounds(bounds);
        lightD.setDirection(new Vector3f(.7f, -.7f, -.7f));
        contentRoot.addChild(lightD);

        DirectionalLight lightD2 = new DirectionalLight();
        lightD2.setInfluencingBounds(bounds);
        lightD2.setDirection(new Vector3f(.5f, .5f, -.7f));
        contentRoot.addChild(lightD2);

        Background background = new Background();
        background.setColor(1.0f, 1.0f, 1.0f);
        background.setApplicationBounds(bounds);
        contentRoot.addChild(background);

        // Let Java 3D perform optimizations on this scene graph.
        // contentRoot.compile();

        return contentRoot;
    } // end of CreateSceneGraph method of BlueLitTwistApp

    // Create a simple scene and attach it to the virtual universe

    public BlueLitTwistApp() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);

        BranchGroup scene = createSceneGraph();

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

	// This will move the ViewPlatform back a bit so the
	// objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
    } // end of BlueLitTwistApp constructor

    //  The following allows this to be run as an application
    //  as well as an applet

    public static void main(String[] args) {
        System.out.println("BlueLitTwistApp - Java 3D");
        System.out.println("A demonstration of lights.");
        Frame frame = new MainFrame(new BlueLitTwistApp(), 256, 256);
    } // end of main method of TwistApp

} // end of class BlueLitTwistApp
