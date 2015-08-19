package com.marklalor.javasim.simulation.frames.subframes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.frames.Image;
import com.marklalor.javasim.simulation.frames.ImageSubframe;

public class Animate extends ImageSubframe
{
	private static final long serialVersionUID = 2937807098313260272L;
	private JTextField startN, stopN;
	private JCheckBox startFromBeginning, stopAtBreakpoint;
	private JTextField startDelay, stopDelay;
	private JTextField frameDelay, saveEvery;
	private JCheckBox loop;
	private JTextField fileLocation;
	private JButton defaultFile, browseFile;
	private JButton animate, cancel;
	
	
	public Animate(Image image)
	{
		super(image);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setTitle("Animation Options");
		
		//Row 1
		JPanel p1 = new JPanel();
		startN =  new JTextField(4);
		startN.setHorizontalAlignment(JTextField.CENTER);
		startN.setText("0");
		startN.setEnabled(false);
		stopN =  new JTextField(4);
		stopN.setHorizontalAlignment(JTextField.CENTER);
		stopN.setText("auto");
		stopN.setEnabled(false);
		p1.add(ImageSubframe.labeledField("First Frame", startN, FILTER_INTEGER));
		p1.add(ImageSubframe.labeledField("Last Frame", stopN, FILTER_INTEGER));
		this.add(p1);
		
		//Row 2
		JPanel p2 = new JPanel();
		startFromBeginning = new JCheckBox("Start From Beginning");
		startFromBeginning.setSelected(true);
		startFromBeginning.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JCheckBox box = (JCheckBox)e.getSource();
				if (box.isSelected())
				{
					startN.setEnabled(false);
					startN.setText("0");
				}
				else
				{
					startN.setEnabled(true);
				}
			}
		});
		stopAtBreakpoint = new JCheckBox("Stop At Breakpoint");
		stopAtBreakpoint.setSelected(true);
		stopAtBreakpoint.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JCheckBox box = (JCheckBox)e.getSource();
				if (box.isSelected())
				{
					stopN.setEnabled(false);
					stopN.setText("auto");
				}
				else
				{
					stopN.setEnabled(true);
					stopN.setText("0");
				}
			}
		});
		p2.add(startFromBeginning);
		p2.add(stopAtBreakpoint);
		this.add(p2);
		
		//Row 3
		JPanel p3 = new JPanel();
		startDelay =  new JTextField(4);
		startDelay.setHorizontalAlignment(JTextField.CENTER);
		startDelay.setText("10");
		stopDelay =  new JTextField(4);
		stopDelay.setHorizontalAlignment(JTextField.CENTER);
		stopDelay.setText("10");
		p3.add(ImageSubframe.labeledField("Initial Frame Delay (ms)", startDelay, FILTER_INTEGER));
		p3.add(ImageSubframe.labeledField("End Frame Delay (ms)", stopDelay, FILTER_INTEGER));
		this.add(p3);
		
		this.add(new JSeparator());
		
		//Row 4
		JPanel p4 = new JPanel();
		frameDelay =  new JTextField(4);
		frameDelay.setHorizontalAlignment(JTextField.CENTER);
		frameDelay.setText("10");
		p4.add(ImageSubframe.labeledField("Intermediate Frame Delay (ms)", frameDelay, FILTER_INTEGER));
		saveEvery = new JTextField(2);
		saveEvery.setHorizontalAlignment(JTextField.CENTER);
		saveEvery.setText("5");
		p4.add(ImageSubframe.labeledField("Save Every…", saveEvery, FILTER_INTEGER));
		loop = new JCheckBox("Loop");
		loop.setSelected(true);
		p4.add(loop);
		this.add(p4);
		
		this.add(new JSeparator());
		
		//Row 5
		JPanel p5 = new JPanel();
		fileLocation = new JTextField(20);
		fileLocation.setText(getDefaultText());
		p5.add(ImageSubframe.labeledField("File: ", fileLocation, FILTER_NONE));
		browseFile = new JButton("Browse…");
		browseFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("Gif Images", "gif"));
				chooser.setSelectedFile(getFile());
		        if (chooser.showSaveDialog(Animate.this) == JFileChooser.APPROVE_OPTION)
		        {
		        	String file = chooser.getSelectedFile().getAbsolutePath();
		        	if (!chooser.getSelectedFile().getName().contains("."))
		        		file += ".gif";
		            fileLocation.setText(file);
		        }
			}
		});
		
		p5.add(browseFile);
		defaultFile = new JButton("Use Default");
		defaultFile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				fileLocation.setText(getDefaultText());
			}
		});
		p5.add(defaultFile);
		this.add(p5);
		
		this.add(new JSeparator());
		
		//Row 6
		JPanel p6 = new JPanel();
		animate = new JButton("Animate!");
		animate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Animate.this.getImage().getSimulation().animate();
				Animate.this.setVisible(false);
			}
		});
		p6.add(animate);
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Animate.this.setVisible(false);
			}
		});
		p6.add(cancel);
		p6.setAlignmentX(RIGHT_ALIGNMENT);
		this.add(p6);
		
		pack();
		
		this.setResizable(false);

		getRootPane().setDefaultButton(animate);
		animate.requestFocus();
	}
	
	private String getDefaultText()
	{
		return new File(this.getImage().getSimulation().getContentDirectory(), "animation_" + Simulation.getTimestamp() + ".gif").getAbsolutePath();
	}
	
	public int getStartFrame()
	{
		return Integer.parseInt(startN.getText());
	}
	
	public int getStopFrame()
	{
		if (stopN.getText().equals("auto"))
			return -1;
		return Integer.parseInt(stopN.getText());
	}
	
	public boolean getStartFromBeginning()
	{
		return startFromBeginning.isSelected();
	}
	
	public boolean getStopAtBreakpoint()
	{
		return stopAtBreakpoint.isSelected();
	}

	public int getStartDelay()
	{
		return Integer.parseInt(startDelay.getText());
	}
	
	public int getStopDelay()
	{
		return Integer.parseInt(stopDelay.getText());
	}
	
	public int getFrameDelay()
	{
		return Integer.parseInt(frameDelay.getText());
	}
	
	public int getSaveEvery()
	{
		return Integer.parseInt(saveEvery.getText());
	}
	
	public boolean getLoop()
	{
		return loop.isSelected();
	}
	
	public File getFile()
	{
		return new File(fileLocation.getText());
	}
}