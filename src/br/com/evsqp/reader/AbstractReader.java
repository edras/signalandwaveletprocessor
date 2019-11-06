package br.com.evsqp.reader;

import java.io.File;
import java.util.Date;
import java.util.TimeZone;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;

import br.com.evsqp.utils.AbstractFilter;
import br.com.evsqp.utils.BigFilter;
import br.com.evsqp.utils.EdfFilter;
import br.com.evsqp.utils.MigFilter;
import br.com.evsqp.utils.WavFilter;
import br.com.evsqp.viewer.AbstractViewer;

public abstract class AbstractReader {

	private boolean debug = false;
	protected File file;
	
	abstract public void close();
	abstract public int  getSamplingRate();
	
	public static AbstractReader factory(File file){

		AbstractFilter[] filters = new AbstractFilter[]{
				new EdfFilter(), new MigFilter(), new WavFilter(), new BigFilter()
		};
		
		for (int i = 0; i < filters.length; i++) {
			if(filters[i].accept(file)) 
				return filters[i].getReader(file);
		}		
		return null;
	}
	
	public abstract void view();
	public abstract AbstractViewer getViewer();
	public abstract double[] getArray();
	public abstract int getNumberOfChannels();

	public File getFile(){
		return file;
	}
	
	public void setDebug(boolean debug){
		this.debug = debug;
	}
	
	public boolean isDebug(){
		return debug;
	}
	
	protected RegularTimePeriod getTimePeriod(long time) {
        return RegularTimePeriod.createInstance(
        		Millisecond.class, 
        		new Date(time), 
        		TimeZone.getDefault());
	}
}
