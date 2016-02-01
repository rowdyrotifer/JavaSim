package com.marklalor.javasim.misc;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.TooManyListenersException;

import com.marklalor.javasim.Home;
import com.marklalor.javasim.JavaSim;

/**
 * <p>
 * I'm releasing this code into the Public Domain. Enjoy.
 * </p>
 * <p>
 * <em>Original author: Robert Harder, rharder@usa.net</em>
 * </p>
 * <p>
 * 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
 * </p>
 *
 * @author Robert Harder
 * @author rharder@users.sf.net
 * @version 1.0.1
 * 
 *          Modified by Mark Lalor for use with JavaSim.
 * 
 */
public class FileDropManager
{
    private DropTargetListener dropListener;
    
    public FileDropManager(final Home home, final Listener listener)
    {
        dropListener = new DropTargetListener()
        {
            public void dragEnter(DropTargetDragEvent evt)
            {
                if(isDragOk(evt))
                {
                    home.setFileDropVisible(true);
                    evt.acceptDrag(DnDConstants.ACTION_COPY);
                }
                else
                    evt.rejectDrag();
            }
            
            // This is called continually as long as the mouse is over the
            // drag target.
            public void dragOver(DropTargetDragEvent evt)
            {
            }
            
            public void drop(DropTargetDropEvent evt)
            {
                try
                { // Get whatever was dropped
                    Transferable tr = evt.getTransferable();
                    
                    // Is it a file list?
                    if(tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                    {
                        evt.acceptDrop(DnDConstants.ACTION_COPY);
                        
                        // Get a useful list
                        @SuppressWarnings("unchecked")
                        List<File> fileList = (List<File>) tr.getTransferData(DataFlavor.javaFileListFlavor);
                        
                        // Convert list to array
                        File[] filesTemp = new File[fileList.size()];
                        fileList.toArray(filesTemp);
                        final File[] files = filesTemp;
                        
                        // Alert listener to drop.
                        if(listener != null)
                            listener.filesDropped(files);
                        
                        // Mark that drop is completed.
                        evt.getDropTargetContext().dropComplete(true);
                    }
                    else
                    // this section will check for a reader flavor.
                    {
                        // Thanks, Nathan!
                        // BEGIN 2007-09-12 Nathan Blomquist -- Linux
                        // (KDE/Gnome) support added.
                        DataFlavor[] flavors = tr.getTransferDataFlavors();
                        boolean handled = false;
                        for(int zz = 0; zz < flavors.length; zz++)
                        {
                            if(flavors[zz].isRepresentationClassReader())
                            {
                                // Say we'll take it.
                                // evt.acceptDrop (
                                // java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE
                                // );
                                evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
                                
                                Reader reader = flavors[zz].getReaderForText(tr);
                                BufferedReader br = new BufferedReader(reader);
                                
                                if(listener != null)
                                    listener.filesDropped(createFileArray(br));
                                
                                // Mark that drop is completed.
                                evt.getDropTargetContext().dropComplete(true);
                                JavaSim.getLogger().trace("FileDrop: drop complete.");
                                handled = true;
                                break;
                            }
                        }
                        if(!handled)
                        {
                            evt.rejectDrop();
                        }
                        // END 2007-09-12 Nathan Blomquist -- Linux
                        // (KDE/Gnome) support added.
                    }
                }
                catch(java.io.IOException e)
                {
                    JavaSim.getLogger().error("FileDrop IOException", e);
                    evt.rejectDrop();
                }
                catch(UnsupportedFlavorException e)
                {
                    JavaSim.getLogger().error("FileDrop UnsupportedFlavorException", e);
                    evt.rejectDrop();
                }
                finally
                {
                    home.setFileDropVisible(false);
                }
            }
            
            public void dragExit(DropTargetEvent evt)
            {
                home.setFileDropVisible(false);
            }
            
            public void dropActionChanged(java.awt.dnd.DropTargetDragEvent evt)
            {
                // Is this an acceptable drag event?
                if(isDragOk(evt))
                {
                    evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY);
                }
                else
                {
                    evt.rejectDrag();
                }
            }
        };
        
        // Make the component (and possibly children) drop targets
        makeDropTarget(home);
    } // end constructor
    
    // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
    private static String ZERO_CHAR_STRING = "" + (char) 0;
    
    private static File[] createFileArray(BufferedReader bReader)
    {
        try
        {
            List<File> list = new ArrayList<File>();
            String line = null;
            while((line = bReader.readLine()) != null)
            {
                try
                {
                    // kde seems to append a 0 char to the end of the reader
                    if(ZERO_CHAR_STRING.equals(line))
                        continue;
                    
                    File file = new File(new URI(line));
                    list.add(file);
                }
                catch(Exception e)
                {
                    JavaSim.getLogger().error("FileDrop: Error with {}: ", line, e);
                }
            }
            
            return (File[]) list.toArray(new File[list.size()]);
        }
        catch(IOException ex)
        {
            JavaSim.getLogger().error("FileDrop IOException");
        }
        return new File[0];
    }
    
    private void makeDropTarget(final Component c)
    {
        // Make drop target
        final DropTarget dt = new DropTarget();
        
        try
        {
            dt.addDropTargetListener(dropListener);
        }
        catch(TooManyListenersException e)
        {
            JavaSim.getLogger().error("FileDrop: Drop will not work due to previous error. Do you have another listener attached?", e);
        }
        
        // Listen for hierarchy changes and remove the drop target when the
        // parent gets cleared out.
        c.addHierarchyListener(new java.awt.event.HierarchyListener()
        {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt)
            {
                java.awt.Component parent = c.getParent();
                if(parent == null)
                {
                    c.setDropTarget(null);
                }
                else
                {
                    new DropTarget(c, dropListener);
                }
            }
        });
        
        if(c.getParent() != null)
            new java.awt.dnd.DropTarget(c, dropListener);
        
        if(c instanceof java.awt.Container)
        {
            // Get the container
            java.awt.Container cont = (java.awt.Container) c;
            
            // Get it's components
            java.awt.Component[] comps = cont.getComponents();
            
            // Set it's components as listeners also
            for(int i = 0; i < comps.length; i++)
                makeDropTarget(comps[i]);
        } // end if: recursively set components as listener
    } // end dropListener
    
    /** Determine if the dragged data is a file list. */
    private boolean isDragOk(final DropTargetDragEvent evt)
    {
        boolean ok = false;
        
        // Get data flavors being dragged
        DataFlavor[] flavors = evt.getCurrentDataFlavors();
        
        // See if any of the flavors are a file list
        int i = 0;
        while(!ok && i < flavors.length)
        {
            // 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
            // Is the flavor a file list?
            final DataFlavor curFlavor = flavors[i];
            if(curFlavor.equals(DataFlavor.javaFileListFlavor) || curFlavor.isRepresentationClassReader())
                ok = true;
            i++;
        }
        
        return ok;
    } // end isDragOk
    
    /**
     * Removes the drag-and-drop hooks from the component and optionally from the all children. You should call this if
     * you add and remove components after you've set up the drag-and-drop. This will recursively unregister all
     * components contained within <var>c</var> if <var>c</var> is a {@link java.awt.Container}.
     *
     * @param c
     *            The component to unregister as a drop target
     * @since 1.0
     */
    public static boolean remove(java.awt.Component c)
    {
        return remove(null, c, true);
    } // end remove
    
    /**
     * Removes the drag-and-drop hooks from the component and optionally from the all children. You should call this if
     * you add and remove components after you've set up the drag-and-drop.
     *
     * @param out
     *            Optional {@link java.io.PrintStream} for logging drag and drop messages
     * @param c
     *            The component to unregister
     * @param recursive
     *            Recursively unregister components within a container
     * @since 1.0
     */
    public static boolean remove(java.io.PrintStream out, java.awt.Component c, boolean recursive)
    {
        c.setDropTarget(null);
        if(recursive && (c instanceof java.awt.Container))
        {
            java.awt.Component[] comps = ((java.awt.Container) c).getComponents();
            for(int i = 0; i < comps.length; i++)
                remove(out, comps[i], recursive);
            return true;
        } // end if: recursive
        else
            return false;
    } // end remove
    
    /* ******** I N N E R I N T E R F A C E L I S T E N E R ******** */
    
    /**
     * Implement this inner interface to listen for when files are dropped. For example your class declaration may begin
     * like this: <code><pre>
     *      public class MyClass implements FileDrop.Listener
     *      ...
     *      public void filesDropped( java.io.File[] files )
     *      {
     *          ...
     *      }   // end filesDropped
     *      ...
     * </pre></code>
     *
     * @since 1.1
     */
    public static interface Listener
    {
        public abstract void filesDropped(java.io.File[] files);
    }
    
    /* ******** I N N E R C L A S S ******** */
    
    /**
     * This is the event that is passed to the {@link FileDropListener#filesDropped filesDropped(...)} method in your
     * {@link FileDropListener} when files are dropped onto a registered drop target.
     *
     * <p>
     * I'm releasing this code into the Public Domain. Enjoy.
     * </p>
     * 
     * @author Robert Harder
     * @author rob@iharder.net
     * @version 1.2
     */
    @SuppressWarnings("serial")
    public static class Event extends EventObject
    {
        private File[] files;
        
        /**
         * Constructs an {@link Event} with the array of files that were dropped and the {@link FileDropManager} that
         * initiated the event.
         *
         * @param files
         *            The array of files that were dropped
         * @source The event source
         * @since 1.1
         */
        public Event(File[] files, Object source)
        {
            super(source);
            this.files = files;
        } // end constructor
        
        /**
         * Returns an array of files that were dropped on a registered drop target.
         *
         * @return array of files that were dropped
         * @since 1.1
         */
        public File[] getFiles()
        {
            return files;
        }
    }
}
