package com.marklalor.javasim;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklalor.javasim.misc.TempFileShutdownHook;
import com.marklalor.javasim.preferences.ApplicationPreferences;

public class JavaSim
{
    private static Logger logger;
    
    public static Logger getLogger()
    {
        return logger;
    }
    
    public static void main(final String[] arguments)
    {
        // Initialize the SLF4J logger.
        JavaSim.logger = LoggerFactory.getLogger(JavaSim.class);
        
        PropertyConfigurator.configure(JavaSim.class.getResourceAsStream("log4j.properties"));
        
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                // Set native system look and feel.
                try
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch(Exception e)
                {
                    JavaSim.getLogger().info("Failed to set system look and feel.", e);
                }
                
                // Create the preferences object (it will let us detect the
                // OS cleanly to find the OS-dependent preferences file).
                ApplicationPreferences preferences = new ApplicationPreferences();
                
                File preferencesFile = null;
                
                // Find the preferences file depending on OS.
                if(preferences.isMacOSX())
                    preferencesFile = new File(System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator + "JavaSim" + File.separator + "preferences.json");
                else if(preferences.isWindows())
                    preferencesFile = new File(System.getenv("APPDATA") + File.separator + "JavaSim" + File.separator + "preferences.json");
                else if(preferences.isLinux())
                    preferencesFile = new File(File.separator + "var" + File.separator + "lib" + File.separator + "javasim" + File.separator + "preferences.json");
                else
                    preferencesFile = new File(System.getProperty("user.home") + File.separator + "JavaSim" + File.separator + "preferences.json");
                
                JavaSim.getLogger().info("Resolved preferences file: {}", preferencesFile.getAbsolutePath());
                
                preferences.parseCommandLineArguments(arguments);
                preferences.parsePreferencesFile(preferencesFile);
                
                // Use OS X native menu bar (should pretty much always be yes, only disable to debug).
                if(preferences.getUseScreenMenuBar())
                    System.setProperty("apple.laf.useScreenMenuBar", "true");
                
                // Manage the temp folder.
                
                // Set the shutdown hook to delete the temporary directory on program exit.
                Thread deleteTempFolderThread = new Thread(new TempFileShutdownHook(preferences.getTempDirectory()));
                deleteTempFolderThread.setName("TempFolderDelete");
                Runtime.getRuntime().addShutdownHook(deleteTempFolderThread);
                // Delete a temp folder that may have remained due to an error.
                try
                {
                    FileUtils.deleteDirectory(preferences.getTempDirectory());
                }
                catch(IOException e)
                {
                    JavaSim.getLogger().error("Could not delete temp folder {}", preferences.getTempDirectory(), e);
                }
                // Recreate it, of course!
                preferences.getTempDirectory().mkdirs();
                
                // Finally, start the home panel.
                Home home = new Home(preferences);
                home.setSize(800, 500);
                home.setVisible(true);
            }
        });
    }
    
    public static String getVersion()
    {
        return JavaSim.class.getPackage().getSpecificationVersion();
    }
}
