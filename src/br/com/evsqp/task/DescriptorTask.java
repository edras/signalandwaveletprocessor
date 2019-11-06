package br.com.evsqp.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;

import br.com.evsqp.main.EVApp;
import br.com.evsqp.main.EVModel;
import br.com.evsqp.math.vector.MathV;
import br.com.evsqp.utils.table.ButtonTableModel;
import br.com.evsqp.utils.table.DescriptorButton;
import br.com.evsqp.utils.table.WaveletButton;

public class DescriptorTask extends EVTask {

	EVApp controller;
	EVModel model;
	private List<String> selDescriptors;
	private List<String> selSignals;
	private ButtonTableModel tModel;
	private ButtonTableModel wModel;
	private Method[] methods;
	private int levels;
	
	public DescriptorTask(EVApp controller) {
		super(controller);
		this.controller = controller;
		model = controller.getModel();
		name = "Descriptor Calculator";
	}

	private void calcMethod(DefaultCategoryDataset[] dataset, WaveletButton wb, Method method, int row) {
				
		HashMap<String, double[]> hash = wb.getResult();
		for(int l=0; l<levels; l++){
			if(dataset[l]==null) dataset[l] = new DefaultCategoryDataset();
			double[] ds = hash.get("D"+(l+1));
			try {
				dataset[l].addValue((Double)method.invoke(null, ds), "D"+(l+1), wb.getText());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}		
	}

	private void insertIntoTable(DefaultCategoryDataset[] dataset, String path,
			String descriptor) {

		DescriptorButton descriptorButton = new DescriptorButton(descriptor, path, dataset);
		descriptorButton.setText(descriptor);
		descriptorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showDescriptorResult(e);				
			}
		});
		
		int row = selDescriptors.indexOf(descriptor);
		int column = selSignals.indexOf(path);
		tModel.setValueAt(row, column, descriptorButton);		
	}

	private void startThread() {
		
		selDescriptors = model.getSelDescriptors();
		selSignals = new ArrayList<String>(model.getSignalFiles().keySet());
		wModel = model.getWaveletTableModel();
		levels = controller.getView().getLevelsCombo().getSelectedIndex()+1;
		
		Collections.sort(selDescriptors);
		Collections.sort(selSignals);

		getDescriptorMethods();
		prepareTable();
	}

	private void getDescriptorMethods() {
		
		methods = new Method[selDescriptors.size()];
		for(int i=0; i<selDescriptors.size(); i++){
			try {
				methods[i] = MathV.class.getDeclaredMethod(selDescriptors.get(i), double[].class);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}		
	}

	private void prepareTable() {
		tModel = model.resetDescriptorTableModel(); 
		controller.resetTable(controller.getView().getDescriptorResultTable(), tModel);
	}

	@Override
	protected Object doInBackground() throws Exception {
		
		startThread();
		
		if(wModel == null){
			return null;
		}
		
		int rows = wModel.getRowCount();
		int cols = wModel.getColumnCount();
		int desc = selDescriptors.size();
		
		int max = rows*cols*desc;
		int value = 0;
		
		setMessage(name + " processing...");
		
		long time = System.currentTimeMillis();
		for(int j=0; j<cols; j++){
			for(int d=0; d<desc; d++){
				final DefaultCategoryDataset[] dataset = new DefaultCategoryDataset[controller.getView().getLevelsCombo().getSelectedIndex()+1];			
				for(int i=0; i<rows; i++){					
					WaveletButton wb = (WaveletButton) wModel.getValueAt(i, j+1);
					calcMethod(dataset, wb, methods[d], i);		
					setProgress(value++, 0, max);
					if(pause) loop_pause();
				}
				insertIntoTable(dataset, selSignals.get(j), selDescriptors.get(d));
				controller.getView().getDescriptorResultTable().repaint();
			}
		}
		System.out.println(Long.toString(System.currentTimeMillis()-time));
		return null;
	}

	@Override
	protected void cancelled() {
		setMessage(name + "canceled!");
		super.cancelled();
	}

	@Override
	protected void succeeded(Object result) {
		setMessage(name + "finished!");
		super.succeeded(result);
	}
	
	
}
