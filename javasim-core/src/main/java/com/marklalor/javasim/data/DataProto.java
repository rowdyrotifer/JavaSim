package com.marklalor.javasim.data;

import java.io.InputStream;
import java.io.OutputStream;

public interface DataProto
{
    public DataObjectType objectType();
    
    public DataObjectConverter objectConverter();
    
    public byte[] toByteArray();
    
    public void writeTo(OutputStream output);
    
    public DataProto parseFrom(byte[] data);

    public DataProto parseFrom(InputStream input);
}
