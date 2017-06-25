package com.marklalor.javasim.preferences;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.SystemUtils;
import org.apache.log4j.Level;

import com.marklalor.javasim.util.PathUtils;

public class PreferencesFile
{   
    private File simulationsDirectory;
    private File saveDirectory;
    
    private SortedSet<File> simulationDirectories;
    private SortedSet<File> simulationFiles;
    
    private Level logLevel;
    
    public void fillDefaults()
    {
        // "Simulations" folder in the base directory (lowercase linux, uppercase otherwise).
        simulationsDirectory = new File(PathUtils.getBaseDirectory(), SystemUtils.IS_OS_LINUX ? "simulations" : "Simulations");
        simulationsDirectory.mkdirs();
        
        // "Saves" folder in the base directory (lowercase linux, uppercase otherwise).
        saveDirectory = new File(PathUtils.getBaseDirectory(), SystemUtils.IS_OS_LINUX ? "saves" : "Saves");
        saveDirectory.mkdirs();
        
        simulationDirectories = new TreeSet<>();
        simulationFiles = new TreeSet<>();
        
        logLevel = Level.INFO;
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
    
    public SortedSet<File> getSimulationFiles()
    {
        return simulationFiles;
    }
    
    public void setSimulationFiles(SortedSet<File> simulationFiles)
    {
        this.simulationFiles = simulationFiles;
    }
    
    public SortedSet<File> getSimulationDirectories()
    {
        return simulationDirectories;
    }
    
    public void setSimulationDirectories(SortedSet<File> simulationDirectories)
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
