import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import plots.JLinePlot;

public class Test {
	
	public static float[] getRandomDataSet(int spread, float bias) {
		Random rd = new Random();
		float[] data = new float[7];
		for (int i = 0; i < 7; ++i) {
			data[i] = spread * rd.nextFloat() + bias;
		}
		return data;
	}

	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setLayout(new BorderLayout());
		JLinePlot js = new JLinePlot();
		js.addDataSet(getRandomDataSet(100, 100), getRandomDataSet(100, 100));
		js.setColor(0, Color.BLUE);
		js.addDataSet(getRandomDataSet(100, 300), getRandomDataSet(100, 300), new TestMarker());
		js.addDataSet(getRandomDataSet(100, 600), getRandomDataSet(100, 600));
		js.xLabel = "Age";
		js.yLabel = "Height";
		
		
		JToolBar toolbar = new JToolBar();
		toolbar.add(new JButton("Penis"));
		window.add(toolbar, BorderLayout.NORTH);
		
		
		window.add(js, BorderLayout.CENTER);
		window.setSize(js.getWidth() + 15, js.getHeight() + 35 + (int) toolbar.getPreferredSize().getHeight());
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		try {
			Thread.sleep(2000);
			//js.setPPU(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
