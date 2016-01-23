package com.marklalor.javasim.simulation.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import com.marklalor.javasim.text.filter.IntegerFilter;

public class SubFrame implements Minimizable
{	
    private JFrame frame;
    private Image image;
    
	public SubFrame(Image image)
	{
	    this.frame = new JFrame();
		this.frame.setAutoRequestFocus(false);
		this.image = image;
	}
	
	public JFrame getFrame()
    {
        return frame;
    }
	
	public Image getImage()
    {
        return image;
    }
	
    public void setSize(int width, int height)
    {
	    frame.setSize(width, height + frame.getInsets().top);
    }
    
    public Dimension getSize()
    {
        Dimension size = frame.getSize();
        return new Dimension(size.width, size.height - frame.getInsets().top);
    }

	@Override
	public void minimize()
	{
		if (frame.isVisible())
		    frame.setState(JFrame.ICONIFIED);
	}
	
	//TODO: remove and use the controls instead. Move this somewhere else...
    public static final int FILTER_NONE = -1;
    public static final int FILTER_INTEGER = 0;
    public static JPanel labeledField(String label, JTextField textField, int filter)
    {
        if (filter == FILTER_INTEGER)
        {
            PlainDocument doc = (PlainDocument) textField.getDocument();
            doc.setDocumentFilter(new IntegerFilter());
        }
        JPanel group = new JPanel(new BorderLayout());
        group.add(new JLabel(label), BorderLayout.WEST);
        group.add(textField, BorderLayout.CENTER);
        return group;
    }
}
