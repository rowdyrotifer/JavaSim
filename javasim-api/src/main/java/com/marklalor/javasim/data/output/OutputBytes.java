package com.marklalor.javasim.data.output;

import java.io.OutputStream;

public interface OutputBytes extends Output
{
    public void put(boolean value);
    
    public OutputStream getOutputStream();
}
