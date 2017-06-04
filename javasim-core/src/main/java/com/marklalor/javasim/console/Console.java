package com.marklalor.javasim.console;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.marklalor.javasim.home.Home;
import com.marklalor.javasim.menu.menus.JavaSimMenu;
import com.marklalor.javasim.simulation.frames.FrameHolder;

public class Console implements FrameHolder
{
    private JFrame frame;
    private Home home;
    private JTextArea textArea;
    
    public Console(Home home)
    {
        this.home = home;
        
        setFrame(new JFrame("JavaSim Console"));
        getFrame().setSize(500, 500);
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        
        getFrame().getContentPane().setLayout(new BorderLayout());
        
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        textAreaScrollPane.setBorder(BorderFactory.createEmptyBorder());
        getFrame().getContentPane().add(textAreaScrollPane, BorderLayout.CENTER);
        getFrame().setAutoRequestFocus(false);
        
        if(!getHome().getApplicationPreferences().getConsoleBind())
            textArea.append("Logger not bound to this console.\nTo bind, run without the \"noconsolebind\" argument.");
        
        JavaSimMenu menu = new JavaSimMenu(getHome(), null, this);
        getFrame().setJMenuBar(menu.getMenuBar());
    }
    
    public Home getHome()
    {
        return home;
    }
    
    public void append(String string)
    {
        textArea.append(string + "\n");
    }
    
    @Override
    public JFrame getFrame()
    {
        return frame;
    }
    
    @Override
    public void setFrame(JFrame frame)
    {
        this.frame = frame;
    }
}
