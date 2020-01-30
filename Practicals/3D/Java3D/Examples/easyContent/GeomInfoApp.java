/*
 *      @(#)GeomInfoApp.java 1.1 00/09/22 14:03
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
 *  GeomInfoApp.java demonstrates the use of the GeometryInfo class
 *  and related classes.
 *
 *  This program creates a car shape (not a fancy car, though) using
 *  GeometryInfo objects.  One GeometryInfo object specifies the side
 *  of the car using a polygon (not a triangle, nor a quad).  The
 *  GeometryInfo and other classes convert the polygons into a triangle
 *  strips with normals.
 *
 *  Note that about half of the source code just specifies the
 *  input data to the GeometryInfo objects.  The interesting part
 *  starts around line 210.
 *
 *  An alternative data set is provided (in the comments) for further
 *  experimentation - see the tutorial text.
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;


public class GeomInfoApp extends Applet {

    float[] createCoordinateData() {
        float[] data = new float[69*3];         // ******
        int i = 0;

        data[i++]= -1.3f; data[i++]= -0.3f; data[i++]= 0.3f; //0
        data[i++]= -0.9f; data[i++]= -0.3f; data[i++]= 0.3f; //1
        data[i++]= -0.8f; data[i++]= -0.1f; data[i++]= 0.3f; //2
        data[i++]= -0.6f; data[i++]= -0.1f; data[i++]= 0.3f; //3
        data[i++]= -0.5f; data[i++]= -0.3f; data[i++]= 0.3f; //4
        data[i++]=  0.2f; data[i++]= -0.3f; data[i++]= 0.3f; //5
        data[i++]=  0.3f; data[i++]= -0.1f; data[i++]= 0.3f; //6
        data[i++]=  0.5f; data[i++]= -0.1f; data[i++]= 0.3f; //7
        data[i++]=  0.6f; data[i++]= -0.3f; data[i++]= 0.3f; //8
        data[i++]=  1.3f; data[i++]= -0.3f; data[i++]= 0.3f; //9
        data[i++]=  1.2f; data[i++]= -0.1f; data[i++]= 0.3f; //10
        data[i++]=  0.5f; data[i++]=  0.0f; data[i++]= 0.3f; //11
        data[i++]=  0.1f; data[i++]=  0.3f; data[i++]= 0.3f; //12
        data[i++]= -0.5f; data[i++]=  0.3f; data[i++]= 0.3f; //13
        data[i++]= -1.1f; data[i++]=  0.0f; data[i++]= 0.3f; //14
        data[i++]= -1.3f; data[i++]=  0.0f; data[i++]= 0.3f; //15
        data[i++]= -1.3f; data[i++]= -0.3f; data[i++]= 0.3f; //16
        System.out.println("end polygon; total vertex count: "+i/3);

        data[i++]= -1.3f; data[i++]= -0.3f; data[i++]=-0.3f; // 0 17
        data[i++]= -1.3f; data[i++]=  0.0f; data[i++]=-0.3f; // 1 18
        data[i++]= -1.1f; data[i++]=  0.0f; data[i++]=-0.3f; // 2 19
        data[i++]= -0.5f; data[i++]=  0.3f; data[i++]=-0.3f; // 3 20
        data[i++]=  0.1f; data[i++]=  0.3f; data[i++]=-0.3f; // 4 21
        data[i++]=  0.5f; data[i++]=  0.0f; data[i++]=-0.3f; // 5 22
        data[i++]=  1.2f; data[i++]= -0.1f; data[i++]=-0.3f; // 6 23
        data[i++]=  1.3f; data[i++]= -0.3f; data[i++]=-0.3f; // 7 24
        data[i++]=  0.6f; data[i++]= -0.3f; data[i++]=-0.3f; // 8 25
        data[i++]=  0.5f; data[i++]= -0.1f; data[i++]=-0.3f; // 9 26
        data[i++]=  0.3f; data[i++]= -0.1f; data[i++]=-0.3f; //10 27
        data[i++]=  0.2f; data[i++]= -0.3f; data[i++]=-0.3f; //11 28
        data[i++]= -0.5f; data[i++]= -0.3f; data[i++]=-0.3f; //12 29
        data[i++]= -0.6f; data[i++]= -0.1f; data[i++]=-0.3f; //13 30
        data[i++]= -0.8f; data[i++]= -0.1f; data[i++]=-0.3f; //14 31
        data[i++]= -0.9f; data[i++]= -0.3f; data[i++]=-0.3f; //15 32
        data[i++]= -1.3f; data[i++]= -0.3f; data[i++]=-0.3f; //16 33
        System.out.println("end polygon; total vertex count: "+i/3);
                                                                  
        data[i++]=  1.3f; data[i++]= -0.3f; data[i++]=-0.3f; // 0 34
        data[i++]=  1.2f; data[i++]= -0.1f; data[i++]=-0.3f; // 1 35
        data[i++]=  1.2f; data[i++]= -0.1f; data[i++]= 0.3f; // 2 36
        data[i++]=  1.3f; data[i++]= -0.3f; data[i++]= 0.3f; // 3 37
        data[i++]=  1.3f; data[i++]= -0.3f; data[i++]=-0.3f; // 4 38
        System.out.println("end polygon; total vertex count: "+i/3);

        data[i++]=  1.2f; data[i++]= -0.1f; data[i++]=-0.3f; // 0 39
        data[i++]=  0.5f; data[i++]=  0.0f; data[i++]=-0.3f; // 1 40
        data[i++]=  0.5f; data[i++]=  0.0f; data[i++]= 0.3f; // 2 41
        data[i++]=  1.2f; data[i++]= -0.1f; data[i++]= 0.3f; // 3 42
        data[i++]=  1.2f; data[i++]= -0.1f; data[i++]=-0.3f; // 4 43
        System.out.println("end polygon; total vertex count: "+i/3);

        data[i++]=  0.5f; data[i++]=  0.0f; data[i++]=-0.3f; // 0 44
        data[i++]=  0.1f; data[i++]=  0.3f; data[i++]=-0.3f; // 1 45
        data[i++]=  0.1f; data[i++]=  0.3f; data[i++]= 0.3f; // 2 46
        data[i++]=  0.5f; data[i++]=  0.0f; data[i++]= 0.3f; // 3 47
        data[i++]=  0.5f; data[i++]=  0.0f; data[i++]=-0.3f; // 4 48
        System.out.println("end polygon; total vertex count: "+i/3);

        data[i++]=  0.1f; data[i++]=  0.3f; data[i++]=-0.3f; // 0 49
        data[i++]= -0.5f; data[i++]=  0.3f; data[i++]=-0.3f; // 1 50
        data[i++]= -0.5f; data[i++]=  0.3f; data[i++]= 0.3f; // 2 51
        data[i++]=  0.1f; data[i++]=  0.3f; data[i++]= 0.3f; // 3 52
        data[i++]=  0.1f; data[i++]=  0.3f; data[i++]=-0.3f; // 4 53
        System.out.println("end polygon; total vertex count: "+i/3);

        data[i++]= -0.5f; data[i++]=  0.3f; data[i++]=-0.3f; // 0 54
        data[i++]= -1.1f; data[i++]=  0.0f; data[i++]=-0.3f; // 1 55
        data[i++]= -1.1f; data[i++]=  0.0f; data[i++]= 0.3f; // 2 56
        data[i++]= -0.5f; data[i++]=  0.3f; data[i++]= 0.3f; // 3 57
        data[i++]= -0.5f; data[i++]=  0.3f; data[i++]=-0.3f; // 4 58
        System.out.println("end polygon; total vertex count: "+i/3);

        data[i++]= -1.1f; data[i++]=  0.0f; data[i++]=-0.3f; // 0 59
        data[i++]= -1.3f; data[i++]=  0.0f; data[i++]=-0.3f; // 1 60
        data[i++]= -1.3f; data[i++]=  0.0f; data[i++]= 0.3f; // 2 61
        data[i++]= -1.1f; data[i++]=  0.0f; data[i++]= 0.3f; // 3 62
        data[i++]= -1.1f; data[i++]=  0.0f; data[i++]=-0.3f; // 4 63
        System.out.println("end polygon; total vertex count: "+i/3);
                                                                    
        data[i++]= -1.3f; data[i++]=  0.0f; data[i++]=-0.3f; // 0 64
        data[i++]= -1.3f; data[i++]= -0.3f; data[i++]=-0.3f; // 1 65
        data[i++]= -1.3f; data[i++]= -0.3f; data[i++]= 0.3f; // 2 66
        data[i++]= -1.3f; data[i++]=  0.0f; data[i++]= 0.3f; // 3 67
        data[i++]= -1.3f; data[i++]=  0.0f; data[i++]=-0.3f; // 4 68
        System.out.println("end polygon; total vertex count: "+i/3);

// ****** This is the data for the hood, roof, trunk, front and rear glass
// ****** remove the comments markers below (slash-star) and (star slash)
// ****** and add the appropriate comment markers above.
// ****** modification of other lines of code is necessary to use this data
// ****** one line above and two lines below
/*
        data[i++]=  1.3f; data[i++]= -0.3f; data[i++]=-0.3f; // 0  35 
        data[i++]=  1.2f; data[i++]= -0.1f; data[i++]=-0.3f; // 1  36
        data[i++]=  0.5f; data[i++]=  0.0f; data[i++]=-0.3f; // 2  37
        data[i++]=  0.1f; data[i++]=  0.3f; data[i++]=-0.3f; // 3  38
        data[i++]= -0.5f; data[i++]=  0.3f; data[i++]=-0.3f; // 4  39
        data[i++]= -1.1f; data[i++]=  0.0f; data[i++]=-0.3f; // 5  40
        data[i++]= -1.3f; data[i++]=  0.0f; data[i++]=-0.3f; // 6  41
        data[i++]= -1.3f; data[i++]= -0.3f; data[i++]=-0.3f; // 7  42
        data[i++]= -1.3f; data[i++]= -0.3f; data[i++]= 0.3f; // 8  43
        data[i++]= -1.3f; data[i++]=  0.0f; data[i++]= 0.3f; // 9  44
        data[i++]= -1.1f; data[i++]=  0.0f; data[i++]= 0.3f; // 10 45
        data[i++]= -0.5f; data[i++]=  0.3f; data[i++]= 0.3f; // 11 46
        data[i++]=  0.1f; data[i++]=  0.3f; data[i++]= 0.3f; // 12 47
        data[i++]=  0.5f; data[i++]=  0.0f; data[i++]= 0.3f; // 13 48
        data[i++]=  1.2f; data[i++]= -0.1f; data[i++]= 0.3f; // 14 49
        data[i++]=  1.3f; data[i++]= -0.3f; data[i++]= 0.3f; // 15 50
        data[i++]=  1.3f; data[i++]= -0.3f; data[i++]=-0.3f; // 16 51
        System.out.println("end polygon; total vertex count: "+i/3);
*/
// ****** end of the alternative polygon data
        return data;
    }
            

    Appearance createMaterialAppearance(){

        Appearance materialAppear = new Appearance();
        PolygonAttributes polyAttrib = new PolygonAttributes();
        polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
        materialAppear.setPolygonAttributes(polyAttrib);

        Material material = new Material();
        material.setDiffuseColor(new Color3f(1.0f, 0.0f, 0.0f));
        materialAppear.setMaterial(material);

        return materialAppear;
    }

    Appearance createWireFrameAppearance(){

        Appearance materialAppear = new Appearance();
        PolygonAttributes polyAttrib = new PolygonAttributes();
        polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        materialAppear.setPolygonAttributes(polyAttrib);
        ColoringAttributes redColoring = new ColoringAttributes();
        redColoring.setColor(1.0f, 0.0f, 0.0f);
        materialAppear.setColoringAttributes(redColoring);

        return materialAppear;
    }

    /////////////////////////////////////////////////
    //
    // create scene graph branch group
    //
    public BranchGroup createSceneGraph(boolean wireFrame) {
        int total = 0;

        System.out.println("\n --- geometry debug information --- \n");

        float[] coordinateData = null;
        coordinateData = createCoordinateData();
        int[] stripCount = {17,17,5,5,5,5,5,5,5};  // ******
//        int[] stripCount = {17,17,17};  // ******

        for(int i=0; i<stripCount.length; i++){
                System.out.println("stripCount["+i+"] = "+stripCount[i]);
                total +=stripCount[i];
        }

        if (total != coordinateData.length/3){
                System.out.println("  coordinateData vertex count: "+coordinateData.length/3);
                System.out.println("stripCount total vertex count: "+total);
        }

        GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
        gi.setCoordinates(coordinateData);
        gi.setStripCounts(stripCount);

        Triangulator tr = new Triangulator();
//        Triangulator tr = new Triangulator(1);
        System.out.println("begin triangulation");
        tr.triangulate(gi);
        System.out.println("  END triangulation");
        gi.recomputeIndices();

        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(gi);
        gi.recomputeIndices();

        Stripifier st = new Stripifier();
        st.stripify(gi);
        gi.recomputeIndices();

        Shape3D part = new Shape3D();
        if(wireFrame==true)
                part.setAppearance(createWireFrameAppearance());
        else
                part.setAppearance(createMaterialAppearance());
        part.setGeometry(gi.getGeometryArray());

        /////////////////////////////

        BranchGroup contentRoot = new BranchGroup();

        // Create the transform group node and initialize it to the
        // identity. Add it to the root of the subgraph.
        TransformGroup objSpin = new TransformGroup();
        objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        contentRoot.addChild(objSpin);

        objSpin.addChild(part);

        ////////////////////////
        LineStripArray lineArray = new LineStripArray(69, LineArray.COORDINATES, stripCount); //*****
//        LineStripArray lineArray = new LineStripArray(51, LineArray.COORDINATES, stripCount); //*****
        lineArray.setCoordinates(0, coordinateData);
        Appearance blueColorAppearance = new Appearance();
        ColoringAttributes blueColoring = new ColoringAttributes();
        blueColoring.setColor(0.0f, 0.0f, 1.0f);
        blueColorAppearance.setColoringAttributes(blueColoring);
        LineAttributes lineAttrib = new LineAttributes();
        lineAttrib.setLineWidth(2.0f);
        blueColorAppearance.setLineAttributes(lineAttrib);
        objSpin.addChild(new Shape3D(lineArray, blueColorAppearance));

        Alpha rotationAlpha = new Alpha(-1, 16000);
  
        RotationInterpolator rotator =
                 new RotationInterpolator(rotationAlpha, objSpin);
 
        // a bounding sphere specifies a region a behavior is active
        // create a sphere centered at the origin with radius of 1
        BoundingSphere bounds = new BoundingSphere();
        rotator.setSchedulingBounds(bounds);
        objSpin.addChild(rotator);

        DirectionalLight lightD = new DirectionalLight();
        lightD.setDirection(new Vector3f(0.0f,-0.7f,-0.7f));
        lightD.setInfluencingBounds(bounds);
        contentRoot.addChild(lightD);

        AmbientLight lightA = new AmbientLight();
        lightA.setInfluencingBounds(bounds);
        contentRoot.addChild(lightA);

        Background background = new Background();
        background.setColor(1.0f, 1.0f, 1.0f);
        background.setApplicationBounds(bounds);
        contentRoot.addChild(background);

        // Let Java 3D perform optimizations on this scene graph.
        // contentRoot.compile();

        return contentRoot;
    } // end of CreateSceneGraph method of MobiusApp

    // Create a simple scene and attach it to the virtual universe

    public GeomInfoApp(String[] args) {
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
            SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);

        BranchGroup scene = createSceneGraph(args.length>0);

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

	// This will move the ViewPlatform back a bit so the
	// objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
    } // end of GeomInfoApp constructor

    //  The following allows this to be run as an application
    //  as well as an applet

    public static void main(String[] args) {
        System.out.print("GeomInfoApp - Java 3D API demo program\n");
        System.out.print("A demonstration of using the GeometryInfo class.\n");
        System.out.print("The blue lines show the input geometry - the red ");
        System.out.print("geometry was created by the GeometryInfo and other classes.\n");
        System.out.print("Running the program without any command line arguments will show a solid object\n");
        System.out.print("Supplying any command line argument will show the wireframe.\n");
        System.out.print("http://java.sun.com/products/java-media/3D/collateral\n");
        Frame frame = new MainFrame(new GeomInfoApp(args), 256, 256);
    } // end of main method of MobiusApp

} // end of class GeomInfoApp
