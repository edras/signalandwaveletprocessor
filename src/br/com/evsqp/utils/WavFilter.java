package br.com.evsqp.utils;

import java.io.File;

import br.com.evsqp.reader.AbstractReader;
import br.com.evsqp.reader.WavReader;

public class WavFilter extends AbstractFilter {

	String[] extensions = new String[] { "wav", "au" };
	
    public boolean accept(File file) {
    	
		if (file.isDirectory()) {
			return true;
		} else {
			String path = file.getAbsolutePath().toLowerCase();
			for (int i = 0, n = extensions.length; i < n; i++) {
				String extension = extensions[i];
				if ((path.endsWith(extension) && (path.charAt(path.length()
						- extension.length() - 1)) == '.')) {
					return true;
				}
			}
		}
		return false;
    }

    //The description of this filter
    public String getDescription() {
        return "(*.wav, *.au) Audio Files";
    }

	@Override
	public AbstractReader getReader(File file) {
		return new WavReader(file);
	}
}
