package com.marklalor.javasim.simulation.preset;

import java.awt.Color;
import java.awt.Graphics2D;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.image.layer.DrawHandler;
import com.marklalor.javasim.simulation.image.layer.ImageLayer;

/**
 * <p>
 * Wrapper class for a {@link Simulation} with a predefined (default white) background color.
 * </p>
 * <p>
 * The class:
 * <ul>
 * <li>Is initialized with a background color, by default <code>java.awt.Color.white</code></li>
 * <li>On initialization, shows the image using <code>getImage().setVisible(true)</code></li>
 * <li>Fills the <code>permanent</code> graphics with the specified color on simulation reset.</li>
 * </ul>
 * </p>
 * 
 * @see Simulation
 */
public abstract class BlankImageSimulation extends Simulation
{
    private Color backgroundColor;
    
    /**
     * Create a <code>Simulation</code> that fills the screen with white initially and on reset.
     * 
     * @see Simulation
     */
    public BlankImageSimulation()
    {
        this(Color.WHITE);
    }
    
    /**
     * Create a <code>Simulation</code> that fills the screen the specified color initially and on reset.
     */
    public BlankImageSimulation(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }
    
    /**
     * Shows the <code>Simulation</code>'s <code>Image</code> on simulation initialization.
     * 
     * @see Image
     */
    @Override
    public void initialize()
    {
        // Simulation is intended for images, show the image pane.
        getImage().getFrame().setVisible(true);
        
        ImageLayer permanentLayer = new ImageLayer(0, false);
        permanentLayer.setDrawHandler(new DrawHandler()
        {
            
            @Override
            public void reset(ImageLayer sender, Graphics2D graphics)
            {
                JavaSim.getLogger().trace("BlankImageSimulation reset");
                graphics.setColor(BlankImageSimulation.this.getBackgroundColor());
                graphics.fillRect(0, 0, BlankImageSimulation.this.getImage().getWidth(), BlankImageSimulation.this.getImage().getHeight());
                BlankImageSimulation.this.reset(graphics);
            }
            
            @Override
            public void draw(ImageLayer sender, Graphics2D graphics)
            {
                JavaSim.getLogger().trace("BlankImageSimulation draw");
                BlankImageSimulation.this.draw(graphics);
            }
        });
        getImage().addLayer(permanentLayer);
    }
    
    public void reset(Graphics2D graphics){}
    
    public void draw(Graphics2D graphics){}
    
    /**
     * @return The background color which fills image on {@link #reset()}
     */
    public Color getBackgroundColor()
    {
        return backgroundColor;
    }
    
    /**
     * @param backgroundColor
     *            The background color which fills image on {@link #reset()}
     */
    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }
}
