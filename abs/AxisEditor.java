package abs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class AxisEditor extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField xAxisInput = new JTextField(), yAxisInput = new JTextField();
	private JTextField xLowerInput = new JTextField(), xUpperInput = new JTextField();
	private JTextField yLowerInput = new JTextField(), yUpperInput = new JTextField();
	
	private JRadioButton xAxisLinear = new JRadioButton("Linear"), xAxisLogarithmic = new JRadioButton("Logarithmic"), yAxisLinear = new JRadioButton("Linear"), yAxisLogarithmic = new JRadioButton("Logarithmic");
	private ButtonGroup xAxisBG = new ButtonGroup(), yAxisBG = new ButtonGroup();
	
	private JPointPlot parentPlot;
	
	protected boolean isXLogarithmic = false, isYLogarithmic = false;
	
	public AxisEditor(JPointPlot parent) {
		parentPlot = parent;
		
		
		int frameWidth = 550, frameHeight = 240;
		JLabel xAxisLabel = new JLabel("X-Axis:");
		xAxisLabel.setBounds(15, 35, 50, 25);
		xAxisInput.setBounds(70, 37, frameWidth / 2 + 15 - 105, 25);
		
		JLabel yAxisLabel = new JLabel("Y-Axis:");
		yAxisLabel.setBounds(frameWidth / 2 + 15, 35, 50, 25);
		yAxisInput.setBounds(frameWidth / 2 + 70, 37, 185, 25);
		
		JLabel frameTitle = new JLabel("Edit Plot Labels");
		
		frameTitle.setBounds((int) Math.round((frameWidth - frameTitle.getPreferredSize().getWidth())/2), 8, 100, 25);
		
		JPanel container = new JPanel();
		container.setLayout(null);
		container.add(frameTitle);
		container.add(xAxisLabel);
		container.add(xAxisInput);
		
		container.add(yAxisLabel);
		container.add(yAxisInput);
		
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				if (o == xAxisLinear) {
					isXLogarithmic = false;
				} else if (o == xAxisLogarithmic) {
					isXLogarithmic = true;
				} else if (o == yAxisLinear) {
					isYLogarithmic = false;
				} else if (o == yAxisLogarithmic) {
					isYLogarithmic = true;
				}
				parentPlot.repaint();
			}
		};
		
		xAxisLinear.addActionListener(al);
		xAxisLogarithmic.addActionListener(al);
		
		xAxisBG.add(xAxisLinear);
		xAxisLinear.setSelected(true);
		xAxisBG.add(xAxisLogarithmic);

		xAxisLinear.setBounds(65, 70, 65, 20);
		xAxisLogarithmic.setBounds(140, 70, 100, 20);
		container.add(xAxisLinear);
		container.add(xAxisLogarithmic);
		
		yAxisLinear.addActionListener(al);
		yAxisLogarithmic.addActionListener(al);
		
		yAxisBG.add(yAxisLinear);
		yAxisLinear.setSelected(true);
		yAxisBG.add(yAxisLogarithmic);
		
		yAxisLinear.setBounds(65 + frameWidth / 2, 70, 65, 20);
		yAxisLogarithmic.setBounds(140 + frameWidth / 2, 70, 100, 20);
		
		container.add(yAxisLinear);
		container.add(yAxisLogarithmic);
		
		JLabel xLower = new JLabel("Lower:"), xUpper = new JLabel("Upper:");
		xUpper.setBounds(70, 110, 39, 20);
		xLower.setBounds(70, 155, 39, 20);
		
		container.add(xLower);
		container.add(xUpper);
		
		JLabel yLower = new JLabel("Lower:"), yUpper = new JLabel("Upper:");
		yUpper.setBounds(70 + frameWidth / 2, 110, 39, 20);
		yLower.setBounds(70 + frameWidth / 2, 155, 39, 20);
		
		container.add(yLower);
		container.add(yUpper);
		
		xUpperInput.setBounds(115, 112, 120, 20);
		xLowerInput.setBounds(115, 157, 120, 20);
		
		container.add(xUpperInput);
		container.add(xLowerInput);
		
		yLowerInput.setBounds(115 + frameWidth / 2, 157, 120, 20);
		yUpperInput.setBounds(115 + frameWidth / 2, 112, 120, 20);
		
		container.add(yUpperInput);
		container.add(yLowerInput);
		
		KeyAdapter ka = new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!parentPlot.resized) {
						parentPlot.saveOriginalBounds();
						parentPlot.homeButton.setEnabled(true);
					}
					parentPlot.doZoomTransformation(Float.parseFloat(xLowerInput.getText()), Float.parseFloat(xUpperInput.getText()), 
							Float.parseFloat(yLowerInput.getText()), Float.parseFloat(yUpperInput.getText()));
					parentPlot.repaint();
				}
			}
		};
		
		xLowerInput.addKeyListener(ka);
		xUpperInput.addKeyListener(ka);
		
		yUpperInput.addKeyListener(ka);
		yLowerInput.addKeyListener(ka);
		
		KeyAdapter ka0 = new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					parentPlot.xLabel = xAxisInput.getText();
					parentPlot.yLabel = yAxisInput.getText();
					parentPlot.repaint();
				}
			}
		};
		
		xAxisInput.addKeyListener(ka0);
		yAxisInput.addKeyListener(ka0);
		
		add(container);
		setSize(frameWidth, frameHeight);
		setResizable(false);
	}
	
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		xAxisInput.setText(parentPlot.xLabel);
		yAxisInput.setText(parentPlot.yLabel);
		
		xUpperInput.setText(parentPlot.xMax + "");
		xLowerInput.setText(parentPlot.xMin + "");
		
		yUpperInput.setText(parentPlot.yMax + "");
		yLowerInput.setText(parentPlot.yMin + "");
	}
}
