package plots;
import java.awt.Graphics;

import abs.JPlotMarker;
import abs.JPointPlot;


public class JScatterPlot extends JPointPlot {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JScatterPlot() {
		super();
		super.title = "Scatter Plot";
	}

	@Override
	protected void drawData(Graphics g) {
		
		
		for (int i = 0; i < assocMarkers.size(); ++i)
		{
			float[] xData = xDataSets.get(i);
			float[] yData = yDataSets.get(i);
			JPlotMarker marker = assocMarkers.get(i);
			for (int j = 0; j < xData.length; ++j)
			{
				marker.plotPoint(g, convertX(xData[j]), convertY(yData[j]));
			}
		}
		
	}
	
}
