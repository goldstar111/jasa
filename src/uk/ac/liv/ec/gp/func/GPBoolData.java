package uk.ac.liv.ec.gp.func;

import ec.gp.*;


/**
 * @author Steve Phelps
 */

public class GPBoolData extends GPData {

  public boolean data;

  public GPData copyTo( GPData other ) {
    ((GPBoolData) other).data = this.data;
    return other;
  }

}