package br.com.evsqp.misc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class EventRenderer extends XYLineAndShapeRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4810182934209529729L;
	private List<EventData> eventList;
	private List<EventData> currentList = new ArrayList<EventData>();
	private DateAxis domainAxis;
	private Date startTime = new Date();
	private Date endTime = new Date();
	private int currentItem = 0;
	private Stroke stroke = new BasicStroke(2.0F);
	private HashMap<Integer, Color> colors = new HashMap<Integer, Color>();
	// = { Color.BLUE, Color.CYAN, Color.MAGENTA, Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.GRAY, Color.PINK, Color.DARK_GRAY, Color.LIGHT_GRAY};

	public EventRenderer() {}

	public EventRenderer(boolean lines, boolean shapes) {
		super(lines, shapes);
	}
	
	@Override
	public Paint getItemPaint(int row, int column) {
		
		if(isItem(row, column)){
			return getColor(row, column);
		} else {
			return super.getItemPaint(row, column);	
		}
	}
	
	@Override
	public Stroke getItemStroke(int row, int column) {
		
		if(isItem(row, column)){
			return stroke;
		} else {
			return super.getItemStroke(row, column);
		}
	}
	
	public void setStroke(Stroke stroke){
		this.stroke = stroke;
	}
	
	public Color getColor(int event){

		if(colors.containsKey(event)){
			return colors.get(event);
		}
		
		Random randomGenerator = new Random();
		int red, green, blue;
		Color color;
		
		switch(event){
			case 0 : color = Color.RED; 
			case 1 : color = Color.GREEN; 
			case 2 : color = Color.BLUE; 
			case 3 : color = Color.YELLOW; 
			case 4 : color = Color.CYAN; 
			case 5 : color = Color.MAGENTA; 
			case 6 : color = Color.ORANGE; 
			case 7 : color = Color.PINK; 
			case 8 : color = Color.LIGHT_GRAY;
			case 9 : color = Color.GRAY;
			default: {
				red = randomGenerator.nextInt(255);
				green = randomGenerator.nextInt(255);
				blue = randomGenerator.nextInt(255);
				color = new Color(red, green, blue);
			}
		}
		colors.put(event, color);
		return color;		
	}
	
	public void setColor(int event, Color color){
		colors.put(event, color);
	}

	private Paint getColor(int row, int column) {
		
		int eventNumber = currentList.get(currentItem).getEventNumber();
		
		if(colors.containsKey(eventNumber))
			return colors.get(eventNumber);
		else 
			return super.getItemPaint(row, column);
	}
	
	public void update(){
		currentList.clear();
		currentItem = 0;
		for (int i = 0; i < eventList.size(); i++) {
			EventData eventData = eventList.get(i);
			if(eventData.getStartTimePeriod().getStart().after(startTime) &&
					eventData.getEndTimePeriod().getStart().before(endTime))
				currentList.add(eventData);
			else if(eventData.getStartTimePeriod().getStart().before(startTime) &&
					eventData.getEndTimePeriod().getStart().after(endTime))
				currentList.add(eventData);
		}
	}

	private boolean isItem(int row, int column) {

		if(!startTime.equals(domainAxis.getMinimumDate())){			
			startTime = domainAxis.getMinimumDate();
			endTime = domainAxis.getMaximumDate();
			update();	
		} 
		

		for (int i = 0; i < currentList.size(); i++) {
			
			EventData eventData = currentList.get(i);
			if(eventData.getChannelNumber()!=row) continue;
			Date date = new Date((long) getPlot().getDataset().getXValue(row, column));
			if(eventData.getStartTimePeriod().getStart().before(date) && eventData.getEndTimePeriod().getStart().after(date))
				currentItem = i;
			else
				continue;
//			if(eventData.getStartTimePeriod().getStart().before(startTime) && eventData.getEndTimePeriod().getStart().after(endTime))
//			{
//					currentItem = i;
//			}
//			else if(eventData.getStartIndex()>=column || eventData.getEndIndex()<=column) continue;
//			else currentItem = i;
			return true;
		}
		
		return false;
	}

	public void setEventList(List<EventData> eventList) {
		this.eventList = eventList;		
	}

	public void setDomainAxis(DateAxis domainAxis) {
		this.domainAxis = domainAxis;		
	}
}
