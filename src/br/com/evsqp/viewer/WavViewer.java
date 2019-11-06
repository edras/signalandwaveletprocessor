package br.com.evsqp.viewer;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import br.com.evsqp.reader.WavReader;

public class WavViewer extends AbstractViewer{
	
	long dr  = 0;
	int currentPage = 0;
	int page = 300;
	int samplingRate;
	Float scale_step = .1F;

	/**
	 * 
	 */
	private static final long serialVersionUID = -8992879357271601735L;

	public WavViewer(WavReader wavReader) {
		this.reader = wavReader;
	}

	@Override
	public void view() {
		
   	     JFreeChart chart = createChart();    
   
   	     ChartPanel chartPanel = new ChartPanel(chart);
   	     getContentPane().add(chartPanel);
   	     pack();
   	     setVisible(true);		
	}

	@Override
	protected JFreeChart createChart() {	
		
		WavReader reader = (WavReader)this.reader;
		
		reader.setFastReading(true);
		TimeSeries series = reader.getTimeSeries(dr, page);
		
		channels = reader.getNumberOfChannels();
 		 
		dataset = new TimeSeriesCollection();
   		dataset.addSeries(series);  
		
		 JFreeChart chart = ChartFactory.createTimeSeriesChart(
        		reader.getFile().getName(), "segundos", "amplitude", 
        		dataset, 
        		false, false, false);
		
  	     XYPlot plot = (XYPlot)chart.getPlot();
   	     
  	     plot.getRangeAxis().setRange(-1.0, 1.0);
  	     domainAxis = plot.getDomainAxis();

  	     plot.setBackgroundPaint(Color.WHITE);
  	     plot.setDomainGridlinePaint(Color.GRAY);
  	     plot.setRangeGridlinePaint(Color.GRAY);
  	     
  	     XYLineAndShapeRenderer renderer = new XYSplineRenderer();
  	     renderer.setSeriesShapesVisible(0, false);
  	     renderer.setSeriesPaint(0, Color.BLACK);
  	     plot.setRenderer(renderer);
  	     
  	     scale = 1;
  	     
  	     return chart;

	}

	@Override
	protected void nextPage() {
		
		domainAxis.setAutoRange(false);
		
		TimeSeries series = dataset.getSeries(0);
		series.removeAgedItems(false);
		series.delete(0, series.getItemCount()-1);
		
		dr += page*reader.getSamplingRate()/1000;
		TimeSeries timeSeries = ((WavReader)reader).getTimeSeries(dr, page);
		
		series.addAndOrUpdate(timeSeries);
		
		domainAxis.setAutoRange(true);
		
		printPageRange();
		changeScale(1);
		
	}

	@Override
	protected void previousPage() {
		
		domainAxis.setAutoRange(false);
		
		TimeSeries series = dataset.getSeries(0);
		series.removeAgedItems(false);
		series.delete(0, series.getItemCount()-1);
		
		dr -= page*reader.getSamplingRate()/1000;
		if(dr < 0) dr = 0;
		TimeSeries timeSeries = ((WavReader)reader).getTimeSeries(dr, page);
		
		series.addAndOrUpdate(timeSeries);
		
		domainAxis.setAutoRange(true);
		
		printPageRange();
		changeScale(1);
		
	}

	@Override
	protected void incScale() {
		
		int maxScale = 2;
		
		double tmp = scale;
		scale += scale_step;
		if(scale > maxScale) scale = maxScale;
		if(tmp != scale) changeScale(tmp);
		
	}

	@Override
	protected void decScale() {
		
		float minScale = 0.5F;
		
		double tmp = scale;
		scale -= scale_step;
		if(scale < minScale) scale = minScale;		
		if(tmp != scale) changeScale(tmp);	
		
	}

	@Override
	protected void goToEnd() {
		
		domainAxis.setAutoRange(false);
		
		TimeSeries series = dataset.getSeries(0);
		series.removeAgedItems(false);
		series.delete(0, series.getItemCount()-1);
		
		dr = -1;
		TimeSeries timeSeries = ((WavReader)reader).getTimeSeries(dr, page);
		
		series.addAndOrUpdate(timeSeries);
		
		domainAxis.setAutoRange(true);
		
		printPageRange();
		changeScale(1);
	}

	@Override
	protected void goHome() {
		
		domainAxis.setAutoRange(false);
		
		TimeSeries series = dataset.getSeries(0);
		series.removeAgedItems(false);
		series.delete(0, series.getItemCount()-1);
		
		dr = 0;
		TimeSeries timeSeries = ((WavReader)reader).getTimeSeries(dr, page);
		
		series.addAndOrUpdate(timeSeries);
		
		domainAxis.setAutoRange(true);
		
		printPageRange();
		changeScale(1);
		
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
		page = pageSize;		
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
		return currentPage;
	}

	@Override
	protected void addPopUpMenu(ChartPanel charPanel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDefaultPageSize() {
		return 300; // 300 milisegundos
	}

}
