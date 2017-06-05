package com.marklalor.javasim.api.simulation;

import java.util.Optional;

public interface Simulation
{
    Optional<String> getName();
    
    Optional<String> getDescription();
    
    Optional<String> getVersion();
    
    Optional<String> getAuthor();
}
