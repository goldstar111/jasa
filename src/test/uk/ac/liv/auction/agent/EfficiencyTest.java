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

package test.uk.ac.liv.auction.agent;

import junit.framework.TestCase;

import test.uk.ac.liv.PRNGTestSeeds;
import uk.ac.liv.auction.agent.AbstractTradingAgent;
import uk.ac.liv.auction.agent.RandomValuer;
import uk.ac.liv.auction.core.*;
import uk.ac.liv.auction.stats.SurplusReport;
import uk.ac.liv.auction.zi.ZITraderAgent;
import uk.ac.liv.prng.GlobalPRNG;
import uk.ac.liv.util.CummulativeDistribution;

/**
 * @author Steve Phelps
 * @version $Revision$
 */
public abstract class EfficiencyTest extends TestCase {
  
  protected KAuctioneer auctioneer;
  
  protected RoundRobinAuction auction;
  
  protected ZITraderAgent[] agents;
  
  static final int NS = 6;
  static final int NB = 6;
  
  static int ITERATIONS = 200;
  
  static final double MIN_VALUE = 50;
  static final double MAX_VALUE = 300;
  
  static final int MAX_DAYS = 100;
  static final int DAY_LENGTH = 5;
  
  
  public EfficiencyTest( String name ) {
    super(name);
  }
  
  protected void assignAuctioneer() {
    auctioneer = new KContinuousDoubleAuctioneer();
    auctioneer.setPricingPolicy( new DiscriminatoryPricingPolicy(0.5) );
    auction.setAuctioneer(auctioneer);
  }
  
  protected void registerTraders() {
    int numAgents = getNumBuyers() + getNumSellers();
    agents = new ZITraderAgent[numAgents];
    for( int i=0; i<numAgents; i++ ) {
      agents[i] = new ZITraderAgent();
      agents[i].setInitialTradeEntitlement(1);
      assignStrategy(agents[i]);
      assignValuationPolicy(agents[i]);
      agents[i].setIsSeller(i < getNumSellers());
      System.out.println("Registering trader " + agents[i]);
      auction.register(agents[i]);
    }
  }
  
  public void testEfficiency() {
    GlobalPRNG.initialiseWithSeed(PRNGTestSeeds.UNIT_TEST_SEED);
    System.out.println("\ntestEfficiency()");
    CummulativeDistribution efficiency = 
      new CummulativeDistribution("efficiency");
    initialiseExperiment();
    for( int i=0; i<ITERATIONS; i++ ) {
      auction.reset();
      auction.run();
      SurplusReport surplus = new SurplusReport(auction);
      surplus.calculate();    
      //surplus.produceUserOutput();
      System.out.println("Iteration " + i + ": efficiency = " + surplus.getEA());
      if ( !Double.isNaN(surplus.getEA()) ) {
        efficiency.newData(surplus.getEA());
      }
    }
    double meanEfficiency = efficiency.getMean();
    
    System.out.println("Mean efficiency = " + meanEfficiency);
    
    assertTrue("infinite efficiency", !Double.isInfinite(meanEfficiency));
    
    assertTrue("mean efficiency too low", 
        			meanEfficiency >= getMinMeanEfficiency());
    
    assertTrue("max efficiency too high", 
        			efficiency.getMax() <= 100 + 10E-6);
    
    assertTrue("negative efficiency encountered",
        			efficiency.getMin() >= 0 );
  }
  
  protected void initialiseExperiment() {
    initialiseAuction();
    assignAuctioneer();
    registerTraders();
  }
  
  protected void initialiseAuction() {
    auction = new RandomRobinAuction();
    auction.setLengthOfDay(DAY_LENGTH);
    auction.setMaximumDays(MAX_DAYS);
  }
  
  protected int getNumBuyers() {
    return NB;
  }
  
  protected int getNumSellers() {
    return NS;
  }
  
  protected void assignValuationPolicy( AbstractTradingAgent agent ) {
    agent.setValuationPolicy( new RandomValuer(MIN_VALUE, MAX_VALUE));
  }
  
  protected abstract void assignStrategy( AbstractTradingAgent agent );
  
  protected abstract double getMinMeanEfficiency();

}