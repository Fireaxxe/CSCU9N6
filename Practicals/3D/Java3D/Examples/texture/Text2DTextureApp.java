/*
 *      @(#)Text2DTextureApp.java 1.2 01/06/18
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

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;

/*   Text2DTextureApp renders a single, Text2D object.  The Text2D object spins
 *   revealing that the backside does not render by default.  The code
 *   to make the Text2D object double-sided is commented out in this
 *   program.
 */

public class Text2DTextureApp extends Applet {

 public BranchGroup createSceneGraph() {

         // Create the root of the branch graph
         BranchGroup objRoot = new BranchGroup();
 
 
         // Create a simple Text2D leaf node, add it to the scene graph.
         Text2D text2d = new Text2D("2D text in Java 3D", 
                                      new Color3f(0.9f, 1.0f, 1.0f), 
                                      "Helvetica", 24, Font.ITALIC);

         objRoot.addChild(text2d);

         Appearance textAppear = text2d.getAppearance();


// The following 12 lines of code use the texture from
// the previous Text2D object on another object.
// This code depends on the above line of code that sets textAppear.
         QuadArray qa = new QuadArray(4, QuadArray.COORDINATES |
                                         QuadArray.TEXTURE_COORDINATE_2|
                                         QuadArray.COLOR_3);
         qa.setCoordinate(0, new Point3f(-3.0f, 0.7f, -5.0f));
         qa.setCoordinate(1, new Point3f(-3.0f,-0.7f, -5.0f));
         qa.setCoordinate(2, new Point3f( 3.0f,-0.7f, -5.0f));
         qa.setCoordinate(3, new Point3f( 3.0f, 0.7f, -5.0f));
         qa.setTextureCoordinate( 0, 0, new TexCoord2f(0.0f, 1.0f));
         qa.setTextureCoordinate( 0, 1, new TexCoord2f(0.0f, 0.0f));
         qa.setTextureCoordinate( 0, 2, new TexCoord2f(1.0f, 0.0f));
         qa.setTextureCoordinate( 0, 3, new TexCoord2f(1.0f, 1.0f));

         Color3f c = new Color3f(1f, 0f, 0f);
         for (int i = 0; i < 4; i++) qa.setColor(i, c);

         objRoot.addChild(new Shape3D(qa, textAppear));
 

         Background backg = new Background();
         backg.setColor(0.4f, 0.4f, 1.0f);
         backg.setApplicationBounds(new BoundingSphere());
         objRoot.addChild(backg);
 
         return objRoot;
     } // end of CreateSceneGraph method


     public Text2DTextureApp() {
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
    } // end of Text2DTextureApp (constructor)

    //  The following allows this to be run as an application
    //  as well as an applet

    public static void main(String[] args) {
        System.out.println("Text2DTextureApp.java - a demonstration of Text2D in Java 3D");
//        System.out.println("The scene is of a rotating Text2D object.");
//        System.out.print("If you don't have a 3D card, it may take a few seconds ");
//        System.out.println("before you see anything.");
        System.out.println("The Java 3D Tutorial is available on the web at:");
        System.out.println("http://java.sun.com/products/java-media/3D/collateral ");
        Frame frame = new MainFrame(new Text2DTextureApp(), 512, 64);
    } // end of main (method of Text2DTextureApp)

} // end of class Text2DTextureApp
