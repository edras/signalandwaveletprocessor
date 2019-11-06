package br.com.evsqp.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jfree.data.category.DefaultCategoryDataset;

import br.com.evsqp.math.vector.MathV;
import br.com.evsqp.reader.AbstractReader;
import br.com.evsqp.reader.WaveletReader;
import br.com.evsqp.utils.BigFilter;
import br.com.evsqp.utils.ClassFinder;
import br.com.evsqp.utils.EVProjFilter;
import br.com.evsqp.utils.EdfFilter;
import br.com.evsqp.utils.MigFilter;
import br.com.evsqp.utils.WavFilter;
import br.com.evsqp.utils.table.ButtonTableModel;
import br.com.evsqp.utils.table.DescriptorButton;
import cs.jwave.handlers.wavelets.Wavelet;

public class EVModel {

	private static final String DescriptorButton = null;
	private HashMap<String, File> signalFiles; // filename and path
	private JList signalList;
	private JFileChooser fc;
	private JFileChooser fp;
	private HashMap<String, Wavelet> wavelets;
	private JList selWavelets;
	private int level;
	private JList descriptors;
	private JList selDescriptors;
	private JList boundaries;
	private JList selBoundaries;
	private String project;
	
	private ButtonTableModel wtmodel;
	private ButtonTableModel dmodel;

	public EVModel(EVApp controller) {
		
		signalFiles = new HashMap<String, File>();
		signalList = new JList(new DefaultListModel());
		
		wavelets = new HashMap<String, Wavelet>();
		selWavelets = new JList(new DefaultListModel());
		
		descriptors = new JList(new DefaultListModel());
		selDescriptors = new JList(new DefaultListModel());
		
		boundaries = new JList(new DefaultListModel());
		selBoundaries = new JList(new DefaultListModel());

		addWavelets();
		addBoundaries();
		addDescriptors();
		
        fc = new JFileChooser();
        fc.addChoosableFileFilter(new EdfFilter());
        fc.addChoosableFileFilter(new MigFilter());
        fc.addChoosableFileFilter(new WavFilter());
        fc.addChoosableFileFilter(new BigFilter());
        fc.setAcceptAllFileFilterUsed(true);
        fc.setMultiSelectionEnabled(true);
        
		fp = new JFileChooser();
		fp.addChoosableFileFilter(new EVProjFilter());
		fc.setAcceptAllFileFilterUsed(false);
		fp.setMultiSelectionEnabled(false);
		
		resetWaveletTableModel();
		resetDescriptorTableModel();

	}

	private void addBoundaries() {
		
		DefaultListModel model = (DefaultListModel) boundaries.getModel();
		
		for(int b = 0; b < Wavelet.WCONV_NONE; b++){
			model.addElement(Wavelet.bName[b]);
		}		
	}
	
	public void loadProject(){
		
		project = fp.getSelectedFile().getAbsolutePath();
		
		try {
			FileInputStream fis = new FileInputStream(project);
			ObjectInputStream in = new ObjectInputStream(fis);
			project = (String) in.readObject();
			signalFiles = (HashMap<String, File>) in.readObject();
			signalList.setModel((DefaultListModel) in.readObject());
			selWavelets.setModel((DefaultListModel) in.readObject());
			level = (Integer) in.readObject();
			descriptors.setModel((DefaultListModel) in.readObject());
			selDescriptors.setModel((DefaultListModel) in.readObject());
			boundaries.setModel((DefaultListModel) in.readObject());
			selBoundaries.setModel((ListModel) in.readObject());
			wtmodel = (ButtonTableModel) in.readObject();
			dmodel = (ButtonTableModel) in.readObject();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public void saveProject(){
		try {
			if(project == null){
				EVProjFilter fileFilter = (EVProjFilter) fp.getFileFilter();
				String extension = fileFilter.getExtension();
				String absolutePath = fp.getSelectedFile().getAbsolutePath();
				if(!absolutePath.contains(extension))
					absolutePath += "."+extension;
				project = absolutePath;
			}
			
			FileOutputStream fos = new FileOutputStream(project);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(project);
			out.writeObject(signalFiles);
			out.writeObject(signalList.getModel());
			out.writeObject(selWavelets.getModel());
			out.writeObject(level);
			out.writeObject(descriptors.getModel());
			out.writeObject(selDescriptors.getModel());
			out.writeObject(boundaries.getModel());
			out.writeObject(selBoundaries.getModel());
			out.writeObject(wtmodel);
			out.writeObject(dmodel);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAsProject(String project){		
		this.project = project;
		saveProject();
	}

	private void addDescriptors() {
		
		DefaultListModel model = (DefaultListModel) descriptors.getModel();
		
		Method[] methods = MathV.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if(Modifier.isPublic(methods[i].getModifiers())){
				if(methods[i].getName().equals("main")) continue;
				model.addElement(methods[i].getName());
			}
		}		
	}

	private void addWavelets() {
		
		List<Class<?>> classes;
		try {
			classes = ClassFinder.getClasses("cs.jwave.handlers.wavelets");

			for(Class<?> c : classes){
				if(c.getSimpleName().toLowerCase().equals("wavelet")) continue;
				wavelets.put(c.getSimpleName(), (Wavelet)c.newInstance());
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}		
	}

	public void addSignal() {
        
        //Show FileChooser
        int returnVal = fc.showOpenDialog(null);
        DefaultListModel model = (DefaultListModel) signalList.getModel();

		// Process the results.
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File[] files = fc.getSelectedFiles();
			for (int i = 0, n = files.length; i < n; i++) {
				
				String tmp = files[i].getName();
				if (signalFiles.containsKey(tmp)) {
					tmp = errorFileAlreadyInDB(tmp);
					if (tmp == null) continue;
				}
				model.insertElementAt(tmp, 0);
				signalFiles.put(tmp, files[i]);
			}
		}
	}

	private String errorFileAlreadyInDB(String fileName) {
		
		String message = "File "+fileName+" already in DB!\n changing name to " + fileName + "_1";
		int confirm = JOptionPane.showConfirmDialog(null, message, "Duplicate Name", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(confirm == JOptionPane.OK_OPTION)
			return fileName+"_1";
		else
			return null;
	}

	public void removeSignal() {
		Object[] values = signalList.getSelectedValues();
		DefaultListModel model = (DefaultListModel) signalList.getModel();
		for (int i = 0; i < values.length; i++) {
			signalFiles.remove(values[i].toString());
			model.removeElement(values[i].toString());	
		}		
	}

	public void removeAllSignals() {
		signalList.setSelectionInterval(0, signalList.getModel().getSize()-1);
		removeSignal();
	}

	public AbstractReader getReader(String path) {		
		return AbstractReader.factory(signalFiles.get(path));
	}

	public AbstractReader getWaveletReader(String waveletName) {
		return new WaveletReader(getWavelet(waveletName));
	}
	
	public List<String> getWaveletNames(){		
		List<String> list = new ArrayList<String>();	
		for(String key : wavelets.keySet())
			list.add(key);	
		return list; 
	}
	
	public Wavelet getWavelet(String waveletName){
		return wavelets.get(waveletName);
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public List<String> getSelWavelets() {
		return convertModelToList(selWavelets.getModel());
	}
	
	private List<String> convertModelToList(ListModel model){
		DefaultListModel tmp = (DefaultListModel) model;
		List<String> list = new ArrayList<String>();
		for(int i=0, d=tmp.getSize(); i<d; i++)
			list.add((String) tmp.get(i));
		return list;
	}

	public HashMap<String, File> getSignalFiles() {
		return signalFiles;
	}
	
	public DefaultListModel getSelWaveletsModel(){
		return (DefaultListModel) selWavelets.getModel();
	}

	public ButtonTableModel resetWaveletTableModel() {
		
		int rows = selWavelets.getModel().getSize()*selBoundaries.getModel().getSize();
		int columns = signalFiles.size();
		wtmodel = new ButtonTableModel(rows, columns);
		
		int i = 0;
		for(String columnName : new TreeSet<String>(signalFiles.keySet())){
			wtmodel.setColumnTitel(i++, columnName);
		}		
		return wtmodel;
	}
	
	public ButtonTableModel getWaveletTableModel(){
		return wtmodel;
	}

	public void setPath(String path) {
		fc.setCurrentDirectory(new File(path));		
	}
	
	public String getPath(){
		return fc.getCurrentDirectory().getAbsolutePath();
	}

	public List<String> getSelDescriptors() {
		return convertModelToList(selDescriptors.getModel());
	}

	public ButtonTableModel getDescriptorTableModel() {
		return dmodel;
	}
	
	public ListModel getDescriptorModel(){
		return descriptors.getModel();
	}
	
	public ListModel getSelectedDescriptorModel(){
		return selDescriptors.getModel();
	}

	public ButtonTableModel resetDescriptorTableModel() {
		
		int rows = selDescriptors.getModel().getSize();
		int columns = signalFiles.size();
		dmodel = new ButtonTableModel(rows, columns);
		
		int i = 0;
		for(String columnName : new TreeSet<String>(signalFiles.keySet())){
			dmodel.setColumnTitel(i++, columnName);
		}		
		return dmodel;
	}

	public void viewSignal() {
		Object[] signals = signalList.getSelectedValues();
		for (int i = 0; i < signals.length; i++) 
			getReader((String) signals[i]).view();		
	}

	public JList resetSignalList(JList itemList) {
		itemList.setModel(new DefaultListModel());
		signalList = itemList;
		return signalList;
	}

	public ListModel getBoundaryModel() {
		return boundaries.getModel();
	}

	public ListModel getSelectedBoundaryModel() {
		return selBoundaries.getModel();
	}

	public List<String> getSelBoundaries() {
		return convertModelToList(selBoundaries.getModel());
	}
	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public int getProjectFile() {
		return fp.showOpenDialog(null);
	}

	public int showSaveProject() {	
		if(project == null){
			Format formatter = new SimpleDateFormat("yyMMddHHmm");
			EVProjFilter evProjFilter = new EVProjFilter();
			File file = new File(formatter.format(new Date())+"."+evProjFilter.getExtension());
			fp.setSelectedFile(file);
		}
		return fp.showSaveDialog(null);
	}
	
	public DefaultListModel getSignalListModel(){
		return (DefaultListModel) signalList.getModel();
	}

	public void exportToExcel(File currentDirectory) {
		
		JOptionPane.showMessageDialog(null, "Iniciando processo de exportação para formato Excel...");
		
		/*
		 * Vamos criar aqui 4 diretórios e em cada diretório teremos um conjunto de planilhas
		 * onde será possível fazer gráficos com os dados calculados a partir deste programa.
		 * dentro do diretório selecionado, será criado uma pasta com o titulo:
		 * EVApp_Export_Data
		 */	
		
		FileOutputStream stream;
		String slash = System.getProperty("file.separator");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd");
		String base = "EVApp_Export_"+simpleDateFormat.format(new Date(System.currentTimeMillis()));
		
		
		/*
		 * Primeiro Diretório - EVApp_export_data/descritores
		 * Cada arquivo corresponde a um descritor
		 * cada planilha corresponde a um sinal
		 * em cada planilha temos as linhas com decomposições para o sinal da planilha
		 * e colunas para cada tipo de borda de convolução 
		 */

		// captura cada descritor
		for(int i=0;i<dmodel.getRowCount();i++){
			
			HSSFWorkbook wb = new HSSFWorkbook();
			String fileName = "";
			
			fileName = ((DescriptorButton)dmodel.getValueAt(i, 1)).getText();
			
			// separa por sinais, cada sinal é uma planilha
			for(int j=0;j<dmodel.getColumnCount();j++){
				
				// cria planilha com o nome do sinal
				HSSFSheet sheet = wb.createSheet(dmodel.getColumnName(j));
				
				// cria header da planilha
				HSSFRow row0 = sheet.createRow(0);
				
				// pega o resultado do descritor especifico do sinal especifico
				DescriptorButton db = (DescriptorButton) dmodel.getValueAt(i, j+1);
				DefaultCategoryDataset[] result = db.getResult();
				sheet.autoSizeColumn(0);
				
				// separa cada nivel de detalhe
				for(int k=result.length;k>0;k--){
					
					DefaultCategoryDataset vector = result[k-1];
					row0.createCell(result.length-k+1).setCellValue("D"+k);
					HSSFRow row;
					
					// insere os valores nas celulas da planilha
					for(int y=0;y<vector.getColumnCount();y++){

						if(k==result.length){
							row = sheet.createRow(sheet.getLastRowNum()+1);
							row.createCell(0).setCellValue((String)vector.getColumnKey(y));
						}
						row = sheet.getRow(y+1);
						row.createCell(result.length-k+1).setCellValue((Double)vector.getValue(0, y));
					}
					
/*					HSSFRow row = sheet.createRow(sheet.getLastRowNum()+1);
					row.createCell(0).setCellValue("D"+k);
					DefaultCategoryDataset vector = result[k-1];
					
					// insere os valores nas celulas da planilha
					for(int y=0;y<vector.getColumnCount();y++){
						if(k==1){
							row0.createCell(y+1).setCellValue((String)vector.getColumnKey(y));
							sheet.autoSizeColumn(y);
						}
						row.createCell(y+1).setCellValue((Double)vector.getValue(0, y));
					}
*/				}
				sheet.autoSizeColumn(0);
			}
		
			// gera o arquivo
			try {
				File file = new File(currentDirectory.getAbsolutePath()+slash+base+slash+"descritores");
				file.mkdirs();
				stream = new FileOutputStream(file+slash+fileName+".xls");
				wb.write(stream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * Segundo Diretório - EVApp_export_data/wavelets
		 * Cada arquivo corresponde a uma wavelet com um tipo borda
		 * cada planilha corresponde a um descritor
		 * em cada planilha temos as linhas com decomposições para o sinal da planilha
		 * e colunas para cada sinal
		 */

		// captura cada wavelet/borda
		DescriptorButton db_ = (DescriptorButton)dmodel.getValueAt(0, 1);
		DefaultCategoryDataset dcd = db_.getResult()[0];
		for(int i=0;i<dcd.getColumnCount();i++){
			
			HSSFWorkbook wb = new HSSFWorkbook();
			String fileName = "";
			
			fileName = (String)dcd.getColumnKey(i);
			
			// separa por descritor, cada descritor é uma planilha
			for(int j=0;j<dmodel.getRowCount();j++){
				
				// cria planilha com o nome do descritor
				HSSFSheet sheet;
				// cria header da planilha
				HSSFRow row0;
				
				sheet = wb.createSheet(((DescriptorButton)dmodel.getValueAt(j, 1)).getText());
				row0 = sheet.createRow(0);	
				row0.createCell(0);
				
				// loop pelos sinais pegando descritor especifico
				for(int k=0;k<dmodel.getColumnCount();k++){
					
					// pega o resultado do descritor especifico do sinal especifico
					DescriptorButton db = (DescriptorButton) dmodel.getValueAt(j, k+1);
					DefaultCategoryDataset[] result = db.getResult();				

					HSSFRow row = sheet.createRow(sheet.getLastRowNum()+1);
					row.createCell(0).setCellValue((String)dmodel.getColumnName(k));
										
					// insere os valores nas celulas da planilha
					for(int y=0;y<result.length;y++){
						
						if(k==0){
							row0.createCell((int)row0.getLastCellNum()).setCellValue("D"+(result.length-y));
						}

						DefaultCategoryDataset vector = result[result.length-y-1];
						row.createCell(y+1).setCellValue((Double)vector.getValue(0, i));
						
//						HSSFRow row;
//						if(k==0){
//							row = sheet.createRow(sheet.getLastRowNum()+1);
//							row.createCell(0).setCellValue("D"+(result.length-y));
//						}
//						else
//							row = sheet.getRow(y+1);
//						
//						if(y==0){
//							row0.createCell((int)row0.getLastCellNum()).setCellValue((String)dmodel.getColumnName(k));
//							sheet.autoSizeColumn(k);
//						}
//
//						DefaultCategoryDataset vector = result[result.length-y-1];
//						row.createCell((int)row.getLastCellNum()).setCellValue((Double)vector.getValue(0, i));
					}
				}
				sheet.autoSizeColumn(0);
			
			}
		
			// gera o arquivo
			try {
				File file = new File(currentDirectory.getAbsolutePath()+slash+base+slash+"wavelets");
				file.mkdirs();
				stream = new FileOutputStream(file+slash+fileName+".xls");
				wb.write(stream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		/*
		 * Terceiro Diretório - EVApp_export_data/sinais
		 * Cada arquivo corresponde a um sinal
		 * cada planilha corresponde a um descritor
		 * em cada planilha temos as linhas com decomposições para o sinal do arquivo
		 * e colunas para cada tipo de wavelet/borda
		 */

		// captura cada sinal
		for(int i=0;i<dmodel.getColumnCount();i++){
			
			HSSFWorkbook wb = new HSSFWorkbook();
			String fileName = "";
			
			fileName = (String)dmodel.getColumnName(i);
			
			// separa por descritor, cada descritor é uma planilha
			for(int j=0;j<dmodel.getRowCount();j++){
				
				// cria planilha com o nome do descritor
				HSSFSheet sheet;
				// cria header da planilha
				HSSFRow row0;
				
				sheet = wb.createSheet(((DescriptorButton)dmodel.getValueAt(j, 1)).getText());
				row0 = sheet.createRow(0);	
				row0.createCell(0);
				
				// pega o resultado do descritor especifico do sinal especifico
				DescriptorButton db = (DescriptorButton) dmodel.getValueAt(j, i+1);
				DefaultCategoryDataset[] result = db.getResult();				

				// loop pelas decomposições
				for(int k=0;k<result.length;k++){
					
					DefaultCategoryDataset vector = result[result.length-k-1];
					row0.createCell(k+1).setCellValue("D"+(result.length-k));
				
					// loop pelos resultados por cada decomposição
					for(int y=0;y<vector.getColumnCount();y++){

						HSSFRow row;
						if(k==0){
							row = sheet.createRow(sheet.getLastRowNum()+1);
							row.createCell(0).setCellValue((String)vector.getColumnKey(y));
						} 
						row = sheet.getRow(y+1);							
						row.createCell(k+1).setCellValue((Double)vector.getValue(0, y));
					}

//					DefaultCategoryDataset vector = result[result.length-k-1];
//					HSSFRow row = sheet.createRow(sheet.getLastRowNum()+1);
//					row.createCell(0).setCellValue("D"+(result.length-k));
//				
//					// loop pelos resultados por cada decomposição
//					for(int y=0;y<vector.getColumnCount();y++){
//						
//						if(k==0) {
//							row0.createCell((int)row0.getLastCellNum()).setCellValue((String)vector.getColumnKey(y));
//							sheet.autoSizeColumn(y);
//						}
//						row.createCell((int)row.getLastCellNum()).setCellValue((Double)vector.getValue(0, y));
//					}
				}
				sheet.autoSizeColumn(0);			
			}
		
			// gera o arquivo
			try {
				File file = new File(currentDirectory.getAbsolutePath()+slash+base+slash+"sinais");
				file.mkdirs();
				stream = new FileOutputStream(file+slash+fileName+".xls");
				wb.write(stream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		/*
		 * Quarto Diretório - EVApp_export_data/comparacao
		 * Existe apenas 1 arquivo onde é calculado o max, min e media de todos os sinais por waveletxborda
		 * cada planilha corresponde a um descritor
		 * em cada planilha temos as linhas com decomposições para todo o conjunto
		 * e colunas para cada sinal
		 */

		// calcula resultados
		
		HSSFWorkbook wb = new HSSFWorkbook();
		String fileName = "comparacao";
		
		// vamos criar planilhas para cada descritor
		for(int i=0; i<dmodel.getRowCount(); i++){
			
			// cria planilha com o nome do descritor
			HSSFSheet sheet;
			// cria header da planilha
			HSSFRow row0, row1, row;
			
			sheet = wb.createSheet(((DescriptorButton)dmodel.getValueAt(i, 1)).getText());
			row0 = sheet.createRow(0);	
			row0.createCell(0);
			row1 = sheet.createRow(1);	
			row1.createCell(0);
			
			DescriptorButton db = (DescriptorButton) dmodel.getValueAt(i, 1);
			DefaultCategoryDataset[] r = db.getResult();
			
			int decomposicoes = r.length;
			int wavelets = r[0].getColumnCount();
			int sinais = dmodel.getColumnCount();
	        double max, min, mean;
				
			// decomposicao
			for (int x = decomposicoes-1; x > -1 ; x--) {					
					
				// wavelet
				for (int y = 0; y < wavelets; y++) {
						
					max = Double.MIN_VALUE;
					min = Double.MAX_VALUE;
					mean = 0;
					String name = "";

					// loop por todos os sinais
					for (int j = 0; j < sinais; j++) {
							
						DescriptorButton d = (DescriptorButton) dmodel.getValueAt(i, j+1);
						DefaultCategoryDataset[] result = d.getResult();
						if(j==0) name = result[x].getColumnKey(y).toString();

						double yValue = result[x].getValue(0, y).doubleValue();
						if(max<yValue) max = yValue;
						if(min>yValue) min = yValue;
						mean += yValue;
					}
						
					mean /= sinais;

					int col = (decomposicoes-x-1)*3;

					if(y==0){
						CellRangeAddress region = new CellRangeAddress(0, 0, 1+col, 3+col);
						sheet.addMergedRegion(region);
						row0.createCell(region.getFirstColumn()).setCellValue(x+1);
						row1.createCell(1+col).setCellValue("max");
						row1.createCell(2+col).setCellValue("min");
						row1.createCell(3+col).setCellValue("mean");
					}
												
					if(x == decomposicoes-1){
						row = sheet.createRow(sheet.getLastRowNum()+1);
						row.createCell(0).setCellValue(name);
					}
					else row = sheet.getRow(y+2);
						
					row.createCell(1+col).setCellValue(max);
					row.createCell(2+col).setCellValue(min);
					row.createCell(3+col).setCellValue(mean);
				}	
				
			}
			sheet.autoSizeColumn(0);	
		}
		
		// gera o arquivo
		try {
			File file = new File(currentDirectory.getAbsolutePath()+slash+base);
			file.mkdirs();
			stream = new FileOutputStream(file+slash+fileName+".xls");
			wb.write(stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(null, "Exportação Finalizada");
	}

	public void exportSignals(File currentDirectory) {
		
		JOptionPane.showMessageDialog(null, "Iniciando processo de exportação para formato Excel...");

		FileOutputStream stream;
		String slash = System.getProperty("file.separator");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd");
		String fileName = "EVApp_Signal_Export_"+simpleDateFormat.format(new Date(System.currentTimeMillis()));

		HSSFWorkbook wb = new HSSFWorkbook();
		// cria planilha com o nome do sinal
		HSSFSheet sheet = wb.createSheet("Sinais");
		sheet.autoSizeColumn(0);

		// cria header da planilha
		HSSFRow row0 = sheet.createRow(0);
		HSSFRow row;
		
		if(signalFiles.size()>250){
			JOptionPane.showMessageDialog(null, "O Excel não aceita mais do que 255 colunas.\n"+
					"Serão exportados somente 250 sinais.");
		}
		
		int f = -1;
		for (File file : signalFiles.values()) {
			f++;
			if(f==251) break;
			double[] data = AbstractReader.factory(file).getArray();
			
			row0.createCell(f).setCellValue(file.getName());
		
			for (int i = 0; i < data.length; i++) {
				
				if(sheet.getLastRowNum()<(i+1)) 
					row = sheet.createRow(i+1);
				else
					row = sheet.getRow(i+1);

				row.createCell(f).setCellValue(data[i]);				
			}
		}		
		// gera o arquivo
		try {
			File file = new File(currentDirectory.getAbsolutePath());
			file.mkdirs();
			stream = new FileOutputStream(file+slash+fileName+".xls");
			wb.write(stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(null, "Exportação Concluída!");
	}
}
