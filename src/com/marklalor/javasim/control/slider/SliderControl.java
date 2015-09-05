package com.marklalor.javasim.control.slider;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import com.marklalor.javasim.control.Control;

public class SliderControl extends Control<Double>
{
	private double min, max;
	private double defaultValue;
	private String label;
	private JLabel jLabel;
	private JSlider slider;
	
	private final int SLIDER_MAX = 10000; //10k different possible slider values, definitely enough to accomadate the screen pixels.
	
	public SliderControl(double min, double max, double value, String label)
	{
		this.min = min;
		this.max = max;
		this.defaultValue = value;
		this.label = label;
	}
	
	@Override
	protected JPanel createPanel()
	{
		JPanel panel = new JPanel(new BorderLayout());
		
		jLabel = new JLabel(label, SwingConstants.CENTER);
		panel.add(jLabel, BorderLayout.NORTH);
		
		slider = new JSlider(0, SLIDER_MAX);
		slider.setMajorTickSpacing(SLIDER_MAX);
	    slider.setPaintTicks(true);
	    Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
	    labelTable.put(new Integer(0), new JLabel(String.valueOf(min)));
	    labelTable.put(new Integer(SLIDER_MAX), new JLabel(String.valueOf(max)));
	    slider.setLabelTable(labelTable);
	    slider.setPaintLabels(true);
	    slider.setValue((int)Math.round((defaultValue / (max-min))*SLIDER_MAX));
	    panel.add(slider, BorderLayout.CENTER);
	    
	    panel.setMaximumSize(new Dimension(SLIDER_MAX, (int) panel.getPreferredSize().getHeight()));
	    
	    return panel;
	}
	
	public JSlider getSlider()
	{
		return slider;
	}
	
	@Override
	public Double getValue()
	{
		return Double.valueOf(min +((slider.getValue() / (double)SLIDER_MAX) * (max-min))); //min + %*range
	}
	
	@Override
	public boolean setValue(Double value)
	{
		if (value < min || value > max)
			return false;
		
		slider.setValue((int)Math.round((value / (max-min))*SLIDER_MAX));
		return true;
	}
}