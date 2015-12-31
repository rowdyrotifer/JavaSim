package com.marklalor.javasim.control.text;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import com.marklalor.javasim.text.filter.IntegerFilter;

public class IntegerControl extends TextFieldControl<Integer>
{
	public IntegerControl(String name, String label, String initialValue)
	{
		super(name, label, initialValue, IntegerFilter.class);
	}
	
	@Override
	public Integer getValue()
	{
		if (textField.getText().isEmpty())
			return 0;
		return Integer.parseInt(textField.getText());
	}
	
	@Override
	public boolean setValue(Object value)
	{
		if (!value.getClass().equals(Integer.class))
			throw new RuntimeException("Tried to set value of " + getClass().getSimpleName() + " to a " + value.getClass().getSimpleName());
		textField.setText(String.valueOf(value));
		return true;
	}

	private JTextField start, end, intervals; //to be used
	
	@Override
	public JPanel createAnimatePanel()
	{
		JPanel animate = new JPanel();
		
		animate.add(new JLabel("Animate " + getName() + " from "));
		start = new JTextField(4);
		PlainDocument startDocument = (PlainDocument) start.getDocument();
	    startDocument.setDocumentFilter(new IntegerFilter());
	    animate.add(start);
	    animate.add(new JLabel(" to "));
	    end = new JTextField(4);
		PlainDocument endDocument = (PlainDocument) end.getDocument();
		endDocument.setDocumentFilter(new IntegerFilter());
	    animate.add(end);
	    animate.add(new JLabel(" over "));
	    intervals = new JTextField("0", 4);
		PlainDocument intervalsDocument = (PlainDocument) intervals.getDocument();
		intervalsDocument.setDocumentFilter(new IntegerFilter());
	    animate.add(intervals);
	    animate.add(new JLabel("intervals."));
		
		return animate;
	}

	@Override
	public Integer[] getAnimateValues()
	{
		int endValue = val(end), startValue = val(start), intervalsValue = (val(intervals) - 1);
		
		
		
		double interval = (endValue - startValue) / ((double)intervalsValue);
		if (intervalsValue == -1)
		{
			intervalsValue = (endValue - startValue) + 1;
			interval = 1;
		}
		
		Integer[] values = new Integer[intervalsValue + 1];
		
		for (int n = 0; n < intervalsValue; n++)
		{
			values[n] = new Integer((int)Math.round(startValue + (n * interval)));
		}
		values[intervalsValue] = new Integer(endValue); //TODO: this for doubles
		
		
		return values;
	}
	
	private int val(JTextField field)
	{
		return field.getText().isEmpty() ? 0 : Integer.valueOf(field.getText());
	}
}
