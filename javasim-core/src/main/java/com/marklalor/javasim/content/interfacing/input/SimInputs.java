package com.marklalor.javasim.content.interfacing.input;

import java.util.Map;
import java.util.UUID;

public interface SimInputs
{
    public Map<UUID, InputVariable<?>> getInputVariables();
    
}
