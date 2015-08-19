package com.marklalor.javasim.simulation.frames.subframes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.marklalor.javasim.simulation.frames.Image;
import com.marklalor.javasim.simulation.frames.ImageSubframe;

public class Resize extends ImageSubframe
{
	private static final long serialVersionUID = -8535874700179225843L;
	
	private JTextField resizeWidth, resizeHeight;
	private int initialWidth, initialHeight;
	private JCheckBox preserveAspectRatio;
	private JButton cancel, resize;
	
	private boolean ignoreNextWidth = false;
	private boolean ignoreNextHeight = false;
	
	public Resize(Image image)
	{
		super(image);
		
		this.addComponentListener(new ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{
				initialWidth = Resize.this.getImage().getSimulation().getWidth();
				initialHeight = Resize.this.getImage().getSimulation().getHeight();
				resizeWidth.setText(String.valueOf(initialWidth));
				resizeHeight.setText(String.valueOf(initialHeight));
			}
		});
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setTitle("Resize Image");
		
		//Row 1
		JPanel p1 = new JPanel();
		resizeWidth =  new JTextField(5);
		resizeWidth.setHorizontalAlignment(JTextField.CENTER);
		
		resizeHeight =  new JTextField(5);
		resizeHeight.setHorizontalAlignment(JTextField.CENTER);
		
		preserveAspectRatio = new JCheckBox("Preserve Aspect Ratio");
		preserveAspectRatio.setSelected(false);
		
		p1.add(ImageSubframe.labeledField("Width", resizeWidth, FILTER_INTEGER));
		p1.add(ImageSubframe.labeledField("Height", resizeHeight, FILTER_INTEGER));
		p1.add(preserveAspectRatio);
		this.add(p1);
		
		//Row 2
		JPanel p2 = new JPanel();
		resize = new JButton("Resize");
		resize.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Resize.this.getImage().setSize(getResizeWidth(), getResizeHeight());
				Resize.this.setVisible(false);
			}
		});
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Resize.this.setVisible(false);
			}
		});

		p2.add(resize);
		p2.add(cancel);
		p2.setAlignmentX(RIGHT_ALIGNMENT);
		this.add(p2);
		
		DocumentListener resizeInputListener = new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) { check(e); }
			public void removeUpdate(DocumentEvent e) { check(e); }
			public void insertUpdate(DocumentEvent e) { check(e); }
			
			public void check(DocumentEvent e)
			{
				boolean isWidth = e.getDocument() == resizeWidth.getDocument();
				
				if (isWidth)
				{
					if(resizeWidth.getText().isEmpty())
						return;
					
					if (Resize.this.ignoreNextWidth)
					{
						Resize.this.ignoreNextWidth = false;
						return;
					}
				}
				else
				{
					if(resizeHeight.getText().isEmpty())
						return;
					
					if (Resize.this.ignoreNextHeight)
					{
						Resize.this.ignoreNextHeight = false;
						return;
					}
				}
				
				if (!shouldPreserveAspectRatio())
				{
					if (isWidth)
						initialWidth = Integer.valueOf(resizeWidth.getText());
					else
						initialHeight = Integer.valueOf(resizeHeight.getText());
				}
				else
				{
					final JTextField changed = (isWidth ? resizeWidth : resizeHeight);
					final JTextField toChange = (isWidth ? resizeHeight : resizeWidth);
					final int changedInitial = (isWidth ? initialWidth : initialHeight);
					final int toChangeInitial = (isWidth ? initialHeight :initialWidth);
					
					final int newValue = Integer.valueOf(changed.getText());
					final float percent = (float)changedInitial / newValue;
					final int calcHeight = Math.round(toChangeInitial / percent);
					
					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							if (toChange == resizeWidth)
								Resize.this.ignoreNextWidth = true;
							else
								Resize.this.ignoreNextHeight = true;
							toChange.setText(String.valueOf(calcHeight));
						}
					});
				}
			}
		};

		resizeWidth.getDocument().addDocumentListener(resizeInputListener);
		resizeHeight.getDocument().addDocumentListener(resizeInputListener);
		
		getRootPane().setDefaultButton(resize);
		
		this.setResizable(false);
		
		pack();
	}
	
	public int getResizeWidth()
	{
		return Integer.parseInt(resizeWidth.getText());
	}
	
	public int getResizeHeight()
	{
		return Integer.parseInt(resizeHeight.getText());
	}
	
	public boolean shouldPreserveAspectRatio()
	{
		return preserveAspectRatio.isSelected();
	}
}
