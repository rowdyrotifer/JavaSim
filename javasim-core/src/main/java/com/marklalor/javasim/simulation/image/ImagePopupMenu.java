package com.marklalor.javasim.simulation.image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.manager.SimulationManager;

public class ImagePopupMenu extends SimulationManager
{
    private JPopupMenu menu;

    private JMenuItem saveImage;
    private JMenuItem saveImageAs;
    
    private JMenuItem copy;
    
    public ImagePopupMenu(Simulation simulation)
    {
        super(simulation);
        menu = new JPopupMenu();
        
        saveImage = new JMenuItem("Save Image");
        saveImage.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                getSimulation().getImage().getMenu().saveImage.doClick();
            }
        });
        menu.add(saveImage);
        
        saveImageAs = new JMenuItem("Save Image As...");
        saveImageAs.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                getSimulation().getImage().getMenu().saveImageAs.doClick();
            }
        });
        menu.add(saveImageAs);
        
        menu.addSeparator();
        
        copy = new JMenuItem("Copy");
        copy.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                getSimulation().getImage().getMenu().copy.doClick();
            }
        });
        menu.add(copy);
    }
    
    public JPopupMenu getMenu()
    {
        return menu;
    }
}
