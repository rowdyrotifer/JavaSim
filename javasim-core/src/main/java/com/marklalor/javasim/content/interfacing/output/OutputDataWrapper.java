package com.marklalor.javasim.content.interfacing.output;

import java.util.Optional;

/**
 * So that a great external power may change the
 * underlying implementation of the OutputData.
 * 
 * @author Mark Lalor
 */
public class OutputDataWrapper<T> implements OutputData<T>
{
    private OutputDataInternal<T> delegate;
    
    public static <U> OutputDataWrapper<U> of(OutputDataInternal<U> delegate)
    {
        return new OutputDataWrapper<>(delegate);
    }

    public OutputDataWrapper(OutputDataInternal<T> delegate)
    {
        this.delegate = delegate;
    }
    
    @Override
    public String getName()
    {
        return delegate.getName();
    }

    @Override
    public Optional<String> getDescription()
    {
        return delegate.getDescription();
    }

    @Override
    public T getData()
    {
        return delegate.getData();
    }
    
    public void setDelegate(OutputDataInternal<T> delegate)
    {
        this.delegate = delegate;
    }
}