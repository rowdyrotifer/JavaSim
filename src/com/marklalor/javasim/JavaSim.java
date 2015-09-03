package com.marklalor.javasim;

import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JavaSim
{
	public static final File location = new File(System.getProperty("user.home") + File.separator + "Documents" + File.separator + "JavaSim");
	public static void main(String[] args)
	{
		if (System.getProperty("os.name").toLowerCase().startsWith("mac os x"))
		{
			try
			{
	            System.setProperty("apple.laf.useScreenMenuBar", "true");
	            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    }
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		makeSureFolderExists();
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				Home home = new Home(location);
				home.setSize(800, 500);
				home.setVisible(true);
			}
		});
	}
	
	private static void makeSureFolderExists()
	{
		location.mkdirs();
	}
}
