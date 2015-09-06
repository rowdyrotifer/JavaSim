package com.marklalor.javasim.control.text;

import javax.swing.JPanel;

import com.marklalor.javasim.text.filter.DoubleFilter;

public class DoubleControl extends TextFieldControl<Double>
{
	public DoubleControl(String name, String label, String initialValue)
	{
		super(name, label, initialValue, DoubleFilter.class);
	}
	
	@Override
	public Double getValue()
	{
		if (textField.getText().isEmpty())
			return 0d;
		return Double.parseDouble(textField.getText());
	}
	
	@Override
	public boolean setValue(Object value)
	{
		if (!value.getClass().equals(Double.class))
			throw new RuntimeException("Tried to set value of " + getClass().getSimpleName() + " to a " + value.getClass().getSimpleName());
		textField.setText(String.valueOf(value));
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
