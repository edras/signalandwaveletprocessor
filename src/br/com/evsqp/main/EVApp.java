/*
 * EVApp.java
 */

package br.com.evsqp.main;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Properties;

import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

import br.com.evsqp.popup.AddRemovePopup;
import br.com.evsqp.task.CheckTask;
import br.com.evsqp.task.DescriptorTask;
import br.com.evsqp.task.EVMonitor;
import br.com.evsqp.task.LDATask;
import br.com.evsqp.task.WaveletTask;
import br.com.evsqp.utils.table.ButtonTableModel;
import br.com.evsqp.utils.table.ButtonTableRenderer;
import br.com.evsqp.utils.table.DescriptorButton;
import br.com.evsqp.utils.table.WaveletButton;
import br.com.evsqp.view.AboutBox;
import br.com.evsqp.view.LogWindowForm;
import br.com.evsqp.view.TrialDescription;
import br.com.evsqp.lda.LDA;
import br.com.evsqp.lda.utils.SpreadSheetFilter;

/**
 * The main class of the application.
 */
public class EVApp extends SingleFrameApplication {

    private EVView view;
    private EVModel model;
    private LDA lda;
	private Properties config;
	private String properties = "config.properties";

    private JDialog aboutBox;
    private EVMonitor monitor;
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        
        view = new EVView(this);
        model = new EVModel(this);
        monitor = new EVMonitor(this);
        
		loadProperties();
		
        initBehavior();
        initMenu();
        initWindows();
        
        show(view);
    }
    
    private void loadProperties() {
    	
		config = new Properties();
		try {
			File file = new File(properties);
			
			if(!file.exists()) 
				file.createNewFile();
			
			config.load(new FileInputStream(properties));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(config.getProperty("path") !=null)
			model.setPath(config.getProperty("path"));
		else {
			String path = "/home/edras/Doutorado/sinais/__amostras_eeg/training";
			config.setProperty("path", path);
			model.setPath(path);
		}				
	}

	private void initBehavior() {
    	
    	final ResourceMap resourceMap = getContext().getResourceMap(EVApp.class);
    	
    	addExitListener(new ExitListener() {
            public boolean canExit(EventObject e) {
                return JOptionPane.showConfirmDialog(null,
                		resourceMap.getString("exit.text"),
                		resourceMap.getString("exit.title"),
                		JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            }
            public void willExit(EventObject event) {

            }
        });
		
	}
	
	public void showDescriptorResult(ActionEvent e) {
		DescriptorButton db = (DescriptorButton) e.getSource();
		view.showDescriptorResult(db);		
	}
    
    @Override
    protected void shutdown() {
        // The default shutdown saves session window state.
        super.shutdown();
        saveProperties();
    }


	private void saveProperties() {
		try {
			config.setProperty("path", model.getPath());
			config.store(new FileOutputStream(properties), "Properties of Doctor Program");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}

	private void initWindows() {
		initCalcButtons();
		initSignalWindow();
		initWaveletWindow();			
		initBorderWindow();
		initDescriptorWindow();
		initLDAWindow();
	}
    
    private void initLDAWindow() {
		
    	lda = new LDA();
    	lda.setEcho(false);
    	
    	view.getLoadedClasses().setModel(new DefaultListModel<String>());
    	view.getLoadedClasses().setPrototypeCellValue("AAAAAAAA");
    	
    	view.getLoadedValidation().setModel(new DefaultListModel<String>());
    	view.getLoadedValidation().setPrototypeCellValue("AAAAAAAA");
    	
    	view.getSelectedWaveletLDA().setModel(new DefaultListModel<String>());
    	view.getSelectedWaveletLDA().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				calcLdaPossibilities();				
			}
		});
    	
    	view.getSelectedDescriptorLDA().setModel(new DefaultListModel<String>());
    	view.getSelectedDescriptorLDA().setPrototypeCellValue("AAAAAAAA");
    	view.getSelectedDescriptorLDA().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				calcLdaPossibilities();				
			}
		});
    	
    	view.getSelectedLevelsLDA().setModel(new DefaultListModel<String>());
    	view.getSelectedLevelsLDA().setPrototypeCellValue("AAAAAAAA");
    	view.getSelectedLevelsLDA().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				calcLdaPossibilities();				
			}
		});
    	
    	view.getLDAButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long time = System.currentTimeMillis();
				try {
					if(calcLdaPossibilities()==0){
						lda.restoreClasses();
						lda.resetLDA();
						lda.calcLdaProj();
						view.getWeightsButton().setEnabled(true);
						view.getDistributionButton().setEnabled(true);
					} else {
						if(lda.isDir()){
							BigInteger ldaFilter = getLDAFilter();
							lda.setShowGraphs(false);
							lda.resetDir();
							System.out.println("Freq; AUC; Sensibilidade; Especificidade;");
							while(lda.loadNextGroup()){
								lda.filterClasses(ldaFilter);
								lda.resetLDA();
								lda.calcLdaProj();
								lda.printROCResult();
							}
							lda.resetDir();
							lda.loadNextGroup();
							lda.setShowGraphs(true);
							return;
						} else {
							lda.filterClasses(getLDAFilter());
							lda.resetLDA();
							lda.calcLdaProj();
							view.getWeightsButton().setEnabled(true);
							view.getDistributionButton().setEnabled(true);
							time = System.currentTimeMillis()-time;
							view.getLabelCombinacoes().setText(Long.toString(time)+" ms");
						}
					}
					
					if(view.getLoadedClasses().getModel().getSize()==2) {
						view.getROCButton().setEnabled(true);
						writeROCvalue();
					}
					else {
						view.getROCButton().setEnabled(false);
					}
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
    	
    	view.getDistributionButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lda.showInput();
				lda.showOutput();				
			}
		});
    	
    	view.getROCButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lda.setShowGraphs(true);
				lda.calcROC();
				lda.setShowGraphs(false);
			}
		});
    	
    	view.getWeightsButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String content = lda.getWtoPrint();
				
				LogWindowForm logWindowForm = new LogWindowForm();
				logWindowForm.setText(content);
				logWindowForm.setTitle("Matrix Weights");
				logWindowForm.setVisible(true);
			}
		});

    	view.getTestButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(view.getLoadedClasses().getModel().getSize()>2){
					JOptionPane.showMessageDialog(null, "Só é possível simular possibilidades para 2 classes!" +
							"\nO algorítmo não será executado.");
					return;
				}
				
				monitor.setLdaTask(true);
				monitor.start();
			}
		});
    	
    	   	
    	view.getTestFiedl().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				TrialDescription trialDescription = new TrialDescription(view.getSelectedWaveletLDA(), 
						view.getSelectedDescriptorLDA(), 
						view.getSelectedLevelsLDA());
				
				trialDescription.getTesteNumber().setText(view.getTestField().getText());				
				trialDescription.setVisible(true);			
			}
		});
    	
    	view.getValidationButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(calcLdaPossibilities()==0){
					lda.restoreValidation();
					lda.calcValidation(BigInteger.valueOf(-1));
				} else {
					String tmp = view.getTestField().getText();
					double testTrial = -1;
					BigInteger big = BigInteger.ZERO;
					if(tmp != null){					
						if(!tmp.isEmpty()){
							try{
								testTrial = (int)Double.parseDouble(tmp);
								double calcLdaPossibilities = calcLdaPossibilities();
								if(testTrial<0 || testTrial>=calcLdaPossibilities){
									JOptionPane.showMessageDialog(null, "Número de teste deve ser um número inteiro entre 0 e "+(calcLdaPossibilities-1));
									return;
								}
								big = new BigInteger(String.valueOf(testTrial));
							}
							catch (Exception e2) { 
								JOptionPane.showMessageDialog(null, "Número de teste inválido");
								return;
							}
						}
						lda.restoreValidation();
						lda.filterClasses(getLDAFilter());
						view.getROCButton().doClick();
						lda.calcValidation(big);
					}
				}
			}
		});
    	
    	view.getLDAButton().setEnabled(false);
    	view.getValidationButton().setEnabled(false);
    	view.getDistributionButton().setEnabled(false);
    	view.getROCButton().setEnabled(false);
    	view.getWeightsButton().setEnabled(false);
    	view.getTestButton().setEnabled(false);
    	view.getTestField().setEnabled(false);
    	
    	initLdaPopup();
    	initLdaValidatePopup();		
    	
	}
    
	public LDA getLda(){
    	return lda;
    }
    
    protected void writeROCvalue() {
		double calcROC = lda.calcROC();
		NumberFormat formatter;
        formatter = new DecimalFormat("0.0000");
        view.getROCButton().setText("ROC = "+formatter.format(calcROC));		
	}

	public BigInteger getLDAFilter() {
    	
    	int   levels			= view.getSelectedLevelsLDA().getModel().getSize();
    	int[] selectedLevels    = view.getSelectedLevelsLDA().getSelectedIndices();

    	int   descriptor		= view.getSelectedDescriptorLDA().getModel().getSize();
    	int[] selectedDesc      = view.getSelectedDescriptorLDA().getSelectedIndices();

    	int   wavelets			= view.getSelectedWaveletLDA().getModel().getSize();
    	int[] selectedWavelets  = view.getSelectedWaveletLDA().getSelectedIndices();

    	BigInteger filter = BigInteger.ZERO;
    	int bitNumber = 0;
    	
    	for (int i = 0; i < selectedWavelets.length; i++) {
			for (int j = 0; j < selectedDesc.length; j++) {
				for (int k = 0; k < selectedLevels.length; k++) {
					bitNumber = selectedLevels[k] + 
								(descriptor-selectedDesc[j]-1)*levels + 
								(wavelets-selectedWavelets[i]-1)*descriptor*levels;
					filter = filter.setBit(bitNumber);
				}
			}
		}
    	
		return filter;
	}

	public double calcLdaPossibilities() {
    	int x,y,z;
    	double combinacoes = 0;
    	x = view.getSelectedWaveletLDA().getSelectedIndices().length;
    	y = view.getSelectedLevelsLDA().getSelectedIndices().length;
    	z = view.getSelectedDescriptorLDA().getSelectedIndices().length;
    	if(x == 0 || y == 0 || z == 0){
    		view.getLabelCombinacoes().setText("0");
    	} else {
    		combinacoes = Math.pow(2, x*y*z)-1;
    		
    		String r = new DecimalFormat("##0E0").format(combinacoes);
    		view.getLabelCombinacoes().setText("2^"+(x*y*z)+" = "+r);
    	}
    	
    	if(combinacoes>1){
    		view.getTestButton().setEnabled(true);
    		view.getTestField().setEnabled(true);
    	}
    	else {
    		view.getTestButton().setEnabled(false);
    		view.getTestField().setEnabled(false);
    	}
    	return combinacoes;
    	
	}
    
    private void initLdaValidatePopup(){
    	
		AddRemovePopup popup = new AddRemovePopup(this);
		popup.setList(view.getLoadedValidation());
		view.getLoadedValidation().addMouseListener(popup);
					
		popup.getAddMenuItem().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DefaultListModel<String> model = (DefaultListModel<String>)view.getLoadedValidation().getModel();
				int number = model.getSize()+1;
				
				int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Deseja carregar arquivos (SIM) ou selecionar de uma classe (NAO)","De onde carregar?",JOptionPane.YES_NO_OPTION);
				if(showConfirmDialog == JOptionPane.YES_OPTION){
					File[] files = lda.loadSpreadSheetDOCValidation(number);
					// se carregou arquivos insere na HMI
					if(files != null){
						model.addElement("Class " + number);
						view.getValidationButton().setEnabled(true);
					}
				}
				else {
					String showInputDialog = JOptionPane.showInputDialog(null, "Digite o nr. da classe da qual serão transferidas as amostras:");
					if(showInputDialog != null){
						if(showInputDialog.isEmpty()) return;
						try{
							lda.restoreClasses();
							lda.restoreValidation();
							
							int val1 = lda.getNumberOfValidationClasses();
							lda.loadValidationElementsFromClass(Integer.parseInt(showInputDialog));
							int val2 = lda.getNumberOfValidationClasses();
							if(val2>val1){
								model.addElement("Class " + val2);
								view.getValidationButton().setEnabled(true);
							}
							
						} catch(Exception e){
							//e.printStackTrace();
							JOptionPane.showMessageDialog(null, "Numero inválido","Erro",JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}				
			}
		});
		
		popup.getRemoveSelectedMenuItem().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeLDAValidationClass();	
			}
		});
		
		popup.getRemoveAllMenuItem().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JList<String> list = (JList<String>)view.getLoadedValidation();
				DefaultListModel<String> model = (DefaultListModel<String>)list.getModel();
				list.setSelectionInterval(0, model.getSize()-1);
				removeLDAValidationClass();
			}
		});
		
		popup.getViewMenuItem().setVisible(false);
		popup.getExportMenuItem().setVisible(false);
    }
    
	private void initLdaPopup() {
    	
		AddRemovePopup popup = new AddRemovePopup(this);
		popup.setList(view.getLoadedClasses());
		view.getLoadedClasses().addMouseListener(popup);
					
		popup.getAddMenuItem().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DefaultListModel<String> model = (DefaultListModel<String>)view.getLoadedClasses().getModel();
				int number = model.getSize()+1;
				File[] files = lda.loadSpreadSheetDOC(number);
				
				// se carregou arquivos insere na HMI
				if(files != null){
					model.addElement("Class " + number);
					
					// se é o primeiro carregamento, carrega nome de variáveis
					if(number == 1){
						loadLDAWavelets(files);
						loadLDADetails(files);
					}		
					if(number > 1){
						view.getLDAButton().setEnabled(true);
						if(number == 2){
							view.getDistributionButton().setEnabled(true);
							view.getROCButton().setEnabled(true);
						} else {
							view.getDistributionButton().setEnabled(false);
							view.getROCButton().setEnabled(false);
						}
					}
					lda.saveClasses();
				}
			}
		});
		
		popup.getRemoveSelectedMenuItem().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeLDAClass();	
			}
		});
		
		popup.getRemoveAllMenuItem().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				while(lda.getNrClasses()!=0){
					JList<String> list = (JList<String>)view.getLoadedClasses();				
					list.setSelectionInterval(0, list.getModel().getSize()-1);
					removeLDAClass();
				}
			}
		});
		
		popup.getLoadLDAMenuItem().setVisible(true);
		popup.getLoadLDAMenuItem().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultListModel<String> model = (DefaultListModel<String>)view.getLoadedClasses().getModel();
				JOptionPane.showMessageDialog(null, "Selecione um diretório de exportação com duas classes distintas.");
				boolean ok = false;
				
				JFileChooser fc = new JFileChooser();
		        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        
				while(!ok){
					
					int showOpenDialog = fc.showOpenDialog(null);
					if(showOpenDialog == JFileChooser.CANCEL_OPTION) return;
					if(showOpenDialog == JFileChooser.APPROVE_OPTION)
					{
						lda.setFolder(fc.getSelectedFile());
						lda.setDir(true);
						
						lda.loadNextGroup();
						for (int i = 0; i < lda.getNrClasses(); i++) 
							model.addElement("Class " + i);

						File[] files = lda.getClassPath();
						loadLDAWavelets(files);
						loadLDADetails(files);
						
						view.getLDAButton().setEnabled(true);
						view.getDistributionButton().setEnabled(true);
						view.getROCButton().setEnabled(true);
						
						ok = true;
					}					
				}				
			}
		});
		
		popup.getViewMenuItem().setVisible(false);
		popup.getExportMenuItem().setVisible(false);		
	}
	
	protected void removeLDAValidationClass() {
		JList<String> list = (JList<String>)view.getLoadedValidation();
		DefaultListModel<String> model = (DefaultListModel<String>)list.getModel();
		List<String> values = list.getSelectedValuesList();
		for (int i = 0; i < values.size(); i++) {
			lda.removeValidationClass(model.indexOf(values.get(i)));
			model.removeElement(values.get(i));	
		}
		if(model.size()==0) view.getValidationButton().setEnabled(false);
	}

	protected void removeLDAClass() {
		JList<String> list = (JList<String>)view.getLoadedClasses();
		DefaultListModel<String> model = (DefaultListModel<String>)list.getModel();
		List<String> values = list.getSelectedValuesList();
		for (int i = 0; i < values.size(); i++) {
			lda.removeClass(model.indexOf(values.get(i)));
			model.removeElement(values.get(i));	
		}
		if(model.getSize()<2){
			view.getLDAButton().setEnabled(false);
			view.getWeightsButton().setEnabled(false);
			view.getTestButton().setEnabled(false);
			view.getTestField().setEnabled(false);
		}
		if(model.getSize()==0){
			removeItemsFromList(view.getSelectedWaveletLDA());
			removeItemsFromList(view.getSelectedLevelsLDA());
			removeItemsFromList(view.getSelectedDescriptorLDA());
		}
		if(model.getSize()==2){
			view.getDistributionButton().setEnabled(true);
			view.getROCButton().setEnabled(true);
		} else {
			view.getDistributionButton().setEnabled(false);
			view.getROCButton().setEnabled(false);
		}
		lda.saveClasses();
	}

	private void removeItemsFromList(JList<String> list) {
		DefaultListModel<String> model = (DefaultListModel<String>)list.getModel();
		list.setSelectionInterval(0, model.getSize()-1);
		
		List<String> values = list.getSelectedValuesList();
		for (int i = 0; i < values.size(); i++) {
			model.removeElement(values.get(i));	
		}
	}

	protected void loadLDADetails(File[] files) {
		FileInputStream fis;
		HSSFWorkbook wb = null;
		
		try {
			fis = new FileInputStream(files[0]);
			wb = new HSSFWorkbook(fis);
		
			int sheets = wb.getNumberOfSheets();

			DefaultListModel<String> model = (DefaultListModel<String>)view.getSelectedDescriptorLDA().getModel();
			String name;
			for (int i = 0; i < sheets; i++) {
				name = wb.getSheetAt(i).getSheetName();
				model.addElement(name);
			}

			HSSFSheet sheet = wb.getSheetAt(0);
			int columns = sheet.getRow(0).getLastCellNum()-1;
			model = (DefaultListModel<String>)view.getSelectedLevelsLDA().getModel();
			for (int i = 0; i < columns; i++) {
				model.addElement(Integer.toString(i+1));
			}
			
			fis.close();
		
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} // TODO Auto-generated method stub
		
	}

	protected void loadLDAWavelets(File[] files) {
		DefaultListModel<String> model = (DefaultListModel<String>)view.getSelectedWaveletLDA().getModel();
		String name;
		for (int i = 0; i < files.length; i++) {
			name = files[i].getName().substring(0, files[i].getName().indexOf('_'));
			model.addElement(name);
		}		
	}

	private void initCalcButtons() {
    	
    	monitor.add(CheckTask.class);
    	monitor.add(WaveletTask.class);
    	monitor.add(DescriptorTask.class); 
    	monitor.add(LDATask.class);
    	
		view.getStartButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				monitor.start();
			}
		});	
		
		view.getPauseButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				monitor.pause();
			}
		});
		
		view.getStopButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				monitor.stop();
			}
		});
		
		view.getStartButton().setEnabled(true);
		view.getPauseButton().setEnabled(false);
		view.getStopButton().setEnabled(false);
	}

	private void initDescriptorWindow() {
		addDescriptors();
		initDescriptorButtons();		
	}

	private void initDescriptorButtons() {
		
		view.initDescriptorTable();
		view.getSelectAllButton2().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addAllItems(view.getItemList2(), view.getSelectedItemList2());
			}
		});
		view.getAddSelectedButton2().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addSelectedItem(view.getItemList2(), view.getSelectedItemList2());
			}
		});
		view.getRemoveSelectedButton2().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeSelectedItems(view.getItemList2(), view.getSelectedItemList2());
			}
		});
		view.getRemoveAllButton2().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeAllItems(view.getItemList2(), view.getSelectedItemList2());
			}
		});		
	}

	private void addDescriptors() {
		syncModelView(
				view.getItemList2(),
				model.getDescriptorModel(),
				view.getSelectedItemList2(), 
				model.getSelectedDescriptorModel());
	}
	
	private void syncModelView(JList list1, ListModel model1, JList list2, ListModel model2){
		sortModel((DefaultListModel) model1);
		list1.setModel(model1);
		list2.setModel(model2);
	}

	private void initBorderWindow() {
		addBoundaries();
		initBoundaryButtons();		
	}

	private void initBoundaryButtons() {
		
		view.getSelectAllButton1().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addAllItems(view.getItemList1(), view.getSelectedItemList1());
			}
		});
		view.getAddSelectedButton1().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addSelectedItem(view.getItemList1(), view.getSelectedItemList1());
			}
		});
		view.getRemoveSelectedButton1().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeSelectedItems(view.getItemList1(), view.getSelectedItemList1());
			}
		});
		view.getRemoveAllButton1().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeAllItems(view.getItemList1(), view.getSelectedItemList1());
			}
		});		
	}

	private void addBoundaries() {
		syncModelView(
				view.getItemList1(), 
				model.getBoundaryModel(),
				view.getSelectedItemList1(),
				model.getSelectedBoundaryModel());
	}

	private void initWaveletWindow() {
		addKnownWavelets();
		initWaveletButtons();		
	}

	private void initWaveletButtons() {
		
		view.initWaveletTable();
		
		JComboBox levelsCombo = view.getLevelsCombo();
		levelsCombo.removeAllItems();
		for(int i=1;i<11;i++)
			levelsCombo.addItem(i);
		
		view.getAddSelectedButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { 
				addSelectedItem(view.getItemList(),view.getSelectedItemList());
			}
		});
		view.getSelectAllButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addAllItems(view.getItemList(),view.getSelectedItemList());				
			}
		});
		view.getRemoveSelectedButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelectedItems(view.getItemList(),view.getSelectedItemList());
			}
		});
		view.getRemoveAllButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeAllItems(view.getItemList(),view.getSelectedItemList());
			}
		});
		view.getViewWaveletButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewWavelet();
			}
		});
		view.getLevelsCombo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeWaveletLevels();
			}
		});
		
		levelsCombo.setSelectedItem(3);

	}
	
	protected void changeWaveletLevels() {
		model.setLevel(view.getLevelsCombo().getSelectedIndex()+1);
	}

	protected void viewWavelet() {
		JList list = view.getItemList();
		Object[] values = list.getSelectedValuesList().toArray();
		for (int i = 0; i < values.length; i++) {
			model.getWaveletReader(values[i].toString()).view();
		}				
	}

	protected void removeAllItems(JList iList, JList siList) {
		
		DefaultListModel siModel = (DefaultListModel) siList.getModel();
		siList.setSelectionInterval(0, siModel.getSize()-1);
		removeSelectedItems(iList, siList);
	}

	protected void removeSelectedItems(JList iList, JList siList) {
		
		DefaultListModel siModel = (DefaultListModel) siList.getModel();
		Object[] values = siList.getSelectedValuesList().toArray();
		DefaultListModel iModel = (DefaultListModel) iList.getModel();
		
		for (int i = 0; i < values.length; i++) {
			siModel.removeElement(values[i]);
			iModel.addElement(values[i]);
		}	
		sortModel(iModel);
	}

	protected void addAllItems(JList iList, JList siList) {
		
		DefaultListModel iModel  = (DefaultListModel) iList.getModel();
		DefaultListModel siModel = (DefaultListModel) siList.getModel();
		
		int n = iModel.getSize();
		for (int i = 0; i < n; i++) {
			Object element = iModel.getElementAt(0);
			iModel.remove(0);
			siModel.addElement(element);
		}			
		sortModel(siModel);
	}

	private void sortModel(DefaultListModel model) {
		Object[] contents = model.toArray();  
		Arrays.sort(contents);  
		model.removeAllElements();
		for (int i = 0; i < contents.length; i++) {
			model.addElement(contents[i]);
		}
	}

	private void addSelectedItem(JList iList, JList siList) {
	
		DefaultListModel iModel = (DefaultListModel) iList.getModel();
		DefaultListModel siModel = (DefaultListModel) siList.getModel();
		Object[] values = iList.getSelectedValues();
				
		for(int i = 0; i < values.length; i++) {
			iModel.removeElement(values[i]);
			siModel.addElement(values[i]);
		}			
		sortModel(siModel); 		
	}
	
	private void addKnownWavelets() {

		DefaultListModel modelList = new DefaultListModel();
		List<String> waveletNames = model.getWaveletNames();
		for(String wavelet : waveletNames)
			modelList.addElement(wavelet);
		
		syncModelView(view.getItemList(), modelList, 
				view.getSelectedItemList(), model.getSelWaveletsModel());
	}

	public EVModel getModel(){
    	return model;
    }
    
    public EVView getView(){
    	return view;
    }

	private void initSignalWindow() {
		
		JList<?> signalList = model.resetSignalList(view.getItemList3());
		AddRemovePopup popup = new AddRemovePopup(this);
		signalList.addMouseListener(popup);
					
		popup.getAddMenuItem().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.addSignal();				
			}
		});
		
		popup.getRemoveSelectedMenuItem().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.removeSignal();				
			}
		});
		
		popup.getRemoveAllMenuItem().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.removeAllSignals();				
			}
		});
		
		popup.getViewMenuItem().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				model.viewSignal();				
			}
		});
		popup.getExportMenuItem().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
		    	JFileChooser fc = new JFileChooser();
		    	fc.setCurrentDirectory(new File(System.getProperty("user.home")));
		    	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    	fc.setVisible(true);
		    	
		    	int returnVal = fc.showOpenDialog(null);
		    	if(returnVal == JFileChooser.APPROVE_OPTION){
		        	model.exportSignals(fc.getSelectedFile());				
		    	}
			}
		});
	}
		
	@Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = getMainFrame();
            aboutBox = new AboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        show(aboutBox);
    }
	
	@Action
    public void showWaveletResult() {
        show(view.getWaveletForm());
    }
	
	@Action
    public void showDescriptorResult() {
        show(view.getDescriptorForm());
    }	
	
	@Action
	public void newProject(){
		monitor.stop();
		model.removeAllSignals();
		removeAllItems(view.getItemList(), view.getSelectedItemList());
		removeAllItems(view.getItemList1(),view.getSelectedItemList1());
		removeAllItems(view.getItemList2(),view.getSelectedItemList2());
		view.getLevelsCombo().setSelectedItem(3);
		resetTable(view.getWaveletResultTable(),model.resetWaveletTableModel());
		resetTable(view.getDescriptorResultTable(), model.resetDescriptorTableModel());
	}

	private void syncViewModel() {
		// Sync Selected Wavelets
		DefaultListModel sModel = model.getSelWaveletsModel();
		view.getSelectedItemList().setModel(sModel);
		DefaultListModel iModel = (DefaultListModel) view.getItemList().getModel();
		for (int i = 0; i < sModel.getSize(); i++)
			iModel.removeElement(sModel.get(i));
		view.getLevelsCombo().setSelectedItem(model.getLevel());
		
		// Sync Boundaries
		view.getItemList1().setModel(model.getBoundaryModel());
		view.getSelectedItemList1().setModel(model.getSelectedBoundaryModel());

		// Sync Descriptors
		view.getItemList2().setModel(model.getDescriptorModel());
		view.getSelectedItemList2().setModel(model.getSelectedDescriptorModel());
		
		// Sync Signals
		view.getItemList3().setModel(model.getSignalListModel());
		
		// Sync wavelet result
		resetTable(view.getWaveletResultTable(), model.getWaveletTableModel());
		
		// Sync descriptor result
		resetTable(view.getDescriptorResultTable(), model.getDescriptorTableModel());
	}

	@Action
	public void loadProject(){		
        int returnVal = model.getProjectFile();        
		// Process the result.
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			model.loadProject();
			syncViewModel();
			syncActionListeners();
		}
		
	}

	private void syncActionListeners() {
		
		ButtonTableModel wModel = model.getWaveletTableModel();
		for (int i = 0, n = wModel.getRowCount(); i < n; i++) {
			for (int j = 1, m = wModel.getColumnCount()+1; j < m; j++) {
				((WaveletButton)wModel.getValueAt(i,j)).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showWaveletResult(e);
					}
				});
			}
		}
		
		ButtonTableModel dModel = model.getDescriptorTableModel();
		for (int i = 0, n = dModel.getRowCount(); i < n; i++) {
			for (int j = 1, m = dModel.getColumnCount()+1; j < m; j++) {
				((DescriptorButton)dModel.getValueAt(i,j)).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showDescriptorResult(e);
					}
				});
			}
		}
	}

	@Action
	public void saveProject(){		
		if(model.getProject() == null)
			saveAsProject();
		else
			model.saveProject();			
	}

	@Action
	public void saveAsProject(){		
        int returnVal = model.showSaveProject();        
		// Process the result.
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			model.saveProject();
		}		
	}

	
	/**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of EVApp
     */
    public static EVApp getApplication() {
        return Application.getInstance(EVApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(EVApp.class, args);
    }

    private void initMenu() {
        
        ActionMap actionMap = getContext().getActionMap(EVApp.class, this);
        ResourceMap resourceMap = getContext().getResourceMap(EVView.class);
        
        view.getAboutMenuItem().setAction(actionMap.get("showAboutBox")); // NOI18N
        view.getAboutMenuItem().setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N    }
        
        view.getWaveletResultItemMenu().setAction(actionMap.get("showWaveletResult"));
        view.getWaveletResultItemMenu().setText(resourceMap.getString("waveletResultItemMenu.text")); // NOI18N    }

        view.getDescriptorResultItemMenu().setAction(actionMap.get("showDescriptorResult"));
        view.getDescriptorResultItemMenu().setText(resourceMap.getString("descriptorResultItemMenu.text")); // NOI18N    }
        
        view.getDescriptorPlotResult().setAction(actionMap.get("showDescriptorPlotResult"));
        view.getDescriptorPlotResult().setText(resourceMap.getString("descriptorPlotResultItemMenu.text")); // NOI18N    }
        
        view.getNewItemMenu().setAction(actionMap.get("newProject"));
        view.getNewItemMenu().setText(resourceMap.getString("newItemMenu.text")); // NOI18N    }

        view.getLoadItemMenu().setAction(actionMap.get("loadProject"));
        view.getLoadItemMenu().setText(resourceMap.getString("loadItemMenu.text")); // NOI18N    }

        view.getSaveItemMenu().setAction(actionMap.get("saveProject"));
        view.getSaveItemMenu().setText(resourceMap.getString("saveItemMenu.text")); // NOI18N    }

        view.getSaveAsMenuItem().setAction(actionMap.get("saveAsProject"));
        view.getSaveAsMenuItem().setText(resourceMap.getString("saveAsMenuItem.text")); // NOI18N    }
        
        view.getExpDescResultMenuItem().setAction(actionMap.get("exportDescResult"));
        view.getExpDescResultMenuItem().setText(resourceMap.getString("expDescResultMenuItem.text"));
    }
    
    @Action
    public void showDescriptorPlotResult() {
		view.showDescriptorPlotResult();
	}
    
    @Action
    public void exportDescResult() {
    	
    	if(model.getSelDescriptors().size() == 0){
    		JOptionPane.showMessageDialog(null, "Não há dados a serem exportados!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
    		return;
    	}
    	
    	JFileChooser fc = new JFileChooser();
    	fc.setCurrentDirectory(new File(System.getProperty("user.home")));
    	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	fc.setVisible(true);
    	
    	int returnVal = fc.showOpenDialog(null);
    	if(returnVal == JFileChooser.APPROVE_OPTION){
        	model.exportToExcel(fc.getSelectedFile());
    	}
	}

	public void showWaveletResult(ActionEvent e) {
		WaveletButton wb = (WaveletButton) e.getSource();
		view.showWaveletResult(wb);		
	}

	public void resetTable(JTable table, ButtonTableModel model) {
		
		for(int c = table.getColumnCount()-1; c >= 0; c--){
			TableColumn tcol = table.getColumnModel().getColumn(c);
		    table.removeColumn(tcol);
		}
		
		table.setModel(model);		
		ButtonTableRenderer renderer = new ButtonTableRenderer();

	    int n = model.getColumnCount();
		for(int i = 0; i<n; i++){
		    TableColumn col = new TableColumn(i+1);
		    col.setHeaderValue(model.getColumnName(i));
		    col.setPreferredWidth(model.getColumnName(i).length()*8);
		    col.setCellRenderer(renderer);
		    table.addColumn(col);
		}
	}

	public EVMonitor getMonitor() {
		return monitor;		
	}
}
