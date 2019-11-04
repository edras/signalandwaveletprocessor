package br.com.evsqp.reader;



import cs.jwave.handlers.wavelets.Wavelet;
import br.com.evsqp.viewer.AbstractViewer;
import br.com.evsqp.viewer.WaveletViewer;

public class WaveletReader extends AbstractReader {

	WaveletViewer viewer;
	Wavelet wavelet;
	
	@Override
	public void view() {
		viewer.view();
	}
	
	public WaveletReader(Wavelet wavelet){
		this.wavelet = wavelet;
		setup();
	}

	private void setup() {
		viewer = new WaveletViewer(this);		
	}

	public double[] getScales() {
		return wavelet.getScales();
	}

	public double[] getCoeffs() {
		return wavelet.getCoeffs();
	}

	public String getWaveletName() {
		return wavelet.getClass().getSimpleName();
	}

	@Override
	public double[] getArray() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return 0;
	}

}
