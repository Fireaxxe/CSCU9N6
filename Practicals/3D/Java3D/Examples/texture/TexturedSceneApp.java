/*
 *      @(#)TexturedSceneApp.java 1.2 01/06/18
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
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.image.TextureLoader;
import NewTextureLoader;
import TexturedPlane;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * TexturedSceneApp creates a single plane with texture mapping.
 */
public class TexturedSceneApp extends Applet {

    BranchGroup createScene() { 
      BranchGroup scene = new BranchGroup();

      Transform3D transform = new Transform3D();

      transform.setTranslation(new Vector3f(-2.0f, -1.5f, -6.0f));
      TransformGroup TG0 = new TransformGroup(transform);

      transform.setTranslation(new Vector3f(0.0f, -2.0f, -10.0f));
      TransformGroup TG1 = new TransformGroup(transform);

      transform.setTranslation(new Vector3f(0.0f, -3.0f, -20.0f));
      TransformGroup TG2 = new TransformGroup(transform);

      scene.addChild(TG0);
      Shape3D treeObj = new TexturedPlane("tree.gif");
      TG0.addChild(treeObj);

      scene.addChild(TG2);
      Shape3D skyObj = new TexturedPlane("clouds.gif");
      TG2.addChild(skyObj);

      Background background = new Background();
      background.setColor(0.1f, 1.0f, 0.1f);
      background.setApplicationBounds(new BoundingSphere());
      scene.addChild(background);

      QuadArray plane = new QuadArray(4, GeometryArray.COORDINATES
                                      | GeometryArray.TEXTURE_COORDINATE_2
                                      );

      Point3f p = new Point3f(-10.0f,  10.0f, 0.0f);
      plane.setCoordinate(0, p);
      p.set(-10.0f, 0.0f,  0.0f);
      plane.setCoordinate(1, p);
      p.set(10.0f,   0.0f,  0.0f);
      plane.setCoordinate(2, p);
      p.set(10.0f,  10.0f,  0.0f);
      plane.setCoordinate(3, p);

      TexCoord2f q = new TexCoord2f( 0.0f,  1.0f);
      plane.setTextureCoordinate(0, 0, q);
      q.set(0.0f, 0.0f);
      plane.setTextureCoordinate(0, 1, q);
      q.set(1.0f, 0.0f);
      plane.setTextureCoordinate(0, 2, q);
      q.set(1.0f, 1.0f);
      plane.setTextureCoordinate(0, 3, q);

      skyObj.setGeometry(plane);

      plane = new QuadArray(4, GeometryArray.COORDINATES
                               | GeometryArray.TEXTURE_COORDINATE_2
                           );

      p.set(-0.5f, 2.0f, 0.0f);
      plane.setCoordinate(0, p);
      p.set(-0.5f, 0.0f,  0.0f);
      plane.setCoordinate(1, p);
      p.set( 0.5f, 0.0f,  0.0f);
      plane.setCoordinate(2, p);
      p.set( 0.5f, 2.0f,  0.0f);
      plane.setCoordinate(3, p);

      q.set( 0.0f, 1.0f);
      plane.setTextureCoordinate(0, 0, q);
      q.set(0.0f, 0.0f);
      plane.setTextureCoordinate(0, 1, q);
      q.set(1.0f, 0.0f);
      plane.setTextureCoordinate(0, 2, q);
      q.set(1.0f, 1.0f);
      plane.setTextureCoordinate(0, 3, q);

      treeObj.setGeometry(plane);



      Transform3D t3D = new Transform3D();
      t3D.setTranslation(new Vector3f(0.0f, 0.0f, -3.0f));
      TransformGroup objMove = new TransformGroup(t3D);
      scene.addChild(objMove);
 
      // Create the transform group node and initialize it to the 
      // identity. Add it to the root of the subgraph.
      TransformGroup objSpin = new TransformGroup();
      objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      objMove.addChild(objSpin);

      Appearance textAppear = new Appearance();

      PolygonAttributes polyAttrib = new PolygonAttributes();
      polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
      textAppear.setPolygonAttributes(polyAttrib);

      TexCoordGeneration tcg = new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,
                                                      TexCoordGeneration.TEXTURE_COORDINATE_2);
      textAppear.setTexCoordGeneration(tcg);

      String filename = "brick.gif";
      System.out.println("attempt to load texture from file: "+filename);
      NewTextureLoader loader = new NewTextureLoader(filename);
      ImageComponent2D image = loader.getImage();
      if(image == null) {
          System.out.println("load failed for texture: "+filename);
      }

      Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                    image.getWidth(), image.getHeight());
      texture.setImage(0, image);
      texture.setEnable(true);

      textAppear.setTexture(texture);

 
      // Create a simple shape leaf node, add it to the scene graph.
      Font3D font3D = new Font3D(new Font("Helvetica", Font.PLAIN, 1),
                                 new FontExtrusion());
      Text3D textGeom = new Text3D(font3D, new String("Textures"));
      textGeom.setAlignment(Text3D.ALIGN_CENTER);
      Shape3D textShape = new Shape3D();
      textShape.setGeometry(textGeom);
      textShape.setAppearance(textAppear);
      objSpin.addChild(textShape);

      // Create a new Behavior object that will perform the desired
      // operation on the specified transform object and add it into
      // the scene graph.
      Alpha rotationAlpha = new Alpha(-1, 10000);
  
      RotationInterpolator rotator =
                 new RotationInterpolator(rotationAlpha, objSpin);
 
      // a bounding sphere specifies a region a behavior is active
      // create a sphere centered at the origin with radius of 100
      BoundingSphere bounds = new BoundingSphere();
      rotator.setSchedulingBounds(bounds);
      objSpin.addChild(rotator);

      return scene;
  }

  public TexturedSceneApp (){
    setLayout(new BorderLayout());
    GraphicsConfiguration config =
       SimpleUniverse.getPreferredConfiguration();

    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    canvas3D.setStereoEnable(false);
    NewTextureLoader.setImageObserver(canvas3D);

    SimpleUniverse u = new SimpleUniverse(canvas3D);

    // This will move the ViewPlatform back a bit so the
    // objects in the scene can be viewed.
    u.getViewingPlatform().setNominalViewingTransform();

    u.addBranchGraph(createScene());
  }
  
  public static void main(String argv[])
  {
    System.out.print("TexturedSceneApp.java \n ");
    System.out.println("This is an example progam from The Java 3D API Tutorial.");
    System.out.println("The Java 3D Tutorial is available on the web.");

    new MainFrame(new TexturedSceneApp(), 512, 256);
  }
}

