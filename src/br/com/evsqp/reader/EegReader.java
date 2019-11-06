package br.com.evsqp.reader;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.evsqp.viewer.EdfViewer;

public class EegReader extends GenericEegReader {

	Double[][] channelInfo;
	HashMap<Integer, String> channelConfig;
	Integer[] ctrlBlockAdress;
	List<String> reference;
	HashMap <Integer, Integer> dataBlock;
	String version;
	RandomAccessFile raf;
	int frequency;
	int drTime;
	private double total_time;
	private int[][] dataBlock_info;
	private long[] initTimeDB;
//	private long current_dr = -1;
	
	ByteBuffer bb;	
	
	public EegReader(File file) {
		super();
		reference = new ArrayList<String>();
		readFile(file);
		
		bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		EegReader reader = new EegReader(new File("/home/edras/Área de Trabalho/CDMiguel/NKT/EEG2100/FA0110R1.EEG"));
		reader.setType(EDF_PARASSAGITAL_BIPOLAR);
		
		final EdfViewer viewer = new EdfViewer(reader);
        viewer.setTitle(reader.getFile().getName());
        viewer.doChart();
	}

/*	private void get10s() {
		
		for(int i = 0; i<ctrlBlockAdress.length; i++){
			Integer[] data = dataBlock.get(ctrlBlockAdress[i]);
			for(int j = 0; j<1; j++){
				
				try {
					// get number of channels
					raf.seek(data[j]+0x0026);
					int channels = raf.readUnsignedByte();
					
					// get record duration in deci-seconds
					int duration = readInt(raf, data[j]+0x001c);
					
					readChannelName(raf, channels, data[j]);
					
					raf.seek(data[j]+0x001a);
					int frequency = raf.readUnsignedByte();
					frequency += raf.readUnsignedByte() << 8;
					frequency &= 0x3fff;
					
					//canais = new double[channels][800];
					
					int record_size = frequency/10*channels*2;
					int raster = frequency/10*2;
					int seconds = 0;
					int deci = 0;
					
					raf.seek(data[j]+0x0027+(channels*10));
					ByteBuffer bb = ByteBuffer.allocate(2);
					bb.order(ByteOrder.LITTLE_ENDIAN);
					
					for (int k = 0; k < 800; k++) {
						for (int c = 0; c < channels; c++) {
							bb.put(raf.readByte());
							bb.put((byte)(raf.readByte()+128));
							//canais[c][k]  = (double) bb.getShort(0);
							//canais[c][k] += channelInfo[c][6];
							//canais[c][k] *= channelInfo[c][5];
							bb.clear();
						}
						raf.readByte();
						raf.readByte();	
					}					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}*/

	private void readChannelName(RandomAccessFile raf, int channels, long offset) throws IOException {
		
		int temp;
		
		channelInfo = new Double[channels][7];
		
		Map<String, Integer> channelName = getRefMap();
		Map<Integer, String> ref = new HashMap<Integer, String>();
		getChannelMap().put(EDF_REFERENCE, ref);	
		
		for (int i = 0; i < channels; i++) {
			
			raf.seek(offset + 0x0027 + (i*10));
			temp = raf.readUnsignedByte();
			
			channelName.put(channelConfig.get(temp), i);
			ref.put(i, channelConfig.get(temp));
		    
		    /* Coletando informações sobre o canal
		     * 
		     * 0 -> Magnitude 0 = uV, 1 = mV
		     * 1 -> Phy. Min  
		     * 2 -> Phy. Max
		     * 3 -> Dig. Min
		     * 4 -> Dig. Max
		     * 5 -> Escala
		     * 6 -> Offset -> DC
		     */		    
		    if(temp<42 || temp>73){
		    	channelInfo[i][0] = 0.0;
		    	channelInfo[i][1] = -3200.0;
		    	channelInfo[i][2] = 3199.902;
		    } else {
		    	channelInfo[i][0] = 1.0;
		    	channelInfo[i][1] = -12002.9;
		    	channelInfo[i][2] = 12002.56;
		    }		    
	    	channelInfo[i][3] = -32768.0;
	    	channelInfo[i][4] = 32767.0;
	    	
	    	channelInfo[i][5] = (channelInfo[i][2]-channelInfo[i][1])/(channelInfo[i][4]-channelInfo[i][3]);
	    	channelInfo[i][6] = Double.valueOf((int)(channelInfo[i][2]/channelInfo[i][5]-channelInfo[i][4]));
		}
	}

	private void readFile(File file) {
		
		this.file = file;
		if(!file.exists()){			
			System.err.println("File: " + file.getPath() + " does not exist!");
			System.exit(0);
		}
		
		readEEGConfig(file);
		
		try {
	    	raf = new RandomAccessFile(file, "r");	      
	    	checkDevice(raf);
	    	getBlocks(raf);
	    	getInfo(raf);	        
	        System.out.flush();

	    } catch (EOFException eof) {
	      System.out.println(" >> Normal program termination.");
	    } catch (FileNotFoundException noFile) {
	      System.err.println("File not found! " + noFile);
	    } catch (IOException io) {
	      System.err.println("I/O error occurred: " + io);
	    } catch (Throwable anything) {
	      System.err.println("Abnormal exception caught !: " + anything);
	    } 	
	}

	private void readEEGConfig(File file) {
		
		File config = new File(file.getPath().replace(".EEG", ".21E"));
		
		channelConfig = new HashMap<Integer, String>();
		
		if(config.exists()){
			InputStream    fis;
			BufferedReader br;
			String         line;
			boolean electrode = false;

			try {
				fis = new FileInputStream(config);
				br = new BufferedReader(new InputStreamReader(fis));
				while ((line = br.readLine()) != null) {
					if (line.toLowerCase().contains("electrode")) {
						electrode = true;
						continue;
					}
					if (line.toLowerCase().contains("reference")) {
						electrode = false;
						continue;
					}
					if (line.toLowerCase().contains("sd_def"))
						break;

					String[] split = line.split("=");
					if (electrode) {
						channelConfig.put(Integer.valueOf(split[0]),split[1]);
					} else {
						String ref = split[1].contains("$")?split[1].substring(1): split[1];
						channelConfig.put(Integer.valueOf(split[0]), "REF_"+ref);
						reference.add(ref);
					}
				}
				fis.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			for(int i=0;i<256;i++){
				switch (i) {
					case  0 : channelConfig.put(i,"Fp1"); break;
					case  1 : channelConfig.put(i,"Fp2"); break;
					case  2 : channelConfig.put(i,"F3");  break;
					case  3 : channelConfig.put(i,"F4");  break;
					case  4 : channelConfig.put(i,"C3");  break;
					case  5 : channelConfig.put(i,"C4");  break;
					case  6 : channelConfig.put(i,"P3");  break;
					case  7 : channelConfig.put(i,"P4");  break;
					case  8 : channelConfig.put(i,"O1");  break;
					case  9 : channelConfig.put(i,"O2");  break;
					case 10 : channelConfig.put(i,"F7");  break;
					case 11 : channelConfig.put(i,"F8");  break;
					case 12 : channelConfig.put(i,"T3");  break;
					case 13 : channelConfig.put(i,"T4");  break;
					case 14 : channelConfig.put(i,"T5");  break;
					case 15 : channelConfig.put(i,"T6");  break;
					case 16 : channelConfig.put(i,"Fz");  break;
					case 17 : channelConfig.put(i,"Cz");  break;
					case 18 : channelConfig.put(i,"Pz");  break;
					case 19 : channelConfig.put(i,"E");   break;
					case 20 : channelConfig.put(i,"Pg1"); break;
					case 21 : channelConfig.put(i,"Pg2"); break;
					case 22 : channelConfig.put(i,"A1");  break;
					case 23 : channelConfig.put(i,"A2");  break;
					case 24 : channelConfig.put(i,"T1");  break;
					case 25 : channelConfig.put(i,"T2");  break;
					case 35 : channelConfig.put(i,"X1i"); break;
					case 36 : channelConfig.put(i,"X11"); break;
					case 74 : channelConfig.put(i,"Bn1"); 	break;
					case 75 : channelConfig.put(i,"Bn2"); break;
					case 76 : channelConfig.put(i,"Mark1"); break;
					case 77 : channelConfig.put(i,"Mark2"); break;
					case 100 : channelConfig.put(i,"X12/Bp1"); break;
					case 101 : channelConfig.put(i,"X13/Bp2"); break;
					case 102 : channelConfig.put(i,"X14/Bp3"); break;
					case 103 : channelConfig.put(i,"X15/Bp4"); break;
					case 254 : channelConfig.put(i,"-");  break;
					case 255 : channelConfig.put(i,"z");  break;
				}
			
			    if((25<i)&&(i<35))    channelConfig.put(i,"X"+(i - 25));
			    if((36<i)&&(i<42))    channelConfig.put(i,"-");
			    if((41<i)&&(i<74))    channelConfig.put(i,"DC"+(i - 41));
			    if((77<i)&&(i<100))   channelConfig.put(i,"-");
			    if((103<i)&&(i<188))  channelConfig.put(i,"X"+(i - 88));
			    if((187<i)&&(i<254))  channelConfig.put(i,"X"+(i - 88));
			}
		}
		
	}

	private void getInfo(RandomAccessFile raf) {
		
	
	}

	private void getBlocks(RandomAccessFile raf) throws IOException {
		
		// a variavel ctrlBlockAdress[] contem todos os enderecos
		// dos blocos de controle
		
		// o hash dataBlock contem todos os enderecos dos blocos 
		// de dados indexados pelo endereco do seu bloco de controle
		// respectivo.
		
		// Posiciona o ponteiro no cabecalho do arquivo onde
		// diz quantos blocos de controle o arquivo possui
		raf.seek(0x0091);
		int ctl_block_cnt = raf.readByte();	
		ctrlBlockAdress = new Integer[ctl_block_cnt];
		
		int total_data_blocks = 0;
		
		if(isDebug()) System.out.println("Foi/Foram encontrado(s) " + ctl_block_cnt + " bloco(s) de controle");
		
		// loop nos blocos de controle
        for(int i=0; i<ctl_block_cnt; i++){
        	
        	// Captura todos os enderecos dos blocos de controle
        	ctrlBlockAdress[i] = readInt(raf, 0x0092 + (i * 20));
	    	
	    	// Cada bloco de controle possui n blocos de dados
	    	raf.seek(ctrlBlockAdress[i]+17);
	    	int dataBlock_cnt = raf.readByte();
	    	
	    	dataBlock = new HashMap<Integer, Integer>();
	    	
	    	if(isDebug()) {
	    		System.out.println("O bloco de controle " +i+ " contem " +dataBlock_cnt+ " bloco(s) de dados");
	    	}

	    	// captura todos os endereços do blocos de dados
	    	for (int j = 0; j < dataBlock_cnt; j++) {
				dataBlock.put(total_data_blocks++, readInt(raf, ctrlBlockAdress[i] + (j*20) + 18));
			}	    	
	    }
        
        total_time = 0;
        dataBlock_info = new int[total_data_blocks][3]; // 0 -duration, 1 -freq, 2 -dataRecords
        int counter = 0;
        
        //get frequency
        raf.seek(dataBlock.get(0)+0x001a);
        frequency = raf.readUnsignedByte();
        frequency += raf.readUnsignedByte() << 8;
        frequency &= 0x3fff;
        		
		for(Integer data : dataBlock.values()){
				
			// get number of channels
			raf.seek(data+0x0026);
		
			// get record duration in deci-seconds
			int duration = readInt(raf, data+0x001c);
			total_time += duration/10;
			dataBlock_info[counter][0] = duration;
			
			dataBlock_info[counter][1] = frequency;					
			dataBlock_info[counter++][2] = duration;
		
		}
		
		raf.seek(dataBlock.get(0)+0x0026);
		int channels = raf.readUnsignedByte();
    	readChannelName(raf, channels, dataBlock.get(0));

		getInitialTime();
	}


	private int readInt(RandomAccessFile raf, long position) throws IOException {

		int tmp;
		
		raf.seek(position);
		
    	tmp  = (int) raf.readUnsignedByte();
    	tmp += (int) ((raf.readUnsignedByte() & 0x000000ff) << 8);
    	tmp += (int) ((raf.readUnsignedByte() & 0x000000ff) << 16);
    	tmp += (int) ((raf.readUnsignedByte() & 0x000000ff) << 24);	  

    	return tmp;
	}

	private void checkDevice(RandomAccessFile raf) throws IOException {
		
		raf.seek(0x0081); // go to 0x0081 to read device version
		byte version_b[] = new byte[16];
		raf.read(version_b);
		version = new String(version_b);
		
		int error = 0;
		if(version.equals("EEG-1100A V01.00")) error++;
		else if(version.equals("EEG-1100B V01.00")) error++;
		else if(version.equals("EEG-1100C V01.00")) error++;
		else if(version.equals("QI-403A   V01.00")) error++;
		else if(version.equals("QI-403A   V02.00")) error++;
		else if(version.equals("EEG-2100  V01.00")) error++;
		else if(version.equals("EEG-2100  V02.00")) error++;
		else if(version.equals("DAE-2100D V01.30")) error++;
		else if(version.equals("DAE-2100D V02.00")) error++;

		if(error ==0){
			System.err.println("Unsuportted Device!");
			System.exit(0);
		}	
		
		if(isDebug()) System.out.println("File version: " + version);
	}

	@Override
	public void view() {
		setType(EegReader.EDF_REFERENCE);
		getViewer().view();			
	}

	@Override
	public double[] getArray() {
		return getDataRecord(0, 0);
	}

	@Override
	public long getInitialTime() {
		
		try {
			raf.seek(0x040);
			byte[] four = new byte[4];
			byte[] two  = new byte[2];
			raf.read(four, 0, 4);			
			int year = Integer.valueOf(new String(four));
			raf.read(two, 0, 2);
			int month = Integer.valueOf(new String(two));
			raf.read(two, 0, 2);
			int day = Integer.valueOf(new String(two));
			
			initTimeDB = new long[dataBlock.size()];
			int counter = 0;
			
			for (Integer data : dataBlock.values()) {

				raf.seek(data+5);
				raf.read(two, 0, 2);
				int hour = Integer.valueOf(new String(two));
				raf.read(two, 0, 2);
				int min = Integer.valueOf(new String(two));
				raf.read(two, 0, 2);
				int sec = Integer.valueOf(new String(two));
				
				GregorianCalendar calendar = new GregorianCalendar(
						year, 
						month-1, 
						day, 
						hour, 
						min, 
						sec);
				
				initTimeDB[counter++] = calendar.getTimeInMillis();
			
			}			
			setTime(initTimeDB[0]);
			return getTime();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public String version() {
		return version;
	}

	@Override
	public double[] getDataRecord(int channel, long dr_number) {
		
		long dr = dr_number;
		
			double[] dataRecord = new double[getNumberOfSamples(channel)];
		
			//encontre o datablock do dr_number
			for (int i = 0; i < dataBlock_info.length; i++) {
				
				if(dataBlock_info[i][2]<(dr+1)){
					dr -= dataBlock_info[i][2];
					continue;
				}
				
				try {
					
					// primeira posicao
					int skip = dataBlock.get(i)+0x0027+(channelInfo.length*10)+channel*2;
					raf.seek(skip);
					
					// pula dataRecords
					skip = (int) ((channelInfo.length+1)*2*dr*frequency/10);
					raf.skipBytes(skip);
					
					// pula todos os canais
					skip = (channelInfo.length-1)*2+2;
					
					for (int k = 0; k < getNumberOfSamples(channel); k++) {
						bb.put(raf.readByte());
						bb.put((byte)(raf.readByte()+128));
						dataRecord[k]  = (double) bb.getShort(0);
						dataRecord[k] += channelInfo[channel][6];
						dataRecord[k] *= channelInfo[channel][5];
						bb.clear();
						raf.skipBytes(skip);
						if(fast)
							raf.skipBytes((skip+2)*4);
						
						//raf.readByte();
						//raf.readByte();	
					}
					setTime(initTimeDB[i]+dr*100);
					
				} catch (IOException e) {
					e.printStackTrace();
				}				
				//current_dr = dr_number;
				break;
			}
		
		return dataRecord;
	}

	@Override
	public double[] getData(int channel, int samples, double offset) {
		
		long drTime = getDataRecordTime(channel, 0);
		
		double[] result = new double[samples];
		
		int dr = (int) (offset/drTime);
		
		//encontre o datablock do dr_number
		for (int i = 0; i < dataBlock_info.length; i++) {
			
			if(dataBlock_info[i][2]<(dr+1)){
				dr -= dataBlock_info[i][2];
				continue;
			}
			
			try {
				
				// primeira posicao
				int skip = dataBlock.get(i)+0x0027+(channelInfo.length*10)+channel*2;
				raf.seek(skip);
				
				// pula dataRecords
				skip = (int) ((channelInfo.length+1)*2*dr*frequency/10);
				raf.skipBytes(skip);
				
				// pula todos os canais
				skip = (channelInfo.length-1)*2+2;
				
				for (int k = 0; k < samples; k++) {
					bb.put(raf.readByte());
					bb.put((byte)(raf.readByte()+128));
					result[k]  = (double) bb.getShort(0);
					result[k] += channelInfo[channel][6];
					result[k] *= channelInfo[channel][5];
					bb.clear();
					raf.skipBytes(skip);
				}
				setTime(initTimeDB[i]+dr*100);
				
			} catch (IOException e) {
				e.printStackTrace();
			}				
		}		
		return result;
	}

	@Override
	public long getDataRecordTime(int channel, long dr_number) {
		
		long dr = dr_number;
		for (int i = 0; i < dataBlock_info.length; i++) {
				
			if(dataBlock_info[i][2]<dr){
				dr -= dataBlock_info[i][2];
				setTime(initTimeDB[i]+dr*100);	
			} else {
				setTime(initTimeDB[i]+dr*100);	
				break;
			}
		}
		return getTime();
	}

	@Override
	public void close() {
		try {
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.flush();
	}

	@Override
	public double getSignalDuration() {
		return total_time;
	}

	@Override
	public double getDataRecordDuration(int channel) {
		return 0.1;
	}

	@Override
	public int getNumberOfSamples(int channel) {
		if(fast) return 20;
		return frequency/10;
	}

	@Override
	public long getNumberOfDataRecords() {
		return (long) (total_time*10);
	}

	@Override
	public int getSamplingRate() {
		if(fast) return 200;
		return frequency;
	}

	@Override
	public List<String> getFilters() {
		
		ArrayList<String> list = new ArrayList<String>();		
		return list; 
	}
}
