package com.marklalor.javasim;

import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaSim
{
	public static final File location = new File(System.getProperty("user.home") + File.separator + "Documents" + File.separator + "JavaSim");
	public static boolean CONSOLE_BIND = true;
	
	private static Logger logger;
	
	public static Logger getLogger()
	{
		return logger;
	}
	
	public static void main(final String[] args)
	{
		JavaSim.logger = LoggerFactory.getLogger(JavaSim.class);	
		
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
				Home home = new Home(location, args);
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
