package br.com.evsqp.misc;

import java.io.Serializable;

import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;

public class EventData implements Serializable, Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2591804906738260682L;
	
	String file;
	String channelName;
	int channelNumber;
	//TimeSeries series = new TimeSeries("event");
	
	String eventName;
	int eventNumber;
	private RegularTimePeriod startTimePeriod;
	private RegularTimePeriod endTimePeriod;
	private int startIndex;
	private int endIndex;	
	
	
	
	@Override
	public EventData clone() throws CloneNotSupportedException {
		return (EventData)super.clone();
	}

	public int getStartIndex() {
		return startIndex;
	}
	
	public void setStartTimePeriod(RegularTimePeriod startTimePeriod) {
		this.startTimePeriod = startTimePeriod;
	}

	public void setEndTimePeriod(RegularTimePeriod endTimePeriod) {
		this.endTimePeriod = endTimePeriod;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
//	public TimeSeries getSeries() {
//		return series;
//	}
//	public void setSeries(TimeSeries series) {
//		this.series = series;
//	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public int getEventNumber() {
		return eventNumber;
	}
	public void setEventNumber(int eventNumber) {
		this.eventNumber = eventNumber;
	}
	
//	// Remove o offset da serie e a escala antes de cria-la
	public void setSeries(TimeSeries series, int channel, double scale) {		
		//this.series.addAndOrUpdate(changeScale(series, -channel*scale));			
		startTimePeriod = series.getTimePeriod(0);
		endTimePeriod = series.getTimePeriod(series.getItemCount()-1);
	}
//	
//	// aplica o offset nos dados da serie
//	public TimeSeries getSeries(int channel, int scale) {
//		try {
//			TimeSeries copy;
//			copy = series.createCopy(startTimePeriod, endTimePeriod);
//			return changeScale(copy, channel*scale);
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
//	private TimeSeries changeScale(TimeSeries series, int value){
//		for(int j=0; j<series.getItemCount(); j++){
//			series.update(j, series.getValue(j).doubleValue()+value);
//		}		
//		return series;
//	}
	
	public RegularTimePeriod getStartTimePeriod() {
		return startTimePeriod;
	}
	public RegularTimePeriod getEndTimePeriod() {
		return endTimePeriod;
	}
	public int getChannelNumber() {
		return channelNumber;
	}
	public void setChannelNumber(int channelNumber) {
		this.channelNumber = channelNumber;
	}
}
