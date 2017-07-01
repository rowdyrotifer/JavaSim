package com.marklalor.javasim.data;

public interface DataSerializer<T, U>
{
    public U serialize(T input);
}
