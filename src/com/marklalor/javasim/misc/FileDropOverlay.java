package com.marklalor.javasim.misc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

import com.marklalor.javasim.Home;

@SuppressWarnings("serial")
public class FileDropOverlay extends JPanel
{
    private Home home;
    
    public FileDropOverlay(Home home)
    {
        this.home = home;
        this.setOpaque(false);
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        if(home.getFileDropVisible())
        {
            Graphics2D g2 = (Graphics2D) g;
            // Draw the translucent red background.
            g2.setColor(new Color(145, 50, 50, 50));
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            // Draw the dashed, rounded, grey, rectangle.
            g2.setColor(new Color(145, 100, 90, 90));
            final BasicStroke dashed = new BasicStroke(6.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f }, 0.0f);
            g2.setStroke(dashed);
            g2.draw(new RoundRectangle2D.Double(30d, 30d, (double) (getWidth() - 60), (double) (getHeight() - 90), 20d, 20d));
        }
    }
}
