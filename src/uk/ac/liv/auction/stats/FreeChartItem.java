/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2001-2005 Steve Phelps
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

package uk.ac.liv.auction.stats;

import org.apache.log4j.Logger;

import ec.util.Parameter;
import ec.util.ParameterDatabase;

import uk.ac.liv.auction.event.AuctionEvent;
import uk.ac.liv.auction.event.AuctionEventListener;
import uk.ac.liv.util.Parameterizable;

/**
 * A class defining the common properties and methods of data series and markers.
 * 
 * <p><b>Parameters</b><br>
 *
 * <table>
 *
 * <tr><td valign=top><i>base</i><tt>.name</tt><br>
 * <font size=-1> string </font></td>
 * <td valign=top></td></tr>
 * 
 * <tr><td valign=top><i>base</i><tt>.axis</tt><br>
 * <font size=-1> int </font></td>
 * <td valign=top>(the index of the axis this chart item is associated with)</td></tr>
 * 
 * <tr><td valign=top><i>base</i><tt>.renderer</tt><br>
 * <font size=-1> int </font></td>
 * <td valign=top>(the index of the renderer this chart item uses)</td></tr>
 * 
 * </table>
 *
 * @author Jinzhong Niu
 * @version $Revision$
 */
public class FreeChartItem implements AuctionEventListener, Parameterizable {

  static Logger logger = Logger.getLogger(FreeChartItem.class);

  public static final String P_NAME = "name";
  public static final String P_AXIS = "axis";
  public static final String P_RENDERER = "renderer";

  protected String name;

  protected FreeChartGraph graph;

  protected int axisIndex;
  protected int rendererIndex;

  public FreeChartItem() {
    name = "";
  }

  public void setup(ParameterDatabase parameters, Parameter base) {
    name = parameters.getStringWithDefault(base.push(P_NAME), name);
    axisIndex = parameters.getIntWithDefault(base.push(P_AXIS), null, 0);
    rendererIndex = parameters.getIntWithDefault(base.push(P_RENDERER), null, 0);
  }

  public void eventOccurred(AuctionEvent event) {
  }
  
  public int getAxisIndex() {
    return axisIndex;
  }
  
  public int getRendererIndex() {
    return rendererIndex;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return Returns the graph.
   */
  public FreeChartGraph getGraph() {
    return graph;
  }

  /**
   * @param graph
   *          The graph to set.
   */
  public void setGraph(FreeChartGraph graph) {
    this.graph = graph;
  }
}