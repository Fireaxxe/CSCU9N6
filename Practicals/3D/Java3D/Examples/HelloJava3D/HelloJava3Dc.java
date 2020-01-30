/*
 *      @(#)HelloJava3Dc.java 1.1 00/09/22 13:55
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
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

//   HelloJava3Dc renders a single, rotating cube.  

public class HelloJava3Dc extends Applet {

 public BranchGroup createSceneGraph() {
         // Create the root of the branch graph
         BranchGroup objRoot = new BranchGroup();
 
         // Create the transform group node and initialize it to the 
         // identity. Add it to the root of the subgraph.
         TransformGroup objSpin = new TransformGroup();
         objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         objRoot.addChild(objSpin);
 
         // Create a simple shape leaf node, add it to the scene graph.
         // ColorCube is a Convenience Utility class
         objSpin.addChild(new ColorCube(0.4));
 
         // Create a new Behavior object that will perform the desired
         // operation on the specified transform object and add it into
         // the scene graph.
         Alpha rotationAlpha = new Alpha(-1, 4000);
  
         RotationInterpolator rotator =
                 new RotationInterpolator(rotationAlpha, objSpin);
 
         // a bounding sphere specifies a region a behavior is active
         // create a sphere centered at the origin with radius of 100
         BoundingSphere bounds = new BoundingSphere();
         rotator.setSchedulingBounds(bounds);
         objSpin.addChild(rotator);
 
         return objRoot;
     } // end of CreateSceneGraph method


     public HelloJava3Dc() {
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
    } // end of HelloJava3D (constructor)

    //  The following allows this to be run as an application
    //  as well as an applet

    public static void main(String[] args) {
	Frame frame = new MainFrame(new HelloJava3Dc(), 256, 256);
    } // end of main (method of HelloJava3D)

} // end of class HelloJava3Dc
