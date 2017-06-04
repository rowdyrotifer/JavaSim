package com.marklalor.javasim.menu;

import javax.swing.JMenuItem;

import com.marklalor.javasim.simulation.frames.FrameHolder;


public class MenuUtils
{
    public static void setEnabled(boolean enabled, JMenuItem... items)
    {
        for(JMenuItem item : items)
            item.setEnabled(enabled);
    }
    
    public static void showRelative(FrameHolder frameHolder, FrameHolder relativeTo)
    {
        frameHolder.getFrame().setLocationRelativeTo(relativeTo.getFrame());
        show(frameHolder);
    }

    public static void show(FrameHolder frameHolder)
    {
        frameHolder.getFrame().setVisible(true);
        frameHolder.getFrame().toFront();
        frameHolder.getFrame().requestFocus();
    }
}
