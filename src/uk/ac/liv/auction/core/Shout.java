package uk.ac.liv.auction.core;

import uk.ac.liv.auction.agent.TraderAgent;

import uk.ac.liv.util.Debug;

import java.io.Serializable;

/**
 * <p>
 * A class representing a shout in an auction.  A shout may be either a bid
 * (offer to buy) or an ask (offer to sell).
 * </p>
 *
 * <p>
 * Shouts are mutable for performance reasons, hence care should be taken not
 * to rely on, e.g. shouts held in collections remaining constant.
 * </p>
 *
 * <p>
 * Note that equality is determined by reference equivalence, hence this
 * class's natural ordering is not consistent with equals.
 * </p>
 * @author Steve Phelps
 *
 */

public class Shout implements Comparable, Cloneable, Serializable {

  /**
   * The number of items offered/wanted.
   */
  int quantity;

  /**
   * The price of this offer
   */
  double price;

  /**
   * The agent placing this offer
   */
  TraderAgent agent;

  /**
   * True if this shout is a bid.
   * False if this shout is an ask.
   */
  boolean isBid;

  Shout child = null;


  public Shout( TraderAgent agent, int quantity, double price, boolean isBid ) {
    this.agent = agent;
    this.quantity = quantity;
    this.price = price;
    this.isBid = isBid;
  }

  public Shout( TraderAgent agent ) {
    this.agent = agent;
  }

  public int getQuantity() {  return quantity; }
  public double getPrice() {  return price; }
  public TraderAgent getAgent() { return agent; }
  public boolean isBid() { return isBid; }
  public boolean isAsk() { return ! isBid; }

  public void setQuantity( int quantity ) { this.quantity = quantity; }
  public void setPrice( double price ) { this.price = price; }
  public void setSelling() { this.isBid = false; }
  public void setBuying() { this.isBid = true; }
  public void setIsBid( boolean isBid ) { this.isBid = isBid; }

  public boolean satisfies( Shout other ) {
    if ( this.isBid() ) {
      return other.isAsk() && this.getPrice() >= other.getPrice();
    } else {
      return other.isBid() && other.getPrice() >= this.getPrice();
    }
  }

  public int compareTo( Object o ) {
    Shout other = (Shout) o;
    if ( price > other.price ) {
      return 1;
    } else if ( price < other.price ) {
      return -1;
    } else {
      return 0;
    }
    // return new Long(this.price).compareTo(new Long(other.getPrice()));
  }

  public boolean isValid() {
    if ( price < 0 ) {
      return false;
    }
    if ( quantity < 1 ) {
      return false;
    }
    return true;
  }

  /**
   * Reduce the quantity of this shout by excess and return a new
   * child shout containing the excess quantity.  After a split,
   * parent shouts keep a reference to their children.
   *
   * @param excess The excess quantity
   *
   * @see getChild()
   *
   */
  public Shout split( int excess ) {
    quantity -= excess;
    Shout newShout = new Shout(agent, excess, price, isBid);
    child = newShout;
    Debug.assert(isValid());
    Debug.assert(newShout.isValid());
    return newShout;
  }

  public Shout splat( int excess ) {
    Shout newShout = new Shout(agent, quantity - excess, price, isBid);
    quantity = excess;
    child = newShout;
    Debug.assert(this.isValid());
    Debug.assert(newShout.isValid());
    return newShout;
  }

  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public String toString() {
    return "(" + getClass() + " quantity:" + quantity + " price:" + price +
                              " isBid:" + isBid + " agent:" + agent + ")";
  }


  public static double maxPrice( Shout s1, Shout s2 ) {
    return Math.max(price(s1,Double.NEGATIVE_INFINITY), price(s2,Double.NEGATIVE_INFINITY));
  }

  public static double minPrice( Shout s1, Shout s2 ) {
    return Math.min(price(s1,Double.POSITIVE_INFINITY), price(s2,Double.POSITIVE_INFINITY));
  }


  private static double price( Shout s, double alt ) {
    if ( s == null ) {
      return alt;
    } else {
      return s.getPrice();
    }
  }

  /**
   * Get the child of this shout.  Shouts have children when they are split().
   *
   * @return The child Shout object, or null if this Shout is childless.
   */
  public Shout getChild() {
    return child;
  }

  protected void makeChildless() {
    child = null;
  }

}