/*
 *      @(#)MultiGeom.java 1.0 00/10/30 14:44
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
 *      In this program, Cart class defines a visual object
 *      by extneding Shape3D.
 *   2. Using TriangleFanArray to create surfaces.
 *      Four TriangleFan array surfaces are contained in an instance of Cart.
 *   3. Placing multiple Geometries in a single Shape3D object.
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


public class MultiGeomApp extends Applet {

    /////////////////////////////////////////////////
    //
    // create scene graph branch group
    //
    public class Cart extends Shape3D{

        static final int nt = 17; // verticies per TriFan

	////////////////////////////////////////////
	//
	// create Shape3D with geometry and appearance
        // the geometry is created in method yoyoGeometry
        // the appearance is created in method yoyoAppearance
	//
        public Cart(float l, float w, float d) {

                this.setGeometry(cartWheelsGeometry( l, w, d));
                this.addGeometry(cartBodyGeometry( l, w, d));
                this.setAppearance(cartAppearance());
	    
        } // end of Cart constructor

	////////////////////////////////////////////
	//
        // create cart wheel geometry
        // four triangle fans represent the wheels of the cart
        // strip   indicies_______________
        //   0     0N+0 to 1N+0 ( 0 to N )
        //   1     1N+1 to 2N+1          t
        //   2     2N+2 to 3N+2
        //   3     3N+4 to 4N+3
	//
        private Geometry cartWheelsGeometry(float l, float w, float h) {

                TriangleFanArray tfa;
                int     totalN = 4*(nt+1);
                Point3f coords[] = new Point3f[totalN];
		Color3f colors[] = new Color3f[totalN];
                Color3f red = new Color3f(1.0f, 0f, 0f);
/* work */                Color3f yellow = new Color3f(0.7f, 0.5f, 0.0f);
                int     stripCounts[] = {nt+1, nt+1, nt+1, nt+1};

                // calculate right radius to use
                float   r = (1+w+h)/15; // could do better than this
                int     n;
                double  a;
                float   x, y;

                // set the central points for four triangle fan strips
                coords[0*(nt+1)] = new Point3f( l/2f, -h/2f,  w/2f);
                coords[1*(nt+1)] = new Point3f(-l/2f, -h/2f,  w/2f);
                coords[2*(nt+1)] = new Point3f( l/2f, -h/2f, -w/2f);
                coords[3*(nt+1)] = new Point3f(-l/2f, -h/2f, -w/2f);

                colors[0*(nt+1)] = red;
                colors[1*(nt+1)] = red;
                colors[2*(nt+1)] = red;
                colors[3*(nt+1)] = red;

                for(a = 0,n = 0; n < nt; a = 2.0*Math.PI/(nt-1) * ++n){
                        x = (float) (r * Math.cos(a));
                        y = (float) (r * Math.sin(a));
                        coords[0*(nt+1)+n+1]  = new Point3f(x+l/2f, y-h/2f,  w/2f);
                        coords[1*(nt+1)+nt-n] = new Point3f(x-l/2f, y-h/2f,  w/2f);
                        coords[2*(nt+1)+n+1]  = new Point3f(x+l/2f, y-h/2f, -w/2f);
                        coords[3*(nt+1)+nt-n] = new Point3f(x-l/2f, y-h/2f, -w/2f);

                        colors[0*(nt+1)+nt-n] = yellow;
                        colors[1*(nt+1)+n+1]  = yellow;
                        colors[2*(nt+1)+nt-n] = yellow;
                        colors[3*(nt+1)+n+1]  = yellow;
                }

		tfa = new TriangleFanArray (totalN, 
					TriangleFanArray.COORDINATES |
					TriangleFanArray.COLOR_3,
					stripCounts);
		
                tfa.setCoordinates(0, coords);
		tfa.setColors(0,colors);

                return tfa;

        } // end of method cartWheelGeometry in class Cart

        private Geometry cartBodyGeometry(float l, float w, float h) {

                QuadArray qa;
                int     N = 4*5; // 4 vert/quad * 5 quads
                Point3f coords[] = new Point3f[N];
                Color3f colors[] = new Color3f[N];
/* work */                Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
/* work */                Color3f yellow = new Color3f(0.7f, 0.5f, 0.0f);

                coords[ 0] = new Point3f( l/2f, -h/2f, -w/2f);
                coords[ 1] = new Point3f(-l/2f, -h/2f, -w/2f);
                coords[ 2] = new Point3f(-l/2f, -h/2f,  w/2f);
                coords[ 3] = new Point3f( l/2f, -h/2f,  w/2f);

                colors[ 0] = red;
                colors[ 1] = red;
                colors[ 2] = red;
                colors[ 3] = red;

                coords[ 4] = coords[0];
                coords[ 5] = coords[1];
                coords[ 6] = new Point3f(-l/2f, h/2f, -w/2f);
                coords[ 7] = new Point3f( l/2f, h/2f, -w/2f);

                if(coords[7] == coords[0]) System.out.println("damn");

                colors[ 4] = red;
                colors[ 5] = red;
                colors[ 6] = red;
                colors[ 7] = red;

                coords[ 8] = coords[1];
                coords[ 9] = coords[2];
                coords[10] = new Point3f(-l/2f, h/2f,  w/2f);
                coords[11] = new Point3f(-l/2f, h/2f, -w/2f);

                colors[ 8] = yellow;
                colors[ 9] = yellow;
                colors[10] = yellow;
                colors[11] = yellow;

                coords[12] = coords[2];
                coords[13] = coords[3];
                coords[14] = new Point3f( l/2f, h/2f,  w/2f);
                coords[15] = new Point3f(-l/2f, h/2f,  w/2f);

                colors[12] = red;
                colors[13] = red;
                colors[14] = red;
                colors[15] = red;

                coords[16] = coords[3];
                coords[17] = coords[0];
                coords[18] = new Point3f( l/2f, h/2f, -w/2f);
                coords[19] = new Point3f( l/2f, h/2f,  w/2f);

                colors[16] = yellow;
                colors[17] = yellow;
                colors[18] = yellow;
                colors[19] = yellow;

                qa = new QuadArray (N, 
                                     QuadArray.COORDINATES |
                                     QuadArray.COLOR_3);    
                qa.setCoordinates(0, coords);
                qa.setColors(0,colors);

                return qa;

        } // end of method cartBodyGeometry in class Cart

        private Appearance cartAppearance() {

                Appearance appear = new Appearance();
                PolygonAttributes polyAttrib = new PolygonAttributes();
                polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
                appear.setPolygonAttributes(polyAttrib);

                return appear;

        } // end of method cartAppearance in class Cart


    } // end of class Cart

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
 
      objSpin.addChild(new Cart(0.8f, 0.4f, 0.25f));

      // Create a new Behavior object that will perform the desired
      // operation on the specified transform object and add it into
      // the scene graph.
      Transform3D yAxis = new Transform3D();
      Alpha rotationAlpha = new Alpha(-1, 4000);

      RotationInterpolator rotator =
          new RotationInterpolator(rotationAlpha, objSpin);
      BoundingSphere bounds =
          new BoundingSphere(new Point3d(0.0,0.0,0.0), 10.0);
      rotator.setSchedulingBounds(bounds);
      objSpin.addChild(rotator);

      // make background white
      Background background = new Background(1.0f, 1.0f, 1.0f);
      background.setApplicationBounds(bounds);
      objRoot.addChild(background);

      // Let Java 3D perform optimizations on this scene graph.
      objRoot.compile();

      return objRoot;
    } // end of CreateSceneGraph method of MultiGeomApp

    // Create a simple scene and attach it to the virtual universe

    public MultiGeomApp() {
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
    } // end of MultiGeomApp constructor

    //  The following allows this to be run as an application
    //  as well as an applet

    public static void main(String[] args) {
        Frame frame = new MainFrame(new MultiGeomApp(), 256, 256);
    } // end of main method of MultiGeomApp

} // end of class MultiGeomApp
