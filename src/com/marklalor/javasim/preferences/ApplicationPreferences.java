package com.marklalor.javasim.preferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.preferences.jsonserializers.FileSerializer;
import com.marklalor.javasim.preferences.jsonserializers.LevelSerializer;

/**
 * <p>
 * Manages the application preferences which come from two places: a preferences file, and command line arguments. This
 * manager class allows preferences to be pulled from an instance of {@link PreferencesFile} and an instance of
 * {@link CommandLineArgs}.
 * </p>
 * <p>
 * 
 * 
 */
public class ApplicationPreferences
{
    private String os;
    
    private CommandLineArgs arguments;
    
    private PreferencesFile preferences;
    private File file;
    private Gson gson;
    
    public ApplicationPreferences()
    {
        // Detect operating system.
        this.os = System.getProperty("os.name").toLowerCase();
        
        // Set up Gson.
        // Wow I love Gson this is so nice. Tell me that's not nice.
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
                .registerTypeAdapter(File.class, new FileSerializer())
                .registerTypeAdapter(Level.class, new LevelSerializer())
                .setPrettyPrinting()
                .create();
    }
    
    public void parsePreferencesFile(File file)
    {
        this.file = file;
        this.preferences = new PreferencesFile(this);
        
        if(file.exists())
        {
            load();
        }
        else
        {
            getPreferences().fillDefaults();
            save();
        }
    }
    
    public void parseCommandLineArguments(String[] arguments)
    {
        this.arguments = new CommandLineArgs(arguments, this);
    }
    
    public void save()
    {
        try
        {
            FileUtils.writeStringToFile(getFile(), gson.toJson(getPreferences()));
        }
        catch(IOException e)
        {
            JavaSim.getLogger().error("Could not save preferences file!", e);
        }
    }
    
    private void load()
    {
        try
        {
            this.preferences = gson.fromJson(new FileReader(getFile()), PreferencesFile.class);
            this.preferences.setApplicationPreferences(this);
        }
        catch(JsonSyntaxException e)
        {
            JavaSim.getLogger().error("Error with the preferences.json file syntax.", e);
        }
        catch(JsonIOException e)
        {
            JavaSim.getLogger().error("Error reading the preferences.json file.", e);
        }
        catch(FileNotFoundException e)
        {
            JavaSim.getLogger().error("preferences.json file not found.", e);
        }
    }
    
    public File getFile()
    {
        return file;
    }
    
    public void setFile(File file)
    {
        this.file = file;
    }
    
    public PreferencesFile getPreferences()
    {
        return preferences;
    }
    
    public void setPreferences(PreferencesFile preferences)
    {
        this.preferences = preferences;
    }
    
    public CommandLineArgs getArguments()
    {
        return arguments;
    }
    
    public void setArguments(CommandLineArgs arguments)
    {
        this.arguments = arguments;
    }
    
    public Gson getGson()
    {
        return gson;
    }
    
    public void setGson(Gson gson)
    {
        this.gson = gson;
    }
    
    // Wrap around the data classes
    
    // PreferencesFile
    
    public File getSimulationsDirectory()
    {
        return preferences.getSimulationsDirectory();
    }
    
    public void setSimulationsDirectory(File simulationDirectory)
    {
        preferences.setSimulationsDirectory(simulationDirectory);
    }
    
    public File getSaveDirectory()
    {
        return preferences.getSaveDirectory();
    }
    
    public void setSaveDirectory(File saveDirectory)
    {
        preferences.setSaveDirectory(saveDirectory);
    }
    
    // CommandLineArgs
    
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
    
    // Operating system variables.
    
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
    
    public File getTempDirectory()
    {
        return new File(getSaveDirectory(), ".temp");
    }
}
