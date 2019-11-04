package br.com.evsqp.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jfree.chart.plot.Marker;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import br.com.evsqp.display.chart.XYChart;
import br.com.evsqp.reader.BigReader;
import br.com.evsqp.viewer.BigViewer;
import cs.jwave.handlers.BasicTransform;
import cs.jwave.handlers.DiscreteFourierTransform;
import flanagan.complex.Complex;
import flanagan.math.Polynomial;

public class DFilter {
	
	// IIR Butterworth
	public static void hpf(int sampling, TimeSeries output, float hpf, int order) {
				
//		Recurrence relation:
//			y[n] = (  1 * x[n- 4])
//			     + ( -4 * x[n- 3])
//			     + (  6 * x[n- 2])
//			     + ( -4 * x[n- 1])
//			     + (  1 * x[n- 0])
//
//			     + ( -0.0176648009 * y[n- 4])
//			     + (  0.0000000000 * y[n- 3])
//			     + ( -0.4860288221 * y[n- 2])
//			     + ( -0.0000000000 * y[n- 1])
		
		float GAIN = 10.64046542F;
		double [] xv = new double[order+1];
		double [] yv = new double[order+1];
		
		for (int i=order; i<output.getItemCount(); i++){ 
			xv[0] = xv[1]; xv[1] = xv[2]; xv[2] = xv[3]; xv[3] = xv[4]; 
	        xv[4] = output.getValue(i).doubleValue()/GAIN;
	        yv[0] = yv[1]; yv[1] = yv[2]; yv[2] = yv[3]; yv[3] = yv[4]; 
	        yv[4] =   (xv[0] + xv[4]) - 4 * (xv[1] + xv[3]) + 6 * xv[2]
	                     + ( -0.0176648009 * yv[0]) + (  0.0000000000 * yv[1])
	                     + ( -0.4860288221 * yv[2]) + ( -0.0000000000 * yv[3]);
	        output.update(i, yv[4]);
	    }
		
		int i_ = output.getItemCount();
		int j_ = order;
		
		// zera valores onde o calculo não eh completo
		for (int i = 0; i < j_; i++) {
			output.update(i,0);
			output.update(i_-i-1,0);
		}
	}
	
	public static void notch(int sampling, TimeSeries output, double[] freq) throws CloneNotSupportedException{
		
		//boolean dc = false;
		TimeSeries input = output.createCopy(0, output.getItemCount()-1);
		
		Polynomial zeroPoly = null;
		Polynomial polePoly = null;
		
		Polynomial zeroPoly_tmp = null;
		Polynomial polePoly_tmp = null;

		for(int i = 0; i< freq.length; i++){

			double angleRad = 2*freq[i]*Math.PI/sampling;
			Complex zeros = new Complex();
			Complex poles = new Complex();
			zeros.polar(1, angleRad);
			poles.polar(0.8, angleRad);
			
			if(freq[i]==0){
				zeroPoly_tmp = new Polynomial(new double[]{-1, 1});
				polePoly_tmp = new Polynomial(new double[]{-0.999, 1});
			} else {
				zeroPoly_tmp = new Polynomial(zeros.times(zeros.conjugate()).getReal(), zeros.plus(zeros.conjugate()).getReal()*-1, 1);
				polePoly_tmp = new Polynomial(poles.times(poles.conjugate()).getReal(), poles.plus(poles.conjugate()).getReal()*-1, 1);
			}
						
			if(zeroPoly == null){
				zeroPoly = zeroPoly_tmp;
				polePoly = polePoly_tmp;
			} else {
				zeroPoly = zeroPoly.times(zeroPoly_tmp);
				polePoly = polePoly.times(polePoly_tmp);
			}			
		}
		
		int size = zeroPoly.getDeg();
		for(int i=0;i<size;i++){
			output.update(i, 0.0);
		}
		
		double tmp;
		
		for (int i = size; i < output.getItemCount(); i++) {
			tmp = input.getValue(i).doubleValue();
			for(int j=0; j< size; j++){
				tmp += zeroPoly.getCoefficient(j)*input.getValue(i-size+j).doubleValue();
				tmp -= polePoly.getCoefficient(j)*output.getValue(i-size+j).doubleValue();
			}			
			output.update(i, tmp);			
		}
	}
	
	public static void fft(int sampling, TimeSeries input){
		
		BasicTransform dft = new DiscreteFourierTransform();

		//int powof2 = 1;
		float size = input.getItemCount();
		//while( powof2 < size ) powof2 <<= 1;
		//int space = (int) (powof2 - size);
		
    	double[] array = new double[input.getItemCount()*2];
    	for (int i = 0; i < size; i++) {
    		array[i*2] = input.getValue(i).doubleValue();
    		array[(i+1)*2-1] = 0;
    	}
    	array = dft.forward(array);
    	
    	XYChart demo = new XYChart("FFT", "");
    	demo.setLabelx("frequency");
    	demo.setLabely("value");
    	
    	XYSeries sfft = new XYSeries(input.getKey());
    	for(int i=0; i<size/2; i++){
    		sfft.add(i/size*sampling, Math.abs(array[i*2]));
    	} 
    	
    	demo.setDataset(new XYSeriesCollection(sfft));
    	demo.setXRange(0, sampling/2);
    	
    	demo.pack();
    	demo.setVisible(true);
	}
	
	public static void notch60(int sampling, TimeSeries output) throws CloneNotSupportedException{
		notch(sampling, output, new double[]{60});
	}
	
	public static void subSampling(int sampling, int subSampling, TimeSeries output) throws CloneNotSupportedException{

		TimeSeries input = output.createCopy(0, output.getItemCount()-1);
		
		int scale = sampling/subSampling;
		// lets do Hard Low Pass Filtering!
		output.clear();
		
		for (int i = 0; i < input.getItemCount()/scale; i++) {
			output.add(input.getTimePeriod(i*scale), input.getValue(i*scale));
		}
	}
	
	public static void lp70(int sampling, TimeSeries output) throws CloneNotSupportedException{
		lpf(sampling, output, 70);
	}

	public static void lpf(int sampling, TimeSeries output, float lpf) throws CloneNotSupportedException{
		
		double[] vector = new double[11];
		double[] out   = new double[output.getItemCount()];
	
		float fc = lpf/sampling;
		vector[0] = 2*fc;
		double con = 2*Math.PI*fc;
		double n;
		double n_1 = 2*(vector.length-1);
		
		// calcula um vetor de 21 coeficientes de sync (apenas 11 por ser refletido) e aplica janela de blackman
		// onde o valor pi no circulo unitario representa a frequencia de amostragem
		for (double i = 1; i < vector.length; i++) {
			n = (i)/(n_1);
			vector[(int)i] = 2*fc*Math.sin(i*con)/(i*con)*(0.42+0.5*Math.cos(2*Math.PI*n)+0.08*Math.cos(4*Math.PI*n));
		}
		
		// faz a convolução do sinal com o vetor resultante para gerar o lpf
		int i_ = output.getItemCount();
		int j_ = vector.length;

		// zera valores onde a convolucao nao eh completa
		for (int i = 0; i < j_; i++) {
			out[i] = 0;
			out[i_-i-1] = 0;
		}
		
		for (int i = j_-1; i < (i_-j_); i++) {			
			out[i] = output.getValue(i).doubleValue()*vector[0];			
			for (int j = 1; j < j_; j++) 
				out[i] += output.getValue(i-j).doubleValue()*vector[j]+
						output.getValue(i+j).doubleValue()*vector[j];
		}
		
		for (int i = 0; i < out.length; i++) {
			output.update(i, out[i]*10);
		}
	}	
	
	private static void subSamplingRoutine(){
		
		File[] exportFile;
		Integer[] subSamples = new Integer[]{500, 333, 250, 200, 166, 142, 125, 111, 100 };
			
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Selecione os eventos para subAmostragem: 1Khz -> 500, 333, 250, 200, 167, 143, 125, 111, 100");
		fc.setMultiSelectionEnabled(true);

		int returnVal = fc.showOpenDialog(null);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
				System.exit(0);
		} 
		
		exportFile = fc.getSelectedFiles();

		for (int i = 0; i < exportFile.length; i++) {
			
			for (int j = 0; j < subSamples.length; j++) {
				
				BigReader bigReader = new BigReader(exportFile[i]);
				
				File dir = new File(exportFile[i].getParentFile()+"_Sub"+subSamples[j]);
				if(!dir.exists()) dir.mkdirs();
				
				String name = exportFile[i].getName();
				File file = new File(dir.getAbsolutePath()+File.separatorChar+name.substring(0, name.lastIndexOf('.'))+"_Sub"+subSamples[j]+".big");

				try {
					
					TimeSeries series = bigReader.getSeries();
					DFilter.subSampling(1000, subSamples[j], series);

					String text = "Sampling "+subSamples[j]+"Hz, Notch:{20,60,120,180,240,300,420}, LPF:";
					float frequency = 200.0F/70.0F;
					DFilter.lpf(subSamples[j], series, ((float)subSamples[j])/frequency);  text +=Double.toString(((float)subSamples[j])/frequency); 
					
					FileOutputStream fo = new FileOutputStream(file);
					ObjectOutputStream oo = new ObjectOutputStream(fo);

					oo.writeLong(bigReader.getStep()*(1000/subSamples[j]));
					oo.writeLong(series.getDataItem(0).getPeriod().getFirstMillisecond());
					oo.writeInt(series.getItemCount());
					for (int k = 0; k < series.getItemCount(); k++) {
						oo.writeInt(series.getDataItem(k).getValue().intValue());
					}
//					oo.writeObject(save);
					oo.writeChar('2'); // versão
					oo.writeUTF(text);
					oo.close();
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}			
		}		
	}
	
	public static void main(String[] args) {
		
		String showInputDialog = JOptionPane.showInputDialog("Digite a opção desejada:"+
		"\n1:Spike Sub-sampling"+
		"\n2:Spike variability 20 200"+
		"\n3:Spike D1/D2 morphology"+
		"\n4:Sine generator", 1);
		
		if(showInputDialog.contains("1")){
			subSamplingRoutine();
		}
		if(showInputDialog.contains("2")){
			spikeVariability();
		}
		if(showInputDialog.contains("3")){
			spikeMorphology();
		}
		if(showInputDialog.contains("4")){
			sineGenerator();
		}
		
	}

	private static void sineGenerator() {
	
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Selecione o diretório para gravar os arquivos de seno");
		fc.setMultiSelectionEnabled(false);
		fc.setDialogType(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);

		fc.showOpenDialog(null);

		File file;
		String name;
		Integer f = 200;
		
		boolean loop = true;
		while(loop){
			String freq = JOptionPane.showInputDialog("Digite a frequencia de amostragem","Frequencia");
			try{
				if(freq == null) System.exit(0);
				f = Integer.parseInt(freq);
				if(f<1) throw new Exception();
				loop = false;
			} catch(Exception e){
				JOptionPane.showMessageDialog(null,"Digite um numero inteiro maior do que zero");
			}			
		}
		
		for (double p = 0; p < 180; p+=15) {
			
			for(double i = 1; i< 60; i+=0.1){
				
				name = "sine_";
				if(i<100) name+="0";
				if(i<10)  name+="0";
				name += i+"Hz_"; 
				if(p<100) name += "0";
				name+=p+"_shifted.mig";
				
				file = new File(fc.getCurrentDirectory()+"/"+p+"/"+name);
				File dir = new File(fc.getCurrentDirectory()+"/"+p);
				if(!dir.exists()) dir.mkdirs();
				
				try{
				
				FileOutputStream fo = new FileOutputStream(file);
				PrintStream ps = new PrintStream(fo);
				ps.println(0);
				ps.println(0);
				
				for(int j=0;j<(f+1);j++){
					double x = Math.sin(((double)j*i)/f*2*Math.PI+Math.toRadians(p));
					//System.out.println(x);
				    ps.println(x*100);
				}
				ps.close();
				}catch(Exception e){
					
					e.printStackTrace();
				}
			}
		}
	}

	private static void spikeMorphology() {
		
		File[] exportFile;
		boolean upSpike = false;
		boolean ok;
			
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Selecione as espículas para calculo D1/D2");
		fc.setMultiSelectionEnabled(true);

		int returnVal = fc.showOpenDialog(null);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
				System.exit(0);
		} 
		
		exportFile = fc.getSelectedFiles();
		
		File file = new File(exportFile[0].getParentFile()+"_Morphology.txt");
		
		try {
			
			FileOutputStream fo = new FileOutputStream(file);
			PrintStream oo = new PrintStream(fo);
			oo.println("File Name; D1; D2; D1/D2; Duration;");

			for (int i = 0; i < exportFile.length; i++) {			
		
				BigReader bigReader = new BigReader(exportFile[i]);
				BigViewer viewer = (BigViewer) bigReader.getViewer();
				viewer.setTitle(viewer.getTitle()+" "+(i+1)+"/"+exportFile.length);
				viewer.view();

				String name = exportFile[i].getName();
				int sizeStart = name.indexOf('_')+1;
				int sizeEnd = name.indexOf('.');
				int spikeSize = 0;				

				double[][] points = null;
				
				ok = false;
				//while(!ok){

					upSpike = true;
	
					int showConfirmDialog = JOptionPane.showConfirmDialog(null, "A ponta da espicula está para baixo?","Direção da Espícula",JOptionPane.YES_NO_CANCEL_OPTION);
					if(showConfirmDialog == JOptionPane.YES_OPTION) upSpike = false;
					if(showConfirmDialog == JOptionPane.CANCEL_OPTION) break;
						
					spikeSize = Integer.parseInt(name.substring(sizeStart, sizeEnd));
					
					points = DFilter.findSpikePoints(bigReader, upSpike, spikeSize);
					
					Marker start = viewer.addMarker(points[0][0], Color.RED, "start", 2);
					Marker ponta = viewer.addMarker(points[1][0], Color.GREEN, "ponta", 2);
					Marker end =   viewer.addMarker(points[2][0], Color.BLUE, "end", 2);				
				
					int confirmDialog = JOptionPane.showConfirmDialog(null, "Correto?","Confirm",JOptionPane.YES_NO_OPTION);
					if(confirmDialog == JOptionPane.NO_OPTION){
						points[0][0] = 0;
					} else ok = true;					
				//}
				
				viewer.dispose();
				
				oo.print(name+"; ");
				double d1 = points[1][0]-points[0][0];
				oo.print(Double.toString(d1)+"; ");
				double d2 = points[2][0]-points[1][0];
				oo.print(Double.toString(d2)+"; ");
				oo.print(Double.toString(d1/d2)+"; ");
				oo.println(spikeSize+";");
				
			}
			oo.close();

		} catch (Exception e) {
				e.printStackTrace();
		}				
	}		

	private static void spikeVariability() {
		
		File exportFile;
		int growRate, spikeSize, targetSize;
		boolean upSpike = true;
			
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Selecione os eventos para redimensionar de 20 a 200 ms");
		fc.setMultiSelectionEnabled(false);

		int returnVal = fc.showOpenDialog(null);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
				System.exit(0);
		} 

		growRate = 1;

//		boolean test = true;
//		while(test){
//			String showInputDialog = JOptionPane.showInputDialog("Digita a taxa de crescimento da espícula em milisegundos:\nEx: 1, 5, 10","1");
//			try{
//				growRate = Integer.parseInt(showInputDialog);
//				test = false;
//			} catch(NumberFormatException e){
//				JOptionPane.showMessageDialog(null, "Apenas numeros!");
//			}
//		}
		
		exportFile = fc.getSelectedFile();
		String name = exportFile.getName();
		String absolutePath = exportFile.getAbsolutePath();
	
		File dir = new File(absolutePath.substring(0, absolutePath.lastIndexOf('.'))+"_Variability");
		if(!dir.exists()) dir.mkdirs();
		
		BigReader bigReader = new BigReader(exportFile);
		
		int showConfirmDialog = JOptionPane.showConfirmDialog(null, "A ponta da espicula está para baixo?","Direção da Espícula",JOptionPane.YES_NO_OPTION);
		if(showConfirmDialog == JOptionPane.YES_OPTION) upSpike = false;

		int sizeStart = name.indexOf('_')+1;
		int sizeEnd = name.indexOf('.');
		spikeSize = 0;
		
		try {
			spikeSize = Integer.parseInt(name.substring(sizeStart, sizeEnd));
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			System.exit(0);
		}

		double spikePoints[][] = findSpikePoints(bigReader, upSpike, spikeSize);
		TimeSeries series_orig = bigReader.getSeries();
		
		for(targetSize = 20; targetSize < 201; targetSize += growRate){
			
			File file = new File(dir.getAbsolutePath()+File.separatorChar+name.substring(0, name.lastIndexOf('.'))+"_"+(targetSize<100?"0"+targetSize:targetSize)+".big");

			try {
				
				TimeSeries series = series_orig.createCopy(0, series_orig.getItemCount()-1);
				DFilter.growSpike(series, spikeSize, targetSize, spikePoints);
				DFilter.subSampling(1000, 200, series);
				FileOutputStream fo = new FileOutputStream(file);
				ObjectOutputStream oo = new ObjectOutputStream(fo);
				
				oo.writeLong(bigReader.getStep());
				oo.writeLong(series.getDataItem(0).getPeriod().getFirstMillisecond());
				oo.writeInt(series.getItemCount());
				for (int k = 0; k < series.getItemCount(); k++) {
					oo.writeInt(series.getDataItem(k).getValue().intValue());
				}
				// oo.writeObject(save);
				oo.close();
			} catch (Exception e) {
				e.printStackTrace();
			}				
		}		
	}

	// esse metodo retorna os 3 pontos da espícula, inicio da ponta, ponta e final da ponta
	private static double[][] findSpikePoints(BigReader bigReader, boolean upSpike, int spikeSize) {

		double points[][] = new double[3][2];
		boolean found = false;
		int index, size;
		
		TimeSeries series = bigReader.getSeries();
		
		// encontra a ponta
		size = series.getItemCount()/2-250;
		index = size;
		Double value = series.getValue(index).doubleValue();
		Double temp;
		Double id_tmp = 0.0;

		// acha a ponta
		for (; index < size+300; index++) {
			temp = series.getValue(index).doubleValue();
			if(upSpike){
				if(temp>value){
					value = temp;
					id_tmp = (double) index;
				}
			} else {
				if(temp<value){
					value = temp;
					id_tmp = (double) index;
				}
			}
		}		
		
		points[1][0] = id_tmp;
		points[1][1] = value;
		
		//encontra o lado direito
		
		size = index;
		
		// acha a ponta
		for (; index < size+300; index++) {
			temp = series.getValue(index).doubleValue();
			if(upSpike){
				if(temp<value){
					value = temp;
					id_tmp = (double) index;
				}
			} else {
				if(temp>value){
					value = temp;
					id_tmp = (double) index;
				}
			}
		}	
		
		
		points[2][0] = id_tmp;
		points[2][1] = value;
		
		index = (int)(points[2][0] - points[1][0]);
		index *= bigReader.getStep();
		index = spikeSize - index;
		index /= bigReader.getStep();
		
		points[0][0] = points[1][0] - index;
		points[0][1] = series.getValue((int)points[0][0]).doubleValue();
				
		return points;
	}

	private static void growSpike(TimeSeries output, long spikeSize, int targetSize, double[][] spikePoints) throws CloneNotSupportedException {
		
		TimeSeries input = output.createCopy(0, output.getItemCount()-1);
		
		output.clear();
		
		int r1 = targetSize/2;
		int r2 = r1;
		if((r1+r2)!= targetSize) r1++;

		int i;
		
		// copia dados até chegar ponto de inicio da espicula
		for (i = 0; i < spikePoints[0][0]; i++) {
			output.add(input.getTimePeriod(i), input.getValue(i));
		}		
		
		double step = (spikePoints[1][1] - spikePoints[0][1])/r1;
		double value = spikePoints[0][1];
		
		// gera primeira rampa
		for (int j = 0; j < r1; j++, i++) {
			output.add(input.getTimePeriod(i), value);
			value += step;
		}
		
		step = (spikePoints[2][1] - spikePoints[1][1])/r2;
		value = spikePoints[1][1];
		
		// gera segunda rampa
		for (int j = 0; j < r2; j++, i++) {
			output.add(input.getTimePeriod(i), value);
			value += step;
		}
		
		// obtem valor do tempo atual do final da espicula
		long time = input.getTimePeriod(i).getFirstMillisecond();
		
		// copia dados até chegar ponto final da espicula
		for (int j = (int)spikePoints[2][0]; j < input.getItemCount()-1; j++, time++) {
			output.add(getTimePeriod(time), input.getValue(j));
		}		
		
		// ajusta tamanho do vetor para manter espicula centrada
		// captura tempo da ponta e remove tudo a partir de 0.750 segundo para cada lado
		// assim amostra final fica com 1.5 segundos
		
		// valor do index da ponta
		i = (int)spikePoints[0][0] + r1;
		
		// remove itens posteriores
		output.delete(i+750, output.getItemCount()-1);
		
		// remove itens anteriores
		output.delete(0, i-750);	

	}
	
	protected static RegularTimePeriod getTimePeriod(long time) {
        return RegularTimePeriod.createInstance(
        		Millisecond.class, 
        		new Date(time), 
        		TimeZone.getDefault());
	}
}
