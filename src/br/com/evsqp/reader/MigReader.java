package br.com.evsqp.reader;



import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYSeries;

import br.com.evsqp.viewer.AbstractViewer;
import br.com.evsqp.viewer.MigViewer;


public class MigReader extends AbstractReader{

	MigViewer viewer;
	
	public MigReader(){		
		setup();
	};
	
	public MigReader(File file){
		setup();
		this.file = file;
	}
	
	private void setup(){
		viewer = new MigViewer(this);
	}
	
	public XYSeries getSeries(){
		
	    Scanner scanner = null;
	    XYSeries series = new XYSeries(file.getName());
	    
    	try {
			scanner = new Scanner(file);
			scanner.nextLine();
			scanner.nextLine();
			
			double i = 0;
			while (scanner.hasNextLine())
				series.add(i++/100,Double.valueOf(scanner.nextLine()));

    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();			
		}
		
		return series;
	}
	
	public TimeSeries getTimeSeries(){
		
	    Scanner scanner = null;
	    TimeSeries series = new TimeSeries(file.getName());
	    
    	try {
			scanner = new Scanner(file);
			scanner.nextLine();
			scanner.nextLine();
			
			long date = new Date(0).getTime();
			int rawOffset = TimeZone.getDefault().getRawOffset();
			date -= rawOffset;
			long i = 0;
			
			while (scanner.hasNextLine()){
				series.add(getTimePeriod(date+i),Double.valueOf(scanner.nextLine()));
				i += 10;
			}

    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();			
		}
		
		return series;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfChannels() {
		
		return 1;
	}
}
