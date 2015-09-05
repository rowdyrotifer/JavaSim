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
	private String label;
	private String initialValue;
	private Class<? extends DocumentFilter> filter;
	
	private JLabel jLabel;
	protected JTextField textField;
	
	public TextFieldControl(String label, String initialValue, Class<? extends DocumentFilter> filter)
	{
		this.label = label;
		this.initialValue = initialValue;
		this.filter = filter;
	}
	
	@Override
	public JPanel createPanel()
	{	
		JPanel panel = new JPanel(new BorderLayout());
		jLabel = new JLabel(label);
		panel.add(jLabel, BorderLayout.WEST);
		
		textField = new JTextField(initialValue);
		
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
}
