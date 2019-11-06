package br.com.evsqp.task;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.TaskService;

import br.com.evsqp.main.EVApp;
import br.com.evsqp.main.EVModel;
import br.com.evsqp.main.EVView;

public class EVMonitor implements Runnable {
	
	private List<Class<?>> list;
	private EVApp controller;
	boolean running, paused;
	boolean lda;
	boolean exlda=false;
	long time;
	
	public EVMonitor(EVApp evApp){
		controller = evApp;
		list = new ArrayList<Class<?>>();
	}
	
	public void add(Class<?> task){
		list.add(task);
	}
	
	public void setLdaTask(boolean lda){
		this.lda = lda;
	}
	
	public void start(){
		
		if(!running){
			if(!canStart()) return;
			controller.getView().getStartButton().setEnabled(false);
			controller.getView().getPauseButton().setEnabled(true);
			controller.getView().getStopButton().setEnabled(true);
			new Thread(this).start();
			time = System.currentTimeMillis();
		}
		else {
			controller.getView().getStartButton().setEnabled(false);
			controller.getView().getPauseButton().setEnabled(true);
			paused = false;
		}
	}
	
	private boolean canStart() {
		
		ResourceMap resourceMap = controller.getContext().getResourceMap(EVApp.class);
		EVModel model = controller.getModel();
		
		if(lda) return true;
		
		int signals = model.getSignalFiles().size();
		int wavelets = model.getSelWavelets().size();
		int bound = model.getSelBoundaries().size();
		int descriptors = model.getSelDescriptors().size();			
		
		if(signals > 0 && wavelets > 0)
			return JOptionPane.showConfirmDialog(null,
				resourceMap.getString("monitorInit.text", signals,
						wavelets, model.getLevel(), bound, descriptors),
        		resourceMap.getString("monitorInit.title"),
        		JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
		else
			JOptionPane.showMessageDialog(null,
				resourceMap.getString("monitorCheck.text"),
	        	resourceMap.getString("monitorCheck.title"),
	        	JOptionPane.WARNING_MESSAGE);
		
		return false;			
	}

	public void stop(){
		time = System.currentTimeMillis()-time; 
		ResourceMap resourceMap = controller.getContext().getResourceMap(EVView.class);
		controller.getView().getStartButton().setText(resourceMap.getString("startButton.text"));
		controller.getView().getStartButton().setEnabled(true);
		controller.getView().getPauseButton().setEnabled(false);
		controller.getView().getStopButton().setEnabled(false);
		running = false;
	}
	
	public void pause(){
		ResourceMap resourceMap = controller.getContext().getResourceMap(EVView.class);
		controller.getView().getStartButton().setText(resourceMap.getString("startButton.paused"));
		controller.getView().getStartButton().setEnabled(true);
		controller.getView().getPauseButton().setEnabled(false);
		controller.getView().getStopButton().setEnabled(true);
		paused = true;
	}

	@Override
	public void run() {
		
		running = true;
		
		for(Class<?> taskClass : list){
			
			if(lda){
				if(taskClass != LDATask.class) continue;
			} else {
				if(taskClass == LDATask.class) continue;
			}
			
			try {
				Constructor<?> constructor = taskClass.getDeclaredConstructor(EVApp.class);
				constructor.setAccessible(true);
				EVTask task = (EVTask) constructor.newInstance(controller);
				TaskService taskService = controller.getContext().getTaskService();
				
				if(!task.running){
					taskService.execute(task);
					task.running = true;
				}
				
				while(!task.isDone()){
					
					try {
						if(paused) 
							task.pause = true;
						else
							task.pause = false;
						
						if(!running) {
							task.cancel(true);
							return;
						}
						Thread.sleep(500);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if(lda) exlda = true;
		stop();
		show_endDialog();			
	}

	private void show_endDialog() {	

		if(exlda){
			exlda = false;
			return;
		}
		
		ResourceMap resourceMap = controller.getContext().getResourceMap(EVApp.class);
        JOptionPane.showMessageDialog(null,
        		resourceMap.getString("monitor.text")+"\n in "+time+" ms",
        		resourceMap.getString("monitor.title"),
        		JOptionPane.INFORMATION_MESSAGE);
		
	}
}
