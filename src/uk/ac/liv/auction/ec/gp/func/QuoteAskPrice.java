package uk.ac.liv.auction.ec.gp.func;

import ec.gp.*;
import ec.*;

import uk.ac.liv.ec.gp.func.*;

import uk.ac.liv.util.GenericDouble;


public class QuoteAskPrice extends GPNode {

  public void eval( EvolutionState state, int thread, GPData input, ADFStack stack, GPIndividual individual, Problem problem ) {
    ((GPNumberData) input).data = new GenericDouble( new Double(((GPAuctioneer) individual).getQuote().getAsk()) );
  }

  public String toString() {
    return "QuoteAskPrice";
  }
}