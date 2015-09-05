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
	
	public CheckboxControl(String label, boolean checked)
	{
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
	public boolean setValue(Boolean value)
	{
		checkbox.setSelected(value);
		return true;
	}
}
