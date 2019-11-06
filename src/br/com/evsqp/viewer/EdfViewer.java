package br.com.evsqp.viewer;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import br.com.evsqp.misc.EventData;
import br.com.evsqp.misc.NotchWindow;
import br.com.evsqp.reader.EdfReader;
import br.com.evsqp.reader.GenericEegReader;
import br.com.evsqp.utils.DFilter;

public class EdfViewer extends AbstractViewer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1170491654917189353L;
	private long dr;
	private long current_dr = 1000;
	private long exceeded = 0;
	private int page = 10;

	private NotchWindow notchWindow;

	public EdfViewer(GenericEegReader reader) {

		super();
		
		this.reader = reader;
		scale = 2000;		
	}

	@Override
	public int getOffset(){
		
		double drTime = ((GenericEegReader)reader).getDataRecordDuration(0);
		int dr_ = (int) (page/drTime);
		long dr = current_dr-dr_;
		if(dr<0) dr = 0;
		return (int) (dr*drTime);
	}
	
	protected void addPopUpMenu(ChartPanel chartPanel) {
		
		JPopupMenu popupMenu = chartPanel.getPopupMenu();
		popupMenu.addSeparator();
		
		JMenuItem fft = new JMenuItem("FFT");
		fft.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(selectedDataItem==null){
					JOptionPane.showMessageDialog(null, "Selecione um canal primeiramente");
					return;
				}
				TimeSeries series = ((TimeSeriesCollection)dataset).getSeries(selectedSeriesIndex);
				try {
					TimeSeries copy = series.createCopy(0, series.getItemCount()-1);
					for (int i = 0; i < series.getItemCount(); i++) {
						copy.update(i, copy.getValue(i).doubleValue()-selectedSeriesIndex*scale);
					}
					int sampling = 0;
					if(((GenericEegReader)reader).getSubSampling()==-1) sampling = reader.getSamplingRate();
					else sampling = ((GenericEegReader)reader).getSubSampling();
					
					DFilter.fft(sampling, copy);
					selectedDataItem = null;
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
			}
		});
		popupMenu.add(fft);
		
		JMenuItem subSampling = new JMenuItem("Sub-Sampling");
		subSampling.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				String newFreq = JOptionPane.showInputDialog("Entre com a nova Frequência de Amostragem\nDeve ser menor que " + reader.getSamplingRate());
				try{
					int freq = Integer.parseInt(newFreq);
					((GenericEegReader)reader).setSubSampling(freq);
					//previousPage();
					doChart();
				} catch (NumberFormatException e1){
					JOptionPane.showMessageDialog(null, "Frequência Inválida / Mal Formatada");
				} catch (NullPointerException e1){
					JOptionPane.showMessageDialog(null, "Frequência Inválida / Mal Formatada");
				}
			}
		});
		popupMenu.add(subSampling);
		
		JMenuItem notch = new JMenuItem("Notch");
		notch.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(notchWindow == null){
					notchWindow = new NotchWindow();
					notchWindow.getApplyButton().addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							doChart();
						}
					});
				}
				notchWindow.setVisible(true);
			}
		});
		popupMenu.add(notch);
		
		JMenuItem fpb = new JMenuItem("FPB");
		fpb.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				String newFreq = JOptionPane.showInputDialog("Entre com a nova Frequência para o Filtro Passa Baixas");
				try{
					float freq = Float.parseFloat(newFreq);
					((GenericEegReader)reader).setLPF(freq);
					doChart();
				} catch (NumberFormatException e1){
					JOptionPane.showMessageDialog(null, "Frequência Inválida / Mal Formatada");
				} catch (NullPointerException e1){
					JOptionPane.showMessageDialog(null, "Frequência Inválida / Mal Formatada");
				}
			}
		});
		popupMenu.add(fpb);
		
	}
	
	@Override
	protected JFreeChart createChart() {		
		
		channels = reader.getNumberOfChannels();

		final JFreeChart chart = ChartFactory.createTimeSeriesChart(
        		"", "time", "Channel", 
        		null, 
        		false, false, false);
        
        XYPlot plot = chart.getXYPlot();
        List<String> names = new ArrayList<String>();        
        

        plot.setRenderer(renderer);
        renderer.setBaseShapesVisible(false);
        ((AbstractRenderer)renderer).setAutoPopulateSeriesPaint(false);
        ((AbstractRenderer)renderer).setAutoPopulateSeriesStroke(false);
        renderer.setBasePaint(Color.BLACK);
        renderer.setBaseStroke(new BasicStroke(0.5F));
        
        for (int i = 0; i < channels; i++)
			names.add(readSeries(i));
        
        current_dr = dr;
        
        plot.setDataset(0, dataset);
        plot.setBackgroundPaint(Color.WHITE);
        
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 8);
        
        domainAxis = plot.getDomainAxis();
        domainAxis.setAutoRange(false);
        domainAxis.setTickLabelFont(font);
        domainAxis.setLabelFont(font);
        
        renderer.setDomainAxis((DateAxis) domainAxis);
        
        rangeAxis = plot.getRangeAxis();
        rangeAxis.setVisible(false);
        rangeAxis.setAutoRange(false);
        rangeAxis.setInverted(true);
        
        SymbolAxis sa = new SymbolAxis("Channel",names.toArray(new String[0]));  
        sa.setLabelFont(font);
        sa.setTickLabelFont(font);
        sa.setInverted(true);
        plot.setRangeAxis(1, sa);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_LEFT);     
        
		for(int i = 0; i < channels; i++) ((TimeSeriesCollection)dataset).getSeries(i).setNotify(false);
		
		changeScale(0);
		
        return chart;
    }

	protected void updateRangeAxis() {
        rangeAxis.setUpperBound((channels-1)*scale+scale/2);
        rangeAxis.setLowerBound(-scale/2);		
	}

	private String readSeries(int channel) {

		String channelName = ((GenericEegReader)reader).getChannelName(channel);
		TimeSeries series = dataset.getSeries(channelName);
		
		// instancia uma nova serie caso esta nao exista
		if(series==null) {
			series = new TimeSeries(channelName);
			series.removeAgedItems(false);
			dataset.addSeries(series);
		}

		// caso a serie jah exista, esvazia a serie
   		if(!series.isEmpty()) 
   			series.delete(0, series.getItemCount()-1);

        double drTime = ((GenericEegReader)reader).getDataRecordDuration(channel);
        int samples = ((GenericEegReader)reader).getNumberOfSamples(channel);
        long total_dr = ((GenericEegReader)reader).getNumberOfDataRecords();

        int step = (int)(drTime/samples*1000);     
        long time, time_check;
        time = ((GenericEegReader)reader).getDataRecordTime(0, current_dr);
        
        // quantidade de DR para uma página
        long dr_ = (int) (page/drTime);

        // configuracao da pagina:
        // estamos no inicio da pagina?
        if(current_dr == dr){       
            
        	dr = current_dr+dr_;
        	
            // pagina excede total de DRs existente no arquivo?
            if(dr>total_dr)            	
           		dr = total_dr;
            
            // o ultimo DR estah temporalmente dentro da janela?
            if(((GenericEegReader)reader).getDataRecordTime(0, dr)>time+page*1000){
	            // o tempo de todos os DRs esta compreendido dentro da pagina?
	            while(dr != current_dr){
	            	dr--;
	            	// pega o tempo que seria o ultimo dr da pagina
	            	time_check = ((GenericEegReader)reader).getDataRecordTime(0, dr);            	
	            	// decrementa o dr maximo da pagina ateh este se encontrar dentro dos limites temporais da pagina
	            	if((time_check-time)<page*1000){ 
	            		dr++;
	            		break;
	            	}
	            }
            }
        }
        
        // copia os dados do arquivo para a serie
        for (long i = current_dr; i < dr; i++) {
       		double[] data = ((GenericEegReader)reader).getDataRecordChannel(channel, i);
       		for (int j = 0; j < data.length; j++) {
				series.add(getTimePeriod(time), data[j]);
				time += step;
			}
        }        

        // aplica filtros ao sinal
        try {
			doFilter(series);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

        // zera os quatro primeiros DR conforme tela recebida por email
        // e zera amostras finais e iniciais para omitir erros 
        // de janelamento dos filtros
//        int zeros = 0;
//        if(current_dr<11) zeros = 8;
//        else zeros = 1;
//        for (int i = 0; i < zeros*samples; i++) {
//			series.update(i, 0);
//			if(i<samples) series.update(series.getItemCount()-1-i, 0);
//		}

        // precisa completar a página com espaço em branco?
        if(dr<current_dr+dr_){
        	exceeded = dr-current_dr;
        	time_check = ((GenericEegReader)reader).getDataRecordTime(0, dr-1);
        	time_check += (current_dr+dr_-dr)*samples*step;
   			series.add(getTimePeriod(time_check), 0);       			
        } else
        	exceeded = 0;

        return channelName;
	}

	private void doFilter(TimeSeries series) throws CloneNotSupportedException {

		int sampling = 0;
		if(((GenericEegReader)reader).getSubSampling()!= -1){
			DFilter.subSampling(((GenericEegReader)reader).getSamplingRate(), ((GenericEegReader)reader).getSubSampling(), series);
			sampling = ((GenericEegReader)reader).getSubSampling();
		} else {
			sampling = ((GenericEegReader)reader).getSamplingRate();
		}
		
		if(sampling == 1000){
			
			DFilter.notch(sampling, series, new double[]{20, 60, 120, 180, 240, 300, 420});
			DFilter.lpf(sampling, series, 350.0F);
			
		} else {
			
			if(!((GenericEegReader)reader).getFilters().contains("N:60Hz")){
				if(notchWindow == null){
					DFilter.notch(sampling, series, new double[]{20, 60});
				} else {
					DFilter.notch(sampling, series, notchWindow.getFrequencies());
				}				
			}
			if(!((GenericEegReader)reader).getFilters().contains("LP:70Hz")){
				DFilter.lpf(sampling, series, ((GenericEegReader)reader).getLPF());
			}
		}
		
		//DFilter.hpf(sampling, series, 250, 4);
		
	}

	public static void main(String[] args) {
		EdfReader.main(args);
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
        
        this.pack();
        this.setVisible(true);
                
        chartPanel.addChartMouseListener(this);
        addListeners();
        addPopUpMenu(chartPanel);
	}
	
	public void exit(){
		 WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
         Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}

	public void setOffset(int offset) {
		double drTime = ((GenericEegReader)reader).getDataRecordDuration(0);
		current_dr = (long) (offset/drTime);
		dr = current_dr;
	}

	public void setPageSize(int page) {
		if(page < 0){
			System.err.println("Page size should be greater than zero.");
			System.exit(0);
		}
		
		if(page > ((GenericEegReader)reader).getSignalDuration()){
			System.err.println("Page size is greater than signal size");
		}		
		this.page = page;		
	}
	
	@Override
	public int getDefaultPageSize(){
		return 10;
	}

	@Override
	public void view() {
		setTitle(reader.getFile().getName());
		setOffset(0);
		setPageSize(10);
		doChart();		
	}
	
	@Override
	protected void exportEvts() {
		
		Integer evtNumber;
		Float windowSize;
		File exportFile = new File(System.getProperty("user.home"));
		
		JFileChooser fc = new JFileChooser(exportFile);
		fc.setDialogTitle("Selecione o Diretório e nome base para exportação de eventos");
		
		int returnVal = fc.showOpenDialog(null);
    	if(returnVal == JFileChooser.APPROVE_OPTION){
    		exportFile = fc.getSelectedFile();
    	} else return;
    	
    	windowSize = Float.valueOf(JOptionPane.showInputDialog("Qual o tamanho da janela a ser utilizado (em segundos)?\n" +
    			"Ex: 1.5","2.0"));
		
    	if(evtCounter == null) {
    		JOptionPane.showMessageDialog(null, "Para exportar eventos, é necessário recarregar o arquivo");
    		return;
    	}
		String message = "Eventos Disponíveis:\n"; 
		for (int i = 0; i < evtCounter.size(); i++) {
			message = message.concat(i+":"+evtCounter.get(i)+" ");
			if(i%10==0) message = message.concat("\n");
		}
		evtNumber = Integer.valueOf(JOptionPane.showInputDialog("Dos eventos registrados, qual deles será exportado?\n" + message,"Selecione Evento desejado"));

		boolean fast = ((GenericEegReader)reader).isFastReading();
		
		for(EventData evt : eventList){
			
			// pula eventos que não são de interesse
			if(evt.getEventNumber()!=evtNumber) continue;
			
			TimeSeries series = null;
			int channelNumber = evt.getChannelNumber();
			
			// acerta a página da aplicação conforme a posição do evento
			while(true){
				Date timeEvent = evt.getEndTimePeriod().getStart();
				series = ((TimeSeriesCollection)dataset).getSeries(channelNumber);
				TimeSeriesDataItem dataItem = series.getDataItem(0);
				Date startTime = dataItem.getPeriod().getStart();
				dataItem = series.getDataItem(series.getItemCount()-1);
				Date endTime = dataItem.getPeriod().getStart();
				if(timeEvent.after(endTime))
					nextPage();
				else if(timeEvent.before(startTime)){
					previousPage();
				}
				else {
					break;
				}
			}

			// ajusta página se necessário para eventos na borda da página
 			if(evt.getStartTimePeriod().equals(series.getDataItem(0).getPeriod())) 
 				previousHalfPage();
 			if(evt.getEndTimePeriod().equals(series.getDataItem(series.getItemCount()-1).getPeriod())) 
 				nextHalfPage();
 			

 			/* Caso o flag "fast reading" esteja acionado, significa que
 			 * o reader está lendo os dados com sub-amostragem. Para exportação
 			 * temos que salvar os dados como eles estão gravados
 			 */
 			
 			fast = ((GenericEegReader)reader).isFastReading();
 			double bckup = scale;
 			
            if(fast){
            	// desliga leitura rápida
            	((GenericEegReader)reader).setFastReading(false);
            }
        	
 			double drTime = ((GenericEegReader)reader).getDataRecordDuration(0);    	        
        	// quantidade de DR para uma página
            int dr_ = (int) (page/drTime);
 	            
            // desloca o leitor para o inicio da página
            current_dr -= dr_;
            
            // leitura de arquivo na amostragem normal
        	readSeries(channelNumber);
        	
        	try {
				series = (TimeSeries)((TimeSeriesCollection)dataset).getSeries(channelNumber).clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
        	
        	if(fast) {
        		// religa leitura rapida
        		((GenericEegReader)reader).setFastReading(true);
        	}
        	
        	scale = 0;
        	changeScale(bckup);
        	// recoloca os dados no grafico da maneira usual
        	readSeries(channelNumber);
        	scale = bckup;
        	changeScale(0);
        	
        	// reestabelece a posição de pagina normal
 	        current_dr = dr;
			
			//captura dados do evento
			EditWindow editWindow = new EditWindow(evt, series, windowSize/2);
			editWindow.setModal(true);
			editWindow.setFile(exportFile);
			editWindow.setFocusable(true);
			editWindow.doChart();
			if(editWindow.stop()) break;
			
		}
		
		JOptionPane.showMessageDialog(null, "Fim da exportação de eventos!");
	}

	@Override
	protected void go10Pages() {
		
		if(current_dr != dr) return;
		
		domainAxis.setAutoRange(false);
		
		double drTime = ((GenericEegReader)reader).getDataRecordDuration(0);
       	// quantidade de DR para uma página
        int dr_ = (int) (page/drTime)*9;
            
        if((dr+dr_)>((GenericEegReader)reader).getNumberOfDataRecords()) return;
        
        dr += dr_;
        current_dr = dr;

        for(int i=0; i<channels; i++){
        	readSeries(i);
        }
        
        current_dr = dr;
		
		domainAxis.setAutoRange(true);
		printPageRange();
		changeScale(0);
		
	}

	@Override
	protected void goHome() {
		
		if(current_dr == 0) return;

		dr = 0;
		current_dr = dr;
		
		domainAxis.setAutoRange(false);
		for(int i=0; i< channels; i++)
			readSeries(i);
		
		current_dr = dr;
		
		domainAxis.setAutoRange(true);
		
		printPageRange();
		changeScale(0);		
	}

	@Override
	protected void goToEnd() {

		if(dr == ((GenericEegReader)reader).getNumberOfDataRecords()) return;
		
		double drTime = ((GenericEegReader)reader).getDataRecordDuration(0);
    	// quantidade de DR para uma página
        int dr_ = (int) (page/drTime);

		dr = ((GenericEegReader)reader).getNumberOfDataRecords()-dr_;
		if(dr<0) dr = 0;
		current_dr = dr;
		
		domainAxis.setAutoRange(false);
		for(int i=0; i< channels; i++)
			readSeries(i);
		
		current_dr = dr;
		
		domainAxis.setAutoRange(true);
		
		printPageRange();
		changeScale(0);		
	}

	@Override
	protected void printPageRange() {
		
		double drTime = ((GenericEegReader)reader).getDataRecordDuration(0);
    	// quantidade de DR para uma página
        int dr_ = (int) (page/drTime);

		int ceil_total = (int) Math.ceil(((GenericEegReader)reader).getNumberOfDataRecords()/dr_);
		int ceil_curr = (int) Math.ceil(current_dr/dr_);
		
		info.setText(ceil_curr+"/"+ceil_total);
		
	}

	@Override
	protected void decScale() {
		
		int minScale = 3000;
		
		double tmp = scale;
		scale += 5;
		if(scale > minScale) scale = minScale;		
		if(tmp != scale) changeScale(tmp);		
	}

	@Override
	protected void incScale() {
		
		int maxScale = 10;
		
		double tmp = scale;
		scale -= 5;
		if(scale < maxScale) scale = maxScale;
		if(tmp != scale) changeScale(tmp);
	}

	@Override
	protected void previousPage() {
		
		domainAxis.setAutoRange(false);
		
		double drTime = ((GenericEegReader)reader).getDataRecordDuration(0);
	        
        if(current_dr == dr && current_dr!=0){	        	
        	// quantidade de DR para uma página
            int dr_ = (int) (page/drTime);
            
            current_dr -= exceeded==0?2*dr_:exceeded+dr_;            
            if(current_dr<0) current_dr = 0;            
            dr = current_dr;            
        }		

        for(int i=0; i<channels; i++){
        	readSeries(i);
        }
        
        current_dr = dr;
		
		domainAxis.setAutoRange(true);
		printPageRange();
		changeScale(0);
	}
	
	
	private void previousHalfPage() {
		
		domainAxis.setAutoRange(false);
		
		double drTime = ((GenericEegReader)reader).getDataRecordDuration(0);
		
        if(current_dr == dr && current_dr!=0){	        	

        	// quantidade de DR para uma página
            int dr_ = (int) (page/drTime);

            current_dr -= exceeded==0?1.5*dr_:exceeded+dr_;         
            if(current_dr<0) current_dr = 0;            
            dr = current_dr;            
        }		

        for(int i=0; i<channels; i++){
        	readSeries(i);
        }
        
        current_dr = dr;
		
		domainAxis.setAutoRange(true);
		printPageRange();
		changeScale(0);
	}
	
	private void nextHalfPage(){

		if(dr == ((GenericEegReader)reader).getNumberOfDataRecords()) return;

		domainAxis.setAutoRange(false);
		
		double drTime = ((GenericEegReader)reader).getDataRecordDuration(0);
       	// quantidade de DR para uma página
        int dr_ = (int) (page/drTime);

		current_dr -= dr_/2;
		dr = current_dr;
		
		for(int i=0; i< channels; i++)
			readSeries(i);
		
		current_dr = dr;
		
		domainAxis.setAutoRange(true);
		
		printPageRange();
		changeScale(0);		
	}

	@Override
	protected void nextPage() {		
		
		if(dr == ((GenericEegReader)reader).getNumberOfDataRecords()) return;

		domainAxis.setAutoRange(false);
		for(int i=0; i< channels; i++)
			readSeries(i);
		
		current_dr = dr;
		
		domainAxis.setAutoRange(true);
		
		printPageRange();
		changeScale(0);
	}

}


