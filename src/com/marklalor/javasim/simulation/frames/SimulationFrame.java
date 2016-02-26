package com.marklalor.javasim.simulation.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import com.marklalor.javasim.menu.menus.JavaSimMenu;
import com.marklalor.javasim.misc.filter.IntegerFilter;
import com.marklalor.javasim.simulation.Simulation;

public class SimulationFrame implements FrameHolder, MenuHolder<JavaSimMenu>, SimulationHolder
{
    private Simulation simulation;
    private JFrame frame;
    private JavaSimMenu menu;
    
    public SimulationFrame(Simulation simulation)
    {
        setFrame(new JFrame());
        getFrame().setAutoRequestFocus(false);
        setSimulation(simulation);
    }
    
    //TODO: use preferredSize/pack.
    
    public void setSize(int width, int height)
    {
        frame.setSize(width, height + frame.getInsets().top);
    }
    
    public Dimension getSize()
    {
        Dimension size = frame.getSize();
        return new Dimension(size.width, size.height - frame.getInsets().top);
    }
    
    // TODO: remove and use the controls instead. Move this somewhere else...
    public static final int FILTER_NONE = -1;
    public static final int FILTER_INTEGER = 0;
    
    public static JPanel labeledField(String label, JTextField textField, int filter)
    {
        if(filter == FILTER_INTEGER)
        {
            PlainDocument doc = (PlainDocument) textField.getDocument();
            doc.setDocumentFilter(new IntegerFilter());
        }
        JPanel group = new JPanel(new BorderLayout());
        group.add(new JLabel(label), BorderLayout.WEST);
        group.add(textField, BorderLayout.CENTER);
        return group;
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
    }
    
    @Override
    public JFrame getFrame()
    {
        return this.frame;
    }
    
    @Override
    public void setFrame(JFrame frame)
    {
        this.frame = frame;
    }

    @Override
    public void setMenu(JavaSimMenu menu)
    {
        this.menu = menu;
    }

    @Override
    public JavaSimMenu getMenu()
    {
        return this.menu;
    }
}
