package plots;

import java.awt.Graphics;
import java.util.Arrays;

import abs.JPlotMarker;
import abs.JPointPlot;
import exceptions.ImproperDataFormatException;

public class JLinePlot extends JPointPlot {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JLinePlot() {
		super();
		super.title = "Line Plot";
	}
	
	private boolean checkData(float[] xData) {
		Arrays.sort(xData);
		for (int i = 0; i < xData.length - 1; ++i) {
			if (xData[i] == xData[i + 1]) {
				return false;
			}
		}
		return true;
	}
	
	private void sortData(float[] xData, float[] yData) {
		for (int i = 0; i < xData.length - 1; ++i) {
			int smallestInd = i;
			
			for (int j = i + 1; j < xData.length; ++j) {
				if (xData[j] < xData[smallestInd]) {
					smallestInd = j;
				}
			}
			
			float swap = xData[i];
			xData[i] = xData[smallestInd];
			xData[smallestInd] = swap;
			
			swap = yData[i];
			yData[i] = yData[smallestInd];
			yData[smallestInd] = swap;
			
		}
	}
	
	@Override 
	public void addDataSet(float[] xData, float[] yData, JPlotMarker marker, String name) {
		if (!checkData(xData)) {
			throw new ImproperDataFormatException("Line plot data set must not contain duplicate x values.");
		}
		super.addDataSet(xData, yData, marker, name);
		sortData(xData, yData);
	}

	@Override
	protected void drawData(Graphics g) {
		for (int i = 0; i < assocMarkers.size(); ++i)
		{
			float[] xData = xDataSets.get(i);
			float[] yData = yDataSets.get(i);
			JPlotMarker marker = assocMarkers.get(i);
			int j;
			for (j = 0; j < xData.length - 1; ++j)
			{
				marker.plotPoint(g, convertX(xData[j]), convertY(yData[j]));
				g.drawLine(convertX(xData[j]), convertY(yData[j]), convertX(xData[j + 1]), convertY(yData[j + 1]));
			}
			marker.plotPoint(g, convertX(xData[j]), convertY(yData[j]));
		}
		
	}

}
