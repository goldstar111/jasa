/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2005 Steve Phelps
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details.
 */

package uk.ac.liv.util.io;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.io.*;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class CSVReader {

  /**
   * @uml.property name="in"
   */
  BufferedReader in;

  /**
   * @uml.property name="seperator"
   */
  char seperator;

  /**
   * @uml.property name="types" multiplicity="(0 -1)" dimension="1"
   */
  Class[] types;

  static final char DEFAULT_SEPERATOR = '\t';

  public CSVReader( InputStream in, Class[] types, char seperator ) {
    this.in = new BufferedReader(new InputStreamReader(in));
    this.seperator = seperator;
    this.types = types;
  }

  public CSVReader( InputStream in, Class[] types ) {
    this(in, types, DEFAULT_SEPERATOR);
  }

  public List nextRecord() throws IOException {
    String line = in.readLine();
    List record = new ArrayList(types.length);
    if ( line == null ) {
      return null;
    }
    StringTokenizer tokens = new StringTokenizer(line, seperator + "");
    for ( int i = 0; i < types.length; i++ ) {
      String fieldStr = tokens.nextToken();
      record.add(convert(fieldStr, types[i]));
    }
    return (List) record;
  }

  public static Object convert( String str, Class type ) {
    Method valueOf = null;
    try {
      Class[] strParam = new Class[1];
      strParam[0] = str.getClass();
      valueOf = type.getDeclaredMethod("valueOf", strParam);
    } catch ( NoSuchMethodException e ) {
      valueOf = null;
    }
    if ( valueOf != null ) {
      Object[] params = new Object[1];
      params[0] = (Object) str;
      try {
        return valueOf.invoke(type, params);
      } catch ( InvocationTargetException e ) {
        throw new NumberFormatException(str);
      } catch ( IllegalArgumentException e ) {
        // fail silently?
      } catch ( IllegalAccessException e ) {
        // fail silently?
      }
    } else {
      // No valueOf method, just return a String
      return (Object) str;
    }
    return null;
  }

}