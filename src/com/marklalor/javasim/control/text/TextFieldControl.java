package com.marklalor.javasim.control.text;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import com.marklalor.javasim.JavaSim;
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
        this(null);
    }
    
    public TextFieldControl(String name)
    {
        this(name, "");
    }
    
    public TextFieldControl(String name, String label)
    {
        this(name, label, null);
    }
    
    public TextFieldControl(String name, String label, String value)
    {
        this(name, label, value, null);
    }
    
    public TextFieldControl(String name, String label, String value, Class<? extends DocumentFilter> filter)
    {
        this(name, label, value, filter, 2);
    }
    
    public TextFieldControl(String name, String label, String value, Class<? extends DocumentFilter> filter, int columns)
    {
        super(name);
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
        
        if(filter != null)
        {
            PlainDocument document = (PlainDocument) textField.getDocument();
            
            try
            {
                document.setDocumentFilter(filter.newInstance());
            }
            catch(Exception e)
            {
                JavaSim.getLogger().error("Error while setting filter on a TextFieldControl.", e);
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
