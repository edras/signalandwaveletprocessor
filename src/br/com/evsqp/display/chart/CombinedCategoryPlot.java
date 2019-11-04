package br.com.evsqp.display.chart;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

import br.com.evsqp.display.renderer.CombinedCategorySampleRenderer;

public class CombinedCategoryPlot extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CombinedDomainCategoryPlot plot;
	private CategoryAxis domainAxis;
	private final CombinedCategorySampleRenderer renderer;
	
	public CombinedCategoryPlot(String titel) {
        
		super(titel);
		
        domainAxis = new CategoryAxis("");
        domainAxis.setTickLabelsVisible(false);
        plot = new CombinedDomainCategoryPlot(domainAxis);
        renderer = new CombinedCategorySampleRenderer();
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());

        final JFreeChart chart = createChart();
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(
        new java.awt.Dimension(600, 350));
        setContentPane(chartPanel);
    }

    private JFreeChart createChart() {

        plot.setGap(10.0);

        final JFreeChart chart = new JFreeChart(
                "",
                new Font("SansSerif", Font.BOLD, 12),
                plot,
                false);

        return chart;
    }
    
    public void addData(double[] data, String title){
    	
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < data.length; i++) 
            dataset.addValue(data[i], title, "" + (i + 1));
                
        final NumberAxis rangeAxis = new NumberAxis(title);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        final CategoryPlot subplot = new CategoryPlot(dataset, null, rangeAxis, renderer);
        subplot.setDomainGridlinesVisible(true);
        plot.add(subplot, 1);
    }
    
    public void addData(DefaultCategoryDataset dataset, String title){
        final NumberAxis rangeAxis = new NumberAxis(title);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        final CategoryPlot subplot = new CategoryPlot(dataset, null, rangeAxis, renderer);
        subplot.setDomainGridlinesVisible(true);
        subplot.setRangeCrosshairVisible(true);
        subplot.setRangeCrosshairPaint(Color.BLACK);
        plot.add(subplot, 1);
    }

    public static void main(final String[] args) {

        final String title = "Combined Category Plot Demo";
        final CombinedCategoryPlot demo = new CombinedCategoryPlot(title);
        
        double[] data1 = new double[]{3, 2, 2, 6, 8, 4, 1, 8, 9, 11};
        double[] data2 = new double[]{5, 4, 3, 6, 7, 6,};
        
        demo.addData(data1, "data 1");
        demo.addData(data2, "data 2");
        demo.setDomainName("Samples");
        
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

	public void setDomainName(String string) {
		domainAxis.setLabel(string);		
	}
} 