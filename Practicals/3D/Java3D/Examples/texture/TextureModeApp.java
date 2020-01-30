/*
 *      @(#)TextureModeApp.java 1.1 00/09/25 02:09
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


public class TextureModeApp extends Applet {

    public BranchGroup createSceneGraph() {

         // Create the root of the branch graph
         BranchGroup objRoot = new BranchGroup();
         TextureAttributes textureAttrib = null;
 
 
         Text2D modulateText = new Text2D("Modulate", 
                                      new Color3f(1.0f, 0.7f, 0.7f), 
                                      "Helvetica", 24, Font.ITALIC);
         textureAttrib = modulateText.getAppearance().getTextureAttributes();
         if (textureAttrib == null) {
                System.out.println("creating attrib for modulate");
                textureAttrib = new TextureAttributes();
                modulateText.getAppearance().setTextureAttributes(textureAttrib);
         }
         textureAttrib.setTextureMode(TextureAttributes.MODULATE);

         Text2D decalText = new Text2D("Decal", 
                                      new Color3f(0.7f, 0.7f, 1.0f), 
                                      "Helvetica", 24, Font.ITALIC);
         textureAttrib = decalText.getAppearance().getTextureAttributes();
         if (textureAttrib == null) {
                System.out.println("creating attrib for decal");
                textureAttrib = new TextureAttributes();
                decalText.getAppearance().setTextureAttributes(textureAttrib);
         }
         textureAttrib.setTextureMode(TextureAttributes.DECAL);

         Text2D blendText = new Text2D("Blend", 
                                      new Color3f(0.7f, 1.0f, 0.7f), 
                                      "Helvetica", 24, Font.ITALIC);
         textureAttrib = blendText.getAppearance().getTextureAttributes();
         if (textureAttrib == null) {
                System.out.println("creating attrib for blend");
                textureAttrib = new TextureAttributes();
                blendText.getAppearance().setTextureAttributes(textureAttrib);
         }
         textureAttrib.setTextureMode(TextureAttributes.BLEND);

         Text2D replaceText = new Text2D("Replace", 
                                      new Color3f(1.0f, 1.0f, 1.0f), 
                                      "Helvetica", 24, Font.ITALIC);
         textureAttrib = replaceText.getAppearance().getTextureAttributes();
         if (textureAttrib == null) {
                System.out.println("creating attrib for replace");
                textureAttrib = new TextureAttributes();
                replaceText.getAppearance().setTextureAttributes(textureAttrib);
         }
         textureAttrib.setTextureMode(TextureAttributes.REPLACE);

         objRoot.addChild(modulateText);

         Background backg = new Background();
         backg.setColor(0.4f, 0.4f, 1.0f);
         backg.setApplicationBounds(new BoundingSphere());
         objRoot.addChild(backg);
 
         return objRoot;
     } // end of CreateSceneGraph method


     public TextureModeApp() {
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
    } // end of TextureModeApp (constructor)

    //  The following allows this to be run as an application
    //  as well as an applet

    public static void main(String[] args) {
        System.out.println("TextureModeApp.java - a demonstration of Text2D in Java 3D");
//        System.out.println("The scene is of a rotating Text2D object.");
//        System.out.print("If you don't have a 3D card, it may take a few seconds ");
//        System.out.println("before you see anything.");
        System.out.println("The Java 3D Tutorial is available on the web at:");
        System.out.println("http://www.sun.com/desktop/java3d/collateral");
        Frame frame = new MainFrame(new TextureModeApp(), 512, 64);
    } // end of main (method of TextureModeApp)

} // end of class TextureModeApp
