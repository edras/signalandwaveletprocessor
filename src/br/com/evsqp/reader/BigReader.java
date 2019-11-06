package br.com.evsqp.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.List;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;

import br.com.evsqp.viewer.AbstractViewer;
import br.com.evsqp.viewer.BigViewer;


public class BigReader extends AbstractReader{

	BigViewer viewer;
	long step;
	int version = 1;
	String info = "";
	
	public BigReader(){		
		setup();
	};
	
	public BigReader(File file){
		setup();
		this.file = file;
	}
	
	private void setup(){
		viewer = new BigViewer(this);
	}
	
	public TimeSeries getSeries(){
		
		TimeSeries series = new TimeSeries(file.getName());
		
		//desserializo o objeto
		try{
			FileInputStream fi = new FileInputStream(file);
			ObjectInputStream oi = new ObjectInputStream(fi);
			//series =(TimeSeries) oi.readObject();
			
			step = oi.readLong();
			Date date = new Date(oi.readLong());
			int counter = oi.readInt();
			for(int i=0;i<counter;i++){
				series.add(getTimePeriod(date.getTime()+i*step), oi.readInt());
			}
			if(oi.available()!=0){
				version = oi.readChar()-48;
				if(version == 2){
					info = oi.readUTF();
					for (int i = 0; i < series.getItemCount(); i++) {
						series.update(i, series.getValue(i).doubleValue()/10.24);
					}
				}
			}
			oi.close();
		} catch(Exception e){e.printStackTrace();}  

		return series;
	}
	
	public long getStep(){
		return step;
	}
	
	public int getVersion(){
		return version;
	}
	
	public String getInfo(){
		return info;
	}

	@Override
	public void view() {
		viewer.view();		
	}

	@Override
	public double[] getArray() {
		List<?> objList = getSeries().getItems();
		double[] values = new double[objList.size()];
		if(objList.get(0) instanceof TimeSeriesDataItem){
			for (int i = 0; i < values.length; i++) {
				TimeSeriesDataItem dataItem = (TimeSeriesDataItem)objList.get(i);
				values[i] = dataItem.getValue().doubleValue();
			}			
		}
		return values;
	}

	@Override
	public AbstractViewer getViewer() {
		return viewer;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSamplingRate() {
		List<?> items = getSeries().getItems();
		TimeSeriesDataItem start = (TimeSeriesDataItem) items.get(0);
		TimeSeriesDataItem end = (TimeSeriesDataItem) items.get(items.size()-1);
		int sampling = (int) (1000*items.size()/(end.getPeriod().getFirstMillisecond()-start.getPeriod().getFirstMillisecond()));
		return sampling;
	}

	@Override
	public int getNumberOfChannels() {
		return 1;
	}
}
