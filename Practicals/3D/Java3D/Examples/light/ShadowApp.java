/*
 *       @(#)ShadowApp.java 1.1 00/09/22 22:35
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
 * This application demonstrates how a programmer can create shadows
 * in Java 3D automatically.
 *
 * The class SimpleShadow produces shadow polygons for simple visual
 * objects in certain situations.
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
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * ShadowApp creates a single plane 
 */
public class ShadowApp extends Applet {
  
  public class LitQuad extends Shape3D {
    LitQuad(Point3f A, Point3f B, Point3f C, Point3f D) {
        this.setGeometry(createGeometry(A, B, C, D));
        this.setAppearance(createAppearance());
    }    

    Geometry createGeometry(Point3f A, Point3f B, Point3f C, Point3f D){

        QuadArray plane = new QuadArray(4, GeometryArray.COORDINATES
                                           | GeometryArray.NORMALS
                                           );

        plane.setCoordinate(0, A);
        plane.setCoordinate(1, B);
        plane.setCoordinate(2, C);
        plane.setCoordinate(3, D);

        Vector3f a = new Vector3f(A.x - B.x, A.y - B.y, A.z - B.z);
        Vector3f b = new Vector3f(C.x - B.x, C.y - B.y, C.z - B.z);
        Vector3f n = new Vector3f();
        n.cross(b, a);

        n.normalize();

        plane.setNormal(0, n);
        plane.setNormal(1, n);
        plane.setNormal(2, n);
        plane.setNormal(3, n);

        return plane;
    }

    Appearance createAppearance() {
        Appearance appear = new Appearance();
        Material material = new Material();
        appear.setMaterial(material);

        return appear;
    }

  }

  public class SimpleShadow extends Shape3D {

    SimpleShadow(GeometryArray geom, Vector3f direction,
                 Color3f col, float height) {

        int vCount = geom.getVertexCount();
        QuadArray poly = new QuadArray(vCount, GeometryArray.COORDINATES
                                           | GeometryArray.COLOR_3
                                           );

        int v;
        Point3f vertex = new Point3f();
        Point3f shadow = new Point3f();
        for (v = 0; v < vCount; v++) {
            geom.getCoordinate(v, vertex);
            shadow.set( vertex.x + (vertex.y-height) * direction.x,
                        height + 0.0001f,
                        vertex.z + (vertex.y-height) * direction.y);
            poly.setCoordinate(v, shadow);
            poly.setColor(v, col);
        }

        this.setGeometry(poly);
    }

  } // end of PolyFour class


  public ShadowApp (){
    setLayout(new BorderLayout());
    GraphicsConfiguration config =
       SimpleUniverse.getPreferredConfiguration();

    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    
    BranchGroup scene = new BranchGroup();

    Shape3D plane = new LitQuad(new Point3f(-0.3f, 0.6f, 0.2f),
                                new Point3f(-0.3f,-0.3f, 0.2f),
                                new Point3f( 0.6f,-0.3f, -0.3f),
                                new Point3f( 0.6f, 0.6f, -0.3f));
    scene.addChild(plane);

    Shape3D floor = new LitQuad(new Point3f(-1.0f, -0.5f, -1.0f),
                                new Point3f(-1.0f, -0.5f,  1.0f),
                                new Point3f( 1.0f, -0.5f,  1.0f),
                                new Point3f( 1.0f, -0.5f, -1.0f));
    scene.addChild(floor);


    AmbientLight lightA = new AmbientLight();
    lightA.setInfluencingBounds(new BoundingSphere());
    scene.addChild(lightA);

    DirectionalLight lightD1 = new DirectionalLight();
    lightD1.setInfluencingBounds(new BoundingSphere());
    Vector3f direction = new Vector3f(-1.0f, -1.0f, -1.0f);
    direction.normalize();
    lightD1.setDirection(direction);
    scene.addChild(lightD1);

    Shape3D shadow = new SimpleShadow((GeometryArray) plane.getGeometry(),
                                      direction,
                                      new Color3f( 0.2f, 0.2f,  0.2f),
                                      -0.5f);
    scene.addChild(shadow);


    SimpleUniverse u = new SimpleUniverse(canvas3D);

    // This will move the ViewPlatform back a bit so the
    // objects in the scene can be viewed.
    u.getViewingPlatform().setNominalViewingTransform();

    u.addBranchGraph(scene);
  }
  
  public static void main(String argv[])
  {
    new MainFrame(new ShadowApp(), 256, 256);
  }
}

