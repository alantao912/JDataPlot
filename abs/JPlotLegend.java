package abs;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

public class JPlotLegend {
	
	private ArrayList<String> dataSetLabels;
	private ArrayList<JPlotMarker> markers;
	
	private final int textHeight = 16;
	
	public boolean visible = true;
	
	
	public JPlotLegend() {
		dataSetLabels = new ArrayList<>();
	}
	
	protected void addLabel(String label) {
		dataSetLabels.add(label);
	}
	
	public void setLabel(int dataSetIndex, String label) {
		dataSetLabels.set(dataSetIndex, label);
	}
	
	protected void setMarkers(ArrayList<JPlotMarker> markers) {
		this.markers = markers;
	}
	
	protected void drawLegend(Graphics g, int x, int y, int plotWidth) {
		FontMetrics fm = g.getFontMetrics();
		int accum = 0;
		
		for (String str : dataSetLabels) {
			accum += 8;
			accum += 2;
			accum += fm.stringWidth(str);
			accum += 10;
		}
		
		if (accum > plotWidth) {
			
		} else {
			g.setColor(Color.BLACK);
			x += (plotWidth - accum) / 2;
			g.drawRect(x, y, accum, textHeight + 4);
			x += 5;
			y += (textHeight + 4) / 2;
			
			for (int i = 0; i < dataSetLabels.size(); ++i) {
				x += 4;
				JPlotMarker marker = markers.get(i);
				marker.plotPoint(g, x, y + 1);
				x += 4;
				x += 2;
				g.setColor(Color.BLACK);
				
				String label = dataSetLabels.get(i);
				g.drawString(label, x, y + textHeight / 2 - 3);
				x += 10;
				x += fm.stringWidth(label);
			}
		}
		
	}

}
