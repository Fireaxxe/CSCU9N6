/*
 *      @(#)LightScopeApp.java 1.1 00/09/24 22:37
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
 * This application demonstrates the use of scoping for controlling
 * the influence of lights.
 *
 * This applicaion is not necessarilly a demonstration of good
 * program design or coding.
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

/**
 * LightScopeApp creates a scene that is paritally light by a light
 * using the scoping feature.
 */
public class LightScopeApp extends Applet {
  

  Appearance createMaterialAppearance(Color3f color) {
    Appearance appear = new Appearance();
    Material material = new Material();
    material.setDiffuseColor(color);
    material.setShininess(50.0f);
    appear.setMaterial(material);

    return appear;
  }

  Shape3D createXZPlane(Point3f p0, Point3f p1, Point3f p2, Point3f p3){
    Shape3D plane = new Shape3D();
    QuadArray planeGeom = new QuadArray(4,QuadArray.COORDINATES|QuadArray.NORMALS );
    planeGeom.setCoordinate(0, p0);
    planeGeom.setCoordinate(1, p1);
    planeGeom.setCoordinate(2, p2);
    planeGeom.setCoordinate(3, p3);
    Vector3f norm = new Vector3f(0.0f, 1.0f, 0.0f);
    planeGeom.setNormal(0, norm);
    planeGeom.setNormal(1, norm);
    planeGeom.setNormal(2, norm);
    planeGeom.setNormal(3, norm);
    plane.setGeometry(planeGeom);
    return plane;
  }

  Shape3D createLampShape(){
    Shape3D lamp = new Shape3D();
    int stripCounts[] = {10, 10};
    TriangleStripArray lampGeom = new TriangleStripArray(20,
                                      GeometryArray.COORDINATES|GeometryArray.NORMALS,
                                      stripCounts);
    lampGeom.setCoordinate(0, new Point3f(-0.01f, 0.9f, 0.01f));
    lampGeom.setCoordinate(1, new Point3f(-0.01f, 0.0f, 0.01f));
    lampGeom.setCoordinate(2, new Point3f( 0.01f, 0.9f, 0.01f));
    lampGeom.setCoordinate(3, new Point3f( 0.01f, 0.0f, 0.01f));
    lampGeom.setCoordinate(4, new Point3f( 0.01f, 0.9f,-0.01f));
    lampGeom.setCoordinate(5, new Point3f( 0.01f, 0.0f,-0.01f));
    lampGeom.setCoordinate(6, new Point3f(-0.01f, 0.9f,-0.01f));
    lampGeom.setCoordinate(7, new Point3f(-0.01f, 0.0f,-0.01f));
    lampGeom.setCoordinate(8, new Point3f(-0.01f, 0.9f, 0.01f));
    lampGeom.setCoordinate(9, new Point3f(-0.01f, 0.0f, 0.01f));
    lampGeom.setCoordinate(10, new Point3f(-0.1f, 0.9f, 0.1f));
    lampGeom.setCoordinate(11, new Point3f(-0.2f, 0.5f, 0.2f));
    lampGeom.setCoordinate(12, new Point3f( 0.1f, 0.9f, 0.1f));
    lampGeom.setCoordinate(13, new Point3f( 0.2f, 0.5f, 0.2f));
    lampGeom.setCoordinate(14, new Point3f( 0.1f, 0.9f,-0.1f));
    lampGeom.setCoordinate(15, new Point3f( 0.2f, 0.5f,-0.2f));
    lampGeom.setCoordinate(16, new Point3f(-0.1f, 0.9f,-0.1f));
    lampGeom.setCoordinate(17, new Point3f(-0.2f, 0.5f,-0.2f));
    lampGeom.setCoordinate(18, new Point3f(-0.1f, 0.9f, 0.1f));
    lampGeom.setCoordinate(19, new Point3f(-0.2f, 0.5f, 0.2f));

    Vector3f norm = new Vector3f(-0.7f, 0.0f, 0.7f);
    lampGeom.setNormal(0, norm);
    lampGeom.setNormal(1, norm);
    norm.set(0.7f, 0.0f, 0.7f);
    lampGeom.setNormal(2, norm);
    lampGeom.setNormal(3, norm);
    norm.set(0.7f, 0.0f, -0.7f);
    lampGeom.setNormal(4, norm);
    lampGeom.setNormal(5, norm);
    norm.set(-0.7f, 0.0f, -0.7f);
    lampGeom.setNormal(6, norm);
    lampGeom.setNormal(7, norm);
    norm.set(-0.7f, 0.0f, 0.7f);
    lampGeom.setNormal(8, norm);
    lampGeom.setNormal(9, norm);
    norm.set(-0.7f, 0.0f, 0.7f);
    lampGeom.setNormal(10, norm);
    lampGeom.setNormal(11, norm);
    norm.set(0.7f, 0.0f, 0.7f);
    lampGeom.setNormal(12, norm);
    lampGeom.setNormal(13, norm);
    norm.set(0.7f, 0.0f, -0.7f);
    lampGeom.setNormal(14, norm);
    lampGeom.setNormal(15, norm);
    norm.set(-0.7f, 0.0f, -0.7f);
    lampGeom.setNormal(16, norm);
    lampGeom.setNormal(17, norm);
    norm.set(-0.7f, 0.0f, 0.7f);
    lampGeom.setNormal(18, norm);
    lampGeom.setNormal(19, norm);

    lamp.setGeometry(lampGeom);
    return lamp;
  }

  BranchGroup createScene(){
    BranchGroup scene  = new BranchGroup();
    TransformGroup tableTG    = new TransformGroup();
    TransformGroup lampTG     = new TransformGroup();
    TransformGroup litBoxTG   = new TransformGroup();
    TransformGroup unLitBoxTG = new TransformGroup();

    scene.addChild(tableTG);
    tableTG.addChild(lampTG);
    tableTG.addChild(litBoxTG);
    tableTG.addChild(unLitBoxTG);

    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
    Color3f red   = new Color3f(1.0f, 0.0f, 0.0f);
    Color3f blue  = new Color3f(0.0f, 1.0f, 0.0f);
    Color3f green = new Color3f(0.0f, 0.0f, 1.0f);
    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);

    Vector3f transVector = new Vector3f();
    Transform3D transTransform = new Transform3D();

    transVector.set(0.0f, -0.4f, 0.5f);
    transTransform.setTranslation(transVector);
    tableTG.setTransform(transTransform);

    transVector.set(-0.4f, 0.001f, 0.1f);
    transTransform.setTranslation(transVector);
    lampTG.setTransform(transTransform);

    transVector.set(-0.2f, 0.1f, 0.2f);
    transTransform.setTranslation(transVector);
    litBoxTG.setTransform(transTransform);

    transVector.set(0.3f, 0.1f, -0.4f);
    transTransform.setTranslation(transVector);
    unLitBoxTG.setTransform(transTransform);

    Shape3D tablePlane = createXZPlane(new Point3f(-1.0f, 0.0f,-1.0f ),
                                       new Point3f(-1.0f, 0.0f, 1.0f ),
                                       new Point3f( 1.0f, 0.0f, 1.0f ),
                                       new Point3f( 1.0f, 0.0f,-1.0f ));
    tablePlane.setAppearance(createMaterialAppearance(white));
    tableTG.addChild(tablePlane);
    litBoxTG.addChild(new Box(0.1f, 0.1f, 0.1f,
                              Box.GENERATE_NORMALS,
                              createMaterialAppearance(red)));
    Shape3D shadowPlane = createXZPlane(new Point3f( 0.1f,-0.095f,-0.1f ),
                                        new Point3f( 0.1f,-0.095f, 0.1f ),
                                        new Point3f( 0.2f,-0.095f, 0.15f ),
                                        new Point3f( 0.2f,-0.095f,-0.15f ));
    shadowPlane.setAppearance(createMaterialAppearance(black));
    litBoxTG.addChild(shadowPlane);

    Appearance redGlowMat = createMaterialAppearance(red);
//    redGlowMat.getMaterial().setEmissiveColor(0.5f, 0.5f, 0.5f);
    unLitBoxTG.addChild(new Box(0.1f, 0.1f, 0.1f, Box.GENERATE_NORMALS, redGlowMat));

    Shape3D lamp = createLampShape();
    Appearance lampAppearance = createMaterialAppearance(blue);
    PolygonAttributes polyAttrib = new PolygonAttributes();
    polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
    polyAttrib.setBackFaceNormalFlip(true);
    lampAppearance.setPolygonAttributes(polyAttrib);
    lamp.setAppearance(lampAppearance);
    lampTG.addChild(lamp);

    PointLight lampLight = new PointLight();
    lampLight.setPosition(0.1f, 0.5f, -0.1f);
    lampLight.setInfluencingBounds(new BoundingSphere());
    lampTG.addChild(lampLight);

    Shape3D litPlane = createXZPlane(new Point3f(-0.4f, 0.0f, -0.4f),
                                     new Point3f(-0.4f, 0.0f,  0.4f),
                                     new Point3f( 0.4f, 0.0f,  0.4f),
                                     new Point3f( 0.4f, 0.0f, -0.4f));
    litPlane.setAppearance(createMaterialAppearance(white));
    lampTG.addChild(litPlane);

    lampLight.addScope(lampTG);
    lampLight.addScope(litBoxTG);

    AmbientLight lightA = new AmbientLight();
    lightA.setInfluencingBounds(new BoundingSphere());
    scene.addChild(lightA);

    DirectionalLight lightD1 = new DirectionalLight();
    lightD1.setInfluencingBounds(new BoundingSphere());
    lightD1.setColor(new Color3f(0.4f, 0.4f, 0.4f));
    Vector3f lightDir = new Vector3f(-1.0f, -1.0f, -1.0f);
    lightDir.normalize();
    lightD1.setDirection(lightDir);
    scene.addChild(lightD1);

    DirectionalLight lightD2 = new DirectionalLight();
    lightD2.setInfluencingBounds(new BoundingSphere());
    lightD2.setColor(new Color3f(0.2f, 0.2f, 0.2f));
    lightDir.set(1.0f, -1.0f, -1.0f);
    lightDir.normalize();
    lightD2.setDirection(lightDir);
    scene.addChild(lightD2);

    Background bg = new Background();
    bg.setColor(1.0f, 1.0f, 1.0f);
    bg.setApplicationBounds(new BoundingSphere());
    scene.addChild(bg);

    return scene;
  }

  public LightScopeApp (){
    setLayout(new BorderLayout());
    GraphicsConfiguration config =
       SimpleUniverse.getPreferredConfiguration();

    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    
    BranchGroup scene = createScene();
    scene.compile();

    SimpleUniverse u = new SimpleUniverse(canvas3D);

    // This will move the ViewPlatform back a bit so the
    // objects in the scene can be viewed.
    u.getViewingPlatform().setNominalViewingTransform();

    u.addBranchGraph(scene);
  }
  
  public static void main(String argv[])
  {
    new MainFrame(new LightScopeApp(), 256, 256);
  }
}

