/*
 *      @(#)SimpleQuadFileLoader.java 0.50 01/02/10 
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
import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import java.io.FileNotFoundException;
import java.io.StreamTokenizer;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.net.MalformedURLException;



/**
 * The SimpleQuadFileLoader class extends the QuadFile class adding the
 * methods for dealing with filenaming and URLs.  The graphics
 * issues are handled in QuadFile.
 * </p>
 * Together, the SimpleQuadFileLoader and QuadFile classes implement the
 * Loader interface.
 * </p>
 * The methods of this class are reasonably generic and could be
 * reused in the creation of other file loaders.
 */

public class SimpleQuadFileLoader extends SimpleQuadScene implements Loader
{

  // Maximum length (in chars) of basePath
  private static final int MAX_PATH_LENGTH = 1024;

  //private int flags;
  private String basePath = null;
  private URL baseUrl = null;
  private boolean fromUrl = false;


  /**
   * Constructor.  Crease Angle set to default of
   * 44 degrees (see NormalGenerator utility for details).
   * @param flags The constants from above or from
   * com.sun.j3d.loaders.Loader, possibly "or'ed" (|) together.
   */
  public SimpleQuadFileLoader(int flags)
  {
    setFlags(flags);

  } // End of FileLoader(int)



  /**
   * Default constructor.  Crease Angle set to default of
   * 44 degrees (see NormalGenerator utility for details).  Flags
   * set to zero (0).
   */
  public SimpleQuadFileLoader()
  {
    this(0);

  } // End of FileLoader()


  /*
   * Takes a file name and sets the base path to the directory
   * containing that file.
   */
  private void setBasePathFromFilename(String fileName)
  {

    // Get ready to parse the file name
    StringTokenizer stok =
      new StringTokenizer(fileName, java.io.File.separator);

    //  Get memory in which to put the path
    StringBuffer sb = new StringBuffer(MAX_PATH_LENGTH);

    // Check for initial slash
    if (fileName!= null && fileName.startsWith(java.io.File.separator))
      sb.append(java.io.File.separator);

    // Copy everything into path except the file name
    for(int i = stok.countTokens() - 1 ; i > 0 ; i--) {
      String a = stok.nextToken();
      sb.append(a);
      sb.append(java.io.File.separator);
    }
    setBasePath(sb.toString());
  } // End of setBasePathFromFilename



  /**
   * The Quad File is loaded from the .quad file specified by
   * the filename.
   * To attach the model to your scene, call getSceneGroup() on
   * the Scene object passed back, and attach the returned
   * BranchGroup to your scene graph.  
   */
  public Scene load(String filename) throws FileNotFoundException,
					    IncorrectFormatException,
					    ParsingErrorException
  {
    setBasePathFromFilename(filename);

    Reader reader = new BufferedReader(new FileReader(filename));
    return load(reader);
  } // End of load(String)



  /**
   * The Quad File is loaded from the .quad file specified by
   * the URL.
   * To attach the model to your scene, call getSceneGroup() on
   * the Scene object passed back, and attach the returned
   * BranchGroup to your scene graph.  
   */
  private void setBaseUrlFromUrl(URL url)
  {
    StringTokenizer stok =
      new StringTokenizer(url.toString(), "/\\", true);
    int tocount = stok.countTokens() - 1;
    StringBuffer sb = new StringBuffer(MAX_PATH_LENGTH);
    for(int i = 0; i < tocount ; i++) {
	String a = stok.nextToken();
	sb.append(a);
    }
    try {
      baseUrl = new URL(sb.toString());
    }
    catch (MalformedURLException e) {
      System.err.println("Error setting base URL: " + e.getMessage());
    }
  } // End of setBaseUrlFromUrl



  /**
   * The file is loaded off of the web.
   * To attach the model to your scene, call getSceneGroup() on
   * the Scene object passed back, and attach the returned
   * BranchGroup to your scene graph.  
   */
  public Scene load(URL url) throws FileNotFoundException,
				    IncorrectFormatException,
				    ParsingErrorException
  {
    BufferedReader reader;

    setBaseUrlFromUrl(url);

    try {
      reader = new BufferedReader(new InputStreamReader(url.openStream()));
    }
    catch (IOException e) {
      throw new FileNotFoundException();
    }
    fromUrl = true;
    return load(reader);
  } // End of load(URL)



  /**
   * For a file loaded from a URL, set the URL where associated files
   * (like material properties files) will be found.
   * Only needs to be called to set it to a different URL
   * from that containing the file.
   */
  public void setBaseUrl(URL url)
  {
    baseUrl = url;
  } // End of setBaseUrl



  /**
   * Return the URL where files associated with this file (like
   * material properties files) will be found.
   */
  public URL getBaseUrl()
  {
    return baseUrl;
  } // End of getBaseUrl



  /**
   * Set the path where files associated with this file is located.
   * Only needs to be called to set it to a different directory
   * from that containing the file.
   */
  public void setBasePath(String pathName)
  {
    basePath = pathName;
    if (basePath == null || basePath == "")
	basePath = "." + java.io.File.separator;
    basePath = basePath.replace('/', java.io.File.separatorChar);
    basePath = basePath.replace('\\', java.io.File.separatorChar);
    if (!basePath.endsWith(java.io.File.separator))
	basePath = basePath + java.io.File.separator;
  } // End of setBasePath



  /**
   * Return the path where files associated with this file (like material
   * files) are located.
   */
  public String getBasePath()
  {
    return basePath;
  } // End of getBasePath


  /**
   * Set parameters for loading the model.
   * Flags defined in Loader.java are ignored by the this Loader
   * because the .quad file format doesn't include lights, fog, background,
   * behaviors, views, or sounds.  However, several flags are defined
   * specifically for use with the SimpleQuadFileLoader (see above).
   */
  public void setFlags(int flags)
  {
    super.flags = flags;
  } // End of setFlags


  /**
   * Get the parameters currently defined for loading the model.
   * Flags defined in Loader.java are ignored by the SimpleQuadFileLoader
   * because the .quad file format doesn't include lights, fog, background,
   * behaviors, views, or sounds.  However, several flags are defined
   * specifically for use with the SimpleQuadFileLoader (see above).
   */
  public int getFlags()
  {
    return flags;
  } // End of getFlags

} // End of class SimpleQuadFileLoader

// End of file SimpleQuadFileLoader.java


