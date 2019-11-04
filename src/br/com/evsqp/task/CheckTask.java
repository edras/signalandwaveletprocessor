package br.com.evsqp.task;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.evsqp.main.EVApp;
import br.com.evsqp.main.EVModel;

public class CheckTask extends EVTask{

	private EVApp controller;
	private String name;
	private EVModel model;
	
	public CheckTask(EVApp controller) {
		super(controller);
		this.controller = controller;
		model = controller.getModel();
		name = "Check Task";
	}

	@Override
	protected Object doInBackground() throws Exception {
		
		setMessage(name + " processing...");
		
		List<String> selWavelets = model.getSelWavelets();
		// Order wavelets by size
		List<String> wavelets = new ArrayList<String>();
		NumberFormat formatter = new DecimalFormat("000");
		for(String tmp : selWavelets)
			wavelets.add(formatter.format(model.getWavelet(tmp).getWaveLength())+"-"+tmp);
		Collections.sort(wavelets);
		Collections.reverse(wavelets);
		
		// Check if wavelet is bigger than the signal
		for(File file : model.getSignalFiles().values()){
			for (String wave : wavelets){
				String[] split = wave.split("-");
				int size = Integer.parseInt(split[0]);
				long length = file.length();
				if(size>length){
					System.err.println(split[1]+" is bigger than signal "+ file.getName()+". Removing selected wavelet...");
					wavelets.remove(wave);
					controller.getView().getSelectedItemList().setSelectedValue(split[1], false);
					controller.getView().getRemoveSelectedButton().doClick();
				} else break;
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
