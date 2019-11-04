package br.com.evsqp.reader;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.evsqp.viewer.EdfViewer;
import edflib.EdflibLibrary;
import edflib.edf_annotation_struct;
import edflib.edf_hdr_struct;

public class EdfReader extends GenericEegReader{

	/**
	 * @param args
	 */
	private static EdflibLibrary edfLib;
	private edf_hdr_struct hdr;
	private List<edf_annotation_struct> annotations;
	
	public EdfReader(String path) {
		super();
		readFile(new File(path));		
	}

	public EdfReader(File file) {
		super();
		readFile(file);		
	}
	
	private int readFile(File file) {

		this.file = file;
		
		hdr = new edf_hdr_struct();
		annotations = new ArrayList<edf_annotation_struct>();
		edfLib = EdflibLibrary.INSTANCE;
		
		edfLib.edfopen_file_readonly(file.getPath(), hdr, EdflibLibrary.EDFLIB_READ_ALL_ANNOTATIONS);
		if(isDebug()) System.out.println("Criado Handle " + hdr.handle);
		
		Map<Integer, String> temp = new HashMap<Integer, String>();
		for (int i = 0; i < hdr.edfsignals; i++) {
			 String name = new String(hdr.signalparam[i].label).trim(); 
			 temp.put(i, name);
			 getRefMap().put(name.contains("-")?name.substring(0, name.indexOf("-")):name, i);
		}
		getChannelMap().put(EDF_REFERENCE,temp);
		
		edf_annotation_struct annot;
		for(int i=0; i<hdr.annotations_in_file; i++){
			annot = new edf_annotation_struct();
			if(edfLib.edf_get_annotation(hdr.handle, i, annot)!=0) {
				System.err.println("\nerror: edf_get_annotations()\n");
				edfLib.edfclose_file(hdr.handle);
				System.exit(0);
			} else {
				annotations.add(annot);
			}
		}
		
		setTime(getInitialTime());
		return hdr.handle;
	}

	public void printHeader() {

		System.out.println("Header:");

		System.out.println("filetype: " + hdr.filetype);
		System.out.println("channels: " + hdr.edfsignals);
		System.out.println("file duration: " + hdr.file_duration / EdflibLibrary.EDFLIB_TIME_DIMENSION + " seconds");
		System.out.println("startdate: " + hdr.startdate_day + "-" + hdr.startdate_month + "-" + hdr.startdate_year);
		System.out.println("starttime: " + hdr.starttime_hour + ":" + hdr.starttime_minute + ":" + hdr.starttime_second);
		System.out.println("patient: " + new String(hdr.patient));
		System.out.println("recording: " + new String(hdr.recording));
		System.out.println("patientcode: " + new String(hdr.patientcode));
		System.out.println("gender: " + new String(hdr.gender));
		System.out.println("birthdate: " + new String(hdr.birthdate));
		System.out.println("patient_name: " + new String(hdr.patient_name));
		System.out.println("patient_additional: " + new String(hdr.patient_additional));
		System.out.println("admincode: " + new String(hdr.admincode));
		System.out.println("technician: " + new String(hdr.technician));
		System.out.println("equipment: " + new String(hdr.equipment));
		System.out.println("recording_additional: " + new String(hdr.recording_additional));
		System.out.println("datarecord duration: " + ((double) hdr.datarecord_duration) / EdflibLibrary.EDFLIB_TIME_DIMENSION + " seconds");
		System.out.println("number of datarecords in the file: " + hdr.datarecords_in_file);
		System.out.println("number of annotations in the file: " + hdr.annotations_in_file);

	}

	public void printSignalInfo(int channel){
		
		System.out.println("\n\n***********************************");
	    System.out.println("Channel " + channel + " parameters:");
	    System.out.println("***********************************\n");
	    
		System.out.println("label: " +  new String(hdr.signalparam[channel].label));
		System.out.println("samples in file: " + hdr.signalparam[channel].smp_in_file);
		System.out.println("samples in datarecord: " + hdr.signalparam[channel].smp_in_datarecord);
		System.out.println("physical maximum: " + hdr.signalparam[channel].phys_max);
		System.out.println("physical minimum: " + hdr.signalparam[channel].phys_min);
		System.out.println("digital maximum: " + hdr.signalparam[channel].dig_max);
		System.out.println("digital minimum: " + hdr.signalparam[channel].dig_min);
		System.out.println("physical dimension: " +  new String(hdr.signalparam[channel].physdimension));
		System.out.println("prefilter: " +  new String(hdr.signalparam[channel].prefilter));
		System.out.println("transducer: " +  new String(hdr.signalparam[channel].transducer));
		System.out.println("samplefrequency: " + ((double)hdr.signalparam[channel].smp_in_datarecord / (double)hdr.datarecord_duration) * EdflibLibrary.EDFLIB_TIME_DIMENSION);
		
	}
	
//	public double[] getDataChannel(int channel) {			
//		return getDataChannel(channel, hdr.signalparam[channel].smp_in_file);		
//	}
//
//	public double[] getDataChannel(int channel, long samples) {
//		return getDataChannel(channel, samples, 0);
//	}

	public void askChannel() {
		
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		int channel;
		int size = getNumberOfChannels()-1;

		try {
			while (!line.equals("exit")) {
				System.out.print("\nDigite o numero do canal de 0 a " + size + " ou 'exit': ");
				line = input.readLine();
				if(!line.toLowerCase().contains("exit")){
					channel = Integer.parseInt(line);
					if(channel <= size && channel > 0)
						printSignalInfo(channel);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void close() {
		if(isDebug()) System.out.println("Desalocando Handle " + hdr.handle);
		edfLib.edfclose_file(hdr.handle);
	}

	public void printAnnotation() {
		
		System.out.println("\nAnnotations in file:");
		for(edf_annotation_struct annot : annotations){
			System.out.println( "annotation: onset: "+ (annot.onset / EdflibLibrary.EDFLIB_TIME_DIMENSION) +
								" duration: " + bats(annot.duration) +
								" description: " + bats(annot.annotation));
		}		
	}
	
	// bats stands for Byte Array To String
	private String bats(byte[] bytes){
		
		if(bytes[0] == 0) return "".toString();
		
		String string = new String(bytes);
		return string;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		String path;
		path = "/home/edras/Área de Trabalho/CDMiguel/NKT/EEG2100/FA0110R1_1-1+.edf";
		//path = "/home/edras/Doutorado/sinais/Elany/REGINALDO.RIBEIRO.13-05-2011 11.43.07 .edf";
		//path = "/home/edras/Doutorado/sinais/Elany/SERGIO ALOISIO.DO REGO BALDAIA.08-06-2011 10.13.07 .edf";
		
		EdfReader edfReader;
		
		if(path==null) 
			edfReader = (EdfReader)GenericEegReader.fileChooser();
		else 
			edfReader = new EdfReader(new File(path));
		
		edfReader.setDebug(false);
		
		//edfReader.printHeader();
		//edfReader.printAnnotation();
		//edfReader.printSignalInfo(1);
		//edfReader.askChannel();
		
		edfReader.setType(EdfReader.EDF_PARASSAGITAL_BIPOLAR);
		EdfViewer edfDisplay = new EdfViewer(edfReader);
		edfDisplay.setTitle(edfReader.getFile().getName());
		edfDisplay.setOffset(0);
		edfDisplay.setPageSize(10);
		edfDisplay.doChart();
		
//		edfReader.setType(EdfReader.EDF_PARASSAGITAL_BIPOLAR);
//		EdfViewer edfDisplay2 = new EdfViewer(edfReader);
//		edfDisplay2.setTitel(path);
//		edfDisplay2.setOffset(0);
//		edfDisplay2.setAmountOfDR(10);
//		edfDisplay2.doChart();

		//edfReader.closeFile();		
	}

	public int getNumberOfSamples(int channel) {
		return hdr.signalparam[channel].smp_in_datarecord;		
	}

	public double getSignalDuration() {
		return hdr.file_duration / EdflibLibrary.EDFLIB_TIME_DIMENSION;
	}

	public long getNumberOfDataRecords() {
		return hdr.datarecords_in_file;
	}

	public double getDataRecordDuration(int channel) {
		return ((double) hdr.datarecord_duration) / EdflibLibrary.EDFLIB_TIME_DIMENSION;
	}

	@Override
	public void view() {		
		setType(EdfReader.EDF_REFERENCE);
		getViewer().view();	
		close();
	}

	@Override
	public double[] getArray() {
		return getDataRecord(0, 0);
	}

	@Override
	public long getInitialTime() {
		GregorianCalendar calendar = new GregorianCalendar(
				hdr.startdate_year, 
				hdr.startdate_month, 
				hdr.startdate_day, 
				hdr.starttime_hour, 
				hdr.starttime_minute, 
				hdr.starttime_second);
		
		Date date = new Date();
		date.setTime(calendar.getTimeInMillis()+hdr.starttime_subsecond*100);
		setTime(date.getTime());
		return getTime();
	}

	@Override
	public String version() {
		return String.valueOf(edfLib.edflib_version());
	}

	@Override
	public double[] getData(int channel, int samples, double offset) {
		edfLib.edfseek(
				hdr.handle, 
				channel, 
				(long)((offset / ((double)hdr.file_duration / (double)EdflibLibrary.EDFLIB_TIME_DIMENSION)) * ((double)hdr.signalparam[channel].smp_in_file)), 
				EdflibLibrary.EDFSEEK_SET);

		double[] buffer = new double[(int)samples]; 
		DoubleBuffer buf = DoubleBuffer.wrap(buffer);
		int n = edfLib.edfread_physical_samples(hdr.handle, channel, (int)samples, buf);
		
		if(n == -1){
			System.err.println("Error reading file: edf_read_physical_samples.");
			edfLib.edfclose_file(hdr.handle);
			buf.clear();
			System.exit(0);
		}		
		
		double[] array = new double[n];
		buf.get(array);
				
		return array;
	}

	@Override
	public double[] getDataRecord(int channel, long dr_number) {
		
		double dataRecordDuration = getDataRecordDuration(channel);
		int samples = getNumberOfSamples(channel);
		
		return getData(channel, samples, dataRecordDuration*dr_number);
	}

	@Override
	public long getDataRecordTime(int channel, long dr_number) {
		
		double dataRecordDuration = getDataRecordDuration(channel);
		
		return (long) (getTime()+dr_number*dataRecordDuration*1000);
	}

	@Override
	public int getSamplingRate() {
		return (int) (((double)hdr.signalparam[0].smp_in_datarecord / (double)hdr.datarecord_duration) * EdflibLibrary.EDFLIB_TIME_DIMENSION);
	}

	@Override
	public List<String> getFilters() {
		
		String[] string = new String(hdr.signalparam[0].prefilter).split(" ");
		ArrayList<String> list = new ArrayList<String>();
		
		for (int i = 0; i < string.length; i++) {
			list.add(string[i]);
		}
		
		return list; 
	}
}
