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
package test.uk.ac.liv.auction.core;

import junit.framework.Test;
import junit.framework.TestSuite;

import uk.ac.liv.auction.core.AbstractAuctioneer;
import uk.ac.liv.auction.core.IllegalShoutException;
import uk.ac.liv.auction.core.KDoubleAuctioneer;
import uk.ac.liv.auction.core.MarketQuote;
import uk.ac.liv.auction.core.Shout;

/**
 * @author Steve Phelps
 * @version $Revision$
 */

public class KDoubleAuctioneerTest extends AuctioneerTest {

  public KDoubleAuctioneerTest( String name ) {
    super(name);
  }
  
  public void setUp() {
    super.setUp();
    auctioneer = new KDoubleAuctioneer(auction, 1);
    auction.setAuctioneer(auctioneer);
  }
  
  public void testAuction1() {

    MarketQuote quote;

    // round 0
    try {
      auctioneer.newShout( new Shout(traders[0], 1, 500, true) );
      auctioneer.newShout( new Shout(traders[1], 1, 400, true) );
      auctioneer.newShout( new Shout(traders[2], 2, 900, false) );
    } catch ( IllegalShoutException e ) {
      fail("invalid IllegalShoutException exception thrown " + e);
      e.printStackTrace();
    }

    auctioneer.endOfRoundProcessing();
    auctioneer.printState();

    quote = auctioneer.getQuote();
    assertTrue( quote.getAsk() == 900 );

    System.out.println("quote = " + quote);

    // round 1
    System.out.println("round1");
    try {
      auctioneer.newShout( new Shout(traders[0], 1, 920, true) );
      auctioneer.newShout( new Shout(traders[1], 1, 950, true) );

    } catch ( IllegalShoutException e ) {
      fail("invalid IllegalShoutException thrown " + e );
      e.printStackTrace();
    }

    auctioneer.endOfRoundProcessing();
    auctioneer.printState();

    quote = auctioneer.getQuote();
    System.out.println("quote = " + quote);
    System.out.println("trader1's price = " + traders[0].lastWinningPrice);
    System.out.println("trader2's price = " + traders[1].lastWinningPrice);

//    assertTrue( quote.getAsk() > 900 );
    assertTrue( traders[0].lastWinningPrice == 900 );
    assertTrue( traders[1].lastWinningPrice == 900 );

    ((KDoubleAuctioneer) auctioneer).reset();
    System.out.println("after reseting, quote = " + auctioneer.getQuote());

    assertTrue( auctioneer.getQuote().getBid() < 0 );

  }
  
  public void testQuote() {
    
    System.out.println("\n\ntestQuote()\n");
    
    MarketQuote quote = auctioneer.getQuote();
    System.out.println("quote at start of auction: " + quote);
    
    // the bid ask spread should start off infinitely wide
    assertTrue( quote.getAsk() == Double.POSITIVE_INFINITY );
    assertTrue( quote.getBid() == Double.NEGATIVE_INFINITY );
    
    try {
      
      // bid: 5
      System.out.println("Bidding $5");
      auctioneer.newShout( new Shout(traders[0], 1, 5, true) );
      
      // ask: 10
      System.out.println("Asking $10");
      auctioneer.newShout( new Shout(traders[3], 1, 10, false));
      
      // quote should not have changed yet
      quote = auctioneer.getQuote();
      System.out.println("quote before clearing: " + quote);
      assertTrue( quote.getAsk() == Double.POSITIVE_INFINITY );
      assertTrue( quote.getBid() == Double.NEGATIVE_INFINITY );
      
      // after clearing it should change
      auctioneer.clear();
      ((AbstractAuctioneer) auctioneer).generateQuote();
      auctioneer.printState();
      
      // but clearing results it no matches, 
      //  so we should have a bid and an ask to beat
      quote = auctioneer.getQuote();
      System.out.println("quote after clearing: " + quote);
      assertTrue( quote.getAsk() == 10 );
      assertTrue( quote.getBid() == 5 );
      
      // ok, now lets match the bid by placing an ask for $4
      System.out.println("Bidding $4");
      auctioneer.newShout( new Shout(traders[2], 1, 4, false));
      auctioneer.clear();
      ((AbstractAuctioneer) auctioneer).generateQuote();
      auctioneer.printState();
      
      // now we should have a single unmatched ask for $10
      quote = auctioneer.getQuote();
      System.out.println("quote after clearing: " + quote);
      // so in order to guarantee a successful bid, we must bid >= $10
      assertTrue( quote.getAsk() == 10);
      // but we cannot guarantee a successful ask
      assertTrue( quote.getBid() == Double.NEGATIVE_INFINITY );
      
      // ok, lets match that ask by bidding $11
      System.out.println("Bidding $11");
      auctioneer.newShout( new Shout(traders[0], 1, 11, true) );
      auctioneer.clear();
      ((AbstractAuctioneer) auctioneer).generateQuote();
      auctioneer.printState();
      
      // so we should have infinite spread
      quote = auctioneer.getQuote();
      System.out.println("quote after clearing: " + quote);
      assertTrue( quote.getAsk() == Double.POSITIVE_INFINITY );
      assertTrue( quote.getBid() == Double.NEGATIVE_INFINITY );
      
      // now see what happens when we have a single unmatched bid of $4
      System.out.println("Bidding $4");
      auctioneer.newShout( new Shout(traders[1], 1, 4, true) );
      ((AbstractAuctioneer) auctioneer).generateQuote();;
      auctioneer.printState();
      
      quote = auctioneer.getQuote();
      System.out.println("quote after clearing: " + quote);
      // in order to guarantee a successsful ask, we must ask <= $4
      assertTrue( quote.getBid() == 4 );
      // but we cannot guarantee a successful bid because there are no asks
      assertTrue( quote.getAsk() == Double.POSITIVE_INFINITY );
      
    } catch ( IllegalShoutException e ) {
      fail("illegal shout " + e.getMessage());
    }
  }

  public static Test suite() {
    return new TestSuite(KDoubleAuctioneerTest.class);
  }

  public static void main( String[] args ) {
    junit.textui.TestRunner.run (suite());
  }
}