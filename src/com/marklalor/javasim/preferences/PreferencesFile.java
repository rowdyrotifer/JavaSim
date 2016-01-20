package com.marklalor.javasim.preferences;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;

public class PreferencesFile
{	
    /**
     * <p>preferences.json file</p>
     * <p>For example, on OS X: <code>/Users/username/Library/Application Support/JavaSim/preferences.json</code></p>
     */
    private final File file;
    
    //ApplicationPreferences parent wrapper class.
	private ApplicationPreferences applicationPreferences;
	
	private File mainDirectory;
    private File saveDirectory;
	
	private List<File> simulationFiles;
	private Level logLevel;
	
	public PreferencesFile(File preferences, ApplicationPreferences applicationPreferences)
	{
		this.applicationPreferences = applicationPreferences;
		this.file = preferences;
		
		if (file.exists())
        {
            parse();
        }
        else
        {
            fillDefaults();
            save();
        }   
		
		mainDirectory.mkdirs();
		saveDirectory.mkdirs();
	}

	private void parse()
	{
		
	}

	private void fillDefaults()
	{
	    saveDirectory = new File(
	            System.getProperty("user.home") + 
	            File.separator + "Documents" + 
	            File.separator + "JavaSim");
	    
		if (getApplicationPreferences().isMacOSX())
		    mainDirectory = new File(
		            System.getProperty("user.home")  + 
		            File.separator + "Library" + 
		            File.separator + "Application Support" + 
		            File.separator + "JavaSim" + 
		            File.separator + "Simulations");
		else if (getApplicationPreferences().isWindows())
		    mainDirectory = new File(
		            System.getenv("APPDATA") + 
		            File.separator + "JavaSim" + 
		            File.separator + "Simulations");
		else if (getApplicationPreferences().isLinux())
		    mainDirectory = new File(
		            File.separator + "var" + 
		            File.separator + "lib" + 
		            File.separator + "javasim" + 
		            File.separator + "simulations");
		else
		    mainDirectory = new File(
		            System.getProperty("user.home") + 
		            File.separator + "Documents" + 
		            File.separator + "JavaSim" + 
                    File.separator + "Simulations");
		
		logLevel = Level.INFO;
	}

	private void save()
	{
		// TODO Auto-generated method stub
	}

	public ApplicationPreferences getApplicationPreferences()
	{
		return applicationPreferences;
	}

	public File getSaveDirectory()
	{
		return saveDirectory;
	}
	
	public void setSaveDirectory(File saveDirectory)
	{
		this.saveDirectory = saveDirectory;
	}
	
	public File getMainDirectory()
	{
		return mainDirectory;
	}
	
	public void setMainDirectory(File mainDirectory)
	{
		this.mainDirectory = mainDirectory;
	}
	
	public List<File> getSimulationFiles()
	{
		return simulationFiles;
	}
	
	public void setSimulationFiles(List<File> simulationFiles)
	{
		this.simulationFiles = simulationFiles;
	}
	
	public Level getLogLevel()
	{
		return logLevel;
	}
	
	public void setLogLevel(Level logLevel)
	{
		this.logLevel = logLevel;
	}
}
