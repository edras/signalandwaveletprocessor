package br.com.evsqp.lda.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class SpreadSheetFilter extends FileFilter {

	String[] extensions = new String[] { "xls", "ods" };
	
	@Override
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

	@Override
    public String getDescription() {
        return "(*.xls, *.ods) Spread Sheet  Files";
    }

}
