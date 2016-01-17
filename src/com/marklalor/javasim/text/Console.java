package com.marklalor.javasim.text;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.simulation.frames.Minimizable;

public class Console extends JFrame implements Minimizable
{
	private static final long serialVersionUID = 6318364263842210392L;
	
	private JTextArea textArea;
	
	public Console()
	{
		setTitle("JavaSim Console");
		setSize(500, 500);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		getContentPane().setLayout(new BorderLayout());
		
		JScrollPane textAreaScrollPane = new JScrollPane(textArea);
		textAreaScrollPane.setBorder(BorderFactory.createEmptyBorder());
		getContentPane().add(textAreaScrollPane, BorderLayout.CENTER);
		setAutoRequestFocus(false);
		
		if (!JavaSim.CONSOLE_BIND)
			textArea.append("STDOUT/STDERR not bound to this console.\nTo bind, run without the \"noconsolebind\" argument.");
	}

	@Override
	public void minimize()
	{
		if (isVisible())
			setState(ICONIFIED);
	}

	public void append(String string)
	{
		textArea.append(string+"\n");
	}
}