package uk.ac.liv.util;

/**
 * Title:
 * Description:
 * Copyright:
 * Company:
 * @author Steve Phelps
 * @version
 */

public class GenericInteger extends GenericNumber {

  Integer value;

  public GenericInteger( Integer value ) {
    this.value = value;
  }

  public GenericNumber add( GenericNumber other ) {
    if ( other instanceof GenericInteger ) {
      return new GenericInteger( new Integer(value.intValue() + other.intValue()) );
    } else if ( other instanceof GenericDouble ) {
      return new GenericDouble( new Double(value.doubleValue() + other.doubleValue()) );
    } else {
      throw new IllegalArgumentException();
    }
  }

  public GenericNumber multiply( GenericNumber other ) {
    if ( other instanceof GenericInteger ) {
      return new GenericInteger( new Integer(value.intValue() * other.intValue()) );
    } else if ( other instanceof GenericDouble ) {
      return new GenericDouble( new Double(value.doubleValue() * other.doubleValue()) );
    } else {
      throw new IllegalArgumentException();
    }
  }

  public GenericNumber subtract( GenericNumber other ) {
    if ( other instanceof GenericInteger ) {
      return new GenericInteger( new Integer(value.intValue() - other.intValue()) );
    } else if ( other instanceof GenericDouble ) {
      return new GenericDouble( new Double(value.doubleValue() - other.doubleValue()) );
    } else {
      throw new IllegalArgumentException();
    }
  }

  public GenericNumber divide( GenericNumber other ) {
    if ( other instanceof GenericInteger ) {
      return new GenericInteger( new Integer(value.intValue() / other.intValue()) );
    } else if ( other instanceof GenericDouble ) {
      return new GenericDouble( new Double(value.doubleValue() / other.doubleValue()) );
    } else {
      throw new IllegalArgumentException();
    }
  }

  public int intValue() {
    return value.intValue();
  }

  public float floatValue() {
    return value.floatValue();
  }

  public double doubleValue() {
    return value.doubleValue();
  }

  public long longValue() {
    return value.longValue();
  }

}
