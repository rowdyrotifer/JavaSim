package com.marklalor.javasim.content.interfacing.typemaps;

import com.marklalor.javasim.content.interfacing.output.OutputDataChangeEvent;
import com.marklalor.javasim.content.interfacing.output.change.BufferedImageOutputDataChangeEvent;

/**
 * PRAISE PARALLEL HIERARCHIES
 */
public enum OutputDataType
{
    BUFFERED_IMAGE(BufferedImageOutputDataChangeEvent.class);
    
    private final Class<? extends OutputDataChangeEvent> changeEventType;
    
    OutputDataType(Class<? extends OutputDataChangeEvent> changeEventType)
    {
        this.changeEventType = changeEventType;
    }
    
    public Class<? extends OutputDataChangeEvent> getChangeEventType()
    {
        return changeEventType;
    }
}
