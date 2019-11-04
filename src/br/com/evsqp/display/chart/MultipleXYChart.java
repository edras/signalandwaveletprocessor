package br.com.evsqp.display.chart;

import java.awt.FlowLayout;

import javax.swing.JFrame;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class MultipleXYChart extends JFrame{

	private static final long serialVersionUID = 7467549376367467743L;

	public MultipleXYChart(String appTitle) {	
		super(appTitle);
	    setLayout(new FlowLayout());
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void addChart(XYChart xyPanel){
		add(xyPanel.getChartPanel());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
  		 XYSeries series = new XYSeries("Serie 1");
   		 for(int i=0;i<10; i++){
   			 series.add(i,i);
   		 }
   		 
   		 XYSeriesCollection dataset = new XYSeriesCollection();
   		 dataset.addSeries(series);         
         
   	     XYChart chart1 = new XYChart("Output", "chart1");
   		 chart1.setLabelx("x");
   		 chart1.setLabely("Y");
   	     chart1.setDataset(dataset);
   	     
   		 XYSeries series2 = new XYSeries("Serie 2");
   		 for(int i=0;i<10; i++){
   			 series2.add(i,i+1);
   		 }
   		 
   		 XYSeriesCollection dataset2 = new XYSeriesCollection();
   		 dataset2.addSeries(series2);         

   		 XYChart chart2 = new XYChart("Output", "chart2");
   		 chart2.setLabelx("x");
   		 chart2.setLabely("Y");
   	     chart2.setDataset(dataset2);
   	     
   	     MultipleXYChart multipleXYChart = new MultipleXYChart("Exemplo de Multiplos JFreeCharts");
   	     multipleXYChart.addChart(chart1);
   	     multipleXYChart.addChart(chart2);
   	     multipleXYChart.pack();
   	     multipleXYChart.setVisible(true);
	}

}
