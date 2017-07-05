package com.marklalor.javasim.data.output;

import java.io.StringWriter;

public interface OutputString extends Output
{
    public void put(String string);

    public StringWriter getOutputStream();
}
