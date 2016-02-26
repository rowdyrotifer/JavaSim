package com.marklalor.javasim.simulation;

import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.apple.eawt.Application;
import com.apple.eawt.FullScreenUtilities;
import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.home.Home;
import com.marklalor.javasim.menu.menus.JavaSimMenu;
import com.marklalor.javasim.misc.MiscUtil;
import com.marklalor.javasim.misc.image.TransferableImage;
import com.marklalor.javasim.simulation.control.Control;
import com.marklalor.javasim.simulation.frames.FrameHolder;
import com.marklalor.javasim.simulation.frames.HomeHolder;
import com.marklalor.javasim.simulation.frames.MenuHolder;
import com.marklalor.javasim.simulation.frames.subframes.Animate;
import com.marklalor.javasim.simulation.frames.subframes.Controls;
import com.marklalor.javasim.simulation.frames.subframes.Resize;
import com.marklalor.javasim.simulation.image.Image;
import com.marklalor.javasim.simulation.manager.play.PlayManager;
import com.marklalor.javasim.simulation.preset.BlankImageSimulation;

/**
 * <p>
 * Master simulation class, containing components such as the {@link SimulationInfo}, the {@link Image}, and the
 * {@link Controls}. A simulation subclass is responsible for implementing the {@link #initialize()}, {@link #reset()},
 * and {@link #draw(Graphics2D, Graphics2D)} methods.
 * </p>
 * <p>
 * For even more bare-bones implementations of a simulation, see presets such as {@link BlankImageSimulation}, which are
 * Simulations designed for simple target behavior, such as creating an image with a white background.
 * </p>
 * 
 * @see <a href = "https://github.com/MarkLalor/JavaSim#example">https://github.com/MarkLalor/JavaSim#example</a>
 * @see #initialize()
 * @see #reset()
 * @see #draw(Graphics2D, Graphics2D)
 */
public abstract class Simulation implements ClipboardOwner, HomeHolder
{
    public static final int DEFAULT_IMAGE_WIDTH = 500, DEFAULT_IMAGE_HEIGHT = 500;
    public static final int DEFAULT_CONTROL_WIDTH = 100, DEFAULT_CONTROL_HEIGHT = 500;
    
    private Home home;
    private SimulationInfo info;
    private Integer jarId;
    
    private File contentDirectory;
    
    private boolean fullscreen = false;
    
    //Controllers / Managers
    private PlayManager playManager;
    
    // Main Frames
    private Image image;
    private Controls controls;
    
    // Other Frames
    private Animate animate;
    private Resize resize;
    
    // Simulation abstract methods:
    
    /**
     * <p>
     * Called by {@link Home#run(Home, SimulationInfo)} when a simulation is ran via the home screen. This is called
     * after most of the core initialization is done by {@link #preInitialize(SimulationInfo)}. And before any
     * initialization-reliant tasks done in {@link #postInitialize()}. This is an appropriate time to do things such as:
     * </p>
     * <p>
     * <ul>
     * <li>initialize your own fields (like a regular constructor).</li>
     * <li>initialize JavaSim controls (see {@link Control}).</li>
     * <li>show, set size of, and set location of panels (e.g. {@link #getImage()}, {@link #getControls()}).</li>
     * </ul>
     * </p>
     * <p>
     * For clarification, this method is called in {@link Home#run(SimulationInfo)} like so:
     * 
     * <pre>
     * {@code
     * simulation.preInitialize(home, info);
     * simulation.initialize();
     * simulation.postInitialize();
     * simulation.resetAction();
     * }
     * </pre>
     * 
     * </p>
     */
    public abstract void initialize();
    
    public void preInitialize(Home home, Integer id, SimulationInfo info)
    {
        // Don't allow anyone to use this method a second time… just… no.
        if(getInfo() != null)
            throw new RuntimeException("Do not call method com.marklalor.javasim.simulation.Simulation#preInitialize more than once (it is called once automatically by JavaSim Home)");
        
        // Set the Home this simulation was launched from.
        this.home = home;
        
        this.jarId = id;
        
        // Inherit the info read from its file earlier.
        this.info = info;
        
        this.playManager = new PlayManager(this);
        
        // Set the simulation's content directory and (make sure it exists).
        contentDirectory = new File(getHome().getApplicationPreferences().getSaveDirectory(), info.getName());
        contentDirectory.mkdirs();
        
        // Create and set up the main image that goes with this simulation.
        image = new Image(this);
        image.setImageSize(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
        image.getFrame().setLocationRelativeTo(null);
        resolveTitle();
        
        // Create and set up the main control panel for this simulation.
        controls = new Controls(this);
        controls.setSize(DEFAULT_CONTROL_WIDTH, DEFAULT_CONTROL_HEIGHT); // TODO: add more wrapper methods (only for
                                                                         // appropriate methods, though...)
        controls.getFrame().setLocationRelativeTo(getImage().getFrame());
        controls.getFrame().setLocation(getControls().getFrame().getLocation().x - (getImage().getFrame().getWidth() / 2) - (getControls().getFrame().getWidth() / 2), getImage().getFrame().getY());
        
        // Other dialogs.
        resize = new Resize(this);
        
        addMenuTo(getImage());
        addMenuTo(getControls());
        addMenuTo(getResize());
        
        // Set sizes to the default dimensions
        getImage().setImageSize(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
        
        if(System.getProperty("os.name").toLowerCase().startsWith("mac os x"))
        {
            // OS X fullscreen support:
            FullScreenUtilities.setWindowCanFullScreen(getImage().getFrame(), true);
            // Allow esc key to... well... escape fullscreen
            getImage().getFrame().addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyPressed(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                        Simulation.this.setFullscreen(false);
                }
            });
        }
    }
    
    /**
     * Contains initialization code that relies on being run after {@link #initialize()}. For example, the created of
     * the animate window for a simulation is done here because the user should have initialized them by the time of
     * <code>initialize</code>, but not necessarily before then.
     * 
     * @see Home#run(SimulationInfo)
     */
    public void postInitialize()
    {
        // Relies on what the user initializes for animating over a variable.
        animate = new Animate(this);
        addMenuTo(getAnimate());
    }
    
    private void setFullscreen(boolean fullscreen)
    {
        if(this.fullscreen != fullscreen)
        {
            if(getHome().getApplicationPreferences().isMacOSX())
                Application.getApplication().requestToggleFullScreen(getImage().getFrame());
            // TODO: Windows, Linux fullscreen.
            
            this.fullscreen = fullscreen;
        }
    }
    
    public void toggleFullscreen()
    {
        if(getHome().getApplicationPreferences().isMacOSX())
            Application.getApplication().requestToggleFullScreen(getImage().getFrame());
        
        this.fullscreen = !this.fullscreen;
    }
    
    private void addMenuTo(MenuHolder<JavaSimMenu> menuHolder)
    {
        if (!(menuHolder instanceof FrameHolder))
            throw new RuntimeException("If you add a menu to a MenuHolder, they have to also be a FrameHolder!");
        
        menuHolder.setMenu(new JavaSimMenu(getHome(), this, (FrameHolder) menuHolder));
        
        ((FrameHolder) menuHolder).getFrame().setJMenuBar(menuHolder.getMenu().getMenuBar());
    }
    
    public void resolveTitle()
    {
        int freq = getPlayManager().getFrequencyMonitor().getFrequency();
        getImage().getFrame().setTitle(info.getName() + " – " + info.getAuthor() + " at " + (freq == -1 ? "?" : freq) + " Hz");
    }
    
    
    public void copyImageToClipboard()
    {
        if(getImage().getAggregateImage() != null)
        {
            TransferableImage transferableImage = new TransferableImage(getImage().getAggregateImage());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferableImage, this);
        }
    }
    
    public void resetAction()
    {
        getPlayManager().setFrameNumber(0);
        getImage().renderAggregateImage(true);
        getImage().repaint();
    }
    
    public File getDefaultFile()
    {
        return new File(getContentDirectory(), "frame_" + MiscUtil.getTimestamp() + ".png");
    }
    
    public void saveAs()
    {
        if(getHome().getApplicationPreferences().isMacOSX())
        {
            // Native file dialog, but with less options / cross platform support.
            FileDialog dialog = new FileDialog(getImage().getFrame(), "Choose a file", FileDialog.SAVE);
            
            dialog.setDirectory(getContentDirectory().getAbsolutePath());
            dialog.setFile(getDefaultFile().getName());
            
            dialog.setVisible(true);
            
            String directory = dialog.getDirectory();
            String fileName = dialog.getFile();
            
            if(directory != null && fileName != null)
            {
                if(!fileName.endsWith(".png"))
                    fileName += ".png";
                save(new File(new File(directory), fileName));
            }
            else
                JavaSim.getLogger().debug("FileDialog canceled.");
        }
        else
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
            chooser.setSelectedFile(getDefaultFile());
            if(chooser.showSaveDialog(getImage().getFrame()) == JFileChooser.APPROVE_OPTION)
            {
                String file = chooser.getSelectedFile().getAbsolutePath();
                if(!chooser.getSelectedFile().getName().contains("."))
                    file += ".png";
                save(new File(file));
            }
        }
    }
    
    public void save(File file)
    {
        try
        {
            ImageIO.write(getImage().getAggregateImage(), "png", file);
            JavaSim.getLogger().info("Saved {}", file);
        }
        catch(IOException e)
        {
            JavaSim.getLogger().error("Could not save file {}", file, e);
        }
    }
    
    public BufferedImage getCurrentImageDeepCopy()
    {
        ColorModel cm = getImage().getAggregateImage().getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = getImage().getAggregateImage().copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
    public void lostOwnership(Clipboard clipboard, Transferable contents)
    {
        JavaSim.getLogger().info("Image no longer on clipboard.");
    }
    
    // Stop and close the simulation.
    public void close()
    {
        getPlayManager().stop();
        getHome().getJarManager().unloadFromId(getJarId());
    }
    
    public void breakpoint()
    {
        if(getPlayManager().shouldStopForBreakpoint())
            getPlayManager().stop();
    }
    
    public void setJarId(Integer jarId)
    {
        this.jarId = jarId;
    }
    
    public Integer getJarId()
    {
        return jarId;
    }
    
    public void setInfo(SimulationInfo info)
    {
        this.info = info;
    }
    
    public SimulationInfo getInfo()
    {
        return info;
    }
    
    @Override
    public Home getHome()
    {
        return home;
    }
    
    @Override
    public void setHome(Home home)
    {
        this.home = home;
    }
    
    public Image getImage()
    {
        return image;
    }
    
    public Controls getControls()
    {
        return controls;
    }
    
    public File getContentDirectory()
    {
        return contentDirectory;
    }
    
    public Animate getAnimate()
    {
        return animate;
    }
    
    public Resize getResize()
    {
        return resize;
    }
    
    public PlayManager getPlayManager()
    {
        return playManager;
    }
    
    // Pseudo-print: opens the png in the system editor.
    public void print()
    {
        File tempFile = new File(getHome().getApplicationPreferences().getTempDirectory(), getInfo().getName() + "_" + MiscUtil.getTimestamp() + ".png");
        
        // Save the file to the temporary file directory.
        try
        {
            ImageIO.write(getImage().getAggregateImage(), "png", tempFile);
        }
        catch(IOException e)
        {
            JavaSim.getLogger().error("Could not save temporary image for printing.", e);
        }
        
        // Open it with the default system editor.
        try
        {
            Desktop.getDesktop().open(tempFile);
        }
        catch(IOException e)
        {
            JavaSim.getLogger().error("Could not print temporary image.", e);
        }
    }
}
