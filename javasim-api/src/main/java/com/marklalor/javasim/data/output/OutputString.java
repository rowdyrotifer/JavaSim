package com.marklalor.javasim.data.output;

import java.io.StringWriter;

import com.marklalor.javasim.data.Terminal;

public interface OutputString
{
    /**
     * Note, this
     */
    @Terminal
    public void put(String string);

    public StringWriter getOutputStream();
}
