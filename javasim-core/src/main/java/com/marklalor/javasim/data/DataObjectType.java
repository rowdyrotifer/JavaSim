package com.marklalor.javasim.data;

/**
 * Keep this in sync with:
 * 1. data_type.proto
 * 2. DataObjectConverter.java
 * 
 */
public enum DataObjectType
{
    BYTES(byte[].class),
    BOOLEAN(boolean.class),
    STRING(String.class),
    INT32(int.class),
    INT64(long.class),
    IMAGE(byte[].class);
    
    private final Class<?> clazz;

    private DataObjectType(Class<?> clazz)
    {
        this.clazz = clazz;
    }
    
    public Class<?> getTargetClass()
    {
        return clazz;
    }
}
