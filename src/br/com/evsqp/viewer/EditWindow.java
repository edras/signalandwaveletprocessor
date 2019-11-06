package br.com.evsqp.viewer;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import br.com.evsqp.misc.EventData;
import br.com.evsqp.misc.EventRenderer;

public class EditWindow extends JDialog implements KeyListener, ChartMouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6866093514539501732L;
	JFreeChart chart;
	private EventData event;
	JLabel info = new JLabel("");
	private EventRenderer renderer;
	ValueAxis domainAxis;
	ValueAxis rangeAxis;
	TimeSeriesCollection dataset;
	private ValueMarker leftMarker;
	private ValueMarker rightMarker;
	private ValueMarker middleMarker;
	private ValueMarker varMarker;
	private Float windowSize;
	private File exportFile;
	private long step;
	private boolean stop = false;
	private int eventNr = -1;
	private Preferences prefs;
	
	private static String window_size = "WINDOW_SIZE";
	private static String window_pos  = "WINDOW_POS";
	
	
	public EditWindow(EventData event, TimeSeries series, Float windowSize) {
		
		try {
			this.event  = event.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.windowSize = windowSize;
		
		dataset = new TimeSeriesCollection(series);		
		this.step = series.getDataItem(1).getPeriod().getFirstMillisecond()-series.getDataItem(0).getPeriod().getFirstMillisecond();
		
        prefs = Preferences.userNodeForPackage(getClass());
        
        String[] pos  = prefs.get(window_pos, "").split(":");        
        if(!pos[0].isEmpty()){
        	this.setLocation(Double.valueOf(pos[0]).intValue(), Double.valueOf(pos[1]).intValue());
        }  
        
        pos  = prefs.get(window_size, "").split(":");        
        if(!pos[0].isEmpty()){
        	this.setSize(Double.valueOf(pos[0]).intValue(), Double.valueOf(pos[1]).intValue());
        }		
		
	}
	
	// esse metodo é utilizado para navegar até um determinado 
	// evento de uma lista de eventos. A janela é configura com
	// eventNr = -1 por padrão.
	public int getEventNr(){
		return eventNr;
	}
	
	// verifique o método getEventNr.
	public void setEventNr(int eventNr){
		this.eventNr = eventNr;
	}
	
	private JFreeChart createChart() {		
		
		final JFreeChart chart = ChartFactory.createTimeSeriesChart(
        		"", "time", "Channel", 
        		null, 
        		false, false, false);
        
        XYPlot plot = chart.getXYPlot();
        List<String> names = new ArrayList<String>();
        
        TimeSeries series = dataset.getSeries(0);
        names.add(series.getKey().toString());
        
        renderer = new EventRenderer();
        List<EventData> eventList = new ArrayList<EventData>();
        eventList.add(event);
        renderer.setEventList(eventList);
        
        plot.setRenderer(renderer);
        renderer.setBaseShapesVisible(false);
        ((AbstractRenderer)renderer).setAutoPopulateSeriesPaint(false);
        ((AbstractRenderer)renderer).setAutoPopulateSeriesStroke(false);
        renderer.setBasePaint(Color.BLACK);
        renderer.setBaseStroke(new BasicStroke(0.5F));
        renderer.setStroke(new BasicStroke(0.5F));

        plot.setDataset(0, dataset);
        plot.setBackgroundPaint(Color.WHITE);
        
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 8);
        
        domainAxis = plot.getDomainAxis();
        domainAxis.setAutoRange(true);
        domainAxis.setTickLabelFont(font);
        domainAxis.setLabelFont(font);
        
        renderer.setDomainAxis((DateAxis) domainAxis);
        
        rangeAxis = plot.getRangeAxis();
        rangeAxis.setVisible(false);
        rangeAxis.setAutoRange(true);
        rangeAxis.setInverted(true);
        
        SymbolAxis sa = new SymbolAxis("Channel",names.toArray(new String[0]));  
        sa.setLabelFont(font);
        sa.setTickLabelFont(font);
        sa.setInverted(true);
        plot.setRangeAxis(1, sa);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_LEFT);     
        
        middleMarker = new ValueMarker((event.getStartTimePeriod().getFirstMillisecond()+event.getEndTimePeriod().getFirstMillisecond())/2);   
        middleMarker.setPaint(Color.BLUE);
        middleMarker.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));  
        middleMarker.setLabelAnchor(RectangleAnchor.TOP);  
        middleMarker.setLabelTextAnchor(TextAnchor.TOP_LEFT);  
        middleMarker.setLabel("centro");  
        middleMarker.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,1.0f, new float[] { 10.0f, 6.0f }, 0.0f));
        plot.addDomainMarker(middleMarker);

        leftMarker = new ValueMarker(middleMarker.getValue()-windowSize*1000);   
    	leftMarker.setPaint(Color.RED);
    	leftMarker.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));  
		leftMarker.setLabelAnchor(RectangleAnchor.TOP);  
		leftMarker.setLabelTextAnchor(TextAnchor.TOP_LEFT);  
		leftMarker.setLabel("início");  
		leftMarker.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,1.0f, new float[] { 10.0f, 6.0f }, 0.0f));
		
		plot.addDomainMarker(leftMarker);

		rightMarker = new ValueMarker(middleMarker.getValue()+windowSize*1000);   
		rightMarker.setPaint(Color.RED);
		rightMarker.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));  
		rightMarker.setLabelAnchor(RectangleAnchor.TOP);  
		rightMarker.setLabelTextAnchor(TextAnchor.TOP_LEFT);  
		rightMarker.setLabel("fim");  
		rightMarker.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,1.0f, new float[] { 10.0f, 6.0f }, 0.0f));
		plot.addDomainMarker(rightMarker);

		varMarker = new ValueMarker(middleMarker.getValue()+20);   
		varMarker.setPaint(Color.MAGENTA);
		varMarker.setLabelFont(new Font("SansSerif", Font.ITALIC, 9));  
		varMarker.setLabelAnchor(RectangleAnchor.BOTTOM);
		varMarker.setLabel(String.valueOf(varMarker.getValue()-middleMarker.getValue()));  
		varMarker.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,1.0f, new float[] { 10.0f, 6.0f }, 0.0f));
		plot.addDomainMarker(varMarker);

        return chart;
    }

	
	public void doChart(){		
		
        chart = createChart();
        final ChartPanel chartPanel = new ChartPanel(chart);
        final JPanel content = new JPanel(new BorderLayout());
        content.add(chartPanel, BorderLayout.CENTER);
        content.add(info, BorderLayout.SOUTH);
        addInfo();
        
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 350));
        setContentPane(content);

        addKeyBinding(content);
        
        chartPanel.addChartMouseListener(this);
        
		addKeyListener(this);

        this.pack(); // não é possível configurar o tamanho da janela se essa opção estiver selecionada
        setVisible(true);                
	}
	
	private void addInfo() {		
		info.setText("< > Move a janela, ESC vai para o próximo evento, I inverte o sinal, S salva o sinal, UP e DOWN move linha variacao (verde), P para a exportação");		
	}
	
	void addKeyBinding(JComponent jc) {
		
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "right pressed");
        jc.getActionMap().put("right pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				shiftRight();  
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "left pressed");
        jc.getActionMap().put("left pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				shiftLeft();  
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "up pressed");
        jc.getActionMap().put("up pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				shiftVarRight();  
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "down pressed");
        jc.getActionMap().put("down pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				shiftVarLeft();  
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "s pressed");
        jc.getActionMap().put("s pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				save();
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0, true), "i pressed");
        jc.getActionMap().put("i pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				invert();
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "esc pressed");
        jc.getActionMap().put("esc pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				skip();
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0, true), "p pressed");
        jc.getActionMap().put("p pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				stop = true;
				skip();
			}
        });
        
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true), "e pressed");
        jc.getActionMap().put("e pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				String showInputDialog = JOptionPane.showInputDialog("Digite o Nr do evento que deseja visualizar:");
				if(showInputDialog != null){
					Integer decode = Integer.decode(showInputDialog);
					setEventNr(decode);
					dispose();
				}				
			}
        });
        
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "space pressed");
        jc.getActionMap().put("space pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) {
				changMarkers(step*100);
			}
        });
        
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, true), "backspace pressed");
        jc.getActionMap().put("backspace pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) {
				changMarkers(-step*100);
			}
        });
	}

	protected void skip() {
		
		prefs.put(window_pos, this.getLocation().getX()+":"+this.getLocation().getY());
		prefs.put(window_size, this.getWidth()+":"+this.getHeight());
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
					
		setEventNr(-1);
		setVisible(false);
		
	}

	protected void invert() {		
		rangeAxis.setInverted(!rangeAxis.isInverted());		
	}

	protected void save() {
		
		if(exportFile == null){
			JFileChooser fc = new JFileChooser(exportFile);
			fc.setDialogTitle("Selecione o Diretório e nome base para exportação de eventos");
			
			int returnVal = fc.showOpenDialog(null);
	    	if(returnVal == JFileChooser.APPROVE_OPTION){
	    		exportFile = fc.getSelectedFile();
	    	} else return;
		}
		
//		File parentFile = exportFile.getParentFile();
//		if(!parentFile.exists()) parentFile.mkdirs();
		
		int invert = rangeAxis.isInverted()?-1:1;
		
		File parentFile = exportFile.getParentFile();
		if(!parentFile.exists()) parentFile.mkdirs();
		
		File[] listFiles = parentFile.listFiles();
		
		List<File> files = new ArrayList<File>(listFiles.length);
		Collections.addAll(files, listFiles);
		
		int counter = 0;
		File file = null;
		
		while(files.contains(new File(exportFile.getAbsolutePath()+"_"+varMarker.getLabel()+"_"+counter+".big"))){
			counter++;
		}
		
		file = new File(exportFile.getAbsolutePath()+"_"+varMarker.getLabel()+"_"+counter+".big");
		
		try{
			TimeSeries series = dataset.getSeries(0);
			int left = series.getIndex(getTimePeriod((long)leftMarker.getValue()));
			int right = series.getIndex(getTimePeriod((long)rightMarker.getValue()))-1;
			TimeSeries save = dataset.getSeries(0).createCopy(left, right);
			FileOutputStream fo = new FileOutputStream(file);
			ObjectOutputStream oo = new ObjectOutputStream(fo);
					
			oo.writeLong(step);
			oo.writeLong(save.getDataItem(0).getPeriod().getFirstMillisecond());
			oo.writeInt(save.getItemCount());
			for(int i=0;i<save.getItemCount();i++){
				double doubleValue = save.getDataItem(i).getValue().doubleValue()*10.24;
				oo.writeInt((int)doubleValue*invert);
			}
			oo.writeChar('2'); // versão
			oo.writeUTF("Sampling 1000Hz, Notch:{20,60,120,180,240,300,420}, LPF:350");
			oo.close();
			JOptionPane.showMessageDialog(this, file.getName()+" salvo com sucesso.");
		}
		catch(Exception e){
			e.printStackTrace();
		}   
		
		//	desserializo o objeto
//		try{
//			FileInputStream fi = new FileInputStream(file);
//			ObjectInputStream oi = new ObjectInputStream(fi);
//			series =(TimeSeries) oi.readObject();
//			oi.close();
//		}catch(Exception e){e.printStackTrace();}                          
		
	}
	
	private RegularTimePeriod getTimePeriod(long time) {
        return RegularTimePeriod.createInstance(
        		Millisecond.class, 
        		new Date(time), 
        		TimeZone.getDefault());
	}

	protected void shiftLeft() {
		changMarkers(-step);
	}

	protected void shiftRight() {
		changMarkers(step);
	}
	
	protected void changMarkers(long step){
		leftMarker.setValue(leftMarker.getValue()+step);
		rightMarker.setValue(rightMarker.getValue()+step);
		middleMarker.setValue(middleMarker.getValue()+step);
		varMarker.setValue(varMarker.getValue()+step);
	}
	
	protected void shiftVarLeft() {
		changVarMarkers(-step);
	}

	protected void shiftVarRight() {
		changVarMarkers(step);
	}
	
	protected void changVarMarkers(long step){
		varMarker.setValue(varMarker.getValue()+step);
		varMarker.setLabel(String.valueOf(varMarker.getValue()-middleMarker.getValue()));
	}

	@Override
	public void chartMouseClicked(ChartMouseEvent arg0) {
		
		ChartEntity entity = arg0.getEntity();
		if (entity == null)
			return;
		
		if(entity instanceof XYItemEntity){		
			XYDataset dataset = ((XYItemEntity)entity).getDataset();
			int seriesIndex = ((XYItemEntity)entity).getSeriesIndex();
			int item = ((XYItemEntity)entity).getItem();
			TimeSeries series = ((TimeSeriesCollection)dataset).getSeries(seriesIndex);
			RegularTimePeriod timePeriod = series.getTimePeriod(item);
			
			middleMarker.setValue(timePeriod.getFirstMillisecond());
			leftMarker.setValue(middleMarker.getValue()-windowSize*1000);   
			rightMarker.setValue(middleMarker.getValue()+windowSize*1000);
			save();
		}
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		int i = 0;
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void setFile(File exportFile) {
		this.exportFile = exportFile;	
	}

	public boolean stop() {
		return stop;
	}

}
