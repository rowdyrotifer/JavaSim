package com.marklalor.javasim.text;

/**
 * 
 * A simple Java Console for your application (Swing version)
 * Requires Java 1.1.5 or higher
 * 
 * Disclaimer the use of this source is at your own risk. 
 * 
 * Permission to use and distribute into your own applications
 * 
 * RJHM van den Bergh , rvdb@comweb.nl
 * 
 * Modified by Mark Lalor to integrate with JavaSim
 * 
 **/

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.simulation.frames.Minimizable;

public class Console extends JFrame implements Minimizable
{
	private static final long serialVersionUID = 6318364263842210392L;

	private class ConsoleAdapter extends WindowAdapter implements WindowListener, Runnable
	{
		public synchronized void windowClosed(WindowEvent evt)
		{
			quit = true;
			this.notifyAll(); // stop all threads
			try
			{
				outThread.join(1000);
				outInputStream.close();
			}
			catch(Exception e)
			{
			}
			try
			{
				errThread.join(1000);
				errInputStream.close();
			}
			catch(Exception e)
			{
			}
			System.exit(0);
		}
		
		public synchronized void run()
		{
			try
			{
				while(Thread.currentThread() == outThread)
				{
					try
					{
						this.wait(100);
					}
					catch(InterruptedException ie)
					{
					}
					if(outInputStream.available() != 0)
					{
						String input = this.readLine(outInputStream);
						textArea.append(input);
					}
					if(quit)
						return;
				}
				
				while(Thread.currentThread() == errThread)
				{
					try
					{
						this.wait(100);
					}
					catch(InterruptedException ie)
					{
					}
					if(errInputStream.available() != 0)
					{
						String input = this.readLine(errInputStream);
						textArea.append(input);
					}
					if(quit)
						return;
				}
			}
			catch(Exception e)
			{
				textArea.append("Internal console error: " + e);
			}
		}
		
		public synchronized String readLine(PipedInputStream in) throws IOException
		{
			String input = "";
			do
			{
				int available = in.available();
				if(available == 0)
					break;
				byte b[] = new byte[available];
				in.read(b);
				input = input + new String(b, 0, b.length);
			}
			while(!input.endsWith("\n") && !input.endsWith("\r\n") && !quit);
			return input;
		}
	}
	
	private JTextArea textArea;
	
	private Thread outThread;
	private Thread errThread;
	
	private boolean quit;
	
	private final PipedInputStream outInputStream = new PipedInputStream();
	private final PipedInputStream errInputStream = new PipedInputStream();
	
	private ConsoleAdapter adapter;
	
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
		
		adapter = new ConsoleAdapter();
		
		addWindowListener(adapter);
		
		try
		{
			PipedOutputStream pipedOut = new PipedOutputStream(this.outInputStream);
			if (JavaSim.CONSOLE_BIND) System.setOut(new PrintStream(pipedOut, true));
		}
		catch(java.io.IOException io)
		{
			textArea.append("Couldn't redirect STDOUT to this console\n" + io.getMessage());
		}
		catch(SecurityException se)
		{
			textArea.append("Couldn't redirect STDOUT to this console\n" + se.getMessage());
		}
		
		try
		{
			PipedOutputStream pipedOut = new PipedOutputStream(this.errInputStream);
			if (JavaSim.CONSOLE_BIND) System.setErr(new PrintStream(pipedOut, true));
		}
		catch(java.io.IOException io)
		{
			textArea.append("Couldn't redirect STDERR to this console\n" + io.getMessage());
		}
		catch(SecurityException se)
		{
			textArea.append("Couldn't redirect STDERR to this console\n" + se.getMessage());
		}
		
		quit = false; // signals the Threads that they should exit
		
		// Starting two seperate threads to read from the PipedInputStreams
		outThread = new Thread(adapter);
		outThread.setDaemon(true);
		outThread.start();
		
		errThread = new Thread(adapter);
		errThread.setDaemon(true);
		errThread.start();
		
		System.out.println("Console Started… JavaSim version: " + JavaSim.getVersion());
		
		if (!JavaSim.CONSOLE_BIND)
		{
			textArea.append("STDOUT/STDERR not bound to this console.\nTo bind, run without the \"noconsolebind\" argument.");
		}
	}

	@Override
	public void minimize()
	{
		if (isVisible())
			setState(ICONIFIED);
	}
}