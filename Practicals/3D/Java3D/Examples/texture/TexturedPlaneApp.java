/*
 *      @(#)TexturedPlaneApp.java 1.1 00/09/25 02:09
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
 * TexturedPlaneApp creates a single plane with texture mapping.
 */
public class TexturedPlaneApp extends Applet {

    BranchGroup createScene() { 
      BranchGroup scene = new BranchGroup();

      Transform3D transform = new Transform3D();

      transform.setTranslation(new Vector3f(0.0f, 0.0f, -6.0f));
      TransformGroup TG0 = new TransformGroup(transform);

      transform.setTranslation(new Vector3f(-2.2f, 0.0f, 0.0f));
      TransformGroup TG1 = new TransformGroup(transform);

      transform.rotX(-0.35f);
      TransformGroup TG2 = new TransformGroup(transform);

      transform.setTranslation(new Vector3f(2.2f, 0.0f, 0.0f));
      TransformGroup TG3 = new TransformGroup(transform);
      transform.rotX(-0.7f);
      TransformGroup TG4 = new TransformGroup(transform);

      scene.addChild(TG0);
      TG0.addChild(TG1);
      TG1.addChild(new TexturedPlane("stripe.gif"));

      TG0.addChild(TG2);
      TG2.addChild(new TexturedPlane("brick.gif"));

      TG0.addChild(TG3);
      TG3.addChild(TG4);
      TG4.addChild(new TexturedPlane("earth.jpg"));

      Background background = new Background();
      background.setColor(1.0f, 1.0f, 1.0f);
      background.setApplicationBounds(new BoundingSphere());
      scene.addChild(background);

      return scene;
  }

  public TexturedPlaneApp (){
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
    System.out.print("TexturedPlaneApp.java \n- ");
    System.out.println();
    System.out.println("This is a simple example progam from The Java 3D API Tutorial.");
    System.out.println("The Java 3D Tutorial is available on the web at:");
    System.out.println("http://java.sun.com/products/java-media/3D/collateral");

    new MainFrame(new TexturedPlaneApp(), 512, 256);
  }
}

