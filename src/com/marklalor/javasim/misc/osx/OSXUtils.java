package com.marklalor.javasim.misc.osx;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import com.marklalor.javasim.JavaSim;

public class OSXUtils
{
    public static void registerFullyQualifiedClassName(String fullyQualifiedOSXRegisterableHandlerClassName, Object... params)
    {
        try
        {
            Class<?> osxRegisterableHandlerClass = Class.forName(fullyQualifiedOSXRegisterableHandlerClassName);
            
            //Fail if the handler does not extend OSXRegisterableHandler.
            if (OSXRegisterableHandler.class.isAssignableFrom(osxRegisterableHandlerClass))
            {
                //Get the Class objects of the "Object[] params" to compare with the constructors of the OSXRegisterableHandler
                Class<?>[] suppliedParameters = new Class<?>[params.length];
                for (int i = 0; i < params.length; i++)
                    suppliedParameters[i] = params[i].getClass();

                //Search through the OSXRegisterableHandler's constructors.
                Constructor<?>[] constructors = osxRegisterableHandlerClass.getConstructors();
                for(Constructor<?> constructor : constructors)
                {
                    Class<?>[] constructorParameters = constructor.getParameterTypes();
                    
                    //If there is a match with the supplied parameters and the constructor, then register a new instance of the handler.
                    if (Arrays.equals(constructorParameters, suppliedParameters))
                    {
                        OSXRegisterableHandler handler = OSXRegisterableHandler.class.cast(constructor.newInstance(params));
                        JavaSim.getLogger().debug("Registering OSXRegisterableHandler: {}", handler.getClass().getName());
                        handler.register();
                        return;
                    }
                }
                
                //If there was a match, the function would have returned by now.
                JavaSim.getLogger().debug("Could not find a matching constructor for OSXRegisterableHandler: {}", osxRegisterableHandlerClass);
            }
            else
                JavaSim.getLogger().error("{} is not assignable from {}", OSXRegisterableHandler.class.getCanonicalName(), fullyQualifiedOSXRegisterableHandlerClassName);
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            JavaSim.getLogger().error("Error registering OSX Handler class {}", fullyQualifiedOSXRegisterableHandlerClassName, e);
        }
    }
}
