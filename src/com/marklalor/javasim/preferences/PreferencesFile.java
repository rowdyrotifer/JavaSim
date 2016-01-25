package com.marklalor.javasim.preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

public class PreferencesFile
{
    //ApplicationPreferences parent wrapper class.
	private transient ApplicationPreferences applicationPreferences;
	
	private File mainDirectory;
    private File saveDirectory;

    private List<File> simulationDirectories;
    private List<File> simulationFiles;
    
	private Level logLevel;
	
	//Initialization option for the deserializer which doesn't know the transient variables.
    public PreferencesFile() { }
	
	public PreferencesFile(ApplicationPreferences applicationPreferences)
	{   
		this.applicationPreferences = applicationPreferences;
	}

	public void fillDefaults()
	{
	    saveDirectory = new File(
	            System.getProperty("user.home") + 
	            File.separator + "Documents" + 
	            File.separator + "JavaSim");
        saveDirectory.mkdirs();
	    
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
		mainDirectory.mkdirs();
		
		simulationDirectories = new ArrayList<File>();
		
		simulationFiles = new ArrayList<File>();
		
		logLevel = Level.INFO;
	}

	public ApplicationPreferences getApplicationPreferences()
	{
		return applicationPreferences;
	}

    public void setApplicationPreferences(ApplicationPreferences applicationPreferences)
    {
        this.applicationPreferences = applicationPreferences;
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
	
	public List<File> getSimulationDirectories()
    {
        return simulationDirectories;
    }
	
	public void setSimulationDirectories(List<File> simulationDirectories)
    {
        this.simulationDirectories = simulationDirectories;
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
