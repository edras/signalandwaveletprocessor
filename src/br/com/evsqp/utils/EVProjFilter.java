package br.com.evsqp.utils;

import java.io.File;

import br.com.evsqp.reader.AbstractReader;

public class EVProjFilter extends AbstractFilter {

	String[] extensions = new String[] { "evproj" };
	
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
        return "(*.evproj) EVProject Files";
    }
    
    public String getExtension(){
    	return extensions[0];
    }

	@Override
	public AbstractReader getReader(File file) {
		return null;
	}
}
