package com.marklalor.javasim.simulation.image.layer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collections;

import com.marklalor.javasim.simulation.image.Image;

public class ImageLayer implements Comparable<ImageLayer>
{
    public static final boolean PERMANENT = false;
    public static final boolean TEMPORARY = true;
    
    private Image image;
    
    private boolean temporary;
    private int index;
    private DrawHandler drawHandler;
    private BufferedImage bufferedImage;
    
    public ImageLayer(int index)
    {
        this(index, false);
    }
    
    public ImageLayer(int index, boolean temporary)
    {
        this.index = index;
        this.temporary = temporary;
    }
    
    public void renderBufferedImage(boolean reset)
    {
        if(getTemporary() || getBufferedImage() == null || reset)
            setBufferedImage(new BufferedImage(getImage().getWidth(), getImage().getHeight(), BufferedImage.TYPE_INT_ARGB));
        
        if(drawHandler != null)
        {
            Graphics2D graphics = getBufferedImage().createGraphics();
            
            if(reset)
            {
                if(!getTemporary())
                    drawHandler.reset(this, graphics);
            }
            else
            {
                drawHandler.draw(this, graphics);
            }
            
            graphics.dispose();
        }
    }
    
    public DrawHandler getDrawHandler()
    {
        return drawHandler;
    }
    
    public void setDrawHandler(DrawHandler drawHandler)
    {
        this.drawHandler = drawHandler;
    }
    
    public boolean getTemporary()
    {
        return temporary;
    }
    
    public void setTemporary(boolean temporary)
    {
        this.temporary = temporary;
    }
    
    public int getIndex()
    {
        return index;
    }
    
    public void setIndex(int index)
    {
        Collections.sort(getImage().getLayers());
        this.index = index;
    }
    
    public BufferedImage getBufferedImage()
    {
        return bufferedImage;
    }
    
    public void setBufferedImage(BufferedImage bufferedImage)
    {
        this.bufferedImage = bufferedImage;
    }
    
    public Image getImage()
    {
        return image;
    }
    
    public void setImage(Image image)
    {
        this.image = image;
    }
    
    @Override
    public int compareTo(ImageLayer other)
    {
        return new Integer(index).compareTo(new Integer(other.getIndex()));
    }
}
