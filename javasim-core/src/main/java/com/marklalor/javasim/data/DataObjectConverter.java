package com.marklalor.javasim.data;

public interface DataObjectConverter
{
    public byte[] asBytes();
    
    public boolean asBoolean();
    
    public String asString();
    
    public int asInt();
    
    public long asLong();
    
    public byte[] asPNG();
}
