package br.com.evsqp.reader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import br.com.evsqp.utils.EdfFilter;
import br.com.evsqp.viewer.EdfViewer;

public abstract class GenericEegReader extends AbstractReader{

	private Integer type = EDF_REFERENCE;
	public static Integer EDF_PARASSAGITAL_BIPOLAR = 0; 
	public static Integer EDF_REFERENCE = 1; 
	private Map<Integer, String> typeMap;
	private Map<String, Integer> refMap;
	private Map<Integer, Map<Integer,String>> channelMap;
	
	private EdfViewer viewer;
	private long time;
	private int subSampling = -1;
	private float lpf = 70;
	protected boolean fast = false;
	
	public abstract void view();
	public abstract double[] getArray();
	public abstract long getInitialTime();
	public abstract String version();
	public abstract double getDataRecordDuration(int channel);
	public abstract int getNumberOfSamples(int channel); // per DataRecord
	public abstract int getSamplingRate();
	public abstract List<String> getFilters();
	public abstract long getNumberOfDataRecords();
	public abstract double[] getDataRecord(int channel, long dr_number);
	public abstract double[] getData(int channel, int samples, double offset);
	public abstract long getDataRecordTime(int channel, long dr_number);
	public abstract void close();
	public abstract double getSignalDuration();
	
	/* este metodo foi criado para acelerar a leitura de arquivos de EEG amostrados
	 * em frequencias acima de 200Hz (i.e. 1KHz) setando esta variavel antes de iniciar
	 * a leitura do arquivo fara com que o arquivo seja interpretado como sendo
	 * amostrado em 200Hz. Inclusive a resposta do metodo "getSamplingRate" serah de
	 * 200Hz.
	 */
	public void setFastReading(boolean fast){
		if(getSamplingRate() == 1000)
			this.fast  = fast;
	}
	
	public boolean isFastReading(){
		return fast;
	}
	
	public GenericEegReader() {
		setup();		
		refMap = new HashMap<String, Integer>();
	}
	
	public static AbstractReader factory(File file){
		
		if(file.getName().toString().toLowerCase().contains(".edf")){
			return new EdfReader(file);
		} else if (file.getName().toString().toLowerCase().contains(".eeg")){
			return new EegReader(file);
		} else {
			System.err.println("this is not an EEG/EDF file!");
			System.exit(0);
		}		
		return null;
	}
	
	private void configReadMode() {
		typeMap = new HashMap<Integer, String>();
		typeMap.put(EDF_PARASSAGITAL_BIPOLAR, "Parassagital Bipolar");
		typeMap.put(EDF_REFERENCE, "Reference");	
		type = EDF_REFERENCE;
	}

	private void setup() {

		configReadMode();
		configModeMap();

		viewer = new EdfViewer(this);		
		if(isDebug()) version();
	}
	
	private void configModeMap() {
		
		channelMap = new HashMap<Integer, Map<Integer, String>>();
		
		// EdfReader.EDF_PARASSAGITAL_BIPOLAR;
		HashMap<Integer, String> temp = new HashMap<Integer, String>(); 
		int i=0;
		
		
		temp.put(i++,"Fp1-F3");		
		temp.put(i++,"F3-C3");
		temp.put(i++,"C3-P3");
		temp.put(i++,"P3-O1");
		temp.put(i++,"Fp1-F7");
		temp.put(i++,"F7-T3");
		temp.put(i++,"T3-T5");
		temp.put(i++,"T5-O1");
		temp.put(i++,"Fp2-F4");
		temp.put(i++,"F4-C4");
		temp.put(i++,"C4-P4");
		temp.put(i++,"P4-O2");
		temp.put(i++,"Fp2-F8");
		temp.put(i++,"F8-T4");
		temp.put(i++,"T4-T6");
		temp.put(i++,"T6-O2");
		temp.put(i++,"Fz-Cz");
		temp.put(i++,"Cz-Pz");
		channelMap.put(EDF_PARASSAGITAL_BIPOLAR, temp);		
	}
	
	public static GenericEegReader fileChooser(){
		return fileChooser("");
	}
	
	public static GenericEegReader fileChooser(String pathname) {
		// TODO Auto-generated method stub
		
		//System.out.println(pathname);
    	JFileChooser fc = new JFileChooser(new File(pathname));
    	EdfFilter edfFilter = new EdfFilter();
        //fc.addChoosableFileFilter(edfFilter);
        fc.setFileFilter(edfFilter);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setMultiSelectionEnabled(false);
        
        //Show it.
        int returnVal = fc.showOpenDialog(null);

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	return (GenericEegReader) edfFilter.getReader(fc.getSelectedFile());
        } 
        
        return null;
	}
	
	protected int[] getChannels(int channel) {
		
		int[] channels = new int[2];
		
		if(type == EDF_REFERENCE) {
			channels[0] = channel;
			return channels;
		} 
		
		Map<Integer, String> map = channelMap.get(type);
		String string = map.get(channel);
		
		if(string.contains("-")){
			String[] split = string.split("-");
			channels[0] = refMap.get(split[0]);
			channels[1] = refMap.get(split[1]);
		} else {
			channels[0] = channel;
			channels[1] = -1;
		}
		
		return channels;		
	}
	
	public double[] getDataRecordChannel(int channel, long dr_number){

		if(channel < 0 && channel > getNumberOfChannels()){
			System.err.println("Valid channels: from 1 to " + getNumberOfChannels());
			return null;	
		}
		
		if(isDebug()) System.out.println("Reading " + 1 + " data record  from channel " + channel);
		if(isDebug()) System.out.println("Data record number " + dr_number);
		if(isDebug()) System.out.println("Reading in " + getTypeMap().get(getType()) + " mode");		
		
		int[] channels = getChannels(channel);
		double[] data1 = getDataRecord(channels[0], dr_number);
		if(getType() == EDF_REFERENCE || channels[1] == -1) return data1;
		
		double[] data2 = getDataRecord(channels[1], dr_number);
		
		for (int i = 0; i < data1.length; i++) {
			data1[i] = data1[i]-data2[i];
		}				
		return data1;		
	}
	
	public double[] getDataChannel(int channel, int samples, double offset) {

		if(channel < 0 && channel > getNumberOfChannels()){
			System.err.println("Valid channels: from 1 to " + getNumberOfChannels());
			return null;	
		}
		
		if(isDebug()) System.out.println("Reading " + samples + " samples from channel " + channel);
		if(isDebug()) System.out.println("Starting at " + offset + " seconds");
		if(isDebug()) System.out.println("Reading in " + getTypeMap().get(getType()) + " mode");		
		
		channel--;
		
		int[] channels = getChannels(channel);
		double[] data1 = getData(channels[0], samples, offset);	
		
		if(getType() == EDF_REFERENCE || channels[1] == -1) return data1;
		
		double[] data2 = getData(channels[1], samples, offset);
		double[] result = new double[data2.length];
		
		for (int i = 0; i < result.length; i++) {
			result[i] = data1[i]-data2[i];
		}		
		
		return result;

	}
	
	public int getNumberOfChannels(){
		return channelMap.get(type).size();
	}
	
	public String getChannelName(int channel){
		return channelMap.get(type).get(channel);
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public EdfViewer getViewer() {
		if(viewer == null){
			viewer = new EdfViewer(this);
		}			
		return viewer;
	}
	public void setViewer(EdfViewer viewer) {
		this.viewer = viewer;
	}
	public Map<Integer, String> getTypeMap() {
		return typeMap;
	}
	public Map<String, Integer> getRefMap() {
		return refMap;
	}
	public Map<Integer, Map<Integer, String>> getChannelMap() {
		return channelMap;
	}
	public String getPath(){
		return file.getPath();
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public void setSubSampling(int subSampling) {
		this.subSampling = subSampling;
	}
	public int getSubSampling() {
		return subSampling;
	}
	public void setLPF(float lpf) {
		this.lpf = lpf;		
	}
	public float getLPF(){
		return lpf;
	}

}
