package com.marklalor.javasim.simulation.frames;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.PlainDocument;

import com.marklalor.javasim.IntegerFilter;
import com.marklalor.javasim.simulation.Simulation;

public class Animate extends JDialog
{
	private static final long serialVersionUID = 2937807098313260272L;
	private Image image;
	private JTextField startN, stopN;
	private JCheckBox startFromBeginning, stopAtBreakpoint;
	private JTextField startDelay, stopDelay;
	private JTextField frameDelay;
	private JCheckBox loop;
	private JTextField fileLocation;
	private JButton defaultFile, browseFile;
	private JButton animate, cancel;
	
	
	public Animate(Image image)
	{
		super(image);
		this.image = image;
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
		p1.add(labeledIntegerField("First Frame", startN, true));
		p1.add(labeledIntegerField("Last Frame", stopN, true));
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
		startDelay.setText("0");
		stopDelay =  new JTextField(4);
		stopDelay.setHorizontalAlignment(JTextField.CENTER);
		stopDelay.setText("0");
		p3.add(labeledIntegerField("Initial Frame Delay (ms)", startDelay, true));
		p3.add(labeledIntegerField("End Frame Delay (ms)", stopDelay, true));
		this.add(p3);
		
		this.add(new JSeparator());
		
		//Row 4
		JPanel p4 = new JPanel();
		frameDelay =  new JTextField(4);
		frameDelay.setHorizontalAlignment(JTextField.CENTER);
		frameDelay.setText("10");
		p4.add(labeledIntegerField("Intermediate Frame Delay (ms)", frameDelay, true));
		loop = new JCheckBox("Loop");
		loop.setSelected(true);
		p4.add(loop);
		this.add(p4);
		
		this.add(new JSeparator());
		
		//Row 5
		JPanel p5 = new JPanel();
		fileLocation = new JTextField(20);
		fileLocation.setText(getDefaultText());
		p5.add(labeledIntegerField("File: ", fileLocation, false));
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
				Animate.this.image.getSimulation().animate();
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
	}

	public File getFile()
	{
		return new File(fileLocation.getText());
	}

	private String getDefaultText()
	{
		return new File(image.getSimulation().getContentDirectory(), "animation_" + Simulation.getTimestamp() + ".gif").getAbsolutePath();
	}

	private JPanel labeledIntegerField(String label, JTextField textField, boolean integer)
	{
		if (integer)
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
