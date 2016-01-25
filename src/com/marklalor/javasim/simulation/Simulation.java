package com.marklalor.javasim.simulation;

import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.apple.eawt.Application;
import com.apple.eawt.FullScreenUtilities;

import com.marklalor.javasim.Home;
import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.control.Control;
import com.marklalor.javasim.imaging.GifSequenceWriter;
import com.marklalor.javasim.imaging.TransferableImage;
import com.marklalor.javasim.simulation.frames.Image;
import com.marklalor.javasim.simulation.frames.subframes.Animate;
import com.marklalor.javasim.simulation.frames.subframes.Controls;
import com.marklalor.javasim.simulation.frames.subframes.Resize;
import com.marklalor.javasim.simulation.preset.BlankImageSimulation;

/**
 * <p>
 * Master simulation class, containing components such as the {@link SimulationInfo}, the {@link Image}, and the {@link Controls}.
 * A simulation subclass is responsible for implementing the {@link #initialize()}, {@link #reset()}, and {@link #draw(Graphics2D, Graphics2D)} methods.
 * </p>
 * <p>
 * For even more bare-bones implementations of a simulation, see presets such as {@link BlankImageSimulation}, which are Simulations designed for simple target behavior, such
 * as creating an image with a white background.
 * </p>
 * @see <a href = "https://github.com/MarkLalor/JavaSim#example">https://github.com/MarkLalor/JavaSim#example</a>
 * @see #initialize()
 * @see #reset()
 * @see #draw(Graphics2D, Graphics2D)
 */
public abstract class Simulation implements ClipboardOwner, MouseListener, MouseWheelListener, MouseMotionListener
{
	public static final int DEFAULT_IMAGE_WIDTH = 500, DEFAULT_IMAGE_HEIGHT = 500;
	public static final int DEFAULT_CONTROL_WIDTH = 100, DEFAULT_CONTROL_HEIGHT = 500;
	public static final int DEFAULT_CONSOLE_WIDTH = 400, DEFAULT_CONSOLE_HEIGHT = 500;
	
	public int imgType = BufferedImage.TYPE_INT_ARGB;
	
	private Home home;
	private SimulationInfo info;
	
	private File contentDirectory;
	
	private boolean fullscreen = false;
	
	// Misc Main stucture;
	private List<Menu> menus;
	
	// Main Frames
	private Image image;
	private Controls controls;
	
	// Other Frames
	private Animate animate;
	private Resize resize;
	
	// Relating to the image
	private BufferedImage permanentImage, temporaryImage, combinedImage;
	private int width, height;
	
	// Timer
	private Timer timerManual;
	// For finding refresh rate
	private int hertz = 0;
	private long startTime;
	private boolean calculating = true;
	private int calculateCount = 0;
	private static final int calculateCountMax = 10;
	private static final int autoRefreshTime = 1; // seconds;
	
	// Breakpoint
	private boolean stopForBreakpoint = false;
	
	// Animation
	private Timer timerAnimation;
	private Timer timerAnimationVariable;
	
	private List<Control<?>> animationControls;
	private Control<?> currentAnimationControl;
	private List<Object> currentAnimationControlValueQueue;
	private int variableAnimationN = 0;
	
	private ImageOutputStream animationOut;
	private GifSequenceWriter animationWriter;
	
	// Simulation abstract methods:
	
	/**
	 * <p>
	 * Called by {@link Home#run(Home, SimulationInfo)} when a simulation is ran
	 * via the home screen. This is called after most of the core initialization
	 * is done by {@link #preInitialize(SimulationInfo)}. And before any
	 * initialization-reliant tasks done in {@link #postInitialize()}. This is
	 * an appropriate time to do things such as:
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
	 * <pre>
	 * {@code
	 * simulation.preInitialize(home, info);
	 * simulation.initialize();
	 * simulation.postInitialize();
	 * simulation.resetAction();
	 * }
	 * </pre>
	 * </p>
	 */
	public abstract void initialize();
	/**
	 * <p>
	 * Called when the simulation should be logically reset. From the GUI, this
	 * is done by Simulation→Reset, but it can also be called programmatically
	 * by {@link #resetAction()}. Currently, the simulation is reset:
	 * </p>
	 * <ul>
	 * <li>After all initialization is completed.</li>
	 * <li>From the menu item Simulation→Reset</li>
	 * <li>At the beginning of creation of an animation.</li>
	 * <li>On resize of a window.</li>
	 * </ul>
	 * </p>
	 * @param permanent <code>Graphics2D</code> object which may be drawn on
	 * to logically reset the simulation. For example, using {@link Graphics2D#fillRect(int, int, int, int)}
	 * to paint white on top of the whole image effectively resets it.
	 */
	public abstract void reset(Graphics2D permanent);
	/**
	 * <p>
	 * Called by an animation timer, typically the one started by Animation→Play,
	 * but also by File→Created Animated Gif. <code>permanent</code> and <code>temporary</code>
	 * are graphics objects intended to be drawn on by the implementer. Items
	 * drawn onto <code>permanent</code> persist throughout multiple frames,
	 * whereas items drawn onto <code>temporary</code> are removed after each frame.
	 * </p>
	 * 
	 * <p>
	 * Both can be used in conjunction depending on the needs of your app. For
	 * example, one might draw one shape onto <code>permanent</code> every frame
	 * in order to build up an image, while drawing a string onto the corner of
	 * <code>temporary</code> in order to keep track of variables that change
	 * each frame.
	 * </p>
	 * 
	 * @param permanent <code>Graphics2D</code> object whose drawings persist each frame.
	 * @param temporary <code>Graphics2D</code> object whose drawings are cleared each frame.
	 * @see Simulation 
	 */
	public abstract void draw(Graphics2D permanent, Graphics2D temporary);
	
	// Predefined listeners
	private class AggregateListner implements ActionListener, ChangeListener
	{
		@Override
		public void stateChanged(ChangeEvent e)
		{
			resetAction();
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			resetAction();
		}
	}
	
	AggregateListner resetAction = new AggregateListner();
	
	public AggregateListner getResetAction()
	{
		return resetAction;
	}
	
	/**
	 * <p>Simulation frame number.</p>
	 * <p>
	 * This field is protected instead of privated for the convenience of the child class.
	 * It is shorthand for {@link #getFrameNumber()}.
	 * </p>
	 * @see #getFrameNumber()
	 */
	protected int n = 0;
	
	/**
	 * <p>
	 * Gets the current frame of the simulation. This starts at 0 and is automatically incremented
	 * by 1 as the simulation is played. It is reset back to 0 when the simulation is reset.
	 * </p>
	 * <p>Use {@link #n} as a shorthand, if you wish. {@link #getFrameNumber()} simply returns {@link #n}.</p>
	 * @see {@link Simulation#n} 
	 * @return The simulation frame number.
	 */
	public int getFrameNumber()
	{
		return this.n;
	}
	
	/**
	 * @param n the new frame for the simulation.
	 * @see  #getFrameNumber()
	 */
	public void setFrameNumber(int n)
	{
		this.n = n;
	}
	
	/**
	 * Increments the current frame number by 1.
	 * @see  #getFrameNumber()
	 */
	public void incrementFrameNumber()
	{
		this.n++;
	}
	
	public void preInitialize(Home home, SimulationInfo info)
	{
		// Don't allow anyone to use this method a second time… just… no.
		if(getInfo() != null)
			throw new RuntimeException("Do not call method com.marklalor.javasim.simulation.Simulation#preInitialize more than once (it is called once automatically by JavaSim Home)");
		
		//Set the Home this simulation was launched from.
		setHome(home);
		
		// Inherit the info read from its file earlier.
		setInfo(info);
		
		// Set the simulation's content directory and (make sure it exists).
		contentDirectory = new File(getHome().getPreferences().getSaveDirectory(), info.getName());
		contentDirectory.mkdirs();
		
		// Create and set up the main image that goes with this simulation.
		image = new Image(this);
		image.getFrame().pack();
		image.setSize(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
		image.getFrame().setLocationRelativeTo(null);
		refreshTitle();
		
		// Create and set up the main control panel for this simulation.
		controls = new Controls(getImage());
		controls.setSize(DEFAULT_CONTROL_WIDTH, DEFAULT_CONTROL_HEIGHT); //TODO: add more wrapper methods (only for appropriate methods, though...)
		controls.getFrame().setLocationRelativeTo(getImage().getFrame());
		controls.getFrame().setLocation(getControls().getFrame().getLocation().x - (getImage().getFrame().getWidth() / 2) - (getControls().getFrame().getWidth() / 2), getImage().getFrame().getY());
		
		// Reposition the console (but skip if the console is not bound)
		if (getHome().getPreferences().getConsoleBind())
		{
			getHome().getConsole().setSize(DEFAULT_CONSOLE_WIDTH, DEFAULT_CONSOLE_HEIGHT);
			getHome().getConsole().setLocationRelativeTo(getImage().getFrame());
			getHome().getConsole().setLocation(getHome().getConsole().getLocation().x + (getImage().getFrame().getWidth() / 2) + (getHome().getConsole().getWidth() / 2), getImage().getFrame().getY());
			getHome().getConsole().setVisible(true);
		}
		
		// Other dialogs.
		resize = new Resize(getImage());
		
		// Make the general, manual animation timer.
		timerManual = new Timer(0, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				draw();
				incrementFrameNumber();
				hertzCheck();
			}
		});
		
		// Make the special, animation window timer.
		timerAnimation = new Timer(0, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				draw();
				hertzCheck();
				
				//Determine the delay depending on where we are in the animation (use start, stop, or intermediate delay)
				int delay = 0;
				if(getFrameNumber() == animate.getStartFrame())
					delay = animate.getStartDelay();
				if(getFrameNumber() != animate.getStopFrame() && getFrameNumber() != animate.getStartFrame())
					delay = animate.getFrameDelay();
				if(getFrameNumber() == animate.getStopFrame() || !timerAnimation.isRunning())
					delay = animate.getStopDelay();
				
				if(getFrameNumber() % animate.getSaveEvery() == 0 ||
						getFrameNumber() == animate.getStartFrame() ||
						getFrameNumber() == animate.getStopFrame() ||
						!timerAnimation.isRunning())
					try
					{
						animationWriter.writeToSequence(combinedImage, delay);
					}
					catch(IOException e1)
					{
					    JavaSim.getLogger().error("Could not write animation frame to file.", e1);
					}
				
				if(getFrameNumber() == animate.getStopFrame())
					stop();
				
				incrementFrameNumber();
			}
		});
		
		// Make the special, animation window timer.
		timerAnimationVariable = new Timer(0, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (currentAnimationControl == null || currentAnimationControlValueQueue.size() == 0)
				{
					currentAnimationControl = animationControls.remove(0);
					currentAnimationControlValueQueue = new ArrayList<Object>(Arrays.asList(currentAnimationControl.getAnimateValues())); //??? gross cast
				}
				
				Object value = currentAnimationControlValueQueue.remove(0);
				currentAnimationControl.setValue(value);

				resetAction();
				hertzCheck();
				
				int delay = 0;
				if (variableAnimationN == 0)
					delay = animate.getStartDelay();
				else
					delay = animate.getFrameDelay();
				
				boolean last = (animationControls.isEmpty() && currentAnimationControlValueQueue.isEmpty());
				
				if (last)
					delay = animate.getStopDelay();
				
				try
				{
					animationWriter.writeToSequence(combinedImage, delay);
				}
				catch(IOException e1)
				{
				    JavaSim.getLogger().error("Could not write (variable) animation frame to file.", e1);
				}
				
				if(last)
					stop();
				
				variableAnimationN++;
			}
		});
		
		this.getImage().getFrame().addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);
				height = getImage().getFrame().getHeight() - getImage().getFrame().getInsets().top;
				width = getImage().getFrame().getWidth();
				setUpGraphics();
				resetAction();
			}
		});
		
		// //Set up the JMenuBar
		// menu = new Menu(this);
		// getImage().setJMenuBar(menu.getMenuBar());
		//
		// menu2 = new Menu(this);
		// getAnimate().setJMenuBar(menu2.getMenuBar());
		
		menus = new ArrayList<Menu>();
		addMenuTo(getImage().getFrame(), getImage());
		addMenuTo(getControls().getFrame(), getControls());
		addMenuTo(getResize().getFrame(), getResize());
		addMenuTo(home.getConsole(), home.getConsole()); //TODO: this is not appropriate, it should just recognize the most recently active simulation.
		
		// Set sizes to the default dimensions
		setWidth(DEFAULT_IMAGE_WIDTH);
		setHeight(DEFAULT_IMAGE_HEIGHT);
		
		if (System.getProperty("os.name").toLowerCase().startsWith("mac os x"))
		{
			// OS X fullscreen support:
			FullScreenUtilities.setWindowCanFullScreen(getImage().getFrame(), true);
			// Allow esc key to... well... escape fullscreen
			getImage().getFrame().addKeyListener(new KeyListener()
			{
				@Override
				public void keyTyped(KeyEvent e){}
				
				@Override
				public void keyReleased(KeyEvent e){}
				
				@Override
				public void keyPressed(KeyEvent e)
				{
					if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
						Simulation.this.setFullscreen(false);
				}
			});
		}
		
		// Set up graphics.
		setUpGraphics();
	}
	
	/**
	 * Contains initialization code that relies on being run after {@link #initialize()}.
	 * For example, the created of the animate window for a simulation is done
	 * here because the user should have initialized them by the time of
	 * <code>initialize</code>, but not necessarily before then.
	 * @see Home#run(SimulationInfo)
	 */
	public void postInitialize()
	{
		//Relies on what the user initializes for animating over a variable.
		animate = new Animate(getImage());
		addMenuTo(getAnimate().getFrame(), getAnimate());
	}
	
	private void setFullscreen(boolean fullscreen)
	{
		if(this.fullscreen != fullscreen)
		{
			if(getHome().getPreferences().isMacOSX())
				Application.getApplication().requestToggleFullScreen(getImage().getFrame());
			//TODO: Windows, Linux fullscreen.
			
			this.fullscreen = fullscreen;
		}
	}
	
	public void toggleFullscreen()
	{
		if(getHome().getPreferences().isMacOSX())
			Application.getApplication().requestToggleFullScreen(getImage().getFrame());
		
		this.fullscreen = !this.fullscreen;
	}
	
	private void addMenuTo(JFrame frame, Object owner)
	{
		Menu menu = new Menu(this, frame, owner);
		menus.add(menu);
		frame.setJMenuBar(menu.getMenuBar());
	}
	
	private void hertzCheck()
	{
		if(calculating)
		{
			if(calculateCount == 0)
			{
				refreshTitle();
				startTime = System.nanoTime();
			}
			else if(calculateCount == calculateCountMax)
			{
				hertz = (int) ((double) (calculateCount) / (System.nanoTime() - startTime) * 1000000000);
				refreshTitle();
				calculating = false;
			}
			calculateCount++;
		}
		
		if((System.nanoTime() - startTime) / 1000000000 > autoRefreshTime)
			calculateHertz();
	}
	
	private void refreshTitle()
	{
		getImage().getFrame().setTitle(info.getName() + " – " + info.getAuthor() + " at " + (hertz == -1 ? "?" : hertz) + " Hz");
	}
	
	private void setUpGraphics()
	{
		permanentImage = new BufferedImage(width, height, imgType);
	}
	
	public void copyImageToClipboard()
	{
		if(combinedImage != null)
		{
			TransferableImage transferableImage = new TransferableImage(combinedImage);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferableImage, this);
		}
	}
	
	public void resetAction()
	{
		setFrameNumber(0);
		reset();
		combinedImage = permanentImage;
		getImage().paintImage();
	}
	
	private void calculateHertz()
	{
		calculating = true;
		calculateCount = 0;
	}
	
	private static final int changeInSpeed = 10; // TODO: maybe make this scale in some way to
												 // accommodate for the 1/x behavior.
	
	public File getDefaultFile()
	{
		return new File(getContentDirectory(), "frame_" + getTimestamp() + ".png");
	}
	
	public void saveAs()
	{
	    if (getHome().getPreferences().isMacOSX())
	    {
	        //Native file dialog, but with less options / cross platform support.
	        FileDialog dialog = new FileDialog(getImage().getFrame(), "Choose a file", FileDialog.SAVE);
	        
            dialog.setDirectory(getContentDirectory().getAbsolutePath());
	        dialog.setFile(getDefaultFile().getName());
	        
	        dialog.setVisible(true);
	    
	        String directory = dialog.getDirectory();
	        String fileName = dialog.getFile();
	        
	        if (directory != null && fileName != null)
	        {
	            if (!fileName.endsWith(".png"))
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
			ImageIO.write(combinedImage, "png", file);
			JavaSim.getLogger().info("Saved {}", file);
		}
		catch(IOException e)
		{
		    JavaSim.getLogger().error("Could not save file {}", file, e);
		}
	}
	
	public void animate()
	{
		stopForBreakpoint = animate.getStopAtBreakpoint();
		
		resetAction();
		
		try
		{
			animationOut = new FileImageOutputStream(animate.getFile());
			animationWriter = new GifSequenceWriter(animationOut, imgType, animate.getFrameDelay(), animate.getLoop());
		}
		catch(Exception e)
		{
		    JavaSim.getLogger().error("Could not start animation.", e);
		}
		
		timerAnimation.start();
	}
	
	public void animateVariable()
	{
		resetAction();
		
		try
		{
			animationOut = new FileImageOutputStream(animate.getFile());
			animationWriter = new GifSequenceWriter(animationOut, imgType, animate.getFrameDelay(), animate.getLoop());
		}
		catch(Exception e)
		{
		    JavaSim.getLogger().error("Could not start (variable) animation.", e);
		}
		
		animationControls = new ArrayList<Control<?>>(getAnimate().getAddedControls());
		currentAnimationControl = null;
		variableAnimationN = 0;
		
		timerAnimationVariable.start();
	}
	
	public void animationComplete()
	{
		JavaSim.getLogger().info("Animation Completed!");
	}
	
	public void reset()
	{
		reset(permanentImage.createGraphics());
	}
	
	public void play()
	{
		if(!timerManual.isRunning())
			timerManual.start();
	}
	
	public void stop()
	{
		//Things for both normal animation and saving
		stopForBreakpoint = false;
		hertz = 0;
		refreshTitle();
		//Things for the user-controlled timer.
		if(timerManual.isRunning())
			timerManual.stop();
		//Things for the animator-controlled timer.
		if (timerAnimation.isRunning())
		{
			timerAnimation.stop();
			animationComplete();
		}
		if (timerAnimationVariable.isRunning())
		{
			timerAnimationVariable.stop();
			animationComplete();
		}	
	}
	
	public void openPreferences()
	{
		JavaSim.getLogger().info("Open Preferences");
	}
	
	public void draw()
	{
	    //Create a temporary image to pass along with the permanent image.
		temporaryImage = new BufferedImage(permanentImage.getWidth(), permanentImage.getHeight(), imgType);
		Graphics2D tempGraphics = temporaryImage.createGraphics();
		Graphics2D permanentGraphics = permanentImage.createGraphics();
		draw(permanentGraphics, tempGraphics);
		
		//Create a "combined" image of the same size.
		combinedImage = new BufferedImage(permanentImage.getWidth(), permanentImage.getHeight(), imgType);
		Graphics2D combinedGraphics = combinedImage.createGraphics();
		//Then draw the permanent and temporary images onto it.
		combinedGraphics.drawImage(permanentImage, 0, 0, null, null);
		combinedGraphics.drawImage(temporaryImage, 0, 0, null, null);
		
		//Dispose of graphics objects created.
		tempGraphics.dispose();
		permanentGraphics.dispose();
		
		//Repaint the image JFrame. 
		image.paintImage();
	}
	
	public BufferedImage getPermanentImage()
	{
		return permanentImage;
	}
	
	public BufferedImage getTemporaryImage()
	{
		return temporaryImage;
	}
	
	public BufferedImage getCurrentImage()
	{
		return combinedImage;
	}
	
	public BufferedImage getCurrentImageDeepCopy()
	{
		ColorModel cm = combinedImage.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = combinedImage.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	public void lostOwnership(Clipboard clipboard, Transferable contents)
	{
		JavaSim.getLogger().info("Image no longer on clipboard.");
	}
	
	//Stop and close the simulation.
	public void delete()
	{
		stop();
		getImage().getFrame().dispose();
		getControls().getFrame().dispose();
	}
	
	public void breakpoint()
	{
		if(stopForBreakpoint)
			stop();
	}
	
	public void setInfo(SimulationInfo info)
	{
		this.info = info;
	}
	
	public SimulationInfo getInfo()
	{
		return info;
	}
	
	public Home getHome()
	{
		return home;
	}
	
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
	
	/**
	 * Sets the width of the simulation so that the image becomes the specified
	 * size (not the window).
	 * 
	 * @param width
	 *            The desired image width, in pixels.
	 */
	public void setWidth(int width)
	{
		getImage().setSize(width, getImage().getFrame().getHeight());
		this.width = width;
	}
	
	/**
	 * Gets the width of the underlying image for the simulation.
	 * 
	 * @return The simulation image width, in pixels.
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Sets the height of the simulation so that the image becomes the specified
	 * size (not the window).
	 * 
	 * @param width
	 *            The desired image height, in pixels.
	 */
	public void setHeight(int height)
	{
		getImage().setSize(getImage().getFrame().getWidth(), height);
		this.height = height;
	}
	
	/**
	 * Gets the height of the underlying image for the simulation.
	 * 
	 * @return The simulation image height, in pixels.
	 */
	public int getHeight()
	{
		return height;
	}
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d_H-m-s-S");
	
	public static String getTimestamp()
	{
		return dateFormat.format(new Date());
	}
	
	public Animate getAnimate()
	{
		return animate;
	}
	
	public void setStopForBreakpoint(boolean stopForBreakpoint)
	{
		this.stopForBreakpoint = stopForBreakpoint;
	}
	
	public void decreaseAnimationSpeed()
	{
		timerManual.setDelay(timerManual.getDelay() + changeInSpeed);
		hertz = -1;
		calculateHertz();
	}
	
	public void increaseAnimationSpeed()
	{
		timerManual.setDelay(timerManual.getDelay() - changeInSpeed >= 1 ? timerManual.getDelay() - changeInSpeed : 0);
		hertz = -1;
		calculateHertz();
	}
	
	public Resize getResize()
	{
		return resize;
	}
	
	// Wrapper for the control window.
	
	// Mouse Adapter Capabilities – Same as java.awt.event.MouseAdapter
	/**
	 * {@inheritDoc}
	 */
	public void mouseClicked(MouseEvent e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mousePressed(MouseEvent e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseReleased(MouseEvent e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseEntered(MouseEvent e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void mouseExited(MouseEvent e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.6
	 */
	public void mouseWheelMoved(MouseWheelEvent e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.6
	 */
	public void mouseDragged(MouseEvent e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.6
	 */
	public void mouseMoved(MouseEvent e)
	{
	    
	}
	
	//Pseudo-print: opens the png in the system editor.
    public void print()
    {
        File tempFile =  new File(getHome().getPreferences().getTempDirectory(), getInfo().getName() + "_" + getTimestamp() + ".png");
        
        //Save the file to the temporary file directory.
        try
        {
            ImageIO.write(combinedImage, "png", tempFile);
        }
        catch(IOException e)
        {
            JavaSim.getLogger().error("Could not save temporary image for printing.", e);
        }
        
        //Open it with the default system editor.
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