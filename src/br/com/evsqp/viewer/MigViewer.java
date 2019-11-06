/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package br.com.evsqp.viewer;

import java.awt.Color;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeriesCollection;

import br.com.evsqp.reader.MigReader;

public class MigViewer extends AbstractViewer {
	
	private static final long serialVersionUID = -7097053492721208024L;

	private MigReader reader;
	
    public MigViewer(MigReader reader) {    	
    	this.reader = reader;
    }

	@Override
	public void view() {
		
  		 dataset = new TimeSeriesCollection();
   		 dataset.addSeries(reader.getTimeSeries());  
   		 
   		 JFreeChart chart = createChart(); 
   		 XYPlot plot = (XYPlot)chart.getPlot();
   	     
   	     plot.getRangeAxis().setRange(-300.0, 250.0);
   	     domainAxis = plot.getDomainAxis();
   	     
   	     ((DateAxis)domainAxis).setDateFormatOverride(new SimpleDateFormat("ss:S"));
   	     
   	     //plot.getDomainAxis().setRange(0, 2.0);    //settsetXTicks(0.001, 2, 10);
   	     //plot.getDomainAxis().sett

   	     plot.setBackgroundPaint(Color.WHITE);
   	     plot.setDomainGridlinePaint(Color.GRAY);
   	     plot.setRangeGridlinePaint(Color.GRAY);
   	     
   	     XYItemRenderer renderer = plot.getRenderer();
   	     renderer.setSeriesPaint(0, Color.BLACK);
   	     
   	     ChartPanel chartPanel = new ChartPanel(chart);
   	     getContentPane().add(chartPanel);
   	     pack();
   	     setVisible(true);	
		
	}
	

	@Override
	protected JFreeChart createChart() {
		return ChartFactory.createTimeSeriesChart(
        		reader.getFile().getName(), "segundos", "microvolts", 
        		dataset, 
        		false, false, false);
	}

	@Override
	protected void nextPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void previousPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void incScale() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void decScale() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void goToEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void goHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void go10Pages() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOffset(int offset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPageSize(int pageSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateRangeAxis() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void printPageRange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void addPopUpMenu(ChartPanel charPanel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDefaultPageSize() {
		return 2;
	}   
    
}


