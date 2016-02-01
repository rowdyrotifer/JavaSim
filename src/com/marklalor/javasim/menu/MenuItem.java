package com.marklalor.javasim.menu;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MenuItem
{
    // Annotations can't be null... well, don't think anyone will ever be using this as a menu name.
    public final static String AUTO_RESOLVE_TEXT = "{{AUTO_RESOLVE_TEXT}}";
    // Nor will anyone try to use Integer.MAX_VALUE as a keycode...
    public final static int NO_ACCELERATOR_KEYCODE = Integer.MAX_VALUE;
    // 0 resolves to no key modifiers.
    public final static int NO_MODIFIERS = 0;
    // -1 resolves to Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() in com.marklalor.javasim.menu.Menu#Menu().
    public final static int COMMAND_OR_CONTROL = -1;
    
    String text() default AUTO_RESOLVE_TEXT;
    
    int keyCode() default NO_ACCELERATOR_KEYCODE;
    
    int[] keyModifiers() default NO_MODIFIERS;
}
