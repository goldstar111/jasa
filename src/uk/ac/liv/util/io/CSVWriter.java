package uk.ac.liv.util.io;

import java.util.Iterator;

import java.io.OutputStream;
import java.io.PrintStream;



/**
 * A class for writing data to CSV (comma-separated variables) text files.
 *
 * @author Steve Phelps
 */

public class CSVWriter  {

  PrintStream out;
  int numColumns;
  int currentColumn = 0;
  char seperator;
  static final char DEFAULT_SEPERATOR = '\t';

  public CSVWriter( OutputStream out, int numColumns, char seperator ) {
    this.out = new PrintStream(out);
    this.numColumns = numColumns;
    this.seperator = seperator;
  }

  public CSVWriter( OutputStream out, int numColumns ) {
    this(out, numColumns, DEFAULT_SEPERATOR);
  }

  public void newData( Iterator i ) {
    while ( i.hasNext() ) {
      newData(i.next());
    }
  }

  public void newData( Object data ) {
    out.print(data);
    currentColumn++;
    if ( currentColumn < numColumns ) {
      out.print(seperator);
    } else {
      out.println();
      currentColumn = 0;
    }
  }

  public void newData( int data ) {
    newData(new Integer(data));
  }

  public void newData( long data ) {
    newData(new Long(data));
  }

  public void newData( boolean data ) {
    if ( data ) {
      newData(1);
    } else {
      newData(0);
    }
  }

  public void newData( double data ) {
    newData(new Double(data));
  }

  public void flush() {
    out.flush();
  }

  public void close() {
    out.close();
  }

}