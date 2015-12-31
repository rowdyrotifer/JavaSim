package com.marklalor.javasim.control.text;

import javax.swing.JPanel;

public class StringControl extends TextFieldControl<String>
{
	public StringControl(String name, String label, String initialValue)
	{
		super(name, label, initialValue, null);
	}

	@Override
	public String getValue()
	{
		return textField.getText();
	}

	@Override
	public boolean setValue(Object value)
	{
		if (!value.getClass().equals(String.class))
			throw new RuntimeException("Tried to set value of " + getClass().getSimpleName() + " to a " + value.getClass().getSimpleName());
		
		textField.setText((String)value);
		return true;
	}
	
	@Override
	public JPanel createAnimatePanel()
	{
		return null;
	}

	@Override
	public String[] getAnimateValues()
	{
		return null;
	}
}
