package com.marklalor.javasim.preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

public class PreferencesFile
{
    // ApplicationPreferences parent wrapper class.
    private transient ApplicationPreferences applicationPreferences;
    
    private File simulationsDirectory;
    private File saveDirectory;
    
    private List<File> simulationDirectories;
    private List<File> simulationFiles;
    
    private Level logLevel;
    
    public PreferencesFile(ApplicationPreferences applicationPreferences)
    {
        this.applicationPreferences = applicationPreferences;
    }
    
    public void fillDefaults()
    {
        final File baseDirectory;
	    if (getApplicationPreferences().isMacOSX())
	        baseDirectory = new File(
                    System.getProperty("user.home")  + 
                    File.separator + "Library" + 
                    File.separator + "Application Support" + 
                    File.separator + "JavaSim");
        else if (getApplicationPreferences().isWindows())
            baseDirectory = new File(
                    System.getenv("APPDATA") + 
                    File.separator + "JavaSim");
        else if (getApplicationPreferences().isLinux())
            baseDirectory = new File(
                    File.separator + "var" + 
                    File.separator + "lib" + 
                    File.separator + "javasim");
        else
            baseDirectory = new File(
                    System.getProperty("user.home") + 
                    File.separator + "JavaSim");
        
        // "Simulations" folder in the base directory (lowercase linux, uppercase otherwise).
        simulationsDirectory = new File(baseDirectory, getApplicationPreferences().isLinux() ? "simulations" : "Simulations");
        simulationsDirectory.mkdirs();
        
        // "Saves" folder in the base directory (lowercase linux, uppercase otherwise).
        saveDirectory = new File(baseDirectory, getApplicationPreferences().isLinux() ? "saves" : "Saves");
        saveDirectory.mkdirs();
        
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
    
    public File getSimulationsDirectory()
    {
        return simulationsDirectory;
    }
    
    public void setSimulationsDirectory(File mainDirectory)
    {
        this.simulationsDirectory = mainDirectory;
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
