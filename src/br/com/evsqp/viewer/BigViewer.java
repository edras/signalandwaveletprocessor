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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import br.com.evsqp.reader.BigReader;
import br.com.evsqp.utils.DFilter;

public class BigViewer extends AbstractViewer {
	
	private static final long serialVersionUID = -7097053492721208024L;

	private BigReader reader;
	private XYPlot xyPlot;
	JMenuItem fftMenuItem = new JMenuItem("FFT");
	
	
    public BigViewer(BigReader reader) {    	
    	this.reader = reader;
    }
    
    public Marker addMarker(double position, Color color, String text, Integer stroke){
    	
    	TimeSeriesDataItem dataItem = dataset.getSeries(0).getDataItem((int)position);
    	Marker marker = new ValueMarker(dataItem.getPeriod().getFirstMillisecond());   
    	marker.setPaint(color);
    	marker.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));  
		marker.setLabelAnchor(RectangleAnchor.TOP);  
		marker.setLabelTextAnchor(TextAnchor.TOP_LEFT);  
    	
		if(text.length()!=0)
	    	 marker.setLabel(text);  
				
		if(stroke != 0)
			marker.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,1.0f, new float[] { 10.0f, 6.0f }, 0.0f));		
				 
		xyPlot.addDomainMarker(marker); 
		
		return marker;
    }

	@Override
	public void view() {
		
		dataset = new TimeSeriesCollection();
		((TimeSeriesCollection)dataset).addSeries(reader.getSeries());  
   		 
   		final JFreeChart chart = createChart();
   	     
   		chart.setTitle(new TextTitle(reader.getFile().getName(), new Font("Arial", Font.PLAIN, 9)));
   		
   		  	     
//   		 chart.setLabelx("segundos");
//   		 chart.setLabely("microvolts");
//   	     chart.setDataset(dataset);	   
//   	     chart.setYRange(-300.0, 250.0);
   	     //chart.setXTicks(0.001, 2, 10);
   	     
   	     //XYSeries series = dataset.getSeries(0);
   	     //double tempo = series.getDataItem(series.getItemCount()-1).getXValue()-series.getDataItem(0).getXValue();
   	     //chart.setXRange(0, 2.0);

   	     xyPlot = chart.getXYPlot();
   	     xyPlot.setRenderer(new XYLineAndShapeRenderer());
   	     xyPlot.setBackgroundPaint(Color.WHITE);
   	     xyPlot.setDomainGridlinePaint(Color.GRAY);
   	     xyPlot.setRangeGridlinePaint(Color.GRAY);
   	     
   	     ValueAxis rangeAxis = xyPlot.getRangeAxis();
   	     //rangeAxis.setLabel("microvolts");
   	     rangeAxis.setRange(-200.0, 200.0);
   	     
   	     XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
   	     renderer.setSeriesPaint(0, Color.BLACK);
   	     renderer.setSeriesStroke(0, new BasicStroke(0.5F));
   	     renderer.setBaseShapesVisible(false);
   	     ((AbstractRenderer)renderer).setAutoPopulateSeriesPaint(false);
   	     ((AbstractRenderer)renderer).setAutoPopulateSeriesStroke(false);
   	     
   	     final ChartPanel chartPanel = new ChartPanel(chart);
   	     
   	     chartPanel.getPopupMenu().addSeparator();
   	     chartPanel.getPopupMenu().add(fftMenuItem);
   	     
   	     fftMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TimeSeries series = reader.getSeries();
				DFilter.fft(reader.getSamplingRate(), series);
			}
		});
   	     
   	     setContentPane(chartPanel);
   	     pack();
   	     setVisible(true);
	}
	
	@Override
	protected JFreeChart createChart() {
		return ChartFactory.createTimeSeriesChart(
        		reader.getFile().getName(), "time", "microvolts", 
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
	public void setOffset(int offset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPageSize(int pageSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDefaultPageSize() {

		return 2; // segundos
	}

    
}


