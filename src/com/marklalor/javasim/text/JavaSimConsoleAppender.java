package com.marklalor.javasim.text;

import java.util.ArrayList;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

public class JavaSimConsoleAppender extends AppenderSkeleton
{
    private ArrayList<LoggingEvent> eventsList = new ArrayList<LoggingEvent>();
    private Console console;

    public JavaSimConsoleAppender(Console console)
	{
    	this.console = console;
	}
    
    @Override
    protected void append(LoggingEvent event)
    {
    	eventsList.add(event);
    	
    	StringBuilder builder = new StringBuilder();
    	builder.append("[");
    	builder.append(event.getLevel().toString());
    	builder.append("] ");
    	if (!event.getLocationInformation().getClassName().contains("marklalor"))
    	{
    	    builder.append("(");
    	    builder.append(event.getLocationInformation().fullInfo);
    	    builder.append(") ");
    	}
    	builder.append(event.getMessage().toString());
    	
    	console.append(builder.toString());
    }

	@Override
	public boolean requiresLayout()
	{
		return false;
	}

	@Override
	public void close()
	{
		
	}
}