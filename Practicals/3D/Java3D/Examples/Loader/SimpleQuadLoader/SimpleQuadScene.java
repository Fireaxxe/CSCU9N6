/*
 *      @(#)SimpleQuadScene.java 0.50 01/02/10 
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

//package com.sun.j3d.loaders;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.SceneBase;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.ArrayList;
import javax.media.j3d.*;
import javax.vecmath.Point3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;



/**
 * The SimpleQuadScene class extends the SimpleQuadObject class adding the
 * ?
 * </p>
 */

class SimpleQuadScene extends SimpleQuadObject
{

  // coordList is converted to an arrays for creating geometry
  //
  private Point3f coordArray[] = null;

  private Point3f[] objectToPoint3fArray(ArrayList inList)
  {
    Point3f outList[] = new Point3f[inList.size()];

    for (int i = 0 ; i < inList.size() ; i++) {
      outList[i] = (Point3f)inList.get(i);
    }
    return outList;
  } // End of objectToPoint3fArray


  private SceneBase makeScene()
  {
    // Create Scene to pass back
    SceneBase scene = new SceneBase();
    BranchGroup group = new BranchGroup();
    scene.setSceneGroup(group);


    // the model will be one Shape3D
    Shape3D shape = new Shape3D();

    coordArray = objectToPoint3fArray(coordList);

    QuadArray qa = new QuadArray(coordArray.length, fileType);
    qa.setCoordinates(0, coordArray);

    // Put geometry into Shape3d
    shape.setGeometry(qa);

    group.addChild(shape);
    scene.addNamedObject("no name", shape);

    return scene;
  } // end of makeScene


  /**
   * The File is loaded from the already opened file.
   * To attach the model to your scene, call getSceneGroup() on
   * the Scene object passed back, and attach the returned
   * BranchGroup to your scene graph.  
   */
  public Scene load(Reader reader) throws FileNotFoundException,
				          IncorrectFormatException,
                                          ParsingErrorException
  {
    // QuadFileParser does lexical analysis
    QuadFileParser st = new QuadFileParser(reader);

    coordList = new ArrayList();

    readQuadFile(st);

    return makeScene();
  } // End of load(Reader)

} // End of class SimpleQuadScene

// End of file SimpleQuadScene.java


