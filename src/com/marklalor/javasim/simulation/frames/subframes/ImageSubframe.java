package com.marklalor.javasim.simulation.frames.subframes;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import com.marklalor.javasim.simulation.frames.Image;
import com.marklalor.javasim.text.filter.IntegerFilter;

public class ImageSubframe extends JDialog
{	
	private static final long serialVersionUID = 6751475825307229601L;
	
	private Image image;
	
	public ImageSubframe(Image image)
	{
		super(image);
		setAutoRequestFocus(false);
		this.image = image;
	}
	
	public Image getImage()
	{
		return image;
	}
	
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
