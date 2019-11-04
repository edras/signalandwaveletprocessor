package br.com.evsqp.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;

import br.com.evsqp.misc.EventData;
import br.com.evsqp.misc.EventRenderer;
import br.com.evsqp.reader.AbstractReader;

abstract public class AbstractViewer extends JFrame implements KeyListener, ChartMouseListener {
	
	List<EventData> eventList;
	JFreeChart chart;
	protected int selectedSeriesIndex;
	protected TimeSeriesDataItem selectedDataItem;
	protected TimeSeriesDataItem selectedDataItem2;
	
	protected AbstractReader reader;
	protected TimeSeriesCollection dataset;
	protected JLabel info;
	protected EventRenderer renderer;
	protected ArrayList<Integer> evtCounter;
	protected int channels;
	protected double scale;
	
	protected ValueAxis domainAxis;
	protected ValueAxis rangeAxis;
	
	protected Timer timer;
	protected TimerTask increase;
	protected TimerTask decrease;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4435427681536858425L;

	abstract public void view();
	abstract public void setOffset(int offset);
	abstract public void setPageSize(int pageSize);
	abstract protected JFreeChart createChart();
	abstract protected void nextPage();	
	abstract protected void previousPage();
	abstract protected void incScale();  
	abstract protected void decScale();  
	abstract protected void goToEnd();  
	abstract protected void goHome(); 
	abstract protected void go10Pages();
	abstract protected void updateRangeAxis();
	abstract protected void printPageRange();
	abstract public    int getOffset();
	abstract protected void addPopUpMenu(ChartPanel charPanel);
	abstract public    int getDefaultPageSize();
	
	public AbstractViewer(){

		dataset = new TimeSeriesCollection();
		
		eventList = new ArrayList<EventData>();
		info = new JLabel();
		info.setOpaque(true);
		info.setBackground(Color.WHITE);
		info.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		info.setText("              ");
		
        renderer = new EventRenderer();
        renderer.setEventList(eventList);
        
        timer = new Timer();
        increase = new TimerTask() {
            @Override
            public void run() {
                incScale();
            }
        };
        
        decrease = new TimerTask() {
            @Override
            public void run() {
                decScale();
            }
        };
        
        addKeyListener(this);
	}
	
	protected RegularTimePeriod getTimePeriod(long time) {
        return RegularTimePeriod.createInstance(
        		Millisecond.class, 
        		new Date(time), 
        		TimeZone.getDefault());
	}
	
	public void setEventList(List<EventData> eventList){
		this.eventList = eventList;
	}
	public List<EventData> getEventList(){
		
		return eventList;
	}
	
	protected void addInfo() {
		
		String text = info.getText();
//		text = text.concat("Sampling: " + reader.getSamplingRate() + " Hz   ");
//		text = text.concat("Filters: N:60Hz LP:70Hz ");
		//for(String filter : reader.getFilters())
		//	text = text.concat(filter+" ");
		info.setText(text);
		
		printPageRange();
	}
	
	protected void addListeners() {

        addWindowListener(new WindowAdapter() {        	 
            @Override
            public void windowClosing(WindowEvent e) {
            	reader.close();
            	//System.exit(0);
            }
        });
	}
	
	// Essa função é a mesma que a doChart, mas para ser usada com o app SpikeSort, modo dockable
	public Object[] getPanel(){

		chart = createChart();
        final ChartPanel chartPanel = new ChartPanel(chart);
        final JPanel content = new JPanel(new BorderLayout());
        content.add(chartPanel, BorderLayout.CENTER);
        content.add(info, BorderLayout.SOUTH);
        addInfo();
        
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 350));
        setContentPane(content);
        
        addKeyBinding(content);

        addWindowListener(new WindowAdapter() {        	 
            @Override
            public void windowClosing(WindowEvent e) {
            	reader.close();
            }
        });
        
        Object[] obj = new Object[2];
        obj[0] = getContentPane();
        obj[1] = chartPanel;        
        
        addPopUpMenu(chartPanel);
        
        return obj;		
	}
	
	void addKeyBinding(JComponent jc) {
		
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "right pressed");
        jc.getActionMap().put("right pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				nextPage();  
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "left pressed");
        jc.getActionMap().put("left pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				previousPage();  
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "up pressed");
        jc.getActionMap().put("up pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				incScale();  
			}
        });
        jc.getActionMap().put("up released", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				timer.cancel();
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "down pressed");
        jc.getActionMap().put("down pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				decScale(); 
				//timer.schedule(decrease, 500, 100);
		        //jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "down released");
			}
        });
        jc.getActionMap().put("down released", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				timer.cancel();
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0, true), "end pressed");
        jc.getActionMap().put("end pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				goToEnd();  
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0, true), "home pressed");
        jc.getActionMap().put("home pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				goHome();  
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0, true), "pg_down pressed");
        jc.getActionMap().put("pg_down pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				go10Pages();
			}
        });
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0, false), "f2 pressed");
        jc.getActionMap().put("f2 pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				exportEvts();
			}
        });
        

        for (char i = 'A'; i < ('Z'+1); i++) {
        	KeyStroke keyStroke = KeyStroke.getKeyStroke(i, 0, false);
            jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, i+" pressed");
            jc.getActionMap().put(i+" pressed", new AbstractAction() {
    			private static final long serialVersionUID = 1L;
    			@Override
                public void actionPerformed(ActionEvent ae) { 
    				if(selectedDataItem!=null){
    					insertEvent(ae.getActionCommand().charAt(0)-'a');
    					renderer.update();
    					chart.fireChartChanged();
    				}
    			}
            });			
		}        
        
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, false), "backSpace pressed");
        jc.getActionMap().put("backSpace pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) { 
				deleteLastEvent();
				renderer.update();
				chart.fireChartChanged();
			}
        });
		
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false), "delete pressed");
        jc.getActionMap().put("delete pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) {
				if(selectedDataItem!=null){
					deleteEvent();
					renderer.update();
					chart.fireChartChanged();
				}
			}
        });
        
        jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "escape pressed");
        jc.getActionMap().put("escape pressed", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
            public void actionPerformed(ActionEvent ae) {
				if(selectedDataItem!=null){
					selectedDataItem = null;
					selectedDataItem2 = null;
				}
			}
        });
	}
	
	protected void exportEvts(){}
	
	private void insertEvent(int event) {
		
		if(selectedDataItem==null) return;
		
		try {
			TimeSeries series = dataset.getSeries(selectedSeriesIndex);
			int index = series.getIndex(selectedDataItem.getPeriod());
			int start_idx = index-reader.getSamplingRate();
			int end_idx = index+reader.getSamplingRate();
			if(end_idx>series.getItemCount()-1) {
				end_idx = series.getItemCount()-1;
				start_idx = end_idx-reader.getSamplingRate();
			}
			if(start_idx<0) start_idx = 0;
			TimeSeries copy = series.createCopy(start_idx, end_idx);
			
			EventData eventData = new EventData();
			eventData.setFile(reader.getFile().getName());
			eventData.setChannelName(series.getDescription());
			eventData.setChannelNumber(selectedSeriesIndex);
			eventData.setEventNumber(event);
			eventData.setSeries(copy, selectedSeriesIndex, scale);	
			if(selectedDataItem2==null){
				eventData.setStartTimePeriod(series.getTimePeriod(start_idx));
				eventData.setEndTimePeriod(series.getTimePeriod(end_idx));
				eventData.setStartIndex(index-20);
				eventData.setEndIndex(index+20);				
			} else {
				eventData.setStartIndex(series.getIndex(selectedDataItem2.getPeriod()));
				eventData.setEndIndex(series.getIndex(selectedDataItem.getPeriod()));	
			}
			
			eventList.add(eventData);
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}		
		
		selectedDataItem = null;	
		selectedDataItem2 = null;
	}
	

	
	@Override
	public void keyTyped(KeyEvent e) {

		int keyCode = e.getKeyChar();
		boolean command = false;
		
		if(keyCode>=KeyEvent.VK_0 && keyCode<=KeyEvent.VK_9 && selectedDataItem!=null){
			insertEvent(keyCode-'0');
			command = true;
		}
		
		if(keyCode == KeyEvent.VK_BACK_SPACE){
			deleteLastEvent();
			command = true;
		}
		
		if(keyCode == KeyEvent.VK_DELETE && selectedDataItem!=null){
			deleteEvent();
			command = true;
		}	
		
		if(command) {
			renderer.update();
			chart.fireChartChanged();
		}
	}

	private void deleteEvent() {
		
		if(selectedDataItem==null) return;
		
		//RegularTimePeriod period = selectedDataItem.getPeriod();
		
		for (int i = 0; i < eventList.size(); i++) {
			EventData eventData = eventList.get(i);
			if(eventData.getChannelNumber()!=selectedSeriesIndex) continue;
			//if(eventData.getSeries().getDataItem(period)==null) continue;
			eventList.remove(i);
			break;
		}	
		
		selectedDataItem = null;
	}
	
	private void deleteLastEvent() {
		if(eventList.isEmpty()) return;
		eventList.remove(eventList.size()-1);
	}
	
	public EventRenderer getRenderer() {		
		return renderer;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		switch (e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			nextPage();
			break;
		case KeyEvent.VK_LEFT:
			previousPage();
			break;
		case KeyEvent.VK_UP:
			incScale();
			break;
		case KeyEvent.VK_DOWN:
			decScale();
			break;
		case KeyEvent.VK_END:
			goToEnd();
			break;
		case KeyEvent.VK_HOME:
			goHome();
			break;
		case KeyEvent.VK_SPACE:
			go10Pages();
			break;
		case KeyEvent.VK_F2:
			exportEvts();
		default:
			break;
		}
		
		gc();
	}
	
	private void gc() {		
		Runtime.getRuntime().gc();		
	}
	
	public void setEventList(ArrayList<EventData> eventList) {
		this.eventList = eventList;
		getRenderer().setEventList(eventList);
		getRenderer().update();
		calcNrOfEvetns();
    }
	
	@Override
	public void chartMouseClicked(ChartMouseEvent event) {
		
		ChartEntity entity = event.getEntity();
		if (entity == null)
			return;
		
		if(entity instanceof XYItemEntity){		
			XYDataset dataset = ((XYItemEntity)entity).getDataset();
			selectedSeriesIndex = ((XYItemEntity)entity).getSeriesIndex();
			int item = ((XYItemEntity)entity).getItem();
			TimeSeries series = ((TimeSeriesCollection)dataset).getSeries(selectedSeriesIndex);
			if(selectedDataItem!=null) selectedDataItem2 = selectedDataItem;
			selectedDataItem = series.getDataItem(item);		
			
			if(reader.isDebug()){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss S");
				System.out.println("Series index: " + selectedSeriesIndex + 
					" Item: " + sdf.format(new Date(selectedDataItem.getPeriod().getFirstMillisecond())) + 
					" value: " + (selectedDataItem.getValue().doubleValue()-selectedSeriesIndex*scale));
			}
		}
		
	}
	protected void calcNrOfEvetns() {
		
		evtCounter = new ArrayList<Integer>();
		for(int i=0;i<26;i++) evtCounter.add(0);

		Integer idx;
		for(EventData evt : eventList){
			idx = evt.getEventNumber();
			if(idx>24 || idx < 0) System.out.println("Erro - Evento fora de indice: " + idx + " : " + evt.getStartTimePeriod());
			else evtCounter.set(idx, evtCounter.get(idx)+1);
		}
		
		for(int i=0;i<evtCounter.size();i++){
			if(evtCounter.get(i)==0) continue;
			System.out.println("Evento "+ (i+1) + ": " + evtCounter.get(i));
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {		
		//int i = 0;		
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent event) {
		
	}
	
	protected void changeScale(double oldScale){
		
		double value;				
		
		if(channels == 1){

			TimeSeries series = dataset.getSeries(0);
			
			int itemCount = series.getItemCount();
			
			for(int j=0; j<series.getItemCount(); j++){
				value = series.getValue(j).doubleValue()/oldScale;
				series.update(j, value*scale);
			}
			
		}
		else {
			for(int i = 1; i < channels; i++) {
				TimeSeries series = dataset.getSeries(i);
				
				value = -i*oldScale+i*scale;
				
				for(int j=0; j<series.getItemCount(); j++){
					series.update(j, series.getValue(j).doubleValue()+value);
				}
			}		
		}
		updateRangeAxis();
	}
}
