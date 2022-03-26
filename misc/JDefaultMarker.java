package misc;

import java.awt.Graphics;
import java.awt.Color;

import abs.JPlotMarker;

public class JDefaultMarker extends JPlotMarker {
	
	public JDefaultMarker(Color color) {
		this.color = color;
	}
	
	public JDefaultMarker(int r, int g, int b) {
		this(new Color(r, g, b));
	}
	
	public JDefaultMarker() {
		this(Color.RED);
	}
	
	@Override 
	public void plotPoint(Graphics g, int x, int y) {
		int[] xp = new int[3], yp = new int[3];
		xp[0] = x;
		xp[1] = x - 3;
		xp[2] = x + 3;
		
		yp[0] = y - 3;
		yp[1] = y + 3;
		yp[2] = y + 3;
		g.setColor(color);
		
		g.drawPolygon(xp, yp, 3);
	}
}
