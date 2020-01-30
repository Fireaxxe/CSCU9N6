/*
 *      @(#)SimpleQuadObject.java 0.50 14/03/01 
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

import QuadFileParser;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.SceneBase;
import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import java.io.FileNotFoundException;
import java.io.StreamTokenizer;
import java.io.Reader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.vecmath.*;
import javax.media.j3d.*;


/**
 * The QuadFile class implements some of the the Loader interface
 * for the OOGL QuadFile.
 * QuadFiles are text based files supporting polygonal surfaces constructed
 * of Quadralerals.
 * </p>
 * This class implements that portion of the loader that is QUAD file
 * specific.
 * The remaining implementation of the Loader interface is accomplished
 * in the QuadFileLoader class which extends this class.
 */

public class SimpleQuadObject 
{
  protected int flags;
  protected int fileType = 0;             // indicate if vertices include CN

  // constants for indicating file type

  protected static final int COORDINATE = GeometryArray.COORDINATES;

  // lists of points are read from the .quad file into this array. . .
  protected ArrayList coordList;          // Holds Point3f


/**
 * readVertex reads one vertex's coordinate data
 * This particular implementation takes the naieve approach
 */
  boolean readVertex(QuadFileParser st)
  {
    Point3f p = new Point3f();

    st.getToken();
    if(st.ttype == st.TT_EOF)
      return false; // reached end of file
    p.x = (float)st.nval;

    st.getToken();
    p.y = (float)st.nval;

    st.getToken();
    p.z = (float)st.nval;

    // Add this vertex to the array
    coordList.add(p);

    return true;
  } // End of readVertex


/**
 * readQuad reads four vertices of the correct type (which in this version
 * is always just coordinate data).
 */
  boolean readQuad(QuadFileParser st)
  {
    int v;
    boolean result = false;

    for(v=0; v < 4; v++)
      result = readVertex(st);

    return result;

  } // End of readQuad


  /*
   * readFile
   *
   *    Read the model data from the file.
   */
  void readQuadFile(QuadFileParser st)
  {
    // verify file type

    st.getToken(); 
    if(st.sval.equals("QUAD") || st.sval.equals("POLY"))
      fileType = COORDINATE;
    else
      throw new ParsingErrorException("bad file type: "+st.sval);

    // read vertices

    while (readQuad(st));

  } // End of readFile


} // End of class QuadFile

// End of file SimpleQuadObject.java


