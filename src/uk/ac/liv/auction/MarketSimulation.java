/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2002 Steve Phelps
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

package uk.ac.liv.auction;

import ec.util.MersenneTwisterFast;
import ec.util.Parameter;
import ec.util.ParameterDatabase;

import uk.ac.liv.auction.core.*;
import uk.ac.liv.auction.agent.*;
import uk.ac.liv.auction.stats.*;

import uk.ac.liv.util.Parameterizable;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import java.io.File;

/**
 * <p>
 * The main JASA application class.  This application takes as an argument
 * the name of a parameter file describing an auction experiment, and
 * proceeds to run that experiment.
 * </p>
 */

public class MarketSimulation implements Parameterizable, Runnable {

  RoundRobinAuction auction;

  Auctioneer auctioneer;

  MarketDataLogger logger;

  MarketStats stats;

  boolean gatherStats;

  static final String P_LOGGER = "logger";
  static final String P_AUCTION = "auction";
  static final String P_AUCTIONEER = "auctioneer";
  static final String P_NUM_AGENT_TYPES = "numagenttypes";
  static final String P_NUM_AGENTS = "numagents";
  static final String P_AGENT_TYPE = "agenttype";
  static final String P_AGENTS = "agents";
  static final String P_CONSOLE = "console";
  static final String P_SIMULATION = "simulation";
  static final String P_STATS = "stats";
  static final String P_GATHER_STATS = "gatherstats";

  public void setup( ParameterDatabase parameters, Parameter base ) {
    System.out.print("Setup.. ");

    gatherStats = parameters.getBoolean(base.push(P_GATHER_STATS), null, false);

    auction =
      (RoundRobinAuction) parameters.getInstanceForParameterEq(base.push(P_AUCTION),
                                                               null, RoundRobinAuction.class);

    auction.setup(parameters, base.push(P_AUCTION));

    auctioneer =
      (Auctioneer) parameters.getInstanceForParameter(base.push(P_AUCTIONEER),
                                                      null, Auctioneer.class);
    ((Parameterizable) auctioneer).setup(parameters, base);
    auction.setAuctioneer(auctioneer);
    auctioneer.setAuction(auction);

    logger = (MarketDataLogger) parameters.getInstanceForParameter(base.push(P_LOGGER), null, MarketDataLogger.class);
    auction.setMarketDataLogger(logger);

    if ( parameters.getBoolean(base.push(P_CONSOLE), null, false) ) {
      auction.activateGUIConsole();
    }

    if ( gatherStats ) {
      stats =
        (MarketStats) parameters.getInstanceForParameter(base.push(P_STATS),
                                                         null, MarketStats.class);
      ((Parameterizable) stats).setup(parameters, base.push(P_STATS));
      stats.setAuction(auction);
    }

    int numAgentTypes = parameters.getInt(base.push(P_NUM_AGENT_TYPES), null, 1);
    for( int t=0; t<numAgentTypes; t++ ) {

      Parameter typeParam = base.push(P_AGENT_TYPE).push(""+t);
      Parameter agentParam = typeParam.push(P_AGENTS);

      int numAgents = parameters.getInt(typeParam.push(P_NUM_AGENTS), null, 0);
      for( int i=0; i<numAgents; i++ ) {

      	RoundRobinTrader agent =
          (RoundRobinTrader) parameters.getInstanceForParameter(typeParam, null,
                                                                RoundRobinTrader.class);
	((Parameterizable) agent).setup(parameters, typeParam);
        auction.register(agent);

      }
    }
    System.out.println("done.");
  }


  public void run() {
    auction.run();
  }


  public void report() {
    logger.finalReport();
    if ( gatherStats ) {
      stats.calculate();
      System.out.println(stats);
    }
  }


  public static void main( String[] args ) {

    try {

      if ( args.length < 1 ) {
        System.err.println("Must specify a parameter file");
        System.exit(1);
      }

      String fileName = args[0];
      ParameterDatabase parameters = new ParameterDatabase(new File(fileName));
      MarketSimulation simulation = new MarketSimulation();
      simulation.setup(parameters, new Parameter(P_SIMULATION));
      simulation.run();
      simulation.report();

    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }
}
