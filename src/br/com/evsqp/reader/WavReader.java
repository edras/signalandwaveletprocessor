package br.com.evsqp.reader;



import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYSeries;

import br.com.evsqp.utils.WavFilter;
import br.com.evsqp.viewer.AbstractViewer;
import br.com.evsqp.viewer.WavViewer;

/* Play a *.wav or *.au file */

public class WavReader extends AbstractReader{
	/*
	 * Play a *.wav or *.au file
	 * 
	 * @param args args[0] on command line is name of file to play
	 */
	private int bufSize;
	private long length;
	private AudioFormat af;
	private DataLine.Info info;	
	private AudioInputStream ais;
	
	WavViewer viewer;
	private boolean fast;

	public WavReader(File file) {		
		setup();
		readFile(file);
		setDebug(false);
	}
	
	public WavReader() {
		setup();
	}
	
	private void setup() {		
		viewer = new WavViewer(this);
	}	

	public static void main(String[] args) {
		
		
		JFileChooser jc = new JFileChooser();
		jc.setFileFilter(new WavFilter());
		jc.setMultiSelectionEnabled(false);
		int showOpenDialog = jc.showOpenDialog(null);
		
		if(showOpenDialog != JFileChooser.APPROVE_OPTION) return;
		
		WavReader wavReader = new WavReader();
		wavReader.setDebug(true);		
		wavReader.readFile(jc.getSelectedFile());
		wavReader.view();
		//wavReader.play(wavReader.getSeries().toArray()[1]);
		//wavReader.play();
		
	}

	private void play(double[] buffer) {
		
        AudioFormat format = new AudioFormat(af.getSampleRate(), af.getSampleSizeInBits(), 1, true, false);
        DataLine.Info localInfo = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine line;
        
		try {
			line = (SourceDataLine) AudioSystem.getLine(localInfo);
			line.open(format, bufSize);
			byte[] data = new byte[bufSize];
			
			int nBytes = af.getSampleSizeInBits()/8;
			
			line.start();
			for (int i = 0, j = 0, n = buffer.length; i < n; i++ ) {
				j += endianOrder(buffer, i, data, j, nBytes);
			}
			
			line.write(data, 0, data.length);
			
			line.drain(); line.stop();
			long time = line.getMicrosecondPosition();
			if (isDebug()) System.out.println("time by playing " + time / 1000000);
			line.close(); 		

		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}       
		
	}

	private int endianOrder(double[] src, int i, byte[] dst, int j, int nBytes) {
		
		double normal = Math.pow(2, af.getSampleSizeInBits()-1);
		double value = src[i]*normal;
		
		for (int k = 0; k < nBytes; k++) {
			dst[j+k] = (byte)(((short)value >> k*8)&0xFF);
		}
		
		return nBytes;
	}

	public void play() {

		byte[] data = new byte[bufSize];
		int bytesRead;

		SourceDataLine line;
		try {
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(af, bufSize);
			line.start();

			while ((bytesRead = ais.read(data, 0, data.length)) != -1) 
				line.write(data, 0, bytesRead);

			line.drain(); line.stop();
			long time = line.getMicrosecondPosition();
			if (isDebug()) System.out.println("time by playing " + time / 1000000);
			line.close();
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readFile(File file) {
		
		this.file = file;
		length = file.length();
		try {
			ais = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		af = ais.getFormat();
		info = new DataLine.Info(SourceDataLine.class, af);

		if (!AudioSystem.isLineSupported(info)) {
			System.out.println("unsupported line");
			System.exit(0);
		}
		
		bufSize = (int)((af.getFrameRate() * af.getFrameSize()) / 10);

		if(isDebug()){
			System.out.println("Frame Rate: " + af.getFrameRate());
			System.out.println("Frame Size: " + af.getFrameSize());
			System.out.println("Buffer Size: " + bufSize);
			System.out.println("Channels : " + af.getChannels());
			System.out.println("Bits per sample : " + af.getSampleSizeInBits());
			Double times = (double)(length / (af.getFrameRate() * af.getChannels() * af.getSampleSizeInBits() / 8)); 
			System.out.println("Song duration: " + times + " seconds");
			System.out.println("Frame Sampling (N):" + ((20 * af.getFrameRate())/1000));
			System.out.println("Run Times: " + ((times * 1000) / 20));
		}
		
	}

	public XYSeries getSeries() {
		
		byte[] data = new byte[bufSize];
		int bytesRead;

		XYSeries series = new XYSeries("Audio File");
		
		try {
			int k = af.getChannels();
			int c = 0;
			double jj = 0;
			double normal = Math.pow(2, af.getSampleSizeInBits()-1);
			int nBytes = af.getFrameSize() / af.getChannels();
			double time = 1/af.getFrameRate()/af.getFrameSize();

			while ((bytesRead = ais.read(data, 0, data.length)) != -1) {
				for (int j = 0; j < bytesRead; j += nBytes) {
					if(c++ == 0){
						jj += nBytes;
						series.add(jj*time, endianOrder(data, j, nBytes)/normal);
					}
					if(c == k) 
						c = 0;					
				}
			}
			return series;
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return series;
	}
	
	/**
	 * 
	 * @param dr Quantidade de bytes para descartar desde o inicio do arquivo
	 * @param page = quantidade de milisegundos por janela
	 * @return
	 */
	
	public TimeSeries getTimeSeries(long dr, int page) {
		
		byte[] data = new byte[bufSize];
		int bytesRead;

		TimeSeries series = new TimeSeries("Audio File");
		
		try {
			
			int k = af.getChannels();
			int c = 0;
			long dr_ = 0;
			double normal = Math.pow(2, af.getSampleSizeInBits()-1);
			int nBytes = af.getFrameSize() / af.getChannels();
			long date = new Date(0).getTime();
			int rawOffset = TimeZone.getDefault().getRawOffset();
			date -= rawOffset;
			long time = (long)((1/af.getFrameRate())*1000000);
			int page_size = page*getSamplingRate()/1000;
			
			if(dr<0){
				dr_ = ais.getFrameLength()*nBytes-page_size*nBytes;
			}
			
			ais.close();
			ais = AudioSystem.getAudioInputStream(file);
			dr_ = dr*nBytes;
			ais.skip(dr_);
			dr_ = dr;
			
			int fastRead = 0;
			
			while ((bytesRead = ais.read(data, 0, data.length)) != -1) {

//				if((bytesRead/nBytes+dr_)<dr){
//					dr_ += bytesRead/nBytes;
//					continue;
//				}
				
				if((dr_-dr) == page_size) 
					break; 

				for (int j = 0; j < bytesRead; j += nBytes) {
					if(c++ == 0){
						dr_++;
						if(dr_>=dr){
							
							if(fast && fastRead == 0){
								series.add(getTimePeriod((long)(date+dr_*time)), endianOrder(data, j, nBytes)/normal);
								fastRead = 5;
							} else if(!fast)
								series.add(getTimePeriod((long)(date+dr_*time)), endianOrder(data, j, nBytes)/normal);
							
							fastRead--;
							
							if((dr_-dr) == page_size) 
								break; 
						}
					}
					if(c == k) 
						c = 0;					
				}
			}
			return series;
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}		
		return series;
	}

	private double endianOrder(byte[] data, int index, int nBytes) {
		
		double result = 0;
		int endian = 0;
		
		if(af.isBigEndian()) 
			endian = nBytes-1;
		
		for(int i=0; i<nBytes; i++){
			result += data[index+i] << (Math.abs(endian-i)*8);
		}		
		
		return result;
	}

	@Override
	public void view() {
		viewer.view();		
	}

	@Override
	public double[] getArray() {
		return getSeries().toArray()[1];
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
		return (int)af.getFrameRate();
	}

	@Override
	public int getNumberOfChannels() {
		return af.getChannels();
	}

	public void setFastReading(boolean fast) {
		this.fast = fast;		
	}
	
	public boolean isFastReading(){
		return fast;
	}
}