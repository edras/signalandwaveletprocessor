package br.com.evsqp.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JList;

import br.com.evsqp.main.EVApp;
import br.com.evsqp.main.EVModel;
import br.com.evsqp.utils.table.ButtonTableModel;
import br.com.evsqp.utils.table.WaveletButton;
import cs.jwave.handlers.FastWaveletTransform;
import cs.jwave.handlers.wavelets.Wavelet;

public class WaveletTask extends EVTask {

	EVApp controller;
	EVModel model;
	private List<String> selWavelets;
	private List<String> selSignals;
	private List<Integer> selBoundaries;
	private ButtonTableModel tModel;
	
	public WaveletTask(EVApp controller) {
		super(controller);
		this.controller = controller;
		model = controller.getModel();
		name = "Wavelet Calculator";
	}

	private void insertIntoTable(HashMap<String, double[]> result, String path,
			String waveName, int boundary) {

		WaveletButton waveletButton = new WaveletButton(waveName, boundary, path, result);
		waveletButton.setText(waveName+"_"+Wavelet.bName[boundary]);
		waveletButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showWaveletResult(e);				
			}
		});
		
		int w = selWavelets.indexOf(waveName);
		int column = selSignals.indexOf(path);
		int d = selBoundaries.size();
		int row = selBoundaries.indexOf(boundary) + w*d;
		tModel.setValueAt(row, column, waveletButton);	
	
	}

	private void startThread() {
		
		selWavelets = model.getSelWavelets();
		selSignals = new ArrayList<String>(model.getSignalFiles().keySet());
		selBoundaries = lsli(model.getSelBoundaries());
		
		Collections.sort(selWavelets);
		Collections.sort(selSignals);
		Collections.sort(selBoundaries);

		int d = selBoundaries.size();
		
		if(d==0){
			d = 1;
			Integer boundary = Wavelet.WCONV_ZPD;
			String defaultBoundary = Wavelet.bName[boundary]; 
			selBoundaries.add(boundary);
			@SuppressWarnings("unchecked")
			JList<String> itemList1 = controller.getView().getItemList1();
			itemList1.setSelectedValue(defaultBoundary, false);
			controller.getView().getAddSelectedButton1().doClick();			
		}
		
		prepareTable();
	}

	private List<Integer> lsli(List<String> boundaries) {
		
		List<Integer> list = new ArrayList<Integer>();
		List<String> listName = Arrays.asList(Wavelet.bName);
		for(String b : boundaries)
			list.add(listName.indexOf(b));

		return list;
	}

	private void prepareTable() {	
		tModel = model.resetWaveletTableModel(); 
		controller.resetTable(controller.getView().getWaveletResultTable(), tModel);
	}

	@Override
	protected Object doInBackground() throws Exception {
		
		startThread();
		
		int max = selSignals.size()*selWavelets.size()*selBoundaries.size();
		int value = 0;
		
		setMessage(name + " processing...");
		
		double[] array;
		FastWaveletTransform fwt;
		HashMap<String, double[]> result;
		int toLevel = (Integer) controller.getView().getLevelsCombo().getSelectedItem();
		
		for(String path : selSignals){
			array = model.getReader(path).getArray();
			for(String waveName : selWavelets){
				fwt = new FastWaveletTransform(model.getWavelet(waveName));
				for(int b : selBoundaries){
					fwt.setBoundary(b);
					result = fwt.forward_ext(array, toLevel);
					insertIntoTable(result, path, waveName, b);
					setProgress(value++,0,max);
					controller.getView().getWaveletResultTable().repaint();
					if(pause) loop_pause();
				}				
			}
		}	
		return null;
	}

	@Override
	protected void cancelled() {
		setMessage(name + " canceled!");
		super.cancelled();
	}

	@Override
	protected void succeeded(Object result) {
		setMessage(name + " finished!");
		super.succeeded(result);
	}

}
