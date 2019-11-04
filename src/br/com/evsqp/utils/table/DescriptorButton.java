package br.com.evsqp.utils.table;

import javax.swing.JButton;

import org.jfree.data.category.DefaultCategoryDataset;

public class DescriptorButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1289751606293460436L;
	private String signal;
	private String descriptor;
	private DefaultCategoryDataset[] result;
	
	public DescriptorButton(String descriptor, String path,
			DefaultCategoryDataset[] dataset) {

		this.descriptor = descriptor;
		this.signal = path;
		this.result = dataset;		
	}
	
	public String getSignal() {
		return signal;
	}
	public void setSignal(String signal) {
		this.signal = signal;
	}
	public String getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(String wavelet) {
		this.descriptor = wavelet;
	}
	public DefaultCategoryDataset[] getResult() {
		return result;
	}
	public void setResult(DefaultCategoryDataset[] result) {
		this.result = result;
	}
}
