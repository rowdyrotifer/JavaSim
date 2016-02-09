package com.marklalor.javasim.menu.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MenuHeader
{
    public final static String AUTO_RESOLVE_TEXT = "{{AUTO_RESOLVE_TEXT}}";
    public final static String SEPERATOR = "{{SEPERATOR}}";
    
    String text() default AUTO_RESOLVE_TEXT;
    
    String[] children();
}
