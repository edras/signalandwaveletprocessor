package br.com.evsqp.task;

import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

abstract class EVTask extends Task<Object, Void>{

	boolean running, pause;
	String name;
	
	public EVTask(Application application) {
		super(application);
	}
	
	protected void loop_pause(){
		while(pause)
			try {
				Thread.sleep(1000);
				setMessage(name + " paused");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		setMessage(name + " processing");
	}

}
