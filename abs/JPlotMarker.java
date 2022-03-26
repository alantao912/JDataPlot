package abs;
import java.awt.Color;
import java.awt.Graphics;

public abstract class JPlotMarker {
	
	public Color color;
	
	public abstract void plotPoint(Graphics g, int x, int y);
	
	
}
