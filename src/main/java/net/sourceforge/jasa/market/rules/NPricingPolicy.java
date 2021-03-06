/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2013 Steve Phelps
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

package net.sourceforge.jasa.market.rules;

import java.io.Serializable;

import net.sourceforge.jabm.util.FixedLengthQueue;
import net.sourceforge.jabm.util.Resetable;
import net.sourceforge.jasa.market.MarketQuote;
import net.sourceforge.jasa.market.Order;

import org.apache.log4j.Logger;

/**
 * A discriminatory pricing policy that uses the average of the last <i>n</i>
 * pair of bid and ask prices leading to transactions as the clearing price. In
 * case of the price falls out of the range between the current bid and ask, the
 * nearest boundary is used.
 * 
 * @author Jinzhong Niu
 * @version $Revision$
 */

public class NPricingPolicy implements PricingPolicy, Resetable, Serializable {

	protected int n;

	protected FixedLengthQueue queue;

	static Logger logger = Logger.getLogger(NPricingPolicy.class);

	public NPricingPolicy() {
		this(1);
	}

	public NPricingPolicy(int n) {
		this.n = n;
	}

	public void initialize() {
		queue = new FixedLengthQueue(2 * n);
	}

	public void reset() {
		queue.reset();
	}

	public double determineClearingPrice(Order bid, Order ask,
	    MarketQuote clearingQuote) {

		queue.newData(bid.getPriceAsDouble());
		queue.newData(ask.getPriceAsDouble());
		double avg = queue.getMean();

		double price = (avg >= bid.getPriceAsDouble()) ? bid.getPriceAsDouble() : ((avg <= ask
		    .getPriceAsDouble()) ? ask.getPriceAsDouble() : avg);

		return price;
	}

	public String toString() {
		return "(" + getClass().getSimpleName() + " n:" + n + ")";
	}

}
