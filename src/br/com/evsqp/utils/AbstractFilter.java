package br.com.evsqp.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import br.com.evsqp.reader.AbstractReader;

abstract public class AbstractFilter extends FileFilter{

	abstract public AbstractReader getReader(File file);
}
