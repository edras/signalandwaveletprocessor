package br.com.evsqp.display.chart;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;


public class PieChart extends JFrame {

	private static final long serialVersionUID = 1L;

	public PieChart(String applicationTitle, String chartTitle, PieDataset dataset) {
        super(applicationTitle);

        // based on the dataset we create the chart
        JFreeChart chart = createChart(dataset, chartTitle);
        // we put the chart into a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        // add it to our application
        setContentPane(chartPanel);

    }
    
    /**
     * Creates a chart
     */
    private JFreeChart createChart(PieDataset dataset, String title) {
        
        JFreeChart chart = ChartFactory.createPieChart3D(
            title,  				// chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(1.0f);
        return chart;
        
    }
    
   	 public static void main(String[] args) {
   		 
         DefaultPieDataset dataset = new DefaultPieDataset();
         dataset.setValue("Linux", 29);
         dataset.setValue("Mac", 20);
         dataset.setValue("Windows", 51);
         
   	     PieChart demo = new PieChart("Comparison", "Which operating system are you using?",dataset);
   	     demo.pack();
   	     demo.setVisible(true);
   	 }
}