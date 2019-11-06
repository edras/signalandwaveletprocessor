package br.com.evsqp.display.wavelet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import br.com.evsqp.display.renderer.SampleRenderer;

public class WaveletPlot extends JFrame  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	double[] coef, scale;

	public WaveletPlot(String titel, double[] coef, double[] scale) {

		super(titel);
		
		this.coef = coef;
		this.scale = scale;
	
        final JFreeChart chart = createChart();
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(
        new java.awt.Dimension(600, 350));
        setContentPane(chartPanel);        
	}
	
    public XYDataset createDataset(double[] array, String name) {
    	
    	if(array == null){
    		System.err.println("Array is empty");
    		System.exit(0);
    	}
    	
        XYSeries series = new XYSeries(name);
       	for (int i = 0; i < array.length; i++) {
       		series.add(i, array[i]);
        }
       	
       	return new XYSeriesCollection(series);
       	
    }

    private JFreeChart createChart() {

        final XYDataset coefDataset  = createDataset(coef, "coefficients");
        final XYDataset scaleDataset = createDataset(scale, "scales");
        
        final SampleRenderer renderer = new SampleRenderer();
        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-5.0, -5.0, 10.0, 10.0));
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
                
       	final NumberAxis coefRangeAxis = new NumberAxis("Coefficients");
        final XYPlot coefSubplot = new XYPlot(coefDataset, null, coefRangeAxis, renderer);
        coefSubplot.setDomainGridlinesVisible(true);
        coefSubplot.setRangeCrosshairVisible(true);
        coefSubplot.setRangeCrosshairPaint(Color.BLACK);
        
       	final NumberAxis scaleRangeAxis = new NumberAxis("Scale");
        final XYPlot scaleSubplot = new XYPlot(scaleDataset, null, scaleRangeAxis, renderer);
        scaleSubplot.setDomainGridlinesVisible(true);
        scaleSubplot.setRangeCrosshairVisible(true);
        scaleSubplot.setRangeCrosshairPaint(Color.BLACK);

        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot();
        plot.getDomainAxis().setLabel("values");
        plot.getDomainAxis().setRange(-1, coef.length);
        plot.add(coefSubplot);
        plot.add(scaleSubplot);
        
        final JFreeChart chart = new JFreeChart(
                "",
                new Font("SansSerif", Font.BOLD, 12),
                plot,
                false);

        return chart;
    }

	public static void main(String[] args) {

	    int _waveLength = 6;

	    double sqrt02 = 1.4142135623730951;
	    double sqrt10 = Math.sqrt( 10. );
	    double constA = Math.sqrt( 5. + 2. * sqrt10 );

	    double[] _scales = new double[ _waveLength ]; // can be done in static way also; faster?

	    _scales[ 0 ] = ( 1. + 1. * sqrt10 + 1. * constA ) / 16. / sqrt02; // h0
	    _scales[ 1 ] = ( 5. + 1. * sqrt10 + 3. * constA ) / 16. / sqrt02; // h1
	    _scales[ 2 ] = ( 10. - 2. * sqrt10 + 2. * constA ) / 16. / sqrt02; // h2
	    _scales[ 3 ] = ( 10. - 2. * sqrt10 - 2. * constA ) / 16. / sqrt02; // h3
	    _scales[ 4 ] = ( 5. + 1. * sqrt10 - 3. * constA ) / 16. / sqrt02; // h4
	    _scales[ 5 ] = ( 1. + 1. * sqrt10 - 1. * constA ) / 16. / sqrt02; // h5

	    double[] _coeffs = new double[ _waveLength ]; // can be done in static way also; faster?

	    _coeffs[ 0 ] = _scales[ 5 ]; //    h5
	    _coeffs[ 1 ] = -_scales[ 4 ]; //  -h4
	    _coeffs[ 2 ] = _scales[ 3 ]; //    h3
	    _coeffs[ 3 ] = -_scales[ 2 ]; //  -h2
	    _coeffs[ 4 ] = _scales[ 1 ]; //    h1
	    _coeffs[ 5 ] = -_scales[ 0 ]; //  -h0

        final String title = "Daubechies 03";
        final WaveletPlot demo = new WaveletPlot(title, _coeffs, _scales);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

	}
}
