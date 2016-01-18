package com.marklalor.javasim.preferences;

import java.io.File;

/**
 * <p>
 * Manages the application preferences which come from two places: a preferences
 * file, and command line arguments. This manager class allows preferences to be
 * pulled from an instance of {@link PreferencesFile} and an instance of
 * {@link CommandLineArgs}.
 * </p>
 * <p>
 * 
 * 
 * 
 */
public class ApplicationPreferences
{
	private String os;
	private PreferencesFile preferences;
	private CommandLineArgs arguments;
	
	public ApplicationPreferences()
	{
		//Detect operating system.
		this.os = System.getProperty("os.name").toLowerCase();
	}	
	
	public void parsePreferencesFile(File preferences)
	{
		this.preferences = new PreferencesFile(preferences, this);
	}
	
	public void parseCommandLineArguments(String[] arguments)
	{
		this.arguments = new CommandLineArgs(arguments, this);
	}
	
	//Wrap around the data classes
	
	//PreferencesFile
	
	public File getSimulationDirectory()
	{
		return preferences.getSimulationDirectory();
	}
	
	public void setSimulationDirectory(File simulationDirectory)
	{
		preferences.setSimulationDirectory(simulationDirectory);
	}
	
	//CommandLineArgs
	
	public boolean getConsoleBind()
	{
		return arguments.getConsoleBind();
	}
	
	public void setConsoleBind(boolean consoleBind)
	{
		arguments.setConsoleBind(consoleBind);
	}
	
	public boolean getUseScreenMenuBar()
	{
		return arguments.getUseScreenMenuBar();
	}
	
	public void setUseScreenMenuBar(boolean useScreenMenuBar)
	{
		arguments.setUseScreenMenuBar(useScreenMenuBar);
	}
	
	//Operating system variables.
	
	/**
	 * @return Contents of <code>System.getProperty("os.name").toLowerCase()</code>
	 */
	public String getOS()
	{
		return os;
	}
	
	public boolean isWindows()
	{
		return os.contains("win");
	}
	
	public boolean isMacOSX()
	{
		return os.contains("mac");
	}
	
	public boolean isLinux()
	{
		return os.contains("nux");
	}
}
