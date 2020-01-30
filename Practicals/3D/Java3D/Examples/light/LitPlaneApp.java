/*
 *      @(#)LitPlaneApp.java 1.1 00/09/24 22:37
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
 * This program demonstrates the absence of automatic shadowing
 * in Java 3D.  The scene has a sphere above a plane lit from
 * above.  Theer is no shadow of the sphere on the plane.
 *
 * Note that no real time 3D graphics API provides automatic
 * shadowing.
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
import java.awt.Label;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class LitPlaneApp extends Applet {
  
  public class LitPlane extends Shape3D {
    LitPlane() {
        this.setGeometry(createGeometry());
        this.setAppearance(createAppearance());
    }    

    Geometry createGeometry(){

        QuadArray plane = new QuadArray(4, GeometryArray.COORDINATES
                                           | GeometryArray.NORMALS
                                           );

        Point3f p = new Point3f(-1.0f,  -0.5f,  -1.0f);
        plane.setCoordinate(0, p);
        p.set(-1.0f, -0.5f,  1.0f);
        plane.setCoordinate(1, p);
        p.set( 1.0f, -0.5f,  1.0f);
        plane.setCoordinate(2, p);
        p.set( 1.0f, -0.5f, -1.0f);
        plane.setCoordinate(3, p);

        Vector3f n = new Vector3f(0.0f, 1.0f, 0.0f);
        plane.setNormal(0, n);
        plane.setNormal(1, n);
        plane.setNormal(2, n);
        plane.setNormal(3, n);

        return plane;
    }

    Appearance createAppearance() {

        Appearance appear = new Appearance();
        Material material = new Material();
        // make modifications to default material properties
        appear.setMaterial(material);

        return appear;
    }

  } // end of LitPlane class


  public LitPlaneApp (){
    setLayout(new BorderLayout());
    GraphicsConfiguration config =
       SimpleUniverse.getPreferredConfiguration();

    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    
    BranchGroup scene = new BranchGroup();

    Shape3D plane = new LitPlane();
    scene.addChild(new LitPlane());
    scene.addChild(new Sphere(0.5f, Sphere.GENERATE_NORMALS, plane.getAppearance()));

    AmbientLight lightA = new AmbientLight();
    lightA.setInfluencingBounds(new BoundingSphere());
    scene.addChild(lightA);

    DirectionalLight lightD1 = new DirectionalLight();
    lightD1.setInfluencingBounds(new BoundingSphere(new Point3d(0.0,0.5,0.0),0.1));
    Vector3f direction = new Vector3f(-1.0f, -1.0f, -1.0f);
    direction.normalize();
    lightD1.setDirection(direction);
    lightD1.setColor(new Color3f(1.0f, 1.0f, 1.0f));
    scene.addChild(lightD1);

    SimpleUniverse u = new SimpleUniverse(canvas3D);

    // This will move the ViewPlatform back a bit so the
    // objects in the scene can be viewed.
    u.getViewingPlatform().setNominalViewingTransform();

    u.addBranchGraph(scene);
  }
  
  public static void main(String argv[])
  {
    new MainFrame(new LitPlaneApp(), 256, 256);
  }
}

