/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2004 Steve Phelps
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

package uk.ac.liv.auction.agent;

import uk.ac.liv.util.Resetable;

import uk.ac.liv.auction.core.Auction;
import uk.ac.liv.auction.core.AuctionEventListener;
import uk.ac.liv.auction.core.Shout;

/**
 * <p>
 * Classes implementing this interface can trade in
 * round-robin auctions, as implemented by the RoundRobinAuction class.
 * </p>
 *
 * @author Steve Phelps
 * @version $Revision$
 */

public interface TradingAgent
    extends Resetable, AuctionEventListener {

 /**
   * Request a shout from this trader.  The trader will perform any bidding activity
   * in this method and return when it is done.  An auction invokes this method
   * on a trader when it is the traders "turn" to bid in that auction.
   *
   * @param auction The auction in which to trade
   */
  public void requestShout( Auction auction );

  public boolean isBuyer();
  
  public boolean isSeller();

  /**
   * This method is used by an auction to notify a buyer that one of
   * its bids has been successful.
   *
   * @param seller  The seller whose ask has been matched
   * @param price   The price of the goods as determined by the auction
   */
  public void informOfSeller( Auction auction, Shout winningShout,
                                TradingAgent seller,
                                double price, int quantity );

  /**
   * This method is used by a buyer to notify a seller that one of its bids
   * has been successful.
   */
  public void informOfBuyer( Auction auction, TradingAgent buyer,
                              double price, int quantity );

}