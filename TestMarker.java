import java.awt.Color;
import java.awt.Graphics;

import abs.JPlotMarker;

public class TestMarker extends JPlotMarker {

	@Override
	public void plotPoint(Graphics g, int x, int y) {
		g.setColor(Color.GREEN);
		int len = 6;
		
		g.drawRect(x - len / 2, y - len / 2, len, len);
		
	}

}
