package com.marklalor.javasim.content.interfacing.output;

import com.marklalor.javasim.content.interfacing.Describable;

public interface OutputData<T> extends Describable
{
    public T getData();
}
