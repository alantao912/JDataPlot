package abs;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import exceptions.ImproperDataFormatException;
import misc.JDefaultMarker;

public abstract class JPointPlot extends JPlot {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected ArrayList<float[]> xDataSets;
	protected ArrayList<float[]> yDataSets;
	protected ArrayList<JPlotMarker> assocMarkers;
	
	private float xMax = 10, xMin = 0, yMax = 10, yMin = 0, xSpacing, ySpacing;
	
	private int ppu = 70;
	
	public JPointPlot() {
		super();
		xDataSets = new ArrayList<>();
		yDataSets = new ArrayList<>();
		assocMarkers = new ArrayList<>();
		legend.setMarkers(assocMarkers);
	}
	
	public JPointPlot(float[] xData, float[] yData) {
		this();
		addDataSet(xData, yData, new JDefaultMarker());
		
	}
	
	public void addDataSet(float[] xData, float[] yData) {
		addDataSet(xData, yData, new JDefaultMarker());
	}
	
	public void addDataSet(float[] xData, float[] yData, JPlotMarker marker) {
		addDataSet(xData, yData, marker, "Data Set " + xDataSets.size());
	}
	
	public void addDataSet(float[] xData, float[] yData, String name) {
		addDataSet(xData, yData, new JDefaultMarker(), name);
	}
	
	public void addDataSet(float[] xData, float[] yData, JPlotMarker marker, String name) {
		if (xData.length != yData.length) {
			throw new ImproperDataFormatException("Length of argument xData does not equal length of argument yData");
		}
		
		if (xData.length != 0) {
			xDataSets.add(xData);
			yDataSets.add(yData);
			assocMarkers.add(marker);
			addLabel(name);
		} else {
			throw new ImproperDataFormatException("Length of inserted data must not be 0");
		}
		
		float newLargest = super.findLargest(xData);
		if (newLargest > xMax) {
			if (newLargest > 0) {
				xMax = 1.05f * newLargest;
			} else {
				xMax = 0.95f * newLargest;
			}
		}
		
		newLargest = super.findLargest(yData);
		if (newLargest > yMax) {
			if (newLargest > 0) {
				yMax = 1.05f * newLargest;
			} else {
				yMax = 0.95f * newLargest;
			}
		}
		
		newLargest = super.findSmallest(xData);
		if (newLargest < xMin) {
			if (xMin > 0) {
				xMin = 0.95f * newLargest;
			} else {
				xMin = 1.05f * newLargest;
			}
		}
		
		newLargest = super.findSmallest(yData);
		if (newLargest < yMin) {
			if (yMin > 0) {
				yMin = 0.95f * newLargest;
			} else {
				yMin = 1.05f * newLargest;
			}
		}
		
		xSpacing = determineXSpacing();
		ySpacing = determineYSpacing();
	}
	
	public void setColor(int dataSetIndex, Color c) {
		assocMarkers.get(dataSetIndex).color = c;
	}
	
	public JPlotLegend getLegend() {
		return legend;
	}
	
	protected int convertX(float x) {
		return leftMargin + Math.round((x - xMin) / xSpacing * ppu);
	}
	
	protected int convertY(float y) {
		return topMargin + Math.round((yMax - y) / ySpacing * ppu);
	}
	
	private float determineXSpacing() {
		int[] multiples = new int[] {1, 2, 5};
		
		float spacing, base = 1.0f;
		
		while (true) {
			spacing = base;
			
			boolean toolarge = false;
			
			for (int i : multiples) {
				spacing *= i;
				
				float numFill = (xMax - xMin) / spacing;
				
				if (numFill < 1) {
					toolarge = true;
					break;
				} else if (numFill < 10) {
					return spacing;
				}
			}
			
			if (toolarge) {
				base /= 10.0f;
			} else {
				base *= 10.0f;
			}
		}
	}
	
	private float determineYSpacing() {
		int[] multiples = new int[] {1, 2, 5};
		
		float spacing, base = 1.0f;
		
		while (true) {
			spacing = base;
			
			boolean toolarge = false;
			
			for (int i : multiples) {
				spacing *= i;
				
				float numFill = (yMax - yMin) / spacing;
				
				if (numFill < 1) {
					toolarge = true;
					break;
				} else if (numFill < 10) {
					return spacing;
				}
			}
			
			if (toolarge) {
				base /= 10.0f;
			} else {
				base *= 10.0f;
			}
		}
	}
	
	public void setPPU(int newPPU) {
		ppu = newPPU;
		repaint();
	}
	
	@Override 
	public int getWidth() {
		return super.rightMargin + super.leftMargin + (int) Math.round((xMax - xMin) / xSpacing * ppu);
	}
	
	@Override
	public int getHeight() {
		return super.topMargin + super.bottomMargin + (int) Math.round((yMax - yMin) / ySpacing * ppu);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawEdge(g);
		int legendStartHeight = drawTicks(g);
		drawData(g);
		
		if (legend.visible) { 
			legend.drawLegend(g, leftMargin, legendStartHeight, (int) ((xMax - xMin) / xSpacing * ppu));
		}
		
		drawTitleAndLabels(g);
		
	}
	
	private void drawEdge(Graphics g) {
		float vSpan = yMax - yMin, hSpan = xMax - xMin;
		
		// Left border
		g.drawLine(leftMargin, topMargin, leftMargin, topMargin + (int) (vSpan / ySpacing* ppu));
		
		// Top border
		g.drawLine(leftMargin, topMargin, leftMargin + (int) (hSpan / xSpacing * ppu), topMargin);
		
		// Bottom border
		g.drawLine(leftMargin, topMargin + (int) (vSpan / ySpacing * ppu), leftMargin + (int) (hSpan / xSpacing * ppu), topMargin + (int) (vSpan / ySpacing * ppu));
		
		// Right border
		g.drawLine(leftMargin + (int) (hSpan / xSpacing * ppu), topMargin, leftMargin + (int) (hSpan / xSpacing * ppu), topMargin + (int) (vSpan / ySpacing * ppu));
	}
	
	private int drawTicks(Graphics g) {

		FontMetrics fm = g.getFontMetrics();
		
		int tickLength = 5, k = fm.getHeight();
		for (float f = yMin; f <= yMax; f += ySpacing) {
			int j = convertY(f);
			g.drawLine(leftMargin, j, leftMargin + tickLength, j);
			String label = Float.toString(f);
			g.drawString(label, leftMargin - fm.stringWidth(label) - 5, j + k / 2 - 2);
		}
		
		int i = topMargin + (int) ((yMax - yMin) / ySpacing * ppu);
		
		for (float f = xMin; f <= xMax; f += xSpacing) {
			int j = convertX(f);
			g.drawLine(j, i, j, i - tickLength);
			String label = Float.toString(f);
			g.drawString(label, j - fm.stringWidth(label) / 2 , i + k);
		}
		return i + 2 * k + fm.getHeight();
	}
	
	private void drawTitleAndLabels(Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int x = Math.round(leftMargin + ((xMax - xMin) / xSpacing * ppu) / 2.0f - fm.stringWidth(title) / 2.0f);
		int y = (topMargin + fm.getHeight()) / 2;
		
		g.setColor(Color.BLACK);
		g.drawString(title, x, y);
		
		x = Math.round(leftMargin + ((xMax - xMin) / xSpacing * ppu) / 2.0f - fm.stringWidth(xLabel) / 2.0f);
		y = topMargin + (int) ((yMax - yMin) / ySpacing * ppu) + 2 * fm.getHeight();
		g.drawString(xLabel, x, y);
		
		AffineTransform at = new AffineTransform();
		at.rotate(3 * Math.PI / 2);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setTransform(at);
		
		x = -1 * Math.round(1.30f * ((yMax - yMin) / ySpacing * ppu) / 2.0f + topMargin +  fm.stringWidth(yLabel) / 2.0f);
		y = 30;
		
		g2d.drawString(yLabel, x, y);
	}
	
	protected abstract void drawData(Graphics g);
	
}
