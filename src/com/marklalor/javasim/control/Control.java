package com.marklalor.javasim.control;

import javax.swing.JPanel;

public abstract class Control<T>
{
	private JPanel panel;
	
	protected abstract JPanel createPanel();
	
	public JPanel getPanel()
	{
		if (this.panel == null)
			this.panel = createPanel();
		
		return this.panel;
	}
	
	public abstract T getValue();
	public abstract boolean setValue(T value);
}
