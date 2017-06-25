package com.marklalor.javasim.content.interfacing.ui;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.google.common.collect.Range;
import com.marklalor.javasim.content.interfacing.output.OutputData;
import com.marklalor.javasim.content.interfacing.ui.capabilities.Playable;
import com.marklalor.javasim.content.interfacing.ui.capabilities.Rewindable;

public class BufferedImageSeriesInterfacer
        extends SwingInterfacerBase<List<BufferedImage>, OutputData<List<BufferedImage>>>
        implements Playable, Rewindable
{
    private List<BufferedImage> images;
    private JLabel component;
    private int currentFrame = 0;
    
    public BufferedImageSeriesInterfacer(OutputData<List<BufferedImage>> data)
    {
        super(data);
        this.images = new ArrayList<>(64);
    }
    
    private static BufferedImage cloneImage(BufferedImage image)
    {
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
    @Override
    protected JComponent createJComponent()
    {
        this.component = new JLabel();
        return this.component;
    }
    
    private void update()
    {
        this.component.setIcon(new ImageIcon(this.images.get(this.currentFrame)));
    }
    
    @Override
    public void setFrame(int frame)
    {
        if(this.currentFrame == frame)
            return;
        
        if(getFrameRange().contains(this.currentFrame))
            this.currentFrame = frame;
        else if(this.currentFrame < getFrameRange().lowerEndpoint())
            this.currentFrame = getFrameRange().lowerEndpoint();
        else if(this.currentFrame > getFrameRange().upperEndpoint())
            this.currentFrame = getFrameRange().upperEndpoint();
        
        update();
    }
    
    @Override
    public Range<Integer> getFrameRange()
    {
        return Range.closed(0, this.images.size());
    }
    
    @Override
    public void next()
    {
        this.currentFrame++;
        update();
    }
}
