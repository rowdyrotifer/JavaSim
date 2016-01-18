package com.marklalor.javasim.preferences;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;

public class PreferencesFile
{	
	private ApplicationPreferences applicationPreferences;
	
	private File saveDirectory;
	private File simulationDirectory;
	private List<File> simulationFiles;
	private Level logLevel;
	
	public PreferencesFile(File preferences, ApplicationPreferences applicationPreferences)
	{
		this.applicationPreferences = applicationPreferences;
		parse(preferences);
	}

	private void parse(File preferences)
	{
		if (!preferences.exists())
		{
			fillDefaults();
			save();
		}
	}

	private void fillDefaults()
	{
		simulationDirectory = new File(System.getProperty("user.home") + File.separator + "Documents" + File.separator + "JavaSim");
		//TODO: add other preferences (as they are added).
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
	
	public File getSimulationDirectory()
	{
		return simulationDirectory;
	}
	
	public void setSimulationDirectory(File simulationDirectory)
	{
		this.simulationDirectory = simulationDirectory;
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
