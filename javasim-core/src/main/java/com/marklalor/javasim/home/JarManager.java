package com.marklalor.javasim.home;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.SimulationInfo;

public class JarManager
{
    private int count;
    private Map<Integer, URLClassLoader> classLoaders;
    private Map<Integer, Class<? extends Simulation>> classes;
    
    public JarManager()
    {
        this.count = 0;
        this.classLoaders = new HashMap<Integer, URLClassLoader>();
        this.classes = new HashMap<Integer, Class<? extends Simulation>>();
    }
    
    public Integer loadJarFromSimulationInfo(SimulationInfo info)
    {
        URLClassLoader classLoader = null;
        
        try
        {
            classLoader = URLClassLoader.newInstance(new URL[] { info.getFile().toURI().toURL() });
        }
        catch(MalformedURLException e)
        {
            JavaSim.getLogger().error("Malformed File URL");
            return null;
        }
        
        try
        {
            Class<?> loadedMainClass = classLoader.loadClass(info.getMain());
            
            if(Simulation.class.isAssignableFrom(loadedMainClass))
            {
                @SuppressWarnings("unchecked")
                Class<? extends Simulation> simulationClass = (Class<? extends Simulation>) loadedMainClass;
                
                final Integer key = count;
                count++;
                
                this.classLoaders.put(key, classLoader);
                this.classes.put(key, simulationClass);
                
                return key;
            }
            else
                throw new RuntimeException("The main class from " + info.getFile() + ", " + info.getMain() + ", does not extend Simulation");
            
        }
        catch(ClassNotFoundException e)
        {
            JavaSim.getLogger().error("Could not find simulation class {} in jar file {}", info.getMain(), info.getFile());
            return null;
        }
    }
    
    public Class<? extends Simulation> getClassFromId(int id)
    {
        return this.classes.get(new Integer(id));
    }
    
    public void unloadFromId(Integer id)
    {
        JavaSim.getLogger().debug("Unloading class loader {}. {}:{}", id, this.classes.get(id).getCanonicalName(), Arrays.asList(this.classLoaders.get(id).getURLs()));
        this.classLoaders.remove(id);
        this.classes.remove(id);
        JavaSim.getLogger().debug("Removed class and classloader references.");
    }
}
