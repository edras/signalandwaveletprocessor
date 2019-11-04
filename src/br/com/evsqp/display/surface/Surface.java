package br.com.evsqp.display.surface;

import java.io.IOException;

import net.masagroup.jzy3d.bridge.ChartLauncher;
import net.masagroup.jzy3d.chart.Chart;
import net.masagroup.jzy3d.colors.Color;
import net.masagroup.jzy3d.colors.ColorMapper;
import net.masagroup.jzy3d.colors.colormaps.ColorMapRainbow;
import net.masagroup.jzy3d.maths.Range;
import net.masagroup.jzy3d.maths.Scale;
import net.masagroup.jzy3d.plot3d.builder.Builder;
import net.masagroup.jzy3d.plot3d.builder.Mapper;
import net.masagroup.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import net.masagroup.jzy3d.plot3d.primitives.Shape;
import net.masagroup.jzy3d.plot3d.primitives.faces.ColorbarFace;
import net.masagroup.jzy3d.plot3d.rendering.canvas.Quality;


public class Surface {
	
	Mapper mapper;
	Range rangeX;
	Range rangeY;
	int stepX;
	int stepY;	
	
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	public void setRangeX(Range rangeX) {
		this.rangeX = rangeX;
	}

	public void setRangeY(Range rangeY) {
		this.rangeY = rangeY;
	}

	public void setStepX(int stepX) {
		this.stepX = stepX;
	}

	public void setStepY(int stepY) {
		this.stepY = stepY;
	}

	public Surface(){
		
	}
	
	public static void main(String[] args) throws IOException{
		
		Surface surface = new Surface();
			
		
		surface.setMapper(new Mapper(){
			public double f(double x, double y) {
				double sigma = 1;
				return Math.exp( -(x*x+y*y) / sigma  )  *  Math.abs( 2*Math.cos( Math.PI * ( x*x + y*y ) ) );
			}
		});
		
		surface.setRangeX(new Range(-1.5, 1.5));
		surface.setRangeY(new Range(-1.5, 1.5));
		surface.setStepX(35);
		surface.setStepY(35);		
		
		//ChartLauncher.openChart(surface.getChart(), new Rectangle(0,200,400,400), "Surface");
		ChartLauncher.openChart(surface.getChart());
	}
	
	public Chart getChart(){

		// Create the object to represent the function over the given range.
		final Shape surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(rangeX, stepX, rangeY, stepY), mapper);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax()));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);
		
		// Create a chart that updates the surface colormapper when scaling changes
		Chart chart = new Chart(){
			public void setScale(Scale scale){
				super.setScale(scale);
				ColorMapper cm = surface.getColorMapper();
				cm.setScale(scale);
				surface.setColorMapper(cm);
			}
		};
		
		chart.getScene().getGraph().add(surface);
		
		// Setup a colorbar for the surface object and add it to the scene
		chart.getScene().getGraph().add(surface);
		surface.setFace(new ColorbarFace(surface, 
							chart.getView().getAxe().getLayout().getZTickProvider(), 
							chart.getView().getAxe().getLayout().getZTickRenderer()));
		surface.setFace2dDisplayed(true); // opens a colorbar on the right part of the display
				
		return chart;
	}

}
