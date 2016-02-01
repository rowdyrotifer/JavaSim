package com.marklalor.javasim.control.checkbox;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.marklalor.javasim.control.Control;

public class CheckboxControl extends Control<Boolean>
{
    private boolean defaultChecked;
    private String defaultLabel;
    private JCheckBox checkbox;
    
    public CheckboxControl(String name, String label, boolean checked)
    {
        super(name);
        defaultLabel = label;
        defaultChecked = checked;
    }
    
    @Override
    public JPanel createPanel()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        
        checkbox = new JCheckBox(defaultLabel, defaultChecked);
        
        panel.add(checkbox);
        
        panel.setMaximumSize(new Dimension(10000, (int) panel.getPreferredSize().getHeight()));
        
        return panel;
    }
    
    public JCheckBox getCheckbox()
    {
        return checkbox;
    }
    
    @Override
    public Boolean getValue()
    {
        return checkbox.isSelected();
    }
    
    @Override
    public boolean setValue(Object value)
    {
        if(!value.getClass().equals(Boolean.class))
            throw new RuntimeException("Tried to set value of " + getClass().getSimpleName() + " to a " + value.getClass().getSimpleName());
        
        checkbox.setSelected((Boolean) value);
        return true;
    }
    
    @Override
    public JPanel createAnimatePanel()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public Boolean[] getAnimateValues()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
