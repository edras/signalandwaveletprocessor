package br.com.evsqp.display.chart;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

public class EEGLikeChart extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2849514945865018466L;
	
	short data[][] = {	{1,0,1,0,1,0,1,0,1,0},
						{1,-1,1,-1,1,-1,1,-1,1,-1},
						{0,0,0,0,5,5,5,0,0,0},
						{-2,-2,-2,-2,-4,-4,-2,-2,-2,-2},
						{1,2,3,1,2,3,1,2,3,1}};
	
	public EEGLikeChart() {
	}
	
	public void setTitel(String title) {
		this.setTitle(title);		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        final String title = "Wavelet coefficients";
        final EEGLikeChart demo = new EEGLikeChart();
        demo.setTitel(title);
        demo.doGraph();

        RefineryUtilities.centerFrameOnScreen(demo);

	}
	
	private void doGraph() {
		
        final JFreeChart chart = createChart();
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 350));
        setContentPane(chartPanel);  

        pack();
        setVisible(true);
		
	}

	private JFreeChart createChart() {

        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot();

        final 	XYSplineRenderer lineRenderer = new XYSplineRenderer();
        lineRenderer.setSeriesPaint(0, Color.BLACK);
        //lineRenderer.setBaseLinesVisible(false);
        
        final XYDataset[] dataset = new XYDataset[data.length];
        
        for(int i=0; i<dataset.length; i++){
        	dataset[i] = createDataset(i);
        	final NumberAxis rangeAxis = new NumberAxis(String.valueOf(i));
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            rangeAxis.setTickMarksVisible(true);
            rangeAxis.setTickLabelsVisible(false);
            rangeAxis.setLabelAngle(Math.toRadians(90));
//            rangeAxis.setLowerBound(-55.0);
//            rangeAxis.setUpperBound(55.0);
            final XYPlot subplot = new XYPlot(dataset[i], null, rangeAxis, lineRenderer);
            //subplot.setDomainGridlinesVisible(true);
            plot.add(subplot, 1);
        }     
        
        plot.setGap(0.0);    
        
        final JFreeChart chart = new JFreeChart(
                "",
                new Font("SansSerif", Font.BOLD, 12),
                plot,
                false);

        return chart;
    }
	
	private XYDataset createDataset(int channel) {

		XYSeries series = new XYSeries(channel+1);
		for (int i = 0; i < data.length; i++)
			series.add(i,data[channel][i]);
        final XYDataset dataset = new XYSeriesCollection(series);
        return dataset;
	}

}
