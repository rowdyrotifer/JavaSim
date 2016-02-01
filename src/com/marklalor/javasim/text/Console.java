package com.marklalor.javasim.text;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.marklalor.javasim.Home;
import com.marklalor.javasim.simulation.frames.FrameHolder;

@SuppressWarnings("serial")
public class Console extends JFrame implements FrameHolder
{
	private Home home;
	
	private JTextArea textArea;
	
	public Console(Home home)
	{
		this.home = home;
		
		setTitle("JavaSim Console");
		setSize(500, 500);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		getContentPane().setLayout(new BorderLayout());
		
		JScrollPane textAreaScrollPane = new JScrollPane(textArea);
		textAreaScrollPane.setBorder(BorderFactory.createEmptyBorder());
		getContentPane().add(textAreaScrollPane, BorderLayout.CENTER);
		setAutoRequestFocus(false);
		
		if (!getHome().getApplicationPreferences().getConsoleBind())
			textArea.append("Logger not bound to this console.\nTo bind, run without the \"noconsolebind\" argument.");
	}

	public Home getHome()
	{
		return home;
	}

	public void append(String string)
	{
		textArea.append(string+"\n");
	}

	@Override
    public JFrame getFrame()
    {
        return this;
    }

    @Override
    public void setFrame(JFrame frame)
    {
        throw new RuntimeException("Cannot change the home frame. This will be removed in the future, when home will HAVE a JFrame / will not BE a JFrame.");
    }
}