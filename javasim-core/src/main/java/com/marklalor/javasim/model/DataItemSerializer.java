package com.marklalor.javasim.model;

public interface DataItemSerializer
{
    public byte[] asBytes();
    
    public boolean asBoolean();
    
    public String asString();
    
    public int asInt();
    
    public long asLong();
    
    public byte[] asPNG();
}
