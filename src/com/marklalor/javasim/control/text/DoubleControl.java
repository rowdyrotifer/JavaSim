package com.marklalor.javasim.control.text;

import com.marklalor.javasim.text.filter.DoubleFilter;

public class DoubleControl extends TextFieldControl<Double>
{
	public DoubleControl(String label, String initialValue)
	{
		super(label, initialValue, DoubleFilter.class);
	}
	
	@Override
	public Double getValue()
	{
		if (textField.getText().isEmpty())
			return 0d;
		return Double.parseDouble(textField.getText());
	}
	
	@Override
	public boolean setValue(Double value)
	{
		textField.setText(String.valueOf(value));
		return true;
	}
}
