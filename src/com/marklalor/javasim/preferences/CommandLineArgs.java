package com.marklalor.javasim.preferences;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.marklalor.javasim.JavaSim;

public class CommandLineArgs
{
	private ApplicationPreferences applicationPreferences;
	
	private boolean consoleBind = true;
	private boolean useScreenMenuBar = true;
	
	public CommandLineArgs(String[] args, ApplicationPreferences applicationPreferences)
	{
		this.applicationPreferences = applicationPreferences;
		
		CommandLineParser parser = new DefaultParser();
		
		//Define possible arguments.
		Options options = new Options();
		
		options.addOption("n", "noconsolebind", false, "Do not bind the logger to the console.");
		options.addOption(null, "noscreenmenubar", false, "Do not use the OS X menu bar.");
		
		try
		{
			//Parse arguments.
			CommandLine cmd = parser.parse(options, args);
			
			consoleBind = !cmd.hasOption("noconsolebind");	
			JavaSim.getLogger().debug("consoleBind: {}", consoleBind);
			useScreenMenuBar = !cmd.hasOption("noscreenmenubar");
			JavaSim.getLogger().debug("useScreenMenuBar: {}", useScreenMenuBar);
		}
		catch(ParseException e)
		{
			JavaSim.getLogger().error("Could not parse the command line args properly.", e);
		}
	}
	
	public ApplicationPreferences getApplicationPreferences()
	{
		return applicationPreferences;
	}
	
	public boolean getConsoleBind()
	{
		return this.consoleBind;
	}
	
	public void setConsoleBind(boolean consoleBind)
	{
		this.consoleBind = consoleBind;
	}
	
	public boolean getUseScreenMenuBar()
	{
		return this.useScreenMenuBar;
	}
	
	public void setUseScreenMenuBar(boolean useScreenMenuBar)
	{
		this.useScreenMenuBar = useScreenMenuBar;
	}
}