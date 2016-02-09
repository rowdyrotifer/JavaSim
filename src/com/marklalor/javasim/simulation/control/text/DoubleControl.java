package com.marklalor.javasim.simulation.control.text;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import com.marklalor.javasim.misc.filter.DoubleFilter;
import com.marklalor.javasim.misc.filter.IntegerFilter;

public class DoubleControl extends TextFieldControl<Double>
{
    public DoubleControl(String name, String label, String initialValue)
    {
        super(name, label, initialValue, DoubleFilter.class);
    }
    
    @Override
    public Double getValue()
    {
        if(textField.getText().isEmpty())
            return 0d;
        return Double.parseDouble(textField.getText());
    }
    
    @Override
    public boolean setValue(Object value)
    {
        if(!value.getClass().equals(Double.class))
            throw new RuntimeException("Tried to set value of " + getClass().getSimpleName() + " to a " + value.getClass().getSimpleName());
        textField.setText(String.valueOf(value));
        return true;
    }
    
    private JTextField start, end, intervals; // to be used
            
    @Override
    public JPanel createAnimatePanel()
    {
        JPanel animate = new JPanel();
        
        animate.add(new JLabel("Animate " + getName() + " from "));
        start = new JTextField(4);
        PlainDocument startDocument = (PlainDocument) start.getDocument();
        startDocument.setDocumentFilter(new DoubleFilter());
        animate.add(start);
        animate.add(new JLabel(" to "));
        end = new JTextField(4);
        PlainDocument endDocument = (PlainDocument) end.getDocument();
        endDocument.setDocumentFilter(new DoubleFilter());
        animate.add(end);
        animate.add(new JLabel(" over "));
        intervals = new JTextField("0", 4);
        PlainDocument intervalsDocument = (PlainDocument) intervals.getDocument();
        intervalsDocument.setDocumentFilter(new IntegerFilter());
        animate.add(intervals);
        animate.add(new JLabel("intervals."));
        
        return animate;
    }
    
    @Override
    public Double[] getAnimateValues()
    {
        double endValue = val(end), startValue = val(start);
        int intervalsValue = (int) val(intervals);
        
        double interval = (endValue - startValue) / ((double) intervalsValue);
        
        Double[] values = new Double[intervalsValue];
        
        for(int n = 0; n < intervalsValue; n++)
        {
            values[n] = new Double(startValue + (n * interval));
        }
        
        return values;
    }
    
    private double val(JTextField field)
    {
        return field.getText().isEmpty() ? 0d : Double.valueOf(field.getText());
    }
}
