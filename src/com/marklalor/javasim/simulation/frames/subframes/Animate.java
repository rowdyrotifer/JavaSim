package com.marklalor.javasim.simulation.frames.subframes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.marklalor.javasim.control.Control;
import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.frames.Image;
import com.marklalor.javasim.simulation.frames.ImageSubframe;

public class Animate extends ImageSubframe
{
	private static final long serialVersionUID = 2937807098313260272L;
	
	private JTextField startN, stopN;
	private JCheckBox startFromBeginning, stopAtBreakpoint;
	
	private JComboBox<String> controlSelection; 
	private JButton addControl;
	private JPanel controlsPanel;
	private List<Control<?>> addedControls;
	
	private JTextField startDelay, stopDelay;
	private JTextField frameDelay, saveEvery;
	private JCheckBox loop;
	private JTextField fileLocation;
	private JButton defaultFile, browseFile;
	private JButton animate, cancel;
	
	private JPanel normalTab, variableTab;
	private JTabbedPane tabbledPane;
	
	public Animate(Image image)
	{
		super(image);
		setTitle("Animation Options");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		tabbledPane = new JTabbedPane();
		normalTab = createNormalTab();
		tabbledPane.addTab("Normal", normalTab);
		variableTab = createVariableTab();
		tabbledPane.addTab("Variable", variableTab);
		this.add(tabbledPane);
		
		// Row 2
		JPanel p3 = new JPanel();
		startDelay = new JTextField(4);
		startDelay.setHorizontalAlignment(JTextField.CENTER);
		startDelay.setText("10");
		stopDelay = new JTextField(4);
		stopDelay.setHorizontalAlignment(JTextField.CENTER);
		stopDelay.setText("10");
		p3.add(ImageSubframe.labeledField("Initial Frame Delay (ms)", startDelay, FILTER_INTEGER));
		p3.add(ImageSubframe.labeledField("End Frame Delay (ms)", stopDelay, FILTER_INTEGER));
		this.add(p3);
		
		// Row 3
		JPanel p4 = new JPanel();
		frameDelay = new JTextField(4);
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
		
		// Row 4
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
				if(chooser.showSaveDialog(Animate.this) == JFileChooser.APPROVE_OPTION)
				{
					String file = chooser.getSelectedFile().getAbsolutePath();
					if(!chooser.getSelectedFile().getName().contains("."))
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
		
		// Row 5
		JPanel p6 = new JPanel();
		animate = new JButton("Animate!");
		animate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (tabbledPane.getSelectedComponent().equals(normalTab))
				{
					Animate.this.getImage().getSimulation().animate();
					Animate.this.setVisible(false);
				}
				else if (tabbledPane.getSelectedComponent().equals(variableTab))
				{
					Animate.this.getImage().getSimulation().animateVariable();
					Animate.this.setVisible(false);
				}
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
	
	private JPanel createNormalTab()
	{
		JPanel normal = new JPanel();
		// Normal
		normal.setLayout(new BoxLayout(normal, BoxLayout.Y_AXIS));
		
		// Row 1
		JPanel p1 = new JPanel();
		startN = new JTextField(4);
		startN.setHorizontalAlignment(JTextField.CENTER);
		startN.setText("0");
		startN.setEnabled(false);
		stopN = new JTextField(4);
		stopN.setHorizontalAlignment(JTextField.CENTER);
		stopN.setText("auto");
		stopN.setEnabled(false);
		p1.add(ImageSubframe.labeledField("First Frame", startN, FILTER_INTEGER));
		p1.add(ImageSubframe.labeledField("Last Frame", stopN, FILTER_INTEGER));
		normal.add(p1);
		
		// Row 2
		JPanel p2 = new JPanel();
		startFromBeginning = new JCheckBox("Start From Beginning");
		startFromBeginning.setSelected(true);
		startFromBeginning.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JCheckBox box = (JCheckBox) e.getSource();
				if(box.isSelected())
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
				JCheckBox box = (JCheckBox) e.getSource();
				if(box.isSelected())
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
		normal.add(p2);
		
		return normal;
	}
	
	private JPanel createVariableTab()
	{
		JPanel variable = new JPanel();
		addedControls = new ArrayList<Control<?>>(getImage().getSimulation().getControls().getControls().size()); //should be able to know max length.
		
		// Normal
		variable.setLayout(new BoxLayout(variable, BoxLayout.Y_AXIS));
		
		Map<String, Control<?>> controls = getImage().getSimulation().getControls().getControls();
		
		String[] keys = new String[controls.keySet().size()];
		List<String> keyList = new ArrayList<String>(controls.keySet());
		Collections.sort(keyList);
		keyList.toArray(keys);
		
		// Row 1
		JPanel p1 = new JPanel();
		controlSelection = new JComboBox<String>(keys);
		p1.add(controlSelection);
		
		addControl = new JButton("Add");
		addControl.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Control<?> newControl = Animate.this.getImage().getSimulation().getControls().getControls().get(String.valueOf(Animate.this.controlSelection.getSelectedItem()));
				JPanel controlPanel = newControl.createAnimatePanel();
				if (controlPanel != null)
				{
					controlsPanel.add(controlPanel);
					Animate.this.addedControls.add(newControl);
					Animate.this.pack();
				}
			}
		});
		p1.add(addControl);
		
		variable.add(p1);
		
		//Row 2 (controls panel)
		controlsPanel = new JPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		controlsPanel.setBorder(BorderFactory.createDashedBorder(new Color(200, 200, 200), 4f, 1f));
		
		variable.add(controlsPanel);
		
		return variable;
	}
	
	public List<Control<?>> getAddedControls()
	{
		return addedControls;
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
		if(stopN.getText().equals("auto"))
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