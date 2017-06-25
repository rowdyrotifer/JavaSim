package com.marklalor.javasim.content.interfacing.ui.capabilities;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum UICapability
{
    PLAYABLE(Playable.class), REWINDABLE(Rewindable.class);
    
    private Class<?> interfayce;
    
    private UICapability(Class<?> interfayce)
    {
        this.interfayce = interfayce;
    }
    
    public Class<?> getInterface()
    {
        return interfayce;
    }
    
    public static List<UICapability> getCapabilities(Object object)
    {
        return Arrays.stream(UICapability.values())
                .filter(t -> t.getInterface().isAssignableFrom(object.getClass()))
                .collect(Collectors.toList());
    }
}
