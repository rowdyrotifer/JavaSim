package com.marklalor.javasim.imaging;

// 
//  GifSequenceWriter.java
//  
//  Created by Elliot Kroo on 2009-04-25.
//
//  Modified by Mark Lalor on 2015-08-17.
//
//  Modifications were made to syntax style
//  as well as a way to have a variable delay
//  between frames. 
//
// This work is licensed under the Creative Commons Attribution 3.0 Unported
// License. To view a copy of this license, visit
// http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
// Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
//

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.image.*;
import java.io.*;
import java.util.Iterator;

public class GifSequenceWriter
{
	private ImageWriter gifWriter;
	private ImageWriteParam imageWriteParam;
	private IIOMetadata imageMetaData;
	
	//Converting the previous local contructor variables to fields to access them to set the delay over and over.
	private String metaFormatName;
	private IIOMetadataNode root;
	private IIOMetadataNode graphicsControlExtensionNode;
	
	/**
	 * Creates a new GifSequenceWriter
	 * 
	 * @param outputStream
	 *            the ImageOutputStream to be written to
	 * @param imageType
	 *            one of the imageTypes specified in BufferedImage
	 * @param delay
	 *            the time between frames in milliseconds
	 * @param loopContinuously
	 *            whether the gif should loop repeatedly
	 * @throws IIOException
	 *             if no gif ImageWriters are found
	 *
	 * @author Elliot Kroo (elliot[at]kroo[dot]net)
	 */
	public GifSequenceWriter(ImageOutputStream outputStream, int imageType, int delay, boolean loopContinuously) throws IIOException, IOException
	{
		// my method to create a writer
		gifWriter = getWriter();
		imageWriteParam = gifWriter.getDefaultWriteParam();
		ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(imageType);
		
		imageMetaData = gifWriter.getDefaultImageMetadata(imageTypeSpecifier, imageWriteParam);
		
		metaFormatName = imageMetaData.getNativeMetadataFormatName();
		
		root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);
		graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
		
		graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
		graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
		graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
		graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(delay));
		// graphicsControlExtensionNode.setAttribute("delayTime","8"); perhaps should be 6?
		// see http://nullsleep.tumblr.com/post/16524517190/animated-gif-minimum-frame-delay-browser
		graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");
		
		IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
		commentsNode.setAttribute("CommentExtension", "Created by MAH");
		
		IIOMetadataNode appEntensionsNode = getNode(root, "ApplicationExtensions");
		IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");
		
		child.setAttribute("applicationID", "NETSCAPE");
		child.setAttribute("authenticationCode", "2.0");
		
		int loop = loopContinuously ? 0 : 1;
		
		child.setUserObject(new byte[] { 0x1, (byte) (loop & 0xFF), (byte)((loop >> 8) & 0xFF) });
		appEntensionsNode.appendChild(child);
		
		imageMetaData.setFromTree(metaFormatName, root);
		gifWriter.setOutput(outputStream);
		gifWriter.prepareWriteSequence(null);
	}
	
	public void writeToSequence(RenderedImage img, int delay) throws IOException
	{
		graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(delay/10));
		imageMetaData.setFromTree(metaFormatName, root);
		gifWriter.writeToSequence(new IIOImage(img, null, imageMetaData), imageWriteParam);
	}
	
	/**
	 * Close this GifSequenceWriter object. This does not close the underlying
	 * stream, just finishes off the GIF.
	 */
	public void close() throws IOException
	{
		gifWriter.endWriteSequence();
	}
	
	/**
	 * Returns the first available GIF ImageWriter using
	 * ImageIO.getImageWritersBySuffix("gif").
	 * 
	 * @return a GIF ImageWriter object
	 * @throws IIOException
	 *             if no GIF image writers are returned
	 */
	private static ImageWriter getWriter() throws IIOException
	{
		Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
		if(!iter.hasNext())
			throw new IIOException("No GIF Image Writers Exist");
		else
			return iter.next();
	}
	
	/**
	 * Returns an existing child node, or creates and returns a new child node
	 * (if the requested node does not exist).
	 * 
	 * @param rootNode
	 *            the <tt>IIOMetadataNode</tt> to search for the child node.
	 * @param nodeName
	 *            the name of the child node.
	 * 
	 * @return the child node, if found or a new node created with the given
	 *         name.
	 */
	private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName)
	{
		int nNodes = rootNode.getLength();
		for(int i = 0; i < nNodes; i++)
			if(rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0)
				return ((IIOMetadataNode) rootNode.item(i));
		IIOMetadataNode node = new IIOMetadataNode(nodeName);
		rootNode.appendChild(node);
		return node;
	}
}
