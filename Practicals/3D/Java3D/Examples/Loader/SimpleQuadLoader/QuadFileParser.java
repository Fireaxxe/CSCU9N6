/*
 *      @(#)QuadFileParser.java 1.6 01/01/27
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

import java.io.StreamTokenizer;
import java.io.IOException;
import java.io.Reader;

class QuadFileParser extends StreamTokenizer {

  // setup
  //
  //    Sets up StreamTokenizer for reading OOGL file formats.
  //
  void setup()
  {
    resetSyntax();

    // some EOL chars are significant, but not all
    eolIsSignificant(false);

    wordChars('A','z');

    // have StreamTokenizer parse numbers (makes double-precision)
    parseNumbers();

    // Comment from # to end of line
    commentChar('#');

    // Whitespace characters deliniate words and numbers
    // blanks, tabs, and newlines are whitespace in OOGL
    whitespaceChars('\t', '\r');   // ht, lf, ff, vt, cr
    whitespaceChars(' ', ' ');     // space

  } // End of setup



  // getToken
  //
  //	Gets the next token from the stream.  Puts one of the four
  //	constants (TT_WORD, TT_NUMBER, TT_EOL, or TT_EOF) or the token value
  //    for single character tokens into ttype.
  //    The value of this method is in the catching of exceptions in this
  //    central location; which is also a flaw of the loader.

  boolean getToken()
  {
    int t;
    boolean done = false;

    try {
      t = nextToken();
    }
    catch (IOException e) {
      System.err.println(
        "IO error on line " + lineno() + ": " + e.getMessage());
      return false;
    }

    return true;
  } // End of getToken



  void printToken()
  {
    switch (ttype) {
    case TT_EOL:
      System.out.println("Token EOL");
      break;
    case TT_EOF:
      System.out.println("Token EOF");
      break;
    case TT_WORD:
      System.out.println("Token TT_WORD: " + sval);
      break;
    case TT_NUMBER:
      System.out.println("Token TT_NUMBER: " + nval);
      break;
    default:
      System.out.println("Token (not typed): " + ttype);
      break;
    }
  } // end of printToken


  // QuadFileParser constructor
  //
  QuadFileParser(Reader r)
  {
    super(r);
    setup();
  } // end of QuadFileParser

} // End of file QuadFileParser.java

