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

package net.sourceforge.jasa.replication.electricity;

import net.sourceforge.jasa.market.rules.ParameterizablePricing;

public class ParameterizedElectricityStats extends ElectricityStats {

	public ParameterizedElectricityStats() {
		super();
	}

	public double calculateEquilibriumPrice() {
		double k = ((ParameterizablePricing) auction.getAuctioneer()).getK();
		return getMinPrice() * k + getMaxPrice() * (1 - k);
	}

}