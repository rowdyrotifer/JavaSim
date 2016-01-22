package com.marklalor.javasim.simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.marklalor.javasim.JavaSim;

public class SimulationInfo implements Serializable
{
	private static final long serialVersionUID = -4872104954414842342L;
	
	private static final String INFO_FILE = "info.properties";
	
	private File file;
	private String name, main, date, author, version, description;

	public SimulationInfo(File file, String name, String main, String date, String author, String version, String description)
	{
		setData(file, name, main, date, author, version, description);
	}
	
	public SimulationInfo(File jar)
	{
		JarFile jarfile = null;
		try
		{
			jarfile = new JarFile(jar);
		}
		catch(IOException e)
		{
		    JavaSim.getLogger().error("Could not open jar file.", e);
		}
		
		JarEntry properties_entry = jarfile.getJarEntry(INFO_FILE);
		if (properties_entry != null)
		{
			Map<String, String> properties = loadMetadata(jar);
			if (properties != null)
				setData(jar, properties.get("name"), properties.get("main"), properties.get("date"), properties.get("author"), properties.get("version"), properties.get("description"));
			else
				setDataBasic(jar);
		}
		else
		{
			setDataBasic(jar);
		}
	}
	
	private void setDataBasic(File jar)
	{
		setData(jar, jar.getName(), "", "", "", "", "");	
	}

	public SimulationInfo(Class<? extends Simulation> simulationClass)
	{
		Map<String, String> properties = loadMetadata(simulationClass);
		setData(new File(SimulationInfo.findPathJar(simulationClass)), properties.get("name"), properties.get("main"), properties.get("date"), properties.get("author"), properties.get("version"), properties.get("description"));
	}

	@SuppressWarnings("unchecked")
	public static Class<? extends Simulation> loadSimulationClass(File file, String targetClassName) throws IOException, ClassNotFoundException
	{
		if (targetClassName == null)
			JavaSim.getLogger().trace("Automatically searching for Simulation subclass in {}", file);
		else
			JavaSim.getLogger().trace("Searching for {} in {}", targetClassName, file);
		
		JarFile jarFile = new JarFile(file);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + file.getAbsolutePath() +"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);
		Class<? extends Simulation> returnClass = null;

	    while (e.hasMoreElements())
	    {
	        JarEntry entry = (JarEntry) e.nextElement();
	        if(entry.isDirectory() || !entry.getName().endsWith(".class"))
	            continue;
		    // -6 because of .class
		    String className = entry.getName().substring(0,entry.getName().length()-6);
		    className = className.replace('/', '.');
		    
		    //Load it and return it if it's a simulation.
		    try
		    {
		    	JavaSim.getLogger().trace("Loading {}", className);
		    	if (targetClassName != null)
		    	{
			    	if (className.equals(targetClassName))	
			    	{
			    		Class<?> simClass = cl.loadClass(className);
			    		if (!Simulation.class.isAssignableFrom(simClass))
			    			throw new RuntimeException("The main class from " + INFO_FILE + ", " + targetClassName + ", does not extend Simulation");
			    		returnClass = (Class<? extends Simulation>) simClass;
			    	}
		    	}
		    	else
		    	{
			    	Class<?> simClass = cl.loadClass(className);
			    	if (Simulation.class.isAssignableFrom(simClass))
				    {
				    	returnClass = (Class<? extends Simulation>) simClass;
				    }
		    	}
		    }
		    //Weird class files...?
		    catch(Exception ex)
		    {
		        JavaSim.getLogger().error("Could not load classes from jar.", e);
		    }
	    }
	    
	    cl.close();
	    jarFile.close();
		return returnClass;
	}
	
	private void setData(File file, String name, String main, String date, String author, String version, String description)
	{
		this.file = file;
		this.name = name;
		this.main = main;
		this.date = date;
		this.author = author;
		this.version = version;
		this.description = description;
	}

	public static Map<String, String> loadMetadata(File jar)
	{
		String in = getPropertiesString(jar);
		return loadPropertiesFromString(in);
	}
	
	public static Map<String, String> loadMetadata(Class<? extends Simulation> clazz)
	{
		String in = getPropertiesString(clazz);
		return loadPropertiesFromString(in);
	}
	
	private static Map<String, String> loadPropertiesFromString(String in)
	{	
		if (in == null)
			return null;
		
		Properties properties = new Properties();
		
		try
		{
			properties.load(new StringReader(in));
		}
		catch(IOException e)
		{
			JavaSim.getLogger().error("Could not load simulation's {} file from input stream.", INFO_FILE, e);
		}
		
		Map<String, String> propertiesMap = new HashMap<String, String>();
		final String[] keys = {"main", "name", "date", "author", "version", "description"};
		for (String key : keys)
			propertiesMap.put(key, properties.getProperty(key));
		
		return propertiesMap;
	}

	private static String getPropertiesString(File jar)
	{
		URL url = null;
		
		try
		{
			url = new URL("file", null, jar.getAbsolutePath());
		}
		catch(MalformedURLException e)
		{
			JavaSim.getLogger().error("Invalid jar url {}", url, e);
		}
		
		JavaSim.getLogger().info("Loading properties from {}/{}", jar, INFO_FILE);
		
		final URLClassLoader urlClassLoader = new URLClassLoader( new URL[] { url } );
		
		InputStream in = urlClassLoader.getResourceAsStream(INFO_FILE);
		
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		String data = "", line = null;
		try
		{
			while((line = input.readLine()) != null)
				data += line + "\n";
		}
		catch(IOException e)
		{
		    JavaSim.getLogger().error("IOException while reading jar info file.", e);
		}
		try
		{
			input.close();
			in.close();
		}
		catch(IOException e)
		{
		    JavaSim.getLogger().error("Error closing jar info file.", e);
		}
		try
		{
			urlClassLoader.close();
		}
		catch(IOException e)
		{
		    JavaSim.getLogger().error("Error closing jar url classloader.", e);
		}
		
		return data;
	}
	
	private static String getPropertiesString(Class<? extends Simulation> clazz)
	{
		URL url = clazz.getResource(INFO_FILE);
		
		JavaSim.getLogger().info("Getting input stream from {}", clazz.getCanonicalName());
		
		InputStream in = null;
		try
		{
			in = url.openStream();
		}
		catch(IOException e)
		{
		    JavaSim.getLogger().error("Error opening jar input stream.", e);
			return null;
		}
		
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		String data = "", line = null;
		try
		{
			while((line = input.readLine()) != null)
				data += line;
		}
		catch(IOException e)
		{
		    JavaSim.getLogger().error("IOException while reading jar info file.", e);
		}
		try
		{
			input.close();
			in.close();
		}
		catch(IOException e)
		{
		    JavaSim.getLogger().error("Error closing jar info file.", e);
		}
		
		return data;
	}
	
	//Getters and setters
	
	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getMain()
	{
		return main;
	}
	
	public void setMain(String main)
	{
		this.main = main;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
	
	
	//TODO: move?
	/**
	 * 
	 * 
	 * If the provided class has been loaded from a jar file that is on the local file system, will find the absolute path to that jar file.
	 * 
	 * @param context The jar file that contained the class file that represents this class will be found. Specify {@code null} to let {@code LiveInjector}
	 *                find its own jar.
	 * @throws IllegalStateException If the specified class was loaded from a directory or in some other way (such as via HTTP, from a database, or some
	 *                               other custom classloading device).
	 */
	public static String findPathJar(Class<?> context) throws IllegalStateException
	{
		String rawName = context.getName();
		String classFileName;
		/*
		 * rawName is something like package.name.ContainingClass$ClassName. We
		 * need to turn this into ContainingClass$ClassName.class.
		 */
		{
			int idx = rawName.lastIndexOf('.');
			classFileName = (idx == -1 ? rawName : rawName.substring(idx + 1)) + ".class";
		}
		
		String uri = context.getResource(classFileName).toString();
		if(uri.startsWith("file:"))
			throw new IllegalStateException("This class has been loaded from a directory and not from a jar file.");
		if(!uri.startsWith("jar:file:"))
		{
			int idx = uri.indexOf(':');
			String protocol = idx == -1 ? "(unknown)" : uri.substring(0, idx);
			throw new IllegalStateException("This class has been loaded remotely via the " + protocol +
					" protocol. Only loading from a jar on the local file system is supported.");
		}
		
		int idx = uri.indexOf('!');
		// As far as I know, the if statement below can't ever trigger, so it's
		// more of a sanity check thing.
		if(idx == -1)
			throw new IllegalStateException("You appear to have loaded this class from a local jar file, but I can't make sense of the URL!");
		
		try
		{
			String fileName = URLDecoder.decode(uri.substring("jar:file:".length(), idx), Charset.defaultCharset().name());
			return new File(fileName).getAbsolutePath();
		}
		catch(UnsupportedEncodingException e)
		{
			throw new InternalError("default charset doesn't exist. Your VM is borked.");
		}
	}

    @Override
    public String toString()
    {
        return getName();
    }
}