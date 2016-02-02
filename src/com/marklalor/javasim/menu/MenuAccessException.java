package com.marklalor.javasim.menu;

@SuppressWarnings("serial")
public class MenuAccessException extends RuntimeException
{
    public MenuAccessException()
    {
        super();
    }
    
    public MenuAccessException(String s)
    {
        super(s);
    }
    
    public MenuAccessException(String s, Throwable throwable)
    {
        super(s, throwable);
    }
    
    public MenuAccessException(Throwable throwable)
    {
        super(throwable);
    }
}
