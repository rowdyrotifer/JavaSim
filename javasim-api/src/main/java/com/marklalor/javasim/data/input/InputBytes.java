package com.marklalor.javasim.data.input;

import java.io.InputStream;

public interface InputBytes extends Input
{
    public byte[] get();
    
    public InputStream getStream();
}
