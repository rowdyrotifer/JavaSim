package com.marklalor.javasim.control.text;

import com.marklalor.javasim.text.filter.IntegerFilter;

public class IntegerControl extends TextFieldControl<Integer>
{
	public IntegerControl(String label, String initialValue)
	{
		super(label, initialValue, IntegerFilter.class);
	}
	
	@Override
	public Integer getValue()
	{
		if (textField.getText().isEmpty())
			return 0;
		return Integer.parseInt(textField.getText());
	}
	
	@Override
	public boolean setValue(Integer value)
	{
		textField.setText(String.valueOf(value));
		return true;
	}
}
