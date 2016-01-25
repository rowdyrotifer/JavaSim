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
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;

import com.marklalor.javasim.Home;
import com.marklalor.javasim.JavaSim;

/**<p>
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
 * Modified by Mark Lalor for use with JavaSim.
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
            public void dragOver(DropTargetDragEvent evt) { }
            
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
                        List fileList = (List)
                                tr.getTransferData(DataFlavor.javaFileListFlavor);
                        Iterator iterator = fileList.iterator();
                        
                        // Convert list to array
                        File[] filesTemp = new File[fileList.size()];
                        fileList.toArray(filesTemp);
                        final File[] files = filesTemp;
                        
                        // Alert listener to drop.
                        if(listener != null)
                            listener.filesDropped(files);
                        
                        // Mark that drop is completed.
                        evt.getDropTargetContext().dropComplete(true);
                    } // end if: file list
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
            List list = new ArrayList();
            String line = null;
            while((line = bReader.readLine()) != null)
            {
                try
                {
                    // kde seems to append a 0 char to the end of the reader
                    if(ZERO_CHAR_STRING.equals(line))
                        continue;
                    
                    java.io.File file = new java.io.File(new java.net.URI(line));
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
            //2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
            //Is the flavor a file list?
            final DataFlavor curFlavor = flavors[i];
            if(curFlavor.equals(DataFlavor.javaFileListFlavor) || curFlavor.isRepresentationClassReader())
                ok = true;
            i++;
        }
        
        return ok;
    } // end isDragOk
    
    /**
     * Removes the drag-and-drop hooks from the component and optionally from
     * the all children. You should call this if you add and remove components
     * after you've set up the drag-and-drop. This will recursively unregister
     * all components contained within <var>c</var> if <var>c</var> is a
     * {@link java.awt.Container}.
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
     * Removes the drag-and-drop hooks from the component and optionally from
     * the all children. You should call this if you add and remove components
     * after you've set up the drag-and-drop.
     *
     * @param out
     *            Optional {@link java.io.PrintStream} for logging drag and drop
     *            messages
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
     * Implement this inner interface to listen for when files are dropped. For
     * example your class declaration may begin like this: <code><pre>
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
     * This is the event that is passed to the
     * {@link FileDropListener#filesDropped filesDropped(...)} method in your
     * {@link FileDropListener} when files are dropped onto a registered drop
     * target.
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
         * Constructs an {@link Event} with the array of files that were dropped
         * and the {@link FileDropManager} that initiated the event.
         *
         * @param files
         *            The array of files that were dropped
         * @source The event source
         * @since 1.1
         */
        public Event(java.io.File[] files, Object source)
        {
            super(source);
            this.files = files;
        } // end constructor
        
        /**
         * Returns an array of files that were dropped on a registered drop
         * target.
         *
         * @return array of files that were dropped
         * @since 1.1
         */
        public java.io.File[] getFiles()
        {
            return files;
        }
    }
    
    /* ******** I N N E R C L A S S ******** */
    
    /**
     * At last an easy way to encapsulate your custom objects for dragging and
     * dropping in your Java programs! When you need to create a
     * {@link java.awt.datatransfer.Transferable} object, use this class to wrap
     * your object. For example:
     * 
     * <pre>
     * <code>
     *      ...
     *      MyCoolClass myObj = new MyCoolClass();
     *      Transferable xfer = new TransferableObject( myObj );
     *      ...
     * </code>
     * </pre>
     * 
     * Or if you need to know when the data was actually dropped, like when
     * you're moving data out of a list, say, you can use the
     * {@link TransferableObject.Fetcher} inner class to return your object Just
     * in Time. For example:
     * 
     * <pre>
     * <code>
     *      ...
     *      final MyCoolClass myObj = new MyCoolClass();
     * 
     *      TransferableObject.Fetcher fetcher = new TransferableObject.Fetcher()
     *      {   public Object getObject(){ return myObj; }
     *      }; // end fetcher
     * 
     *      Transferable xfer = new TransferableObject( fetcher );
     *      ...
     * </code>
     * </pre>
     *
     * The {@link java.awt.datatransfer.DataFlavor} associated with
     * {@link TransferableObject} has the representation class
     * <tt>net.iharder.dnd.TransferableObject.class</tt> and MIME type
     * <tt>application/x-net.iharder.dnd.TransferableObject</tt>. This data
     * flavor is accessible via the static {@link #DATA_FLAVOR} property.
     *
     *
     * <p>
     * I'm releasing this code into the Public Domain. Enjoy.
     * </p>
     * 
     * @author Robert Harder
     * @author rob@iharder.net
     * @version 1.2
     */
    public static class TransferableObject implements java.awt.datatransfer.Transferable
    {
        /**
         * The MIME type for {@link #DATA_FLAVOR} is
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @since 1.1
         */
        public final static String MIME_TYPE = "application/x-net.iharder.dnd.TransferableObject";
        
        /**
         * The default {@link java.awt.datatransfer.DataFlavor} for
         * {@link TransferableObject} has the representation class
         * <tt>net.iharder.dnd.TransferableObject.class</tt> and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @since 1.1
         */
        public final static DataFlavor DATA_FLAVOR = new DataFlavor(FileDropManager.TransferableObject.class, MIME_TYPE);
        
        private Fetcher fetcher;
        private Object data;
        
        private DataFlavor customFlavor;
        
        /**
         * Creates a new {@link TransferableObject} that wraps <var>data</var>.
         * Along with the {@link #DATA_FLAVOR} associated with this class, this
         * creates a custom data flavor with a representation class determined
         * from <code>data.getClass()</code> and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @param data
         *            The data to transfer
         * @since 1.1
         */
        public TransferableObject(Object data)
        {
            this.data = data;
            this.customFlavor = new java.awt.datatransfer.DataFlavor(data.getClass(), MIME_TYPE);
        } // end constructor
        
        /**
         * Creates a new {@link TransferableObject} that will return the object
         * that is returned by <var>fetcher</var>. No custom data flavor is set
         * other than the default {@link #DATA_FLAVOR}.
         *
         * @see Fetcher
         * @param fetcher
         *            The {@link Fetcher} that will return the data object
         * @since 1.1
         */
        public TransferableObject(Fetcher fetcher)
        {
            this.fetcher = fetcher;
        }
        
        /**
         * Creates a new {@link TransferableObject} that will return the object
         * that is returned by <var>fetcher</var>. Along with the
         * {@link #DATA_FLAVOR} associated with this class, this creates a
         * custom data flavor with a representation class <var>dataClass</var>
         * and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @see Fetcher
         * @param dataClass
         *            The {@link java.lang.Class} to use in the custom data
         *            flavor
         * @param fetcher
         *            The {@link Fetcher} that will return the data object
         * @since 1.1
         */
        public TransferableObject(Class dataClass, Fetcher fetcher)
        {
            this.fetcher = fetcher;
            this.customFlavor = new java.awt.datatransfer.DataFlavor(dataClass, MIME_TYPE);
        } // end constructor
        
        /**
         * Returns the custom {@link java.awt.datatransfer.DataFlavor}
         * associated with the encapsulated object or <tt>null</tt> if the
         * {@link Fetcher} constructor was used without passing a
         * {@link java.lang.Class}.
         *
         * @return The custom data flavor for the encapsulated object
         * @since 1.1
         */
        public java.awt.datatransfer.DataFlavor getCustomDataFlavor()
        {
            return customFlavor;
        } // end getCustomDataFlavor
        
        /* ******** T R A N S F E R A B L E M E T H O D S ******** */
        
        /**
         * Returns a two- or three-element array containing first the custom
         * data flavor, if one was created in the constructors, second the
         * default {@link #DATA_FLAVOR} associated with
         * {@link TransferableObject}, and third the
         * {@link java.awt.datatransfer.DataFlavor.stringFlavor}.
         *
         * @return An array of supported data flavors
         * @since 1.1
         */
        public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors()
        {
            if(customFlavor != null)
                return new java.awt.datatransfer.DataFlavor[]
                { customFlavor,
                        DATA_FLAVOR,
                        java.awt.datatransfer.DataFlavor.stringFlavor
                }; // end flavors array
            else
                return new java.awt.datatransfer.DataFlavor[]
                { DATA_FLAVOR,
                        java.awt.datatransfer.DataFlavor.stringFlavor
                }; // end flavors array
        } // end getTransferDataFlavors
        
        /**
         * Returns the data encapsulated in this {@link TransferableObject}. If
         * the {@link Fetcher} constructor was used, then this is when the
         * {@link Fetcher#getObject getObject()} method will be called. If the
         * requested data flavor is not supported, then the
         * {@link Fetcher#getObject getObject()} method will not be called.
         *
         * @param flavor
         *            The data flavor for the data to return
         * @return The dropped data
         * @since 1.1
         */
        public Object getTransferData(java.awt.datatransfer.DataFlavor flavor)
                throws java.awt.datatransfer.UnsupportedFlavorException, java.io.IOException
        {
            // Native object
            if(flavor.equals(DATA_FLAVOR))
                return fetcher == null ? data : fetcher.getObject();
            
            // String
            if(flavor.equals(java.awt.datatransfer.DataFlavor.stringFlavor))
                return fetcher == null ? data.toString() : fetcher.getObject().toString();
            
            // We can't do anything else
            throw new java.awt.datatransfer.UnsupportedFlavorException(flavor);
        } // end getTransferData
        
        /**
         * Returns <tt>true</tt> if <var>flavor</var> is one of the supported
         * flavors. Flavors are supported using the <code>equals(...)</code>
         * method.
         *
         * @param flavor
         *            The data flavor to check
         * @return Whether or not the flavor is supported
         * @since 1.1
         */
        public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor)
        {
            // Native object
            if(flavor.equals(DATA_FLAVOR))
                return true;
            
            // String
            if(flavor.equals(java.awt.datatransfer.DataFlavor.stringFlavor))
                return true;
            
            // We can't do anything else
            return false;
        } // end isDataFlavorSupported
        
        /* ******** I N N E R I N T E R F A C E F E T C H E R ******** */
        
        /**
         * Instead of passing your data directly to the
         * {@link TransferableObject} constructor, you may want to know exactly
         * when your data was received in case you need to remove it from its
         * source (or do anyting else to it). When the {@link #getTransferData
         * getTransferData(...)} method is called on the
         * {@link TransferableObject}, the {@link Fetcher}'s {@link #getObject
         * getObject()} method will be called.
         *
         * @author Robert Harder
         * @copyright 2001
         * @version 1.1
         * @since 1.1
         */
        public static interface Fetcher
        {
            /**
             * Return the object being encapsulated in the
             * {@link TransferableObject}.
             *
             * @return The dropped object
             * @since 1.1
             */
            public abstract Object getObject();
        } // end inner interface Fetcher
    }
}
