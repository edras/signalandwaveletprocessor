package br.com.evsqp.display.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

public class ScatterChart extends JFrame {

	private static final long serialVersionUID = 1L;
	JFreeChart chart;
	ChartPanel chartPanel;
	XYPlot xyPlot;
	XYSeriesCollection dataset;
	String chartTitle;
	String labelx;
	String labely;
	
	public ScatterChart(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        this.chartTitle = chartTitle;
        this.dataset = new XYSeriesCollection();
        this.labelx = "eixo x";
        this.labely = "eixo y";
    }
	
	public XYPlot getXyPlot() {
		return xyPlot;
	}
	
	public void setLabelx(String labelx) {
		this.labelx = labelx;
	}
	
	public void setLabely(String labely) {
		this.labely = labely;
	}
	
	public void setDataset(XYSeriesCollection dataset){
		
		this.dataset = dataset;
		
        // based on the dataset we create the chart
        chart = createChart(dataset, chartTitle);
        // we put the chart into a panel
        chartPanel = new ChartPanel(chart);
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        // add it to our application
        setContentPane(chartPanel);		
	}
	
	public XYSeriesCollection getDataset(){
		return dataset;
	}
	
	public JFreeChart getChart(){
		return chart;
	}
	
	public ChartPanel getChartPanel(){
		return chartPanel;
	}
	
	public void setXAutoRange(boolean b){
    	ValueAxis xAxis = xyPlot.getDomainAxis();
    	xAxis.setAutoRange(b);		
	}
    
	public void setYAutoRange(boolean b){
    	ValueAxis yAxis = xyPlot.getRangeAxis();
    	yAxis.setAutoRange(b);
	}

	public void setXRange(double min, double max){
    	ValueAxis xAxis = xyPlot.getDomainAxis();
    	xAxis.setAutoRange(false);
    	xAxis.setRange(min, max);
	}
	
	public void setXTicks(double min_ticks, double max_ticks, double division){
		ValueAxis xAxis = xyPlot.getDomainAxis();
		TickUnits tickUnits = new TickUnits();
		//double tick = (max_ticks-min_ticks)/division;
		for(double a=max_ticks;a>=min_ticks;a/=10){
			TickUnit tickUnit = new NumberTickUnit(a);
			tickUnits.add(tickUnit);			
		}
		xAxis.setStandardTickUnits(tickUnits);
		//TickUnitSource standardTickUnits = xAxis.getStandardTickUnits();
		//int i = 0;
	}

	public void setYRange(double min, double max){
    	ValueAxis yAxis = xyPlot.getRangeAxis();
    	yAxis.setAutoRange(false);
    	yAxis.setRange(min, max);
	}

	/**
     * Creates a chart
     */
    private JFreeChart createChart(XYSeriesCollection dataset, String title) {
        
    	JFreeChart chart = ChartFactory.createScatterPlot(
    		    title,                		// Title
    		    labelx,                     // x-axis Label
    		    labely,                     // y-axis Label
    		    dataset,                    // Dataset
    		    PlotOrientation.VERTICAL,  // Plot Orientation
    		    true,                      // Show Legend
    		    true,                      // Use tooltips
    		    false                      // Configure chart to generate URLs?
    		    );

    	xyPlot = chart.getXYPlot();
    	return chart;        
    }
    
    public Marker addMarker(double position){
    	return addMarker(position, Color.red, "", 0);
    }
    
    public Marker addMarker(double position, Color color, String text, Integer stroke){
	     
    	Marker marker = new ValueMarker(5.0);   
    	marker.setPaint(color);
    	marker.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));  
		marker.setLabelAnchor(RectangleAnchor.TOP);  
		marker.setLabelTextAnchor(TextAnchor.TOP_LEFT);  
    	
		if(text.length()!=0)
	    	 marker.setLabel("text on the line");  
				
		if(stroke != 0)
			marker.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,1.0f, new float[] { 10.0f, 6.0f }, 0.0f));
				 
		xyPlot.addDomainMarker(marker); 
		return marker;
    }
    
    public XYSeriesCollection createCollection(Double[] matrix, String name){
    	
  		 XYSeries series = new XYSeries(name);
  		 for(int i=0; i<matrix.length; i++){
  	   		 series.add(i, matrix[i]);
  		 }   		 
   		 
   		 dataset.addSeries(series);
   		 
   		 return dataset;   	
    }
    
   	 public static void main(String[] args) {
   		
   		 XYSeries series = new XYSeries("Serie 1");
   		 for(int i=0;i<10; i++){
   			 series.add(i,i);
   		 }
   		 
   		 XYSeriesCollection dataset = new XYSeriesCollection();
   		 dataset.addSeries(series);         
         
   	     ScatterChart demo = new ScatterChart("Output", "growing rate");
   		 demo.setLabelx("x");
   		 demo.setLabely("Y");
   	     demo.setDataset(dataset);
   	     
   	     demo.pack();
   	     demo.setVisible(true);
   	     
   		 XYSeries series2 = new XYSeries("Serie 2");
   		 for(int i=0;i<10; i++){
   			 series2.add(i,i+1);
   		 }
   		 
   		 dataset.addSeries(series2);
   		 
   		 Marker marker = demo.addMarker(5.0); 	
   		 marker.setAlpha((float) 0.8);
   		 
   	 }
}


