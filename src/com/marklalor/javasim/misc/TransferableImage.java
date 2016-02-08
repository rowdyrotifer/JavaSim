package com.marklalor.javasim.misc;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TransferableImage implements Transferable
{
    private BufferedImage image;
    
    public TransferableImage(BufferedImage image)
    {
        this.image = image;
    }
    
    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        if(flavor.equals(DataFlavor.imageFlavor) && image != null)
            return image;
        else
            throw new UnsupportedFlavorException(flavor);
    }
    
    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        DataFlavor[] flavors = new DataFlavor[1];
        flavors[0] = DataFlavor.imageFlavor;
        return flavors;
    }
    
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        DataFlavor[] flavors = getTransferDataFlavors();
        for(int i = 0; i < flavors.length; i++)
            if(flavor.equals(flavors[i]))
                return true;
        
        return false;
    }
}
