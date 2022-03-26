package abs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import exceptions.ImproperDataFormatException;
import misc.JDefaultMarker;

public abstract class JPointPlot extends JPlot {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private float xMax = 10, xMin = 0, yMax = 10, yMin = 0, xSpacing, ySpacing;
	
	private int ppu = 50;
	
	private Dimension preferredSize = new Dimension(Math.round(rightMargin + leftMargin + (xMax - xMin) / xSpacing * ppu)
	, Math.round(topMargin + bottomMargin + (yMax - yMin) / ySpacing * ppu + (float) editorBar.getPreferredSize().getHeight()));
	
	protected ArrayList<float[]> xDataSets;
	protected ArrayList<float[]> yDataSets;
	protected ArrayList<JPlotMarker> assocMarkers;
	
	public JPointPlot() {
		super();
		xDataSets = new ArrayList<>();
		yDataSets = new ArrayList<>();
		assocMarkers = new ArrayList<>();
		legend.setMarkers(assocMarkers);
		addUIComponents();
	}
	
	private void addUIComponents() {
		editorBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		try {
			editorBar.add(new JButton(new ImageIcon(ImageIO.read(new File("res\\drag.png")))));
			editorBar.add(new JButton(new ImageIcon(ImageIO.read(new File("res\\zoom.png")))));
			editorBar.add(new JButton(new ImageIcon(ImageIO.read(new File("res\\axis.png")))));
			editorBar.add(new JButton(new ImageIcon(ImageIO.read(new File("res\\clipboard.png")))));
			repaint();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		return Math.round((float) editorBar.getPreferredSize().getHeight() + topMargin + (yMax - y) / ySpacing * ppu);
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
	public Dimension getPreferredSize() {
		return preferredSize;
	}
	
	@Override 
	public int getWidth() {
		return Math.round(rightMargin + leftMargin + (xMax - xMin) / xSpacing * ppu);
	}
	
	@Override
	public int getHeight() {
		return Math.round(topMargin + bottomMargin + (yMax - yMin) / ySpacing * ppu + (float) editorBar.getPreferredSize().getHeight());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		editorBar.repaint();
		drawEdge(g);
		int legendStartHeight = drawTicks(g);
		drawData(g);
		
		if (legend.visible) { 
			legend.drawLegend(g, leftMargin, legendStartHeight, (int) ((xMax - xMin) / xSpacing * ppu));
		}
		
		drawTitleAndLabels(g);
		
	}
	
	private void drawEdge(Graphics g) {
		
		int i = convertX(xMin), j = convertX(xMax), k = convertY(yMin), l = convertY(yMax);
		
		// Left border
		g.drawLine(i, l, i, k);
		
		// Top border
		g.drawLine(i, l, j, l);
		
		// Bottom border
		g.drawLine(i, k, j, k);
		
		// Right border
		g.drawLine(j, l, j, k);
	}
	
	private int drawTicks(Graphics g) {

		FontMetrics fm = g.getFontMetrics();
		
		int tickLength = 5, k = fm.getHeight();
		for (float f = yMin; f <= yMax; f += ySpacing) {
			int j = convertY(f);
			g.drawLine(convertX(xMin), j, convertX(xMin) + tickLength, j);
			String label = Float.toString(f);
			g.drawString(label, leftMargin - fm.stringWidth(label) - 5, j + k / 2 - 2);
		}
		
		int i = convertY(yMin);
		
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
		int y = (convertY(yMax)+ topMargin) / 2;
		
		g.setColor(Color.BLACK);
		g.drawString(title, x, y);
		
		x = Math.round(leftMargin + ((xMax - xMin) / xSpacing * ppu) / 2.0f - fm.stringWidth(xLabel) / 2.0f);
		y = convertY(yMin) + 2 * fm.getHeight();
		g.drawString(xLabel, x, y);
		
		AffineTransform at = new AffineTransform();
		at.rotate(3 * Math.PI / 2);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setTransform(at);
		
		x = -1 * Math.round(1.30f * ((yMax - yMin) / ySpacing * ppu) / 2.0f + topMargin +  fm.stringWidth(yLabel) / 2.0f  + (float) editorBar.getPreferredSize().getHeight());
		y = 30;
		
		g2d.drawString(yLabel, x, y);
	}
	
	protected abstract void drawData(Graphics g);
	
}
