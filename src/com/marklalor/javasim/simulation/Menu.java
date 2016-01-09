package com.marklalor.javasim.simulation;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import com.marklalor.javasim.Home;

import apple.dts.samplecode.osxadapter.OSXAdapter; //TODO: apple Application class can replace?

public class Menu implements ActionListener
{
	private Simulation sim;
	private JFrame parent;
	
	private JMenuBar menuBar;
	
	private JMenu file;
	private JMenuItem newSimulation, reloadSimulation, saveImage, saveImageAs, animateMenuItem, openHomeFolder, openContentFolder, closeSimulation, openProperties;
	private JMenu edit;
	private JMenuItem copy;
	private JMenu animation;
	private JMenuItem play, playUntilBreakpoint, stop, nextFrame, decreaseSpeed, increaseSpeed;
	private JMenu simulation;
	private JMenuItem reset, resizeMenuItem, fullscreen, showConsole;
	
	public JMenuBar getMenuBar()
	{
		return menuBar;
	}
	
	public Menu(Simulation sim, JFrame parent)
	{
		this.sim = sim;
		this.parent = parent;
		
		menuBar = new JMenuBar();
		
		file = new JMenu("File");
		
		// New Simulation – Command + N
		newSimulation = new JMenuItem("New " + sim.getInfo().getName());
		newSimulation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		newSimulation.addActionListener(this);
		file.add(newSimulation);
		
		file.addSeparator();
		
		// Save – Command + S
		saveImage = new JMenuItem("Save Current Image");
		saveImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		saveImage.addActionListener(this);
		file.add(saveImage);
		
		// Save As – Command + Shift + S
		saveImageAs = new JMenuItem("Save Current Image As…");
		saveImageAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK)));
		saveImageAs.addActionListener(this);
		file.add(saveImageAs);
		
		// Create Animated Gif – Command + I
		animateMenuItem = new JMenuItem("Create Animated Gif");
		animateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		animateMenuItem.addActionListener(this);
		file.add(animateMenuItem);
		
		file.addSeparator();
		
		// Open Content Folder – Command + M
		openContentFolder = new JMenuItem("Open Content Folder");
		openContentFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		openContentFolder.addActionListener(this);
		file.add(openContentFolder);
		
		// Open Home Folder – Command + Shift + M
		openHomeFolder = new JMenuItem("Open Home Folder");
		openHomeFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK)));
		openHomeFolder.addActionListener(this);
		file.add(openHomeFolder);
		
		file.addSeparator();
		
		// Close – Command + W
		closeSimulation = new JMenuItem("Close Simulation");
		closeSimulation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		closeSimulation.addActionListener(this);
		file.add(closeSimulation);
		
		//Preferences
		if(Simulation.IS_MAC_OS_X)
		{
			try
			{
				OSXAdapter.setPreferencesHandler(sim, sim.getClass().getMethod("openPreferences", (Class[]) null));
				OSXAdapter.setQuitHandler(sim, sim.getClass().getMethod("delete", (Class[]) null));
			}
			catch(SecurityException e)
			{
				e.printStackTrace();
			}
			catch(NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			file.addSeparator();
			
			// Properties – No Shortcut
			openProperties = new JMenuItem("Properties");
			// openProperties.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
			// Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() ));
			openProperties.addActionListener(this);
			file.add(openProperties);
		}
		
		menuBar.add(file);
		
		edit = new JMenu("Edit");
		
		// Copy – Command + C
		copy = new JMenuItem("Copy");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		copy.addActionListener(this);
		edit.add(copy);
		
		menuBar.add(edit);
		
		animation = new JMenu("Animation");
		
		// Play – Command + P
		play = new JMenuItem("Play");
		play.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		play.addActionListener(this);
		animation.add(play);
		
		// Play Until Breakpoint – Command + Shift + P
		playUntilBreakpoint = new JMenuItem("Play Until Breakpoint");
		playUntilBreakpoint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.SHIFT_MASK)));
		playUntilBreakpoint.addActionListener(this);
		animation.add(playUntilBreakpoint);
		
		// Pause – Command + .
		stop = new JMenuItem("Stop");
		stop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		stop.addActionListener(this);
		animation.add(stop);
		
		animation.addSeparator();
		
		// Next Frame – Space
		nextFrame = new JMenuItem("Next Frame");
		nextFrame.setAccelerator(KeyStroke.getKeyStroke(' '));
		nextFrame.addActionListener(this);
		animation.add(nextFrame);
		
		animation.addSeparator();
		
		// Decrease Speed – [
		decreaseSpeed = new JMenuItem("Decrease Speed");
		decreaseSpeed.setAccelerator(KeyStroke.getKeyStroke('['));
		decreaseSpeed.addActionListener(this);
		animation.add(decreaseSpeed);
		
		// Increase Speed – ]
		increaseSpeed = new JMenuItem("Increase Speed");
		increaseSpeed.setAccelerator(KeyStroke.getKeyStroke(']'));
		increaseSpeed.addActionListener(this);
		animation.add(increaseSpeed);
		
		menuBar.add(animation);
		
		simulation = new JMenu("Simulation");
		
		// Reset – Command + R
		reset = new JMenuItem("Reset");
		reset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		reset.addActionListener(this);
		simulation.add(reset);
		
		// Reload Simulation – F5
		reloadSimulation = new JMenuItem("Reload");
		reloadSimulation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		reloadSimulation.addActionListener(this);
		simulation.add(reloadSimulation);
		
		simulation.addSeparator();
		
		// Resize – No Shortcut
		resizeMenuItem = new JMenuItem("Resize");
		// resize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
		// Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		resizeMenuItem.addActionListener(this);
		simulation.add(resizeMenuItem);
		
		// Fullscreen – Command + Shift + F (OSX) F11 (Windows)
		fullscreen = new JMenuItem("Enter Full Screen");
		if (Simulation.IS_MAC_OS_X)
			fullscreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.CTRL_MASK)));
		else
			fullscreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
		fullscreen.addActionListener(this);
		simulation.add(fullscreen);
		
		// Show Console – Command + J
		showConsole = new JMenuItem("Show Console");
		showConsole.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		showConsole.addActionListener(this);
		simulation.add(showConsole);
		
		menuBar.add(simulation);
	}

	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("[MENU] " + ((JMenu) ((JPopupMenu) ((JMenuItem) e.getSource()).getParent()).getInvoker()).getText() + " \u2192 " + ((JMenuItem) e.getSource()).getText());
		
		if(e.getSource() == newSimulation)
		{
			Home.run(sim.getHome(), sim.getInfo());
		}
		else if(e.getSource() == saveImage)
		{
			sim.save(sim.getDefaultFile());
		}
		else if(e.getSource() == saveImageAs)
		{
			sim.saveAs();
		}
		else if(e.getSource() == animateMenuItem)
		{
			sim.getAnimate().setLocationRelativeTo(sim.getImage());
			sim.getAnimate().setVisible(true);
		}
		else if(e.getSource() == openContentFolder)
		{
			try
			{
				Desktop.getDesktop().open(sim.getContentDirectory());
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == openHomeFolder)
		{
			try
			{
				Desktop.getDesktop().open(sim.getContentDirectory().getParentFile());
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == closeSimulation)
		{
			sim.delete();
		}
		else if(e.getSource() == openProperties)
		{
			sim.openPreferences();
		}
		else if(e.getSource() == copy)
		{
			sim.copyImageToClipboard();
		}
		else if(e.getSource() == play)
		{
			sim.setStopForBreakpoint(false);
			sim.play();
		}
		else if(e.getSource() == playUntilBreakpoint)
		{
			sim.setStopForBreakpoint(true);
			sim.play();
		}
		else if(e.getSource() == stop)
		{
			sim.stop();
		}
		else if(e.getSource() == nextFrame)
		{
			sim.incrementN();
			sim.draw();
		}
		else if(e.getSource() == decreaseSpeed)
		{
			sim.decreaseAnimationSpeed();
		}
		else if(e.getSource() == increaseSpeed)
		{
			sim.increaseAnimationSpeed();
		}
		else if(e.getSource() == reset)
		{
			sim.resetAction();
		}
		else if(e.getSource() == reloadSimulation)
		{
			Home.run(sim.getHome(), sim.getInfo());
			sim.delete();
		}
		else if (e.getSource() == resizeMenuItem)
		{
			sim.getResize().setLocationRelativeTo(sim.getImage());
			sim.getResize().setVisible(true);
		}
		else if (e.getSource() == fullscreen)
		{
			sim.toggleFullscreen();
		}
		else if (e.getSource() == showConsole)
		{
			sim.getHome().getConsole().setVisible(true);
		}
	}
	
	public JFrame getParent()
	{
		return parent;
	}
}
