package com.marklalor.javasim.control.text;

public class StringControl extends TextFieldControl<String>
{
	public StringControl(String label, String initialValue)
	{
		super(label, initialValue, null);
	}

	@Override
	public String getValue()
	{
		return textField.getText();
	}
}
