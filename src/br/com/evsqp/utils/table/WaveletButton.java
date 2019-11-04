package br.com.evsqp.utils.table;

import java.util.HashMap;

import javax.swing.JButton;

public class WaveletButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2097462140277596613L;
	private String signal;
	private String wavelet;
	private int boundary;
	private HashMap<String, double[]> result;
	
	public WaveletButton(String waveName, int boundary, String path,
			HashMap<String, double[]> result) {

		this.wavelet = waveName;
		this.boundary = boundary;
		this.signal = path;
		this.result = result;		
	}
	
	public String getSignal() {
		return signal;
	}
	public void setSignal(String signal) {
		this.signal = signal;
	}
	public String getWavelet() {
		return wavelet;
	}
	public void setWavelet(String wavelet) {
		this.wavelet = wavelet;
	}
	public int getBoundary() {
		return boundary;
	}
	public void setBoundary(int boundary) {
		this.boundary = boundary;
	}
	public HashMap<String, double[]> getResult() {
		return result;
	}
	public void setResult(HashMap<String, double[]> result) {
		this.result = result;
	}
		
}
