package abs;


import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public abstract class JPlot extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected JToolBar editorBar;
	
	public int topMargin = 60, rightMargin = 60, bottomMargin = 90, leftMargin = 80;
	public  String title, xLabel = "x - axis", yLabel = "y - axis";
	
	public final JPlotLegend legend;
	
	public JPlot() {
		legend = new JPlotLegend();
		editorBar = new JToolBar();
		editorBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout());
		add(editorBar, BorderLayout.NORTH);
	}
	
	
	protected float findSmallest(float[] arr) {
		float smallest = Float.POSITIVE_INFINITY;
		for (int i = 0; i < arr.length; ++i) {
			if (arr[i] < smallest) {
				smallest = arr[i];
			}
		}
		return smallest;
	}
	
	protected float findLargest(float[] arr) {
		float largest = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < arr.length; ++i) {
			if (arr[i] > largest) {
				largest = arr[i];
			}
		}
		return largest;
	}
	
	public void setLabel(int dataSetIndex, String name) {
		legend.setLabel(dataSetIndex, name);
	}
	
	protected void addLabel(String name) {
		legend.addLabel(name);
	}
}
