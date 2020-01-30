/*
 *      @(#)TwistByRefApp.java 1.0 00/11/05 22:57
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
 * TwistByRefApp.java demonstrates BY_REFERENCE geometry.
 * A 'twisted strip' is a continuous surface with a two twists in it.
 * (A Mobius strip has one twist)
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;


public class TwistByRefApp extends Applet {

    /////////////////////////////////////////////////
    //
    // create Twist visual object
    //
    public class Twist extends Shape3D{

	////////////////////////////////////////////
	//
        // create twist subgraph
	//
        public Twist() {
	
            this.setGeometry(createGeometry());
            this.setAppearance(createAppearance());
        } // end of twist constructor 

        Geometry createGeometry(){

            TriangleStripArray twistStrip = null; 
            final Color3b red  = new Color3b(Color.red);
            int N = 80;                    // number of vertices
            int stripCounts[] = {N};       // 1 strip of N verts
            float[] coord = new float[N*3];
            Color3b[] color = new Color3b[N*3];

            // create triangle strip for twist
            twistStrip = new TriangleStripArray(N,
              TriangleStripArray.COORDINATES
              | TriangleStripArray.BY_REFERENCE
              | TriangleStripArray.COLOR_3,
              stripCounts
            );

            double a;
            int v;
            for(v = 0, a=0.0; v < N; v+=2, a=v*2.0*Math.PI/(N-2)){
                coord[v*3+0] = (float)(0.7*Math.sin(a)+0.2*Math.cos(a));
                coord[v*3+1] = (float)(0.3*Math.sin(a));
                coord[v*3+2] = (float)(0.7*Math.cos(a)+0.2*Math.cos(a));
                coord[v*3+3] = (float)(0.7*Math.sin(a)-0.2*Math.cos(a));
                coord[v*3+4] = (float)(-0.3*Math.sin(a));
                coord[v*3+5] = (float)(0.7*Math.cos(a)-0.2*Math.cos(a));
                color[v]   = red;
                color[v+1] = red;
	    }

            twistStrip.setCoordRefFloat(coord);
            twistStrip.setColorRef3b(color);

            return twistStrip;

        }
	    	    
        // create Appearance for Twist Strip
        //
        // this method creates the default Appearance for the
        // twist strip.  The commented line of code containting
        // the setCullFace will fix the problem of half of the
        // Twisted Strip disappearing.

        Appearance createAppearance(){

            Appearance twistAppear = new Appearance();
            PolygonAttributes polyAttrib = new PolygonAttributes();
            // polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
            twistAppear.setPolygonAttributes(polyAttrib);

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

        // Duplicate the twist strip geometry and set the
        // appearance of the new Shape3D object to line mode
        // without culling.
        // Add the POLYGON_FILLED and POLYGON_LINE strips
        // in the scene graph at the same point.
        // This will show the triangles of the original Mobius strip that
        // are clipped.  The PolygonOffset is set to prevent stitching.
        PolygonAttributes polyAttrib = new PolygonAttributes();
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
        polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        polyAttrib.setPolygonOffset(0.001f);
        Appearance polyAppear = new Appearance();
        polyAppear.setPolygonAttributes(polyAttrib);
        objSpin.addChild(new Shape3D(twist.getGeometry(), polyAppear));

        Alpha rotationAlpha = new Alpha(-1, 16000);
  
        RotationInterpolator rotator =
                 new RotationInterpolator(rotationAlpha, objSpin);
 
        // a bounding sphere specifies a region a behavior is active
        // create a sphere centered at the origin with radius of 1
        BoundingSphere bounds = new BoundingSphere();
        rotator.setSchedulingBounds(bounds);
        objSpin.addChild(rotator);

        // make background white
        Background background = new Background(1.0f, 1.0f, 1.0f);
        background.setApplicationBounds(bounds);
        contentRoot.addChild(background);

        // Let Java 3D perform optimizations on this scene graph.
        contentRoot.compile();

        return contentRoot;
    } // end of CreateSceneGraph method of TwistByRefApp

    // Create a simple scene and attach it to the virtual universe

    public TwistByRefApp() {
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

    } // end of TwistByRefApp constructor

    //  The following method allows this to be run as an application

    public static void main(String[] args) {
        System.out.println("TwistByRefApp - Java 3D API");
        System.out.println("This program demonstrates back face culling.");
        System.out.print("In this program two visual objects rotate, ");
        System.out.println("one wireframe and one solid surface.");
        System.out.print("The wire frame is visible only when components");
        System.out.println(" of the surface are culled.");

        Frame frame = new MainFrame(new TwistByRefApp(), 256, 256);
    } // end of main method of TwistByRefApp

} // end of class TwistByRefApp
