package br.com.evsqp.display.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;

public class Histogram {
	
	double[] vector;

	public static void main(String[] args) {
		
		Histogram histogram = new Histogram();
		histogram.loadData();
		
	    JFreeChart chart = ChartFactory.createHistogram(
	              "Class Distribution Histogram", 
	              null, 
	              null, 
	              histogram.getData(), 
	              PlotOrientation.VERTICAL, 
	              true, 
	              true, 
	              false
	          );
	    
	    ChartPanel cpanel = new ChartPanel(chart);
	    XYPlot plot1 = (XYPlot)chart.getPlot();
	    plot1.getRenderer().setSeriesPaint(0, Color.BLUE);
	    JFrame jFrame = new JFrame();
	    jFrame.getContentPane().add(cpanel, BorderLayout.CENTER); 
	    jFrame.pack();
	    jFrame.setVisible(true);

	}

	private IntervalXYDataset getData() {
		
		HistogramDataset histogram = new HistogramDataset();
		histogram.addSeries(0, vector, 20);
		return histogram;
	}

	private void loadData() {
		
		int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Load random data?", "Question", JOptionPane.OK_CANCEL_OPTION);
		if(showConfirmDialog == JOptionPane.OK_OPTION){
			vector = new double[100];
			for (int i = 0; i < vector.length; i++) {
				vector[i] = Math.random();
			}
		}
		else {
			if(!loadFile()) System.exit(0);
		}
		
	}

	private boolean loadFile() {
		
		JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {        	
        	File[] selectedFiles = fc.getSelectedFiles();
        	vector = new double[selectedFiles.length];
        	for (int i = 0; i < selectedFiles.length; i++) {
				File file = selectedFiles[i];
				String[] split = file.getName().split("_");
				if(split.length <2) return false;
				vector[i] = Double.valueOf(split[1]);
			}
        }
        else return false;
        
        return true;
	}

}
