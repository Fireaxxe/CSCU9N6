/*
 *      @(#)SimpleQuadLoad.java 1.00 01/03/15 
 *
 * Copyright (c) 1996-2001 Sun Microsystems, Inc. All Rights Reserved.
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

import SimpleQuadFileLoader;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.Scene;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.io.*;
import com.sun.j3d.utils.behaviors.mouse.*;

public class SimpleQuadLoad extends Applet {

    private boolean spin = false;
    private String filename = null;

    public BranchGroup createSceneGraph(String args[]) {
	// Create the root of the branch graph
	BranchGroup objRoot = new BranchGroup();

        // Create a Transformgroup to scale all objects so they
        // appear in the scene.
        TransformGroup objScale = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setScale(0.7);
        objScale.setTransform(t3d);
        objRoot.addChild(objScale);

	// Create the transform group node and initialize it to the
	// identity.  Enable the TRANSFORM_WRITE capability so that
	// our behavior code can modify it at runtime.  Add it to the
	// root of the subgraph.
	TransformGroup objTrans = new TransformGroup();
	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	objScale.addChild(objTrans);

        int flags = 0; 
        SimpleQuadFileLoader f = new SimpleQuadFileLoader(flags);
	Scene s = null;
	try {
	  s = f.load(filename);
	}
	catch (FileNotFoundException e) {
	  System.err.println(e);
	  System.exit(1);
	}
	catch (ParsingErrorException e) {
	  System.err.println(e);
	  System.exit(1);
	}
	catch (IncorrectFormatException e) {
	  System.err.println(e);
	  System.exit(1);
	}
	  
	objTrans.addChild(s.getSceneGroup());

	BoundingSphere bounds =
	  new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);

        if (spin) {
	  Transform3D yAxis = new Transform3D();
	  Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE,
					  0, 0,
					  4000, 0, 0,
					  0, 0, 0);

	  RotationInterpolator rotator =
	      new RotationInterpolator(rotationAlpha, objTrans, yAxis,
				       0.0f, (float) Math.PI*2.0f);
	  rotator.setSchedulingBounds(bounds);
	  objTrans.addChild(rotator);
	} else {
	  // Create the rotate behavior node
	  MouseRotate behavior = new MouseRotate();
	  behavior.setTransformGroup(objTrans);
	  objTrans.addChild(behavior);
	  behavior.setSchedulingBounds(bounds);

	  // Create the zoom behavior node
	  MouseZoom behavior2 = new MouseZoom();
	  behavior2.setTransformGroup(objTrans);
	  objTrans.addChild(behavior2);
	  behavior2.setSchedulingBounds(bounds);

	  // Create the translate behavior node
	  MouseTranslate behavior3 = new MouseTranslate();
	  behavior3.setTransformGroup(objTrans);
	  objTrans.addChild(behavior3);
	  behavior3.setSchedulingBounds(bounds);
	}

        // Set up the background
        Color3f bgColor = new Color3f(0.05f, 0.05f, 0.5f);
        Background bgNode = new Background(bgColor);
        bgNode.setApplicationBounds(bounds);
        objRoot.addChild(bgNode);

        // Set up the ambient light
        Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);

        // Set up the directional lights
        Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
        Vector3f light1Direction  = new Vector3f(4.0f, -7.0f, -12.0f);
        Color3f light2Color = new Color3f(0.3f, 0.3f, 0.4f);
        Vector3f light2Direction  = new Vector3f(-6.0f, -2.0f, -1.0f);

        DirectionalLight light1
            = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        objRoot.addChild(light1);

        DirectionalLight light2
            = new DirectionalLight(light2Color, light2Direction);
        light2.setInfluencingBounds(bounds);
        objRoot.addChild(light2);

	return objRoot;
    }

    private void usage()
    {
      System.out.println(
        "Usage: java SimpleQuadLoad [-s] <.quad file>");
      System.out.println("  -s Spin (no user interaction)");
      System.exit(0);
    } // End of usage

    public SimpleQuadLoad(String args[]) {

        if (args.length == 0) {
	  usage();
	} else {
	  for (int i = 0 ; i < args.length ; i++) {
	    if (args[i].startsWith("-")) {
	      if (args[i].equals("-s")) {
		spin = true;
	      } else {
		System.err.println("Argument '" + args[i] + "' ignored.");
	      }
	    } else {
              if(filename == null) {
                filename = args[i];
              } else {
                System.err.print("Argument '" + args[i] + "' invalid;");
                System.err.println("Filename: '" + filename + "' already specified.");
                usage();
              }
	    }
	  }
	}

	if (filename == null) {
          System.err.println("no filename specified.");
	  usage();
	}

	setLayout(new BorderLayout());
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D c = new Canvas3D(config);
	add("Center", c);

	// Create a simple scene and attach it to the virtual universe
	BranchGroup scene = createSceneGraph(args);
	SimpleUniverse u = new SimpleUniverse(c);
        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        u.getViewingPlatform().setNominalViewingTransform();
	u.addBranchGraph(scene);
    }



    //
    // The following allows SimpleQuadLoad to be run as an application
    // as well as an applet
    //
    public static void main(String[] args) {
        new MainFrame(new SimpleQuadLoad(args), 700, 700);
    }
}
