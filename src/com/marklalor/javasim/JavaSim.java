package com.marklalor.javasim;

import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JavaSim
{
	public static final File location = new File(System.getProperty("user.home") + File.separator + "Documents" + File.separator + "JavaSim");
	public static boolean CONSOLE_BIND = true;
	
	public static void main(String[] args)
	{
		//Simple args for now.
		for (String arg : args)
		{
			if (arg.equals("noconsolebind"))
				CONSOLE_BIND = false;
		}
		
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
	
	public static String getVersion()
	{
		return JavaSim.class.getPackage().getSpecificationVersion();
	}
	
	private static void makeSureFolderExists()
	{
		location.mkdirs();
	}
}
