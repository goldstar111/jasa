package net.sourceforge.jasa.report;

import net.sourceforge.jabm.event.RoundFinishedEvent;
import net.sourceforge.jasa.market.MarketQuote;
import net.sourceforge.jasa.market.MarketSimulation;

public class SpreadReportVariables extends MarketPriceReportVariables {

	@Override
	public String getName() {
		return "spread";
	}

	@Override
	public double getPrice(RoundFinishedEvent event) {
		MarketQuote quote = ((MarketSimulation) event.getSimulation())
				.getQuote();
		return quote.getBid() - quote.getAsk();
	}

}
