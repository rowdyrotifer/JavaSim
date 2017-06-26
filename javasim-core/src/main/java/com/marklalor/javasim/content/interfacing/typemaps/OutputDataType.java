package com.marklalor.javasim.content.interfacing.typemaps;

import java.awt.image.BufferedImage;

import com.marklalor.javasim.content.interfacing.output.BufferedImageOutputData;
import com.marklalor.javasim.content.interfacing.output.OutputDataChangeEvent;
import com.marklalor.javasim.content.interfacing.output.OutputDataInternal;
import com.marklalor.javasim.content.interfacing.output.change.BufferedImageOutputDataChangeEvent;

/**
 * PRAISE PARALLEL HIERARCHIES
 */
public enum OutputDataType
{
    CACHED_BUFFERED_IMAGE(
            // 1. Backing data class for this particular implementation.
            BufferedImage.class,                       
            // 2. Parent class (real instance may be swapped for any child)
            BufferedImageOutputData.class,             
            // 3. Child class (may be swapped for another child class)
            // (each enum constant represents a different child class)
            CachedBufferedImageOutputData.class,      
            // 4. Event class, with this the UI can hope to give rich feedback.
            BufferedImageOutputDataChangeEvent.class);

    private final Class<?> rawType;
    private final Class<? extends OutputDataInternal<?>> parentType;
    private final Class<? extends OutputDataInternal<?>> childType;
    private final Class<? extends OutputDataChangeEvent> eventType;
    
    OutputDataType(
            Class<?> rawType,
            Class<? extends OutputDataInternal<?>> parentType,
            Class<? extends OutputDataInternal<?>> childType,
            Class<? extends OutputDataChangeEvent> eventType)
    {
        this.rawType = rawType;
        this.parentType = parentType;
        this.childType = childType;
        this.eventType = eventType;
    }
    
    public Class<?> getRawType()
    {
        return rawType;
    }
    
    public Class<? extends OutputDataInternal<?>> getParentType()
    {
        return parentType;
    }
    
    public Class<? extends OutputDataInternal<?>> getChildType()
    {
        return childType;
    }
    
    public Class<? extends OutputDataChangeEvent> getEventType()
    {
        return eventType;
    }
}
