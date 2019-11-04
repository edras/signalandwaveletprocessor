package br.com.evsqp.lda;

import br.com.evsqp.lda.utils.SpreadSheetFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mloss.roc.Curve;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CustomXYToolTipGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.math.plot.Plot3DPanel;

import br.com.evsqp.lda.view.SampleSelection;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import br.com.evsqp.display.chart.ScatterChart;

public class LDA {

	List<Matrix> classes; 
	List<Matrix> originalClasses;
	List<Matrix> bckp;
	List<Matrix> validation;
	List<Matrix> originalValidation;
	List<Matrix> covariances;
	List<Matrix> means;
	Matrix mean;
	Matrix sw;
	Matrix sb;
	Matrix V;
	Matrix D;
	Matrix W;
	double max, min;
	double threshold;
	double[] rank;
	
	List<String> sheets = new ArrayList<String>();
	int decomposition;
	boolean showGraphs;
	boolean echo = true;
	private boolean diretorio = false;
	private File dir_base;
	private List<String> dirs = new ArrayList<String>();
	private List<String> className= new ArrayList<String>();
	private int currentDir;
	
	public LDA(){
		
		classes = new ArrayList<Matrix>();
		originalClasses = new ArrayList<Matrix>();
		bckp = new ArrayList<Matrix>();
		validation = new ArrayList<Matrix>();
		originalValidation = new ArrayList<Matrix>();
		means = new ArrayList<Matrix>();
		covariances = new ArrayList<Matrix>();
		mean = null;
		sw = null;
		sb = null;
	}
	
	public void bckpClasses(){
		bckp.clear();
		bckp.addAll(classes);
	}
	
	public void saveClasses(){
		originalClasses.clear();
		originalClasses.addAll(classes);
	}
	
	public void filterClasses(BigInteger filter){
		
		classes.clear();
		for (Matrix m : originalClasses) {
			classes.add(reduceMatrix(filter,m));
		}
		
		validation.clear();
		for (Matrix m : originalValidation) {
			validation.add(reduceMatrix(filter,m));
		}
	}
	
	public void saveValidation(){
		originalValidation.clear();
		originalValidation.addAll(validation);
	}
	
	public void restoreValidation(){
		validation.clear();
		validation.addAll(originalValidation);
	}
	
	public void restoreClasses(){
		classes.clear();
		classes.addAll(originalClasses);
	}
	
	public static double[][] generateData(int size, double meanx, double meany, double meanz, double var){
		
		double[][] distr = new double [size][3];
		
		for (int i = 0; i < size; i++) {
			
			distr[i][0] = Math.random()*var+meanx;
			distr[i][1] = Math.random()*var+meany;
			distr[i][2] = Math.random()*var+meanz;
		}
		
		return distr;
	}

	public static void main(String[] args) {
		
		LDA lda = new LDA();
		
		boolean test = true;
		
		if(test){
			
			double[][] x1 = generateData(500,2,2,2,3); //{{4.,2.,4},{2.,4.,4.5},{2.,3.,5.6},{3.,6.,4.2},{4,4,4}}; 
			double[][] x2 = generateData(500,2.5,2.5,2.5,3); //{{9.,10.,1},{6.,8.,2},{9.,5.,1.5},{8.,7.,2},{10,8,1.8}};
			//double[][] x3 = generateData(500,6,6,6,3); //{{1.,2.,1},{1.5,2.2,2},{1.8,1.0,1.5},{1.3,2.4,2},{1.7,2.7,1.8}};
			//double[][] x4 = generateData(500,5,5,5,3);
			lda.loadValues(x1);
			lda.loadValues(x2);
			//lda.loadValues(x3);
			//lda.loadValues(x4);

			lda.showGraphs = true;			
			
			try {
				lda.calcLdaProj();
			} catch (Exception e){
				return;
			}
			lda.calcROC();
			
		} else {
			
			double nrFeatures = lda.loadSpreadSheet();
			
			if(nrFeatures < 0) return;
			
			nrFeatures = Math.pow(2, nrFeatures);
			
	        int showConfirmDialog = JOptionPane.showConfirmDialog(null, 
	        		"Would you like to test all features together (YES)\n" +
	        		"OR want you try every possible combination (NO)?\n" +
	        		"There are " + nrFeatures + " combinations.", "Options", JOptionPane.YES_NO_CANCEL_OPTION);
	        
	        if(showConfirmDialog == JOptionPane.CANCEL_OPTION) return;
	        if(showConfirmDialog == JOptionPane.YES_OPTION){
	        	lda.showGraphs = true;
	        	try{
	        		lda.calcLdaProj();
	        	} catch (Exception e){
	        		System.err.println("Error:");
	        		return;
	        	}
	        	long time = System.currentTimeMillis();
				lda.calcROC();
				System.out.println("time: " + (System.currentTimeMillis() - time) + "ms");
	        } else {
	        
	        	showConfirmDialog = JOptionPane.showConfirmDialog(null, "Calc all " + nrFeatures + " possibilities?", "Selection", JOptionPane.YES_NO_OPTION);
	        	BigInteger experiment = BigInteger.ZERO.subtract(BigInteger.ONE); 
	        	
	        	if(showConfirmDialog == JOptionPane.NO_OPTION){
	        		String input = JOptionPane.showInputDialog(null, "Type a test number from 0 to " + nrFeatures);
	        		experiment = BigInteger.valueOf(Long.decode(input));
	        	}
	        	
	        	showConfirmDialog = JOptionPane.showConfirmDialog(null, "Show partial results?", "Selection", JOptionPane.YES_NO_OPTION);
	        	if(showConfirmDialog == JOptionPane.NO_OPTION) lda.echo = false;
	        	
	        	lda.doSearch(experiment);
	        }			
		}
	}

	/**
	 * Este metodo faz uma analise combinatoria de todas as caracteristicas e calcula
	 * a curva ROC para cada uma delas. O resultado eh armazenado em um arquivo com 
	 * nome LDAexperiment.txt
	 * 
	 * * @param experiment
	 *  = -1 executa todas as possibilidades
	 *  >= 0 executa apenas a possibilidade correspondente
	 */
	private void doSearch(BigInteger experiment) {
		
		Matrix x1 = classes.get(0);
		Matrix x2 = classes.get(1);
		
		long exp = 0;
		double maxArea = 0;
		double temp = 0;
		
		int end = (int)Math.pow(2, x1.getColumnDimension());
		BigInteger i = experiment;
		
		if(experiment.signum()!=-1) {
			int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Keep on processing?","continue?", JOptionPane.OK_CANCEL_OPTION);
			if(showConfirmDialog == JOptionPane.CANCEL_OPTION)
				experiment = experiment.add(BigInteger.ONE);
		} else {
			i = i.add(BigInteger.ONE);
		}
		
		System.out.println(new Date(System.currentTimeMillis()));
		System.out.println("Test result: \n");
		
		for (int start = 1;start < end; start++) {

			i = i.add(BigInteger.ONE);
			
			if(echo) System.out.print(i+" : ");
			
			resetLDA();

			classes.clear();
			classes.add(reduceMatrix(i,x1));
			classes.add(reduceMatrix(i,x2));
			
			try {
				long time = System.currentTimeMillis();
				calcLdaProj();
				if(echo) System.out.print("Time: " + (System.currentTimeMillis()-time) + "ms ");
			} catch (Exception e){
				System.err.println("Error on test: " + start);
				continue;
			}
			
			temp = calcROC();
			if(temp>maxArea) {
				maxArea = temp;
				exp = start;
				if(!echo){
					System.out.println(exp + ": AUC:" + maxArea);
				}
			}
		}		
		
		System.out.println(new Date(System.currentTimeMillis()));
		if(echo) System.out.println("Best result: " + (exp+1) + ": AUC:" + maxArea);
	}
	
	/**
	 * Este metodo calcula projeção LDA para apenas uma possibilidade. 
	 * O resultado eh armazenado em um arquivo com 
	 * nome LDAexperiment.txt
	 * 
	 * * @param experiment
	 *   executa apenas a possibilidade correspondente
	 */
	public void calcLdaProjTrial(BigInteger experiment) {
		
		BigInteger i = experiment;
		
		i = i.add(BigInteger.ONE);

		classes.clear();
		for (Matrix matrix : bckp) {
			classes.add(reduceMatrix(i,matrix));
		}

		try {
			resetLDA();
			calcLdaProj();
		} catch (Exception e){
			System.err.println("Error on LDA processing");
		}		
	}

	public void resetLDA() {
		
		covariances.clear();
		means.clear();
		
		mean = null;
		sw = null;
		sb = null;
		V = null;
		D = null;
		W = null;
	}

	/**
	 * Essa função seleciona e retorna somente 
	 * as colunas que serão mantidas da matrix x
	 * de acordo com o numero binario i
	 * @param i
	 * @param x
	 * @return
	 */
	
	private Matrix reduceMatrix(BigInteger i, Matrix x) {
		
		int k = 1;
		Matrix idt = new Matrix(x.getColumnDimension(), i.bitCount(), 0);
		for(int j=1;j<x.getColumnDimension()+1;j++){
			if(k>i.bitCount()) break;
			if(i.testBit(j-1)){
				idt.set(x.getColumnDimension()-j, idt.getColumnDimension()-k, 1);
				k++;
			}
		}		
		return x.times(idt);
	}
	
	public File[] loadSpreadSheetDOCValidation(int number){
		
		JOptionPane.showMessageDialog(null, "Please select spreadsheets with validation data "+number);
		
		JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new SpreadSheetFilter());        
        fc.setMultiSelectionEnabled(true);
        
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {        	
        	loadValidation(makeMatrixFromSpreadSheets(fc.getSelectedFiles()));
        	return fc.getSelectedFiles();
        }
        else return null;
		
	}
	
	public File[] loadSpreadSheetDOC(int number){
		
		JOptionPane.showMessageDialog(null, "Select spreadsheets containing data of class "+number);
		
		JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new SpreadSheetFilter());        
        fc.setMultiSelectionEnabled(true);
        
//        File[] files = new File[3];
//        if(number == 1) {
//        	files[0] = new File("/home/edras/Dropbox/Doutorado/tabelas/Energy_CenteredEnergy_Mean/EVApp_Export_20140613_sinais/wavelets/Haar02Orthogonal_SP0.xls");
//        	files[1] = new File("/home/edras/Dropbox/Doutorado/tabelas/Energy_CenteredEnergy_Mean/EVApp_Export_20140613_sinais/wavelets/Lege04_SP0.xls");
//        	files[2] = new File("/home/edras/Dropbox/Doutorado/tabelas/Energy_CenteredEnergy_Mean/EVApp_Export_20140613_sinais/wavelets/RBior31_SP0.xls");
//        } else {
//            files[0] = new File("/home/edras/Dropbox/Doutorado/tabelas/Energy_CenteredEnergy_Mean/EVApp_Export_20140613_fundo/wavelets/Haar02Orthogonal_SP0.xls");
//            files[1] = new File("/home/edras/Dropbox/Doutorado/tabelas/Energy_CenteredEnergy_Mean/EVApp_Export_20140613_fundo/wavelets/Lege04_SP0.xls");
//            files[2] = new File("/home/edras/Dropbox/Doutorado/tabelas/Energy_CenteredEnergy_Mean/EVApp_Export_20140613_fundo/wavelets/RBior31_SP0.xls");
//        	
//        }        
//        loadValues(makeMatrixFromSpreadSheets(files));
//        return files;

        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {        	
        	loadValues(makeMatrixFromSpreadSheets(fc.getSelectedFiles()));
        	return fc.getSelectedFiles();
        }
        else return null;
		
	}
	
	public void loadSpreadSheets(File[] files) {
		loadValues(makeMatrixFromSpreadSheets(files));
		
	}

	private int loadSpreadSheet() {

		JOptionPane.showMessageDialog(null, 
				"This program applies the LDA algorithm and calculates the ROC curve on only two classes.\n" +
				"Firstly you should provide one or more spreadsheets for class X1. After that \n"+
				"one or more spreadsheets belonging to class X2.");
		
		JOptionPane.showMessageDialog(null, "Select spreadsheets containing data of class X1.");
		
		JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new SpreadSheetFilter());        
        fc.setMultiSelectionEnabled(true);
        
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {        	
        	loadValues(makeMatrixFromSpreadSheets(fc.getSelectedFiles()));
        }
        else return -1;
        
		JOptionPane.showMessageDialog(null, "Select spreadsheets containing data of class X2.");
		
        returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {        	
        	loadValues(makeMatrixFromSpreadSheets(fc.getSelectedFiles()));
        } 		
        else return -1;
        
        Matrix matrix = classes.get(0);
        int x1_rows = matrix.getRowDimension();
        int x1_cols = matrix.getColumnDimension();

        matrix = classes.get(1);
        int x2_rows = matrix.getRowDimension();
        int x2_cols = matrix.getColumnDimension();

        JOptionPane.showMessageDialog(null, 
        		"Class X1 -> " + x1_rows + " samples, each one containing " + x1_cols + " features.\n" +
        		"Class X2 -> " + x2_rows + " samples, each one containing " + x2_cols + " features.\n" +
        		(x1_cols!=x2_cols?"The number of features must be the same for both classes!":
        			"The classes are ready for processing!"));
        
        if(x1_cols!=x2_cols) return -1;
        
        return x1_cols;

	}

	private double[][] makeMatrixFromSpreadSheets(File[] selectedFiles) {

		double[][] x = null;
		
    	for (int i = 0; i < selectedFiles.length; i++) {
    		//System.out.println(selectedFiles[i]);
    		double[][] temp = readSpreadSheet(selectedFiles[i]);
    		x = concatMatrix(x, temp);
		}
    	return x;
	}
	
	@SuppressWarnings("unused")
	private Matrix concatMatrix(Matrix x, Matrix y){
		return new Matrix(concatMatrix(x.getArray(), y.getArray()));
	}

	private double[][] concatMatrix(double[][] x, double[][] temp) {
		
		double[][] array = null;
		
		if(x==null) return temp;
		else {
			int mColumn = x[0].length;
			array = new double[x.length][mColumn+temp[0].length];
			for (int j = 0; j < array.length; j++) {
				for (int k = 0; k < array[0].length; k++) {
					if(k<mColumn) 
						array[j][k] = x[j][k];
					else 
						array[j][k] = temp[j][k-mColumn];
				}
			}
		}
		return array;		
	}

	private double[][] readSpreadSheet(File file){
		
		FileInputStream fis;
		HSSFWorkbook wb = null;
		
		try {
			fis = new FileInputStream(file);
			wb = new HSSFWorkbook(fis);
		
			int sheets = wb.getNumberOfSheets();
			HSSFSheet sheet = wb.getSheetAt(0);
			int rows = sheet.getLastRowNum();
			HSSFRow row = sheet.getRow(1);
			int columns = row.getLastCellNum()-1;
			
			double[][] matrix = new double[rows][sheets*columns];
			
			for (int s = 0; s < sheets; s++) {
				
				sheet = wb.getSheetAt(s);
				if(!this.sheets.contains(sheet.getSheetName()))
					this.sheets.add(sheet.getSheetName());
					
				for (int i = 0; i < rows; i++) {
					row = sheet.getRow(i+1);
					for (int j = 0; j < columns; j++) {
						matrix[i][s*columns+j] = row.getCell(j+1).getNumericCellValue();
					}
				}			
			}
			fis.close();
			return matrix;
		
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} 
		
		return null;
	}
	
	public Double[] calcSensEspec(){

		int i = 0;
		double[] x1 = classes.get(0).times(W).getColumnPackedCopy();
		double[] x2 = classes.get(1).times(W).getColumnPackedCopy();
		
		int labels[] = new int[x1.length+x2.length];
		for (i = 0; i < x1.length; i++) labels[i] = 1;
		
		rank = new double[labels.length];
		System.arraycopy(x1, 0, rank, 0, x1.length);
		System.arraycopy(x2, 0, rank, x1.length, x2.length);
		
		// Create the analysis.  The ranking is computed automatically.
		Curve analysis = new Curve.PrimitivesBuilder()
		    .predicteds(rank)
		    .actuals(labels)
		    .build();
		
		// Calculate AUC ROC
		double area = analysis.rocArea();
		
		if(area<0.5){
			labels = new int[x1.length+x2.length];
			for (i = x1.length; i < labels.length; i++) labels[i] = 1;
			
			// Create the analysis.  The ranking is computed automatically.
			analysis = new Curve.PrimitivesBuilder()
			    .predicteds(rank)
			    .actuals(labels)
			    .build();
			
			// Calculate AUC ROC
			area = analysis.rocArea();			
		}
		
		
		rank = sortArray(rank);

		double sens = 0;
		double espc = 1;
		threshold = 0;
			
		for (i = 0; i < rank.length; i++) {

			double[] rocPoint = analysis.rocPoint(i);
				
			if((rocPoint[1]>=(1-rocPoint[0])) && (threshold == 0)){
				sens = rocPoint[1];
				espc = 1-rocPoint[0];
				threshold = rank[i];					
			}
		}		
		
		Double[] result = new Double[3];
		result[0] = area;
		result[1] = sens;
		result[2] = espc;
		return result;
	}

	public double calcROC() {
		
		int i = 0;
		double[] x1 = classes.get(0).times(W).getColumnPackedCopy();
		double[] x2 = classes.get(1).times(W).getColumnPackedCopy();
		
		int labels[] = new int[x1.length+x2.length];
		for (i = 0; i < x1.length; i++) labels[i] = 1;
		
		rank = new double[labels.length];
		System.arraycopy(x1, 0, rank, 0, x1.length);
		System.arraycopy(x2, 0, rank, x1.length, x2.length);
		
		// Create the analysis.  The ranking is computed automatically.
		Curve analysis = new Curve.PrimitivesBuilder()
		    .predicteds(rank)
		    .actuals(labels)
		    .build();
		
		// Calculate AUC ROC
		double area = analysis.rocArea();
		
		if(area<0.5){
			labels = new int[x1.length+x2.length];
			for (i = x1.length; i < labels.length; i++) labels[i] = 1;
			
			// Create the analysis.  The ranking is computed automatically.
			analysis = new Curve.PrimitivesBuilder()
			    .predicteds(rank)
			    .actuals(labels)
			    .build();
			
			// Calculate AUC ROC
			area = analysis.rocArea();			
		}
		
		
		if(showGraphs){			
		
			rank = sortArray(rank);

			XYSeriesCollection dataset = new XYSeriesCollection();
			
			CustomXYToolTipGenerator toolTip = new CustomXYToolTipGenerator();
			ArrayList<String> tips = new ArrayList<String>();
			
			DecimalFormat df = new DecimalFormat("0.0000");  
			double sens = 0;
			double espc = 1;
			threshold = 0;
			
			XYSeries series2 = new XYSeries("Curve", false);
			for (i = 0; i < rank.length; i++) {

				double[] rocPoint = analysis.rocPoint(i);
				series2.add(rocPoint[0], rocPoint[1]);
				
				tips.add("threshold: "+df.format(rank[i])+
						" Sensitivity (TPR): "+df.format(rocPoint[1])+
						" Specificity (TNR): "+df.format(1-rocPoint[0]));
				
				if((rocPoint[1]>=(1-rocPoint[0])) && (threshold == 0)){
					sens = rocPoint[1];
					espc = 1-rocPoint[0];
					threshold = rank[i];					
				}					
			}
			
			toolTip.addToolTipSeries(tips);
			
//			dataset.addSeries(series1);
			dataset.addSeries(series2);
			
			ScatterChart demo = new ScatterChart("", "");
			
			demo.setLabelx("(1-Specificity)");
			demo.setLabely("Sensitivity");
			demo.setDataset(dataset);
			
			XYPlot plot = (XYPlot) demo.getChart().getPlot();
			plot.getDomainAxis().setRange(0, 1);
			plot.setDomainGridlinePaint(Color.GRAY);			
			plot.getRangeAxis().setRange(0,1);
			plot.setRangeGridlinePaint(Color.GRAY);			
			plot.setBackgroundPaint(Color.WHITE);
			
			XYLineAndShapeRenderer sampleRenderer = new XYLineAndShapeRenderer();
			sampleRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
			sampleRenderer.setSeriesPaint(0, Color.BLACK);
			//sampleRenderer.setSeriesLinesVisible(0, false);
			sampleRenderer.setSeriesShapesVisible(0, false);
			sampleRenderer.setBaseToolTipGenerator(toolTip);
			plot.setRenderer(sampleRenderer);
			
			JFreeChart chart = demo.getChart();
			chart.removeLegend();
			
			demo.setTitle("AUC: " + area + " Best -> thr: " + df.format(threshold) +
					" Sensitivity: "+df.format(sens)+
					" Specificity: "+df.format(espc));
			
			demo.pack();
			demo.setSize(600, 605);
			demo.setVisible(true);	
			
		}		
		
		return area;
	}
	
	public double getBestThreshold(){
		return threshold;
	}
	
	private double[] sortArray(double[] x) {
		
		double var;
		
		for (int i = 0; i < x.length-1; i++) {
			for (int j = i+1; j < x.length; j++) {
				if(x[i]<x[j]){
					var = x[i];
					x[i] = x[j];
					x[j] = var;
				}
			}			
		}
		
		return x;
	}

	public String getWtoPrint(){
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PrintStream ps = new PrintStream(baos);

	    // IMPORTANT: Save the old System.out!
	    PrintStream old = System.out;
	    
	    // Tell Java to use your special stream
	    System.setOut(ps);
	    
	    NumberFormat numberFormat = NumberFormat.getInstance();
	    numberFormat.setMaximumFractionDigits(4);
	    numberFormat.setMinimumFractionDigits(4);
	    W.print(numberFormat, 7);
	    
	    // Put things back
	    System.out.flush();

	    System.setOut(old);
	    
	    return baos.toString();
		
	}

	public boolean isShowGraphs() {
		return showGraphs;
	}

	public void setShowGraphs(boolean showGraphs) {
		this.showGraphs = showGraphs;
	}

	public void calcLdaProj() throws Exception {
		
		calcMeans();
		calcCovariances();
		calcScatter();
		calcBetweenClassesScatter();
		calcEig();
		calcWeights();
		
		//printResult();		
		if(showGraphs) showInput();	
		if(showGraphs) showOutput();

	}

	public void showOutput() {
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		HistogramDataset histogram = new HistogramDataset();
		HistogramDataset histogram2 = new HistogramDataset();
		
		 int b = 1;
		 for(int j = 0; j<classes.size(); j++){
			 Matrix c = classes.get(j);
			 
			 if(c.times(W).getColumnDimension()>1){
				 
				 double[][] array = c.times(W).getArray();
				 XYSeries series = new XYSeries("Class"+b++);
				 for (int i = 0; i < array.length; i++) {
					 series.add(array[i][0], array[i][1]);
				 }
				 dataset.addSeries(series);
				 
			 }
			 else{
				 
				 double[] array = c.times(W).getColumnPackedCopy();
				 
				 XYSeries series = new XYSeries("Class"+b++);
				 for (int i = 0; i < array.length; i++) {
					 series.add(array[i], 0);
				 }
				 dataset.addSeries(series);
				 if(j == 0) histogram.addSeries(j, array, 30);
				 if(j == 1) histogram2.addSeries(j, array, 30);
			 }
  		 }
	 
	    JFreeChart chart = ChartFactory.createHistogram(
	              "Class Distribution Histogram", 
	              null, 
	              null, 
	              histogram2, 
	              PlotOrientation.VERTICAL, 
	              true, 
	              true, 
	              false
	          );

 	     ScatterChart demo = new ScatterChart("After LDA", "Class Distribution");
 		 demo.setLabelx("dimension 1");
 		 demo.setLabely("");
 	     demo.setDataset(dataset);
 	     XYPlot plot = (XYPlot)demo.getChart().getPlot();
 	     plot.getRangeAxis().setRange(-0.5, 1.5);
 	     plot.setBackgroundPaint(Color.WHITE);
 	     Range range = plot.getDomainAxis().getRange();
 	     max = range.getUpperBound();
 	     min = range.getLowerBound();
 	     
 	     demo.pack();
 	     demo.setVisible(true);  	     

	    ChartPanel cpanel = new ChartPanel(chart);
	    XYPlot plot1 = (XYPlot)chart.getPlot();
	    plot1.getRenderer().setSeriesPaint(0, Color.BLUE);
	    plot1.getDomainAxis().setRange(range);
	    
	    
	    JFrame jFrame = new JFrame();
	    jFrame.getContentPane().add(cpanel, BorderLayout.CENTER); 
	    jFrame.pack();
	    jFrame.setVisible(true);
	    
	    JFreeChart chart2 = ChartFactory.createHistogram(
	              "Class Distribution Histogram", 
	              null, 
	              null, 
	              histogram, 
	              PlotOrientation.VERTICAL, 
	              true, 
	              true, 
	              false
	          );
	    
	    chart2.setBackgroundPaint(Color.WHITE);
	    XYPlot plot2 = (XYPlot)chart2.getPlot();
	    plot2.getRenderer().setSeriesPaint(0, Color.RED);
	    plot2.getDomainAxis().setRange(range);
	    
	    	    
	    ChartPanel cpanel2 = new ChartPanel(chart2);
	    JFrame jFrame2 = new JFrame();
	    jFrame2.getContentPane().add(cpanel2, BorderLayout.CENTER); 
	    jFrame2.pack();
	    jFrame2.setVisible(true);
  		 
        

//  		 Marker marker = demo.addMarker(5.0); 	
//  		 marker.setAlpha((float) 0.8);

		
	}

	public void showInput() {
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
//		double[] x;
//		double[] y;
//		double[] z;
 		 
		 int b = 1;
		 
		 Plot3DPanel plot = new Plot3DPanel();
		 
 		 for(Matrix c : classes){
 			 
 			 double[][] array = c.getArray();
 			 
 			 if(array[0].length>3) {
 				 System.err.println("It is not possible to plot data different than 1D, 2D or 3D");
 				 return;
 			 }
 			 
 			if(array[0].length==3){
				 plot.addScatterPlot("Classes", array);
 			}
 			 
 			if(array[0].length==2){
 				 XYSeries series = new XYSeries("Class"+b++);
 				 for (int i = 0; i < array.length; i++) {
 					 series.add(array[i][0], array[i][1]);
 				 }
 				 dataset.addSeries(series);
 			 }
 			
 			if(array[0].length==1){
				 XYSeries series = new XYSeries("Class"+b++);
				 for (int i = 0; i < array.length; i++) {
					 series.add(array[i][0], 0);
				 }
				 dataset.addSeries(series);
			 }

   		 }
   		 
 		 if(plot.getPlots().size()!=0) {
 			 
 			  JFrame frame = new JFrame("Classes and samples scatter");
 			  frame.setContentPane(plot);
 			  frame.setSize(600, 600);
 			  frame.setVisible(true);
 			 
 		 } else {
 			 
 			 ScatterChart demo = new ScatterChart("Before LDA", "Class Distribution");
 			 demo.setLabelx("feature 1");
 			 demo.setLabely("feature 2");
 			 demo.setDataset(dataset);
 			 
 			 demo.pack();
 			 demo.setVisible(true);   	     
 		 }  

	
	}

	@SuppressWarnings("unused")
	private void printResult() {
		
		int dimension = classes.size()-1;
		int features = classes.get(0).getColumnDimension();

		System.out.println("Entered " + classes.size() + " classes with " + features + " dimensions");
		System.out.println("Selecting C-1 dimensions resulting in " + dimension + " dimension(s)");
		System.out.println("\nWeights");
		//W.print(5, 4);	
		
		System.out.println("Resulting data:");
		for (Matrix c : classes) {
			c.times(W).print(5, 4);
		}
	}

	private void calcWeights() {
		
		int dimension = classes.size()-1;
		int features = classes.get(0).getColumnDimension();
		
		double[][] diag = getDiagonal(D).getArray();
		double[][] sorted_diag = sort(diag);
		
		W = new Matrix(features, classes.size()-1);
		for (int i = 0; i < dimension; i++) 
			W.setMatrix(0, features-1, i, i, V.getMatrix(0, features-1, (int)sorted_diag[i][0], (int)sorted_diag[i][0]));		
	}

	private double[][] sort(double[][] diag) {
		
		double[][] sorted_diag = new double[diag[0].length][2];
		
		for (int i = 0; i < diag[0].length; i++) {
			sorted_diag[i][0] = i;
			sorted_diag[i][1] = diag[0][i];
		}
		double[] swap = new double[2];
		for (int i = 0; i < sorted_diag.length-1; i++) {
			if(sorted_diag[i][1]<sorted_diag[i+1][1]){
				swap[0] = sorted_diag[i][0];
				swap[1] = sorted_diag[i][1];
				sorted_diag[i][0] = sorted_diag[i+1][0];
				sorted_diag[i][1] = sorted_diag[i+1][1];
				sorted_diag[i+1][0] = swap[0];
				sorted_diag[i+1][1] = swap[1];
			}				
		}
		
		return sorted_diag;
	}

	private Matrix getDiagonal(Matrix d2) {
		
		Matrix matrix = new Matrix(1, d2.getColumnDimension());
		for (int i = 0; i < d2.getColumnDimension(); i++)
			matrix.set(0, i, d2.get(i, i));		
		return matrix;
	}

	private void calcEig() throws Exception {
		
		EigenvalueDecomposition dec = sw.inverse().times(sb).eig();
		D = dec.getD();
		V = dec.getV();
		//	D.print(5, 4);
		//	V.print(5, 4);
	}

	private void calcBetweenClassesScatter() {
		
		for(int i = 0; i<means.size(); i++){
			Matrix u = means.get(i);
			Matrix minusU = u.minus(mean);
			Matrix sb_ = minusU.transpose().times(minusU);
			//int nrow = classes.get(i).getRowDimension();
			//sb_.timesEquals(nrow);
			if(sb == null) sb = sb_.copy();
			else sb.plusEquals(sb_);			
		}		
		sb.timesEquals(means.size()); // tem algum erro na formula
	}

	private void calcScatter() {
		
		for(Matrix s : covariances){
			if(sw == null) sw = s;
			else sw.plusEquals(s);
		}		
	}

	private void calcMeans() {
		
		for(Matrix x : classes){
			Matrix m = columnMean(x);
			means.add(m);
			if(mean == null) mean = m.copy();
			else mean.plusEquals(m);
		}		
		mean.timesEquals(1/(double)means.size());
	}

	private Matrix columnMean(Matrix m) {
		
		Matrix matrix = new Matrix(1, m.getColumnDimension());
		double mean;
		for (int i = 0; i < m.getColumnDimension(); i++) {
			mean = 0;
			for (int j = 0; j < m.getRowDimension(); j++) {
				mean+=m.get(j, i);
			}
			matrix.set(0, i, mean/m.getRowDimension());
		}
		return matrix;
	}

	private void calcCovariances() {
		
		for (int c = 0; c < classes.size(); c++) {
			Matrix c_ = classes.get(c);
			Matrix minusU = columnMinusMean(c_, means.get(c));
			Matrix cov = minusU.transpose().times(minusU).times(1/(double)(c_.getRowDimension()-1));
			covariances.add(cov);
		}
	}

	private Matrix columnMinusMean(Matrix m, Matrix mean) {
		double[][] matrix = m.getArrayCopy();
		double[][] media = mean.getArray();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] -= media[0][j];	
			}			
		}
		return new Matrix(matrix);
	}
	
	private void loadValidation(double[][] matrix){
		Matrix x = new Matrix(matrix);
		
		restoreValidation();
		validation.add(x);	
		saveValidation();
	}

	private void loadValues(double[][] matrix) {
		Matrix x = new Matrix(matrix);
		classes.add(x);
	}

	public void removeClass(int index) {
		classes.remove(index);
	}

	public void removeValidationClass(int index) {
		
		restoreValidation();
		restoreClasses();
		boolean number = true;
		
		while(number){
			String showInputDialog = JOptionPane.showInputDialog("Type in the number of classes to be used\n"+
									"or cancel to discard them");
			if(showInputDialog != null){
				try{
					int parseInt = Integer.parseInt(showInputDialog);
					if((parseInt > classes.size()) || (parseInt < 0)) JOptionPane.showMessageDialog(null, "Type the correct class number");
					else {
						Matrix matrix_val = validation.get(index);
						Matrix matrix_class = classes.get(parseInt-1);
						matrix_class = rowAppend(matrix_class, matrix_val);
						classes.set(parseInt-1, matrix_class);
						number = false;
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Invalid number");
				}
			}
			else
				number = false;
		}		
		validation.remove(index);
		saveValidation();
		saveClasses();
	}

	public void setEcho(boolean b) {
		echo = b;
	}

	public boolean isEcho() {
		return echo;
	}

	public void calcValidation(BigInteger experiment) {
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		Plot3DPanel plot = new Plot3DPanel();
		
		if(experiment.bitCount()!=0){
			experiment = experiment.add(BigInteger.ONE);
			
			validation.clear();
			for (Matrix matrix : originalValidation) {
				validation.add(reduceMatrix(experiment,matrix));
			}
		}

		int b = 0;
		String line = "Validation:";
		DecimalFormat df = new DecimalFormat("0.0000");

		for(Matrix c : validation){
				
			b++;
			line = line.concat("\nX"+b+": ");
			
			double[][] array = c.times(W).getArray();
		 			 
			if(array[0].length>3) {
				JOptionPane.showMessageDialog(null, "It is not possible to plot data different than 1D, 2D or 3D", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		 			 
			if(array[0].length==3){
				plot.addScatterPlot("Classes", array);
			}
		 			 
			if(array[0].length==2){
				XYSeries series = new XYSeries("Class"+b++);
				for (int j = 0; j < array.length; j++) {
					series.add(array[j][0], array[j][1]);
				}
				dataset.addSeries(series);
			}
		 			
			if(array[0].length==1){
				XYSeries series = new XYSeries("Class"+b++);
				for (int j = 0; j < array.length; j++) {
					series.add(array[j][0], b);
				}
				dataset.addSeries(series);
			}
			
			int counter = 0;
			for (int i = 0; i < array.length; i++) {
				if(array[i][0]>=threshold) counter++;
			}
			line = line.concat("Total:"+ array.length+" POS:"+counter+" NEG:"+(array.length-counter)+" %POS:"+df.format((((double)counter)/array.length)));

		}
		
		b = JOptionPane.showConfirmDialog(null, "Would you like to include training data?","Include data?",JOptionPane.YES_NO_OPTION);
		
		if(b == JOptionPane.YES_OPTION){

			b = classes.size();
			
			for(Matrix c : classes){
				
				b++;
				
				double[][] array = c.times(W).getArray();
				
				if(array[0].length==3){
					plot.addScatterPlot("Classes", array);
				}
			 			 
				if(array[0].length==2){
					XYSeries series = new XYSeries("Class"+b++);
					for (int j = 0; j < array.length; j++) {
						series.add(array[j][0], array[j][1]);
					}
					dataset.addSeries(series);
				}
			 			
				if(array[0].length==1){
					XYSeries series = new XYSeries("Class"+b++);
					for (int j = 0; j < array.length; j++) {
						series.add(array[j][0], b);
					}
					dataset.addSeries(series);
				}
			}
		}
		   		 
		if(plot.getPlots().size()!=0) {
			
			JFrame frame = new JFrame("Validation scatter panel");
			frame.setContentPane(plot);
			frame.setSize(600, 600);
			frame.setVisible(true);
			
		} else {
			
			ScatterChart demo = new ScatterChart("Validation Result", "Class Distribution");
			demo.setLabelx("feature 1");
			demo.setLabely("feature 2");
			demo.setDataset(dataset);
			
	    	Marker marker = new ValueMarker(getBestThreshold());   
	    	marker.setPaint(Color.BLACK);
	    	marker.setAlpha((float) 1.0);
	    	XYPlot plot2 = (XYPlot)demo.getChart().getPlot();
	    	plot2.addDomainMarker(marker);

			demo.pack();
			demo.setVisible(true);
 		}
		
		JOptionPane.showMessageDialog(null, line);
	}

	public void loadValidationElementsFromClass(int parseInt) {
		
		int counter = 1;
		if(classes.isEmpty()){
			JOptionPane.showMessageDialog(null, "Impossible to select elements. No classes are loaded", "Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		final SampleSelection sampleSelection = new SampleSelection();
		JList<String> list1 = sampleSelection.getjList1();
		DefaultListModel<String> model = new DefaultListModel<String>();
		list1.setModel(model);
		
		sampleSelection.getOkButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				copyFromClassToValidation(sampleSelection.getjList1().getSelectedIndices(),
						sampleSelection.getSampleClassNumber());
				sampleSelection.setVisible(false);
			}
		});
		
		sampleSelection.getjList1().addListSelectionListener(new ListSelectionListener() {			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				sampleSelection.getAmostrasSelecionadas().setText(String.valueOf(
						sampleSelection.getjList1().getSelectedIndices().length));
			}
		});
		
		sampleSelection.getCancelButton().setVisible(false);
		
		for(Matrix c : classes){
			
			if(counter != parseInt){
				counter++;
				continue;
			}
			else
				break;
		}
			
		JOptionPane.showMessageDialog(null, "Select elements for validation");

		Matrix c = classes.get(counter-1);
		sampleSelection.setSampleClassNumber(counter);		
		
		for (int i = 0; i < c.getRowDimension(); i++) {
			model.addElement(String.valueOf(i));				
		}
		
		sampleSelection.getAmostrasTotal().setText(String.valueOf(model.getSize()));
		sampleSelection.pack();
		sampleSelection.setModal(true);
		sampleSelection.setVisible(true);		
				
	}

	protected void copyFromClassToValidation(int[] selectedIndices, Integer classNumber) {
		
		Matrix matrix = classes.get(classNumber-1);
		Matrix val = new Matrix(selectedIndices.length, matrix.getColumnDimension());
		
		val.setMatrix(0, val.getRowDimension()-1, 0, matrix.getColumnDimension()-1, 
				matrix.getMatrix(selectedIndices, 0, matrix.getColumnDimension()-1));
		for (int i = selectedIndices.length-1; i > -1; i--) {
			matrix = deleteRow(matrix, selectedIndices[i]);
		}
		classes.set(classNumber-1, matrix);	
		validation.add(val);
		saveClasses();
		saveValidation();
	}
	
	public int getNumberOfValidationClasses(){
		return validation.size();
	}
	
	/** Deletes a row from a matrix.  Does not change the passed matrix.
	 * @param m the matrix.
	 * @param row the row to delete.
	 * @return m with the specified row deleted.
	 */
	public static Matrix deleteRow(Matrix m, int row) {
		int numRows = m.getRowDimension();
		int numCols = m.getColumnDimension();
		Matrix m2 = new Matrix(numRows-1,numCols);
		for (int mi=0,m2i=0; mi < numRows; mi++) {
			if (mi == row)
				continue;  // skips incrementing m2i
			for (int j=0; j<numCols; j++) {
				m2.set(m2i,j,m.get(mi,j));
			}
			m2i++;
		}
		return m2;
	}
	
	/** Appends additional rows to the first matrix.
	 * @param m the first matrix.
	 * @param n the matrix to append containing additional rows.
	 * @return a matrix with all the rows of m then all the rows of n.
	 */
	public static Matrix rowAppend(Matrix m, Matrix n) {
		int mNumRows = m.getRowDimension();
		int mNumCols = m.getColumnDimension();
		int nNumRows = n.getRowDimension();
		int nNumCols = n.getColumnDimension();
		
		if (mNumCols != nNumCols)
			throw new IllegalArgumentException("Number of columns must be identical to row-append.");
		
		Matrix x = new Matrix(mNumRows+nNumRows,mNumCols);
		x.setMatrix(0,mNumRows-1,0,mNumCols-1,m);
		x.setMatrix(mNumRows,mNumRows+nNumRows-1,0,mNumCols-1,n);
		
		return x;
	}

	public boolean isDir() {
		return diretorio ;
	}

	public void setFolder(File selectedFile) {
		dir_base = selectedFile;		
		
		File[] listFiles = selectedFile.listFiles();
		for (File subdir : listFiles) {
			if(!subdir.isDirectory()) continue;
			String[] split = subdir.getName().split("_");

			if(!className.contains(split[0]))
					className.add(split[0]);
			
			if(!dirs.contains(split[1]))
				dirs.add(split[1]);			
		}
		
		Collections.sort(dirs);
		Collections.sort(className);
	}

	public void setDir(boolean b) {
		diretorio = b;		
	}

	public void printROCResult() {
		String text;
		text = dirs.get(currentDir-1);
		Double[] rocSensEspc = calcSensEspec();
		text += "; "+ rocSensEspc[0];
		text += "; "+ rocSensEspc[1];
		text += "; "+rocSensEspc[2]+";";
		System.out.println(text);
		
	}

	public boolean loadNextGroup() {
		
		if(currentDir == dirs.size())
			return false;	

		classes.clear();
		String slash = System.getProperty("file.separator");
		
		for(String names : className){
			
			File file = new File(dir_base+slash+names+"_"+dirs.get(currentDir)+slash+"wavelets");
			File[] listFiles = file.listFiles();
			Arrays.sort(listFiles);
			loadValues(makeMatrixFromSpreadSheets(listFiles));
		}
		
		saveClasses();
		
		currentDir++;
		return true;	
	}

	public void resetDir() {
		currentDir = 0;		
	}

	public int getNrClasses() {
		return classes.size();
	}

	public File[] getClassPath() {
		
		String slash = System.getProperty("file.separator");
		File file = new File(dir_base+slash+className.get(0)+"_"+dirs.get(0)+slash+"wavelets");
		File[] listFiles = file.listFiles();
		Arrays.sort(listFiles);
		return listFiles;
	}
}
