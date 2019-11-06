package br.com.evsqp.display.wavelet;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import br.com.evsqp.display.renderer.SampleRenderer;


public class WaveletResultPlot extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	double[] signal, result;
	HashMap<String,double[]> resultHs;
	XYDataset approx;
	
	Integer levels = 0;
	Integer size = 0;
	double maxValue = 0;
	boolean absolute = false;
	boolean showSignal = false;
	boolean image = false;
	boolean lastApprox = false;
	boolean allApprox = false;
	boolean normalized = false;
	
	SampleRenderer coeffRenderer;
	XYLineAndShapeRenderer signalRenderer;
	XYSplineRenderer appRenderer;

	private JFreeChart chart;

	public WaveletResultPlot() {
		
		coeffRenderer = new SampleRenderer();
		signalRenderer = new XYLineAndShapeRenderer();
		appRenderer = new XYSplineRenderer();
		
		config();
	}
	
	public WaveletResultPlot(String title, double[] signal, double[] result, int levels) {
		super(title);
		setSignal(signal);
		setResult(result);
		setLevel(levels);
		showLastAprox();
	}

	private void testResultArray(){

		if(result == null && resultHs == null){
    		System.err.println("Result array is empty");
    		System.exit(0);
    	}
    	
    	if(levels == null){
    		System.err.println("Number of levels is not defined");
    		System.exit(0);
    	}		
	}
	
	private XYDataset[] createDataset() {
    	
		testResultArray();	
		
		final XYDataset[] dataset;

		if(showSignal)	size = signal.length;
		
		if(resultHs == null){
			
			if(size==0) size = result.length;
			if(allApprox) dataset = readApprox();
			else dataset = (XYDataset[]) readDetails();
			
		} else {
			
			if(size==0) size = resultHs.get("D1").length;
			dataset = readHash();
		}		

		return dataset;
	}

	private XYDataset[] readHash() {
		
		// Check if there are all levels
		if(levels == 0){ // levels is not known
			for(int i=0; i<resultHs.size(); i++){
				if(!resultHs.containsKey("D"+(i+1))){
					levels = i;
					break;
				}
			}
		} else { // levels is known
			for (int i = 0; i < levels; i++) {
				String t = (allApprox?"A":"D")+(i+1);
				if(!resultHs.containsKey(t)) {
					levels = i;
					break;
				}
			}
		}
		
		final XYDataset[] dataset = new XYDataset[levels];
		
		String t = null;
		
		for(int i=0; i<levels; i++){
			
			t = (allApprox?"A":"D")+(i+1);
			double[] ds = resultHs.get(t);
			
			XYSeries series = new XYSeries(allApprox?"A"+(i+1):"D"+(i+1));
			insertData(series, ds, 0, ds.length, absolute);
	    	dataset[i] = new XYSeriesCollection(series);
		}
		
		if(lastApprox){
			
			double[] ds = resultHs.get(t.replace("D", "A"));
			
			XYSeries series = new XYSeries("Approximation");
			insertData(series, ds, 0, ds.length, false);
	    	approx = new XYSeriesCollection(series);
		}
		
		return dataset;
	}

	private XYDataset[] readApprox() {
		
		final XYDataset[] approx = new XYSeriesCollection[levels];
		
		// read the levels inside the vector		
		readVector(approx);
		
		return approx;
	}

	private XYDataset[] readDetails() {
		
		final XYDataset[] details = new XYSeriesCollection[levels];
		
		// read the levels inside the vector and return the
		// last index vector read
		int end = readVector(details);
		
		if(lastApprox){			
			XYSeries series = new XYSeries("Approximation");
			insertData(series, result, 0, end, false);
			approx = new XYSeriesCollection(series);	    	
		}
		
        return details;
    }

    private int readVector(XYDataset[] data) {
		
    	int division;
    	int start;
    	int end = result.length;
    	
    	for(int level = 1; level <= levels ; level++){
        	
        	division = (int)Math.pow(2, level);
        	start = (int) (result.length/division);
        	
			XYSeries series = new XYSeries(allApprox?"A"+level:"D"+level);
			insertData(series, result, start, end, absolute);
        	data[level-1] = new XYSeriesCollection(series);
        	
        	end = start;
        }
    	
		return end;
	}
    
    private void insertData(XYSeries series, double[] vector, int start, int end, boolean abs){

    	double index;
    	double factor = (double)(size-1)/(double)(end-start-1);
    	
    	for (int i = start; i < end; i++){
    		index = (i-start)*factor;
    		series.add(index, abs?Math.abs(vector[i]):vector[i]);
    	}
    }

	private JFreeChart createChart() {

		final CombinedDomainXYPlot plot = new CombinedDomainXYPlot();
		
        if(showSignal){
        	final XYDataset signalDataset = createSignalDataset();
        	plot.add(createSubplot(signalDataset, signalRenderer, "Signal"), 1); 	
        }
        
        final XYDataset[] dataset = createDataset();
        
        if(lastApprox)
        	plot.add(createSubplot(approx, appRenderer, "A"+levels), 1); 	
        
        
        for(int i=dataset.length-1; i>-1; i--){
        	plot.add(createSubplot(
        			dataset[i], 
        			allApprox?appRenderer:coeffRenderer, 
        			allApprox?"A"+(i+1):"D"+(i+1)),	1);
        }     
        
		ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabel("Samples");
		domainAxis.setTickLabelsVisible(false);
		domainAxis.setRange(0, size-1);

        plot.setGap(0.0);    
        
        final JFreeChart chart = new JFreeChart(
                "",
                new Font("SansSerif", Font.BOLD, 12),
                plot,
                false);

        return chart;
    }

	private XYPlot createSubplot(XYDataset signal, AbstractXYItemRenderer renderer, String titel) {
		
		final NumberAxis rangeAxis = new NumberAxis(titel);
        
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setTickLabelsVisible(false);
        rangeAxis.setTickMarksVisible(false);
        if(normalized)
        	rangeAxis.setRange(0, maxValue);
        
        if(!titel.contains("D"))
        	rangeAxis.setAutoRangeStickyZero(false);
        
        final XYPlot subplot = new XYPlot(signal, null, rangeAxis, renderer);
        
        return subplot;
	}
	
	

	private void config() {
		
        signalRenderer.setBaseShapesVisible(false);
        signalRenderer.setSeriesPaint(0, Color.BLUE);
        
        appRenderer.setBaseShapesVisible(false);		
        appRenderer.setSeriesPaint(0, Color.RED);

        coeffRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        coeffRenderer.setSeriesPaint(0, Color.RED);
        
        addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == 'n'){
					normalized = !normalized;
					
					CombinedDomainXYPlot plot = (CombinedDomainXYPlot) chart.getPlot();
					List<?> subplots = plot.getSubplots();
					
					if(subplots.get(0) instanceof XYPlot){
						for(int i = 0; i< subplots.size(); i++){
							XYPlot subplot = (XYPlot) subplots.get(i);
							ValueAxis rangeAxis = subplot.getRangeAxis(0);
							if(!rangeAxis.getLabel().contains("D")) continue;
							if(normalized) {
								rangeAxis.setAutoRange(false);
								rangeAxis.setRange(0, maxValue);
							}
							else rangeAxis.setAutoRange(true);
						}			
						
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
        
	}

	private XYDataset createSignalDataset() {
		
    	if(signal == null){
    		System.err.println("Signal array is empty");
    		System.exit(0);
    	}
    	
    	XYSeries series = new XYSeries("Signal");
    	
       	for (int i = 0; i < signal.length; i++)
        	series.add(i, signal[i]);
        
        final XYDataset dataset = new XYSeriesCollection(series);
        return dataset;
	}

	public static void main(String[] args) {

		double[] signal = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1 };
		double[] result = new double[]{0, 1, 0, 1, 2, 2, 3, 2, 1, 1, 1, 1, 2, 1, 1, 1 };
		int levels = 2;
		
        final String title = "Wavelet coefficients";
        final WaveletResultPlot demo = new WaveletResultPlot();
        demo.setTitel(title);
        demo.setSignal(signal);
        demo.setResult(result);
        demo.setLevel(levels);
        demo.setNormalized(false);
        demo.showLastAprox();
        demo.showDetails();

        RefineryUtilities.centerFrameOnScreen(demo);

	}

	public void setTitel(String title) {
		this.setTitle(title);		
	}

	public void setSignal(double[] signal) {
		this.signal = signal;	
		this.showSignal = true;
	}

	public void setResult(double[] result) {
		this.result = result;
		maxValue = 0;
		for (int i = 0; i < result.length; i++) {
			if(Math.abs(result[i])>maxValue) maxValue = Math.abs(result[i]); 
		}
	}
	
	public void setResult(HashMap<String,double[]> resultHs) {
		this.resultHs = resultHs;
		maxValue = 0;
		for(String key : resultHs.keySet()){
			
			if(!key.toLowerCase().contains("d")) continue;

			double[] result = resultHs.get(key);
			for (int i = 0; i < result.length; i++) {
				if(Math.abs(result[i])>maxValue) 
					maxValue = Math.abs(result[i]); 
			}
		}
	}

	public void setLevel(int levels) {
		this.levels = levels;		
	}

	public void showAproximation() {
		this.allApprox = true;
		this.lastApprox = false;
		this.absolute = false;
		doChart();
	}
	
	private void doChart(){		
		
        chart = createChart();
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 350));
        this.setContentPane(chartPanel);  

        this.pack();
        this.setVisible(true);
	}

	public void showLastAprox() {
		lastApprox = true;		
	}

	public void showDetails() {
		doChart();
	}

	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;		
	}

	public boolean isNormalized() {
		return normalized;
	}

	public void setNormalized(boolean normalized) {
		this.normalized = normalized;
	}
}
