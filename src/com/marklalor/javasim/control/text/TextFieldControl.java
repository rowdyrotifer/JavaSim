package com.marklalor.javasim.control.text;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import com.marklalor.javasim.control.Control;

public abstract class TextFieldControl<T> extends Control<T>
{
	private String defaultLabel;
	private String defaultValue;
	private int defaultColumns;
	private Class<? extends DocumentFilter> filter;
	
	private JLabel label;
	protected JTextField textField;
	
	public TextFieldControl()
	{
		this("");
	}
	
	public TextFieldControl(String label)
	{
		this(label, "");
	}
	
	public TextFieldControl(String label, String value)
	{
		this(label, value, null);
	}
	
	public TextFieldControl(String label, String value, Class<? extends DocumentFilter> filter)
	{
		this(label, value, filter, 2);
	}
	
	public TextFieldControl(String label, String value, Class<? extends DocumentFilter> filter, int columns)
	{
		this.defaultLabel = label;
		this.defaultValue = value;
		this.filter = filter;
		this.defaultColumns = columns;
	}
	
	@Override
	public JPanel createPanel()
	{	
		JPanel panel = new JPanel(new BorderLayout());
		label = new JLabel(defaultLabel);
		panel.add(label, BorderLayout.WEST);
		
		textField = new JTextField(defaultValue, defaultColumns);
		
		if (filter != null)
		{
			PlainDocument document = (PlainDocument) textField.getDocument();
			
		    try
			{
				document.setDocumentFilter(filter.newInstance());
			}
			catch(InstantiationException e)
			{
				e.printStackTrace();
			}
			catch(IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		
		panel.add(textField, BorderLayout.CENTER);
		panel.setMaximumSize(panel.getPreferredSize());
		
		return panel;
	}
	
	public JTextField getTextField()
	{
		return textField;
	}
	
	public void setTextFieldWidth(int columns)
	{
		getTextField().setColumns(columns);
		getPanel().setMaximumSize(getPanel().getPreferredSize());
		getTextField().revalidate();
	}
	
	public int getTextFieldWidth()
	{
		return getTextField().getColumns();
	}
}