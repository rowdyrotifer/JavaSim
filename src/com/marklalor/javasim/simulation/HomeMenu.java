package com.marklalor.javasim.simulation;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import com.marklalor.javasim.Home;
import com.marklalor.javasim.JavaSim;

public class HomeMenu implements ActionListener
{
	private Home home;
	
	private JMenuBar menuBar;
	
	private JMenu file;
	private JMenuItem openSimulation, openHomeFolder, refreshSimulations, close;
	private JMenu window;
	private JMenuItem minimize, showConsole;
	
	public JMenuBar getMenuBar()
	{
		return menuBar;
	}
	
	public HomeMenu(Home home)
	{
		this.home = home;
		
		menuBar = new JMenuBar();
		
		file = new JMenu("File");
		 
		// Open Simulation – Command + O
		openSimulation = new JMenuItem("Open Simulation");
		openSimulation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		openSimulation.addActionListener(this);
		file.add(openSimulation);
		
		file.addSeparator();
		
		// Open Home Folder – Command + D
		openHomeFolder = new JMenuItem("Open Home Folder");
		openHomeFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		openHomeFolder.addActionListener(this);
		file.add(openHomeFolder);
		
		// Refresh Simulations – F5
		refreshSimulations = new JMenuItem("Refresh Simulations");
		refreshSimulations.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		refreshSimulations.addActionListener(this);
		file.add(refreshSimulations);
		
		file.addSeparator();
		
		// Close – Command + W
		close = new JMenuItem("Close");
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		close.addActionListener(this);
		file.add(close);
		
		menuBar.add(file);
		
		window = new JMenu("Window");
		
		// Minimize – Command + M
		minimize = new JMenuItem("Minimize");
		minimize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		minimize.addActionListener(this);
		window.add(minimize);
		
		// Show Console – Command + J
		showConsole = new JMenuItem("Show Console");
		showConsole.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		showConsole.addActionListener(this);
		window.add(showConsole);
		
		menuBar.add(window);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JavaSim.getLogger().info("{}\u2192{}", ((JMenu) ((JPopupMenu) ((JMenuItem) e.getSource()).getParent()).getInvoker()).getText(), ((JMenuItem) e.getSource()).getText());
		
		if(e.getSource() == openSimulation)
		{
			
		}
		else if(e.getSource() == openHomeFolder)
		{
			try
			{
				Desktop.getDesktop().open(getHome().getPreferences().getMainDirectory());
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == refreshSimulations)
		{
		    getHome().loadSimulations();
		}
		else if(e.getSource() == close)
		{
			getHome().dispose();
		}
		else if (e.getSource() == minimize)
		{
			home.minimize();
		}
		else if (e.getSource() == showConsole)
		{
			getHome().getConsole().setVisible(true);
		}
	}
	
	public Home getHome()
	{
		return home;
	}
}
