package abs;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	
	// TODO Create logarithmic scale
	// TODO Create drag feature
	// TODO Create zoom feature
	// TODO Create 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR), selectCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
	
	protected float xMax = 10, xMin = 0, yMax = 10, yMin = 0, xSpacing, ySpacing;
	
	private int ppuX = 50, ppuY = 50;
	
	private Dimension preferredSize = new Dimension(Math.round(rightMargin + leftMargin + (xMax - xMin) / xSpacing * ppuX)
	, Math.round(topMargin + bottomMargin + (yMax - yMin) / ySpacing * ppuY + (float) editorBar.getPreferredSize().getHeight()));
	
	private byte mode = -1;
	private int x1, y1, x2, y2;
	private JButton zoomButton = null;
	
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
		
		JButton dragButton = null, axisButton = null, dataButton = null, homeButton = null;
		
		try {
			dragButton = new JButton(new ImageIcon(ImageIO.read(new File("res\\drag.png"))));
			dragButton.addActionListener((ActionEvent e) -> {
				mode = 0;
			});
			
			homeButton = new JButton(new ImageIcon(ImageIO.read(new File("res\\home.png"))));
			homeButton.addActionListener((ActionEvent e) -> {});
			
			zoomButton = new JButton(new ImageIcon(ImageIO.read(new File("res\\zoom.png"))));
			zoomButton.addActionListener((ActionEvent e) -> {
				mode = 1;
				zoomButton.setEnabled(false);
				setCursor(selectCursor);
			});
			
			
			axisButton = new JButton(new ImageIcon(ImageIO.read(new File("res\\axis.png"))));
			axisButton.addActionListener((ActionEvent e) -> {
				
			});
			
			dataButton = new JButton(new ImageIcon(ImageIO.read(new File("res\\clipboard.png"))));
			dataButton.addActionListener((ActionEvent e) -> {
			});
		} catch(IOException e) {}
			
		editorBar.add(dragButton);
		editorBar.add(homeButton);
		homeButton.addActionListener((ActionEvent e) -> {
			
			
			
			repaint();
		});
		
		editorBar.add(zoomButton);
		editorBar.add(axisButton);
		editorBar.add(dataButton);
		
		addMouseMotionListener(new MouseAdapter() {
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if (mode != 1) {
					return;
				}
				
				if (e.getX() > convertX(xMin) && e.getX() < convertX(xMax) && e.getY() < convertY(yMin) && e.getY() > convertY(yMax)) {
					x2 = e.getX();
					y2 = e.getY();
				}
				repaint();
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (mode != 1) {
					return;
				}
				
				if (e.getX() > convertX(xMin) && e.getX() < convertX(xMax) && e.getY() < convertY(yMin) && e.getY() > convertY(yMax)) {
					x1 = e.getX();
					y1 = e.getY();
				}
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (mode != 1) {
					return;
				}
				setCursor(defaultCursor);
				mode = -1;
				zoomButton.setEnabled(true);
				
				doZoomTransformation();
				
				repaint();
			}
		});
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
		return leftMargin + Math.round((x - xMin) / xSpacing * ppuX);
	}
	
	protected int convertY(float y) {
		return Math.round((float) editorBar.getPreferredSize().getHeight() + topMargin + (yMax - y) / ySpacing * ppuY);
	}
	
	protected float convertX(int x) {
		return xMin + (x - leftMargin) * xSpacing / ppuX;
	}
	
	protected float convertY(int y) {
		return Math.round(yMax - (y - (float) editorBar.getPreferredSize().getHeight() - topMargin) * ySpacing / ppuY);
	}
	
	private void doZoomTransformation() {
		float topLeftX = convertX(Math.min(x1, x2)), topLeftY = convertY(Math.min(y1, y2));
		float bottomRightX = convertX(Math.max(x1, x2)), bottomRightY = convertY(Math.max(y1, y2));
		
		float prevNumPixelX = (xMax - xMin) * ppuX / xSpacing;
		
		xMin = topLeftX;
		xMax = bottomRightX;
		
		float prevNumPixelY = (yMax - yMin) * ppuY / ySpacing;
		
		yMax = topLeftY;
		yMin = bottomRightY;
		
		xSpacing = determineXSpacing();
		ySpacing = determineYSpacing();
		
		ppuX = Math.round(prevNumPixelX * xSpacing / (xMax - xMin));
		ppuY = Math.round(prevNumPixelY * ySpacing / (yMax - yMin));
	}

	private float determineXSpacing() {
		int[] multiples = new int[] {1, 2, 5};
		
		float spacing, base = 1.0f;
		
		while (true) {
			
			boolean toolarge = false;
			
			for (int i : multiples) {
				spacing = base * i;
				
				float numFill = (xMax - xMin) / spacing;
				
				if (numFill < 4) {
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
			
			boolean toolarge = false;
			
			for (int i : multiples) {
				spacing = base * i;
				
				float numFill = (yMax - yMin) / spacing;
				
				if (numFill < 4) {
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
	
	public void setPPU(int ppuX, int ppuY) {
		this.ppuX = ppuX;
		this.ppuY = ppuY;
		repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return preferredSize;
	}
	
	@Override 
	public int getWidth() {
		return Math.round(rightMargin + leftMargin + (xMax - xMin) / xSpacing * ppuX);
	}
	
	@Override
	public int getHeight() {
		return Math.round(topMargin + bottomMargin + (yMax - yMin) / ySpacing * ppuY + (float) editorBar.getPreferredSize().getHeight());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		editorBar.repaint();
		drawEdge(g);
		int legendStartHeight = drawTicks(g);
		drawData(g);
		
		if (legend.visible) { 
			legend.drawLegend(g, leftMargin, legendStartHeight, (int) ((xMax - xMin) / xSpacing * ppuX));
		}
		
		drawTitleAndLabels(g.create());
		
		if (mode == 1) {
			g.setColor(Color.LIGHT_GRAY);
			g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
		}
		
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
		int x = Math.round(leftMargin + ((xMax - xMin) / xSpacing * ppuX) / 2.0f - fm.stringWidth(title) / 2.0f);
		int y = (convertY(yMax)+ topMargin) / 2;
		
		g.setColor(Color.BLACK);
		g.drawString(title, x, y);
		
		x = Math.round(leftMargin + ((xMax - xMin) / xSpacing * ppuX) / 2.0f - fm.stringWidth(xLabel) / 2.0f);
		y = convertY(yMin) + 2 * fm.getHeight();
		g.drawString(xLabel, x, y);
		
		AffineTransform at = new AffineTransform();
		at.rotate(3 * Math.PI / 2);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setTransform(at);
		
		x = -1 * Math.round(1.30f * ((yMax - yMin) / ySpacing * ppuY) / 2.0f + topMargin +  fm.stringWidth(yLabel) / 2.0f  + (float) editorBar.getPreferredSize().getHeight());
		y = 30;
		
		g2d.drawString(yLabel, x, y);
	}
	
	protected abstract void drawData(Graphics g);
	
}
