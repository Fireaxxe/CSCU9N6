/*
 *      @(#)TexCoordGenApp.java 1.1 00/09/25 01:58
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
 * TexCoordGenApp.java demonstrates automatic texture coord generation.
 * A 'twisted strip' is a continuous surface with a two twists in it.
 * (A Mobius strip has one twist)
 * The Class Strip creates a surface using a TriangleStripArray.
 * A twist strip is placed in a scene graph with a RotationInterpolator
 * so the strip spins.  
 *
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.*;
import javax.vecmath.*;


public class TexCoordGenApp extends Applet {

    Shape3D newTwist() {
        Shape3D twist = new Shape3D();
        twist.setGeometry(createTwistGeometry());
        twist.setAppearance(createTwistAppearance());
        return twist;
    }

    Geometry createTwistGeometry(){

        TriangleStripArray twistStrip; 
        Color3f blue  = new Color3f(0.0f, 0.0f, 1.0f);

        // create triangle strip for twist
        int N = 80;
        int stripCounts[] = {N};
        twistStrip = new TriangleStripArray(N, TriangleStripArray.COORDINATES,
                                            stripCounts);
        double a;
        int v;
        for(v = 0, a=0.0; v < N; v+=2, a=v*2.0*Math.PI/(N-2)){
            twistStrip.setCoordinate(v, new Point3d(
                    0.7*Math.sin(a)+0.2*Math.cos(a),
                    0.3*Math.sin(a),
                    0.7*Math.cos(a)+0.2*Math.cos(a)));
            twistStrip.setCoordinate(v+1, new Point3d(
                    0.7*Math.sin(a)-0.2*Math.cos(a),
                    -0.3*Math.sin(a),
                    0.7*Math.cos(a)-0.2*Math.cos(a)));
        }
        return twistStrip;
    }
	    	    
    // create Appearance for Twist Strip
    //
                          
    Appearance createTwistAppearance(){

        Appearance twistAppear = new Appearance();

        PolygonAttributes polyAttrib = new PolygonAttributes();
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
        twistAppear.setPolygonAttributes(polyAttrib);

        TexCoordGeneration tcg = new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,
                                                        TexCoordGeneration.TEXTURE_COORDINATE_2);
        twistAppear.setTexCoordGeneration(tcg);

        String filename = "earth.jpg";
        System.out.println("attempt to load texture from file: "+filename);
        TextureLoader loader = new TextureLoader(filename, this);
        ImageComponent2D image = loader.getImage();

        if(image == null) {
            System.out.println("load failed for texture: "+filename);
        }

        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                      image.getWidth(), image.getHeight());
        texture.setImage(0, image);
        texture.setEnable(true);

        twistAppear.setTexture(texture);

        return twistAppear;
    }

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

        Shape3D twist = newTwist();
        objSpin.addChild(twist);

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
    } // end of CreateSceneGraph method of TexCoordGenApp

    // Create a simple scene and attach it to the virtual universe

    public TexCoordGenApp() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.setStereoEnable(false);

        BranchGroup scene = createSceneGraph();

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

	// This will move the ViewPlatform back a bit so the
	// objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);

    } // end of TexCoordGenApp constructor

    //  The following method allows this to be run as an application

    public static void main(String[] args) {
        System.out.println("TexCoordGenApp - Java 3D API version 1.1");
        System.out.print("See \"Getting Started with the Java 3D API\"");
        System.out.println(" (Chapter 7)");
        System.out.println("This program demonstrates line texturing.");

        Frame frame = new MainFrame(new TexCoordGenApp(), 256, 256);
    } // end of main method of TexCoordGenApp

} // end of class TexCoordGenApp
