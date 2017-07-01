package com.marklalor.javasim.data.output;

import java.io.OutputStream;

import com.marklalor.javasim.data.Terminal;

public interface OutputBytes
{
    @Terminal
    public void put(boolean value);
    
    public OutputStream getOutputStream();
}
