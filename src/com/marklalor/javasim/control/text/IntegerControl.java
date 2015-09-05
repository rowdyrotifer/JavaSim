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
		return Integer.parseInt(textField.getText());
	}
}
