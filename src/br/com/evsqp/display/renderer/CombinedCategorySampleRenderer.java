package br.com.evsqp.display.renderer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.category.CategoryDataset;
import org.jfree.util.PublicCloneable;
import org.jfree.util.ShapeUtilities;

public class CombinedCategorySampleRenderer extends
		AbstractCategoryItemRenderer implements Cloneable, PublicCloneable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -96985153640225354L;

	@Override
	public void drawItem(Graphics2D g2, CategoryItemRendererState state,
			Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis,
			ValueAxis rangeAxis, CategoryDataset dataset, int row, int column,
			int pass) {

		// first check the number we are plotting...
		Number value = dataset.getValue(row, column);
		if (value != null) {
			
			// current data point...
			double x1 = domainAxis.getCategoryMiddle(column, getColumnCount(),
					dataArea, plot.getDomainAxisEdge());
			double y1 = rangeAxis.valueToJava2D(value.doubleValue(), dataArea,
					plot.getRangeAxisEdge());
			double zero = rangeAxis.valueToJava2D(0, dataArea, 
					plot.getRangeAxisEdge());

			g2.setPaint(getItemPaint(row, column));
			g2.setStroke(getItemStroke(row, column));
			Shape shape = getItemShape(row, column);
			Shape top = null;

			PlotOrientation orient = plot.getOrientation();

			if (orient == PlotOrientation.VERTICAL) {
				g2.draw(new Line2D.Double(x1, zero, x1, y1));
				top = ShapeUtilities.createTranslatedShape(shape, x1, y1);
			} else {
				g2.draw(new Line2D.Double(zero, x1, y1, x1));
				top = ShapeUtilities.createTranslatedShape(shape, y1, x1);
			}

			g2.fill(top);

			// collect entity and tool tip information...
			if (state.getInfo() != null) {
				EntityCollection entities = state.getEntityCollection();
				if (entities != null && shape != null) {
					String tip = null;
					CategoryToolTipGenerator tipster = getToolTipGenerator(row,
							column);
					if (tipster != null) {
						tip = tipster.generateToolTip(dataset, row, column);
					}
					CategoryItemEntity entity = new CategoryItemEntity(
							top, tip, null, dataset,
							dataset.getRowKey(row),
							dataset.getColumnKey(column));
					// CategoryItemEntity entity = new CategoryItemEntity(shape,
					// tip, null, dataset, row,
					// dataset.getColumnKey(column), column);
					entities.add(entity);
				}
			}
		}

	}
}