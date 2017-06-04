package com.marklalor.javasim.simulation.control.slider;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import com.marklalor.javasim.simulation.control.Control;

public class SliderControl extends Control<Double>
{
    private double min, max;
    private double defaultValue;
    private String label;
    private JLabel jLabel;
    private JSlider slider;
    
    private final int SLIDER_MAX = 10000; // 10k different possible slider
                                          // values, definitely enough to
                                          // accommodate the screen pixels.
    
    public SliderControl(String name, double min, double max, double value, String label)
    {
        super(name);
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
        final int maxChar = 6;
        labelTable.put(new Integer(0), new JLabel(String.valueOf(min).substring(0, String.valueOf(min).length() > maxChar ? maxChar : String.valueOf(min).length())));
        labelTable.put(new Integer(SLIDER_MAX), new JLabel(String.valueOf(max).substring(0, String.valueOf(max).length() > maxChar ? maxChar : String.valueOf(max).length())));
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        slider.setValue((int) Math.round((defaultValue / (max - min)) * SLIDER_MAX));
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
        return Double.valueOf(min + ((slider.getValue() / (double) SLIDER_MAX) * (max - min)));
    }
    
    @Override
    public boolean setValue(Object value)
    {
        if(!value.getClass().equals(Double.class))
            throw new RuntimeException("Tried to set value of " + getClass().getSimpleName() + " to a " + value.getClass().getSimpleName());
        
        Double doubleValue = (Double) value;
        
        if(doubleValue < min || doubleValue > max)
            return false;
        
        slider.setValue((int) Math.round((doubleValue / (max - min)) * SLIDER_MAX));
        return true;
    }
    
    @Override
    public JPanel createAnimatePanel()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Double[] getAnimateValues()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
