/*
 *      @(#)SimpleTextureApp.java 1.2 01/06/18
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

/*
 
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * SimpleTextureSpinApp creates a single plane with texture mapping.
 */
public class SimpleTextureSpinApp extends Applet {

    BranchGroup createScene() { 
      BranchGroup objRoot = new BranchGroup();

      Transform3D transform = new Transform3D();

      QuadArray plane = new QuadArray(4, GeometryArray.COORDINATES
                                        | GeometryArray.TEXTURE_COORDINATE_2);

      Point3f p = new Point3f(-1.0f,  1.0f,  0.0f);
      plane.setCoordinate(0, p);
      p.set(-1.0f, -1.0f,  0.0f);
      plane.setCoordinate(1, p);
      p.set(1.0f, -1.0f,  0.0f);
      plane.setCoordinate(2, p);
      p.set(1.0f,  1.0f,  0.0f);
      plane.setCoordinate(3, p);

      TexCoord2f q = new TexCoord2f( 0.0f,  1.0f);
      plane.setTextureCoordinate(0, 0, q);
      q.set(0.0f, 0.0f);
      plane.setTextureCoordinate(0, 1, q);
      q.set(1.0f, 0.0f);
      plane.setTextureCoordinate(0, 2, q);
      q.set(1.0f, 1.0f);
      plane.setTextureCoordinate(0, 3, q);

      Appearance appear = new Appearance();

      String filename = "stripe.gif";  
      TextureLoader loader = new TextureLoader(filename, this);
      ImageComponent2D image = loader.getImage();

      if(image == null) {
            System.out.println("load failed for texture: "+filename);
      }

      // can't use parameterless constuctor
      Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());
      texture.setImage(0, image);
      //texture.setEnable(false);

      appear.setTexture(texture);

      appear.setTransparencyAttributes(
           new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.1f));

      Shape3D planeObj = new Shape3D(plane, appear);

	// rotate object has composited transformation matrix
	Transform3D rotate = new Transform3D();
	Transform3D tempRotate = new Transform3D();

        rotate.rotX(Math.PI/4.0d);
	tempRotate.rotY(Math.PI/5.0d);
        rotate.mul(tempRotate);

	TransformGroup objRotate = new TransformGroup(rotate);

	// Create the transform group node and initialize it to the
	// identity.  Enable the TRANSFORM_WRITE capability so that
	// our behavior code can modify it at runtime.  Add it to the
	// root of the subgraph.
	TransformGroup objSpin = new TransformGroup();
	objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        objRoot.addChild(objRotate);
	objRotate.addChild(objSpin);
 
	// Create a simple shape leaf node, add it to the scene graph.
	// ColorCube is a Convenience Utility class
        objSpin.addChild(planeObj);

	Transform3D yAxis = new Transform3D();
	Alpha rotationAlpha = new Alpha(-1, 4000);

	RotationInterpolator rotator =
	    new RotationInterpolator(rotationAlpha, objSpin, yAxis,
				     0.0f, (float) Math.PI*2.0f);

	// a bounding sphere specifies a region a behavior is active
	// create a sphere centered at the origin with radius of 1
	BoundingSphere bounds = new BoundingSphere();
	rotator.setSchedulingBounds(bounds);
	objSpin.addChild(rotator);
      Background background = new Background();
      background.setColor(1.0f, 1.0f, 1.0f);
      background.setApplicationBounds(new BoundingSphere());
      objRoot.addChild(background);

      return objRoot;
  }

  public SimpleTextureSpinApp (){
    setLayout(new BorderLayout());
    GraphicsConfiguration config =
       SimpleUniverse.getPreferredConfiguration();

    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);

    canvas3D.setStereoEnable(false);

    SimpleUniverse u = new SimpleUniverse(canvas3D);

    // This will move the ViewPlatform back a bit so the
    // objects in the scene can be viewed.
    u.getViewingPlatform().setNominalViewingTransform();

    u.addBranchGraph(createScene());
  }
  
  public static void main(String argv[])
  {
    System.out.print("SimpleTextureSpinApp.java \n- ");
    System.out.println("The simpliest example of using texture mapping.\n");
    System.out.println("This is a simple example progam from The Java 3D API Tutorial.");
    System.out.println("The Java 3D Tutorial is available on the web at:");
    System.out.println("http://java.sun.com/products/java-media/3D/collateral ");

    new MainFrame(new SimpleTextureSpinApp(), 256, 256);
  }
}

