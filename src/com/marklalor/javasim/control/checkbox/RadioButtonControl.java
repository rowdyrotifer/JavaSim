package com.marklalor.javasim.control.checkbox;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.marklalor.javasim.control.Control;

public class RadioButtonControl extends Control<String>
{
	private String[] defaultLabels;
	private String defaultSelected;
	
	private ButtonGroup group;
	private JRadioButton[] buttons;
	
	public RadioButtonControl(String[] labels, String selected)
	{
		this.defaultLabels = labels;
		this.defaultSelected = selected;
	}

	@Override
	public JPanel createPanel()
	{
		JPanel panel = new JPanel();
		
		group = new ButtonGroup();
		buttons = new JRadioButton[defaultLabels.length];
		int i = 0;
		for (String label : defaultLabels)
		{
			buttons[i] = new JRadioButton(label);
			buttons[i].setActionCommand(label);
			group.add(buttons[i]);
			panel.add(buttons[i]);
			i++;
		}
		
		for (JRadioButton button : buttons)
			button.setSelected(button.getText().equals(defaultSelected));
		
		return panel;
	}

	@Override
	public String getValue()
	{
		return group.getSelection().getActionCommand();
	}
}
