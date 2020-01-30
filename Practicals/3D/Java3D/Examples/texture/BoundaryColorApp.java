/*
 *      @(#)BoundaryColorApp.java 1.2 01/06/18
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
 * using the TexturedPlane class, this program displays four
 * TexturedPlanes with different TextureCoordinate settings
 * showing some of the possibilites.
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import TexturedPlane;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * BoundaryColorApp creates a single plane with texture mapping.
 */
public class BoundaryColorApp extends Applet {

    BranchGroup createScene() { 
      BranchGroup scene = new BranchGroup();

      Transform3D transform = new Transform3D();
      Shape3D texturedPlane = null;
      QuadArray texturedQuad = null;
      Appearance textureBundle = null;

      transform.setTranslation(new Vector3f(-3.2f, 0.0f, -8.0f));
      TransformGroup TG0 = new TransformGroup(transform);

      transform.setTranslation(new Vector3f(-1.1f, 0.0f, -8.0f));
      TransformGroup TG1 = new TransformGroup(transform);

      transform.setTranslation(new Vector3f( 1.1f, 0.0f, -8.0f));
      TransformGroup TG2 = new TransformGroup(transform);

      transform.setTranslation(new Vector3f( 3.2f, 0.0f, -8.0f));
      TransformGroup TG3 = new TransformGroup(transform);

      scene.addChild(TG0);
      texturedPlane = new TexturedPlane("stripe.gif");
      texturedPlane.getAppearance().getTexture().setBoundaryColor(1f, 0f, 0f, 0f);  // green
      texturedPlane.getAppearance().getTexture().setMinFilter(Texture.BASE_LEVEL_LINEAR);
      texturedPlane.getAppearance().getTexture().setMagFilter(Texture.BASE_LEVEL_LINEAR);
      TG0.addChild(texturedPlane);
      texturedQuad = (QuadArray) texturedPlane.getGeometry();
      texturedQuad.setTextureCoordinate( 0, 0, new TexCoord2f(-1.0f, 1.5f));
      texturedQuad.setTextureCoordinate( 0, 1, new TexCoord2f(-1.0f,-1.0f));
      texturedQuad.setTextureCoordinate( 0, 2, new TexCoord2f( 1.5f,-1.0f));
      texturedQuad.setTextureCoordinate( 0, 3, new TexCoord2f( 1.5f, 1.5f));
      
      scene.addChild(TG1);
      texturedPlane = new TexturedPlane("stripe.gif");
      texturedPlane.getAppearance().getTexture().setBoundaryColor(1f, 0f, 0f, 0f);  // red
      texturedPlane.getAppearance().getTexture().setMinFilter(Texture.BASE_LEVEL_LINEAR);
      texturedPlane.getAppearance().getTexture().setMagFilter(Texture.BASE_LEVEL_LINEAR);
      TG1.addChild(texturedPlane);
      texturedQuad = (QuadArray) texturedPlane.getGeometry();
      texturedQuad.setTextureCoordinate( 0, 0, new TexCoord2f(-1.0f, 1.5f));
      texturedQuad.setTextureCoordinate( 0, 1, new TexCoord2f(-1.0f,-1.0f));
      texturedQuad.setTextureCoordinate( 0, 2, new TexCoord2f( 1.5f,-1.0f));
      texturedQuad.setTextureCoordinate( 0, 3, new TexCoord2f( 1.5f, 1.5f));
      texturedPlane.getAppearance().getTexture().setBoundaryModeS(Texture.CLAMP);

      scene.addChild(TG2);
      texturedPlane = new TexturedPlane("stripe.gif");
      texturedPlane.getAppearance().getTexture().setBoundaryColor(1f, 0f, 0f, 0f);  // green
      texturedPlane.getAppearance().getTexture().setMinFilter(Texture.BASE_LEVEL_LINEAR);
      texturedPlane.getAppearance().getTexture().setMagFilter(Texture.BASE_LEVEL_LINEAR);
      TG2.addChild(texturedPlane);
      texturedQuad = (QuadArray) texturedPlane.getGeometry();
      texturedQuad.setTextureCoordinate( 0, 0, new TexCoord2f(-1.0f, 1.5f));
      texturedQuad.setTextureCoordinate( 0, 1, new TexCoord2f(-1.0f,-1.0f));
      texturedQuad.setTextureCoordinate( 0, 2, new TexCoord2f( 1.5f,-1.0f));
      texturedQuad.setTextureCoordinate( 0, 3, new TexCoord2f( 1.5f, 1.5f));
      texturedPlane.getAppearance().getTexture().setBoundaryModeT(Texture.CLAMP);
      
      scene.addChild(TG3);
      texturedPlane = new TexturedPlane("stripe.gif");
      texturedPlane.getAppearance().getTexture().setBoundaryColor(1f, 0f, 0f, 0f);  // red
      texturedPlane.getAppearance().getTexture().setMinFilter(Texture.BASE_LEVEL_LINEAR);
      texturedPlane.getAppearance().getTexture().setMagFilter(Texture.BASE_LEVEL_LINEAR);
      TG3.addChild(texturedPlane);
      texturedQuad = (QuadArray) texturedPlane.getGeometry();
      texturedQuad.setTextureCoordinate( 0, 0, new TexCoord2f(-1.0f, 1.5f));
      texturedQuad.setTextureCoordinate( 0, 1, new TexCoord2f(-1.0f,-1.0f));
      texturedQuad.setTextureCoordinate( 0, 2, new TexCoord2f( 1.5f,-1.0f));
      texturedQuad.setTextureCoordinate( 0, 3, new TexCoord2f( 1.5f, 1.5f));
      texturedPlane.getAppearance().getTexture().setBoundaryModeS(Texture.CLAMP);
      texturedPlane.getAppearance().getTexture().setBoundaryModeT(Texture.CLAMP);

      Background background = new Background();
      background.setColor(0.1f, 1.0f, 0.1f);
      background.setApplicationBounds(new BoundingSphere());
      scene.addChild(background);

      return scene;
  }

  public BoundaryColorApp (){
    setLayout(new BorderLayout());
    GraphicsConfiguration config =
       SimpleUniverse.getPreferredConfiguration();

    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    NewTextureLoader.setImageObserver(canvas3D);

    canvas3D.setStereoEnable(false);

    SimpleUniverse u = new SimpleUniverse(canvas3D);

    // This will move the ViewPlatform back a bit so the
    // objects in the scene can be viewed.
    u.getViewingPlatform().setNominalViewingTransform();

    u.addBranchGraph(createScene());
  }
  
  public static void main(String argv[])
  {
    System.out.println("BoundaryColorApp.java");
    System.out.println("A demonstration of texturing possibilites.");
    System.out.println("This is a simple example progam from The Java 3D API Tutorial.");
    System.out.println("The Java 3D Tutorial is available on the web at:");
    System.out.println("http://java.sun.com/products/java-media/3D/collateral ");

    new MainFrame(new BoundaryColorApp(), 512, 128);
  }
}

