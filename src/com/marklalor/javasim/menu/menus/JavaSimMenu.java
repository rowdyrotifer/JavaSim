package com.marklalor.javasim.menu.menus;

import java.awt.Event;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.marklalor.javasim.home.Home;
import com.marklalor.javasim.menu.Menu;
import com.marklalor.javasim.menu.MenuUtils;
import com.marklalor.javasim.menu.annotations.MenuHeader;
import com.marklalor.javasim.menu.annotations.MenuItem;
import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.frames.FrameHolder;
import com.marklalor.javasim.simulation.frames.HomeHolder;
import com.marklalor.javasim.simulation.frames.SimulationHolder;

public class JavaSimMenu extends Menu implements SimulationHolder, HomeHolder
{   
    @MenuHeader(text = "File", children = {
            "newSimulation",
            "openSimulation",
            MenuHeader.SEPERATOR,
            "close",
            "closeAll",
            MenuHeader.SEPERATOR,
            "saveImage",
            "saveImageAs",
            "animateMenuItem",
            MenuHeader.SEPERATOR,
            "refresh",
            MenuHeader.SEPERATOR,
            "openHomeFolder",
            "openContentFolder",
            MenuHeader.SEPERATOR,
            "print" })
    public JMenu file;
    
    @MenuHeader(text = "Edit", children = {
            "copy" })
    public JMenu edit;
    
    @MenuHeader(text = "Animation", children = {
            "play",
            "playUntilBreakpoint",
            "stop",
            MenuHeader.SEPERATOR,
            "nextFrame",
            MenuHeader.SEPERATOR,
            "decreaseSpeed",
            "increaseSpeed" })
    public JMenu animation;
    
    @MenuHeader(text = "Simulation", children = {
            "reset",
            "reloadSimulation",
            MenuHeader.SEPERATOR,
            "resizeMenuItem",
            "fullscreen" })
    public JMenu _simulation;
    
    @MenuHeader(text = "Window", children = {
            "minimize",
            MenuHeader.SEPERATOR,
            "homeItem",
            "console",
            "controls" })
    public JMenu window;
    
    @MenuHeader(text = "Help", children = {
            "openGitHub" })
    public JMenu help;
    
    // FILE
    @MenuItem(text = "New Simulation", keyCode = KeyEvent.VK_N, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem newSimulation;
    @MenuItem(text = "Open Simulation...", keyCode = KeyEvent.VK_O, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem openSimulation;
    @MenuItem(text = "Close", keyCode = KeyEvent.VK_W, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem close;
    @MenuItem(text = "Close All Simulations", keyCode = KeyEvent.VK_W, keyModifiers = { MenuItem.COMMAND_OR_CONTROL, Event.SHIFT_MASK })
    public JMenuItem closeAll;
    @MenuItem(text = "Save Image", keyCode = KeyEvent.VK_S, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem saveImage;
    @MenuItem(text = "Save Image As...", keyCode = KeyEvent.VK_S, keyModifiers = { MenuItem.COMMAND_OR_CONTROL, Event.SHIFT_MASK })
    public JMenuItem saveImageAs;
    @MenuItem(text = "Create Animated Gif...", keyCode = KeyEvent.VK_I, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem animateMenuItem;
    @MenuItem(text = "Refresh Simulations", keyCode = KeyEvent.VK_F5, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem refresh;
    @MenuItem(text = "Open Home Folder", keyCode = KeyEvent.VK_D, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem openHomeFolder;
    @MenuItem(text = "Open Content Folder", keyCode = KeyEvent.VK_D, keyModifiers = { MenuItem.COMMAND_OR_CONTROL, Event.SHIFT_MASK })
    public JMenuItem openContentFolder;
    @MenuItem(text = "Print...", keyCode = KeyEvent.VK_P, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem print;
    @MenuItem(text = "Properties...", keyCode = KeyEvent.VK_W, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem openProperties;
    
    // EDIT
    @MenuItem(text = "Copy", keyCode = KeyEvent.VK_C, keyModifiers = MenuItem.COMMAND_OR_CONTROL)
    public JMenuItem copy;
    
    // ANIMATION
    @MenuItem(text = "Play", keyCode = KeyEvent.VK_L, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem play;
    @MenuItem(text = "Play Until Breakpoint", keyCode = KeyEvent.VK_L, keyModifiers = { MenuItem.COMMAND_OR_CONTROL, Event.SHIFT_MASK })
    public JMenuItem playUntilBreakpoint;
    @MenuItem(text = "Stop", keyCode = KeyEvent.VK_PERIOD, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem stop;
    @MenuItem(text = "Next Frame", keyCode = KeyEvent.VK_SPACE)
    public JMenuItem nextFrame;
    @MenuItem(text = "Decrease Speed", keyCode = KeyEvent.VK_OPEN_BRACKET)
    public JMenuItem decreaseSpeed;
    @MenuItem(text = "Increase Speed", keyCode = KeyEvent.VK_CLOSE_BRACKET)
    public JMenuItem increaseSpeed;
    
    // SIMULATION
    @MenuItem(text = "Reset", keyCode = KeyEvent.VK_R, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem reset;
    @MenuItem(text = "Reload", keyCode = KeyEvent.VK_F5)
    public JMenuItem reloadSimulation;
    @MenuItem(text = "Resize")
    public JMenuItem resizeMenuItem;
    @MenuItem(text = "Enter Full Screen", keyCode = KeyEvent.VK_F, keyModifiers = { MenuItem.COMMAND_OR_CONTROL, Event.CTRL_MASK })
    public JMenuItem fullscreen;
    
    // WINDOW
    @MenuItem(text = "Minimize", keyCode = KeyEvent.VK_M, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem minimize;
    @MenuItem(text = "Home", keyCode = KeyEvent.VK_0, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem homeItem;
    @MenuItem(text = "Console", keyCode = KeyEvent.VK_1, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem console;
    @MenuItem(text = "Controls", keyCode = KeyEvent.VK_2, keyModifiers = { MenuItem.COMMAND_OR_CONTROL })
    public JMenuItem controls;
    
    @MenuItem(text = "Open GitHub Page")
    public JMenuItem openGitHub;
    
    
    private Simulation simulation;
    private Home home;
    
    public JavaSimMenu(Home home, Simulation simulation, FrameHolder frameHolder)
    {
        super(frameHolder);
        setHome(home);
        setSimulation(simulation);
    }

    @Override
    public Simulation getSimulation()
    {
        return simulation;
    }
    
    @Override
    public void setSimulation(Simulation simulation)
    {
        this.simulation = simulation;
        checkHasSimulation();
    }
    
    @Override
    public Home getHome()
    {
        return home; 
    }
    
    @Override
    public void setHome(Home home)
    {
        this.home = home;
    }
    
    private void checkHasSimulation()
    {
        if(getSimulation() != null)
            newSimulation.setText("New " + getSimulation().getInfo().getName());
        
        boolean simulationItemsEnabled = getSimulation() != null;
        MenuUtils.setEnabled(simulationItemsEnabled, saveImage, saveImageAs, animateMenuItem, openContentFolder, print);
        MenuUtils.setEnabled(simulationItemsEnabled, copy);
        MenuUtils.setEnabled(simulationItemsEnabled, play, playUntilBreakpoint, stop, nextFrame, decreaseSpeed, increaseSpeed);
        MenuUtils.setEnabled(simulationItemsEnabled, reset, reloadSimulation, resizeMenuItem, fullscreen);
        MenuUtils.setEnabled(simulationItemsEnabled, controls);
    }

    @Override
    protected void finishInitializingMenuItems()
    {
        
    }
    
    @Override
    protected void finishInitializingMenuHeaders()
    {
        
    }
}
