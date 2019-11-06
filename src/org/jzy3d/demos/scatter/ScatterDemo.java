package org.jzy3d.demos.scatter;

import javax.swing.JFrame;

import org.math.plot.Plot3DPanel;

public class ScatterDemo {
	
	public static void main(String[] args) throws Exception {
		
		  double[] x = {1,2,3,4,5};
		  double[] y = {6,7,8,9,10};
		  double[] z = {1,1,2,3,4};
		  
		  Plot3DPanel plot = new Plot3DPanel();
			 
		  plot.addScatterPlot("Teste", x, y, z);
		  plot.addScatterPlot("Teste2", z, x, y);
				 
		  // put the PlotPanel in a JFrame, as a JPanel
		  JFrame frame = new JFrame("a scatter panel");
		  frame.setContentPane(plot);
		  frame.pack();
		  frame.setVisible(true);
	}	
	
}