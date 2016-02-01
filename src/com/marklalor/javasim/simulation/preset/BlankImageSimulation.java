package com.marklalor.javasim.simulation.preset;

import java.awt.Color;
import java.awt.Graphics2D;

import com.marklalor.javasim.simulation.Simulation;

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
    private Color backgroundColor = Color.white;
    
    /**
     * Create a <code>Simulation</code> that fills the screen with white initially and on reset.
     * 
     * @see Simulation
     */
    public BlankImageSimulation()
    {
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
    }
    
    /**
     * Fills the screen, from corner to corner, with the specified color (default white) when the simulation is reset
     * (typically by Simulation→Reset).
     * 
     * @see #BlankImageSimulation(Color)
     * @see #setBackgroundColor(Color)
     */
    @Override
    public void reset(Graphics2D permanent)
    {
        permanent.setColor(backgroundColor);
        permanent.fillRect(0, 0, getWidth(), getHeight());
    }
    
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
