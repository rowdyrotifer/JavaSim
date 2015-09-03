package com.marklalor.javasim.simulation;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.apple.eawt.Application;
import com.apple.eawt.FullScreenUtilities;
import com.marklalor.javasim.Home;
import com.marklalor.javasim.imaging.GifSequenceWriter;
import com.marklalor.javasim.imaging.TransferableImage;
import com.marklalor.javasim.simulation.frames.Image;
import com.marklalor.javasim.simulation.frames.subframes.Animate;
import com.marklalor.javasim.simulation.frames.subframes.Control;
import com.marklalor.javasim.simulation.frames.subframes.Resize;

public abstract class Simulation implements ClipboardOwner, MouseListener, MouseWheelListener, MouseMotionListener
{
	public static final int DEFAULT_IMAGE_WIDTH = 500, DEFAULT_IMAGE_HEIGHT = 500;
	public static final int DEFAULT_CONTROL_WIDTH = 100, DEFAULT_CONTROL_HEIGHT = 500;
	public static final int DEFAULT_CONSOLE_WIDTH = 400, DEFAULT_CONSOLE_HEIGHT = 500;
	
	public int imgType = BufferedImage.TYPE_INT_ARGB;
	
	public static boolean IS_MAC_OS_X = System.getProperty("os.name").toLowerCase().startsWith("mac os x");
	
	private Home home;
	private SimulationInfo info;
	
	private File contentDirectory;
	
	private boolean fullscreen = false;
	
	//Misc Main stucture;
	private List<Menu> menus; //give every window a copy of the menu, this gets around issues with JDialog's incapability to properly handle its parent menu's accelerators…
	
	// Main Frames
	private Image image;
	private Control control;
	
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
	private ImageOutputStream animationOut;
	private GifSequenceWriter animationWriter;
	
	//Simulation features.
	public abstract void initialize();
	public abstract void reset(Graphics2D permanent);
	public abstract void draw(Graphics2D permanent, Graphics2D temporary);
	
	//Simulation N (frame) variable.
	private int n = 0;
	public int getN() { return this.n; }
	public void setN(int n) { this.n = n; }
	public void incrementN() { this.n++; }
	
	public void javaSimInitialize(SimulationInfo info)
	{
		//Don't allow anyone to use this method a second time… just… no.
		if (this.info != null)
			throw new RuntimeException("Do not call method com.marklalor.javasim.simulation.Simulation#javaSimInitialize more than once (it is called once automatically by JavaSim Home)");
		
		//Inherit the info read from its file earlier.
		this.info = info;
		
		//Set the simulation's content directory and (make sure it exists)
		contentDirectory = new File(getHome().getHomeDirectory(), info.getName());
		contentDirectory.mkdirs();
		
		//Create and set up the main image that goes with this simulation.
		image = new Image(this);
		image.pack();
		image.setSize(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
		image.setLocationRelativeTo(null);
		refreshTitle();
		
		//Create and set up the main control panel for this simulation.
		control = new Control(getImage());
		control.setSize(DEFAULT_CONTROL_WIDTH, DEFAULT_CONTROL_HEIGHT);
		control.setLocationRelativeTo(getImage());
		control.setLocation(getControl().getLocation().x - (getImage().getWidth() / 2) - (getControl().getWidth() / 2), getImage().getY());
		
		//Reposition the console.
		getHome().getConsole().getFrame().setSize(DEFAULT_CONSOLE_WIDTH, DEFAULT_CONSOLE_HEIGHT);
		getHome().getConsole().getFrame().setLocationRelativeTo(getImage());
		getHome().getConsole().getFrame().setLocation(getHome().getConsole().getFrame().getLocation().x + (getImage().getWidth() / 2) + (getHome().getConsole().getFrame().getWidth() / 2), getImage().getY());
		
		//Other dialogs.
		animate = new Animate(getImage());
		resize = new Resize(getImage());
		
		//Make the general, manual animation timer.
		timerManual = new Timer(10, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				incrementN();
				draw();
				hertzCheck();
			}
		});
		
		//Make the special, animation window timer.
		timerAnimation = new Timer(0, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				draw();
				hertzCheck();
				int delay = 0;
				if (getN() == animate.getStartFrame())
					delay = animate.getStartDelay();
				if (getN() != animate.getStopFrame() && getN() != animate.getStartFrame())
					delay = animate.getFrameDelay();
				if  (getN() == animate.getStopFrame() || !timerAnimation.isRunning())
					delay = animate.getStopDelay();
				
				if (getN() % animate.getSaveEvery() == 0 || 
						getN() == animate.getStartFrame() || 
						getN() == animate.getStopFrame() || 
						!timerAnimation.isRunning())
					try
					{
						animationWriter.writeToSequence(combinedImage, delay);
					}
					catch(IOException e1)
					{
						e1.printStackTrace();
					}
				
				if (getN() == animate.getStopFrame())
					stop();
				
				incrementN();
			}
		});
		
		this.getImage().addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);
				height = getImage().getHeight() - getImage().getInsets().top;
				width = getImage().getWidth();
				setUpGraphics();
				resetAction();
			}
		});
		
//		//Set up the JMenuBar
//		menu = new Menu(this);
//		getImage().setJMenuBar(menu.getMenuBar());
//		
//		menu2 = new Menu(this);
//		getAnimate().setJMenuBar(menu2.getMenuBar());
		
		menus = new ArrayList<Menu>();
		addMenuTo(getImage());
		addMenuTo(getAnimate());
		addMenuTo(getResize());
		addMenuTo(home.getConsole().getFrame());
		
		//Set sizes to the default dimensions
		setWidth(DEFAULT_IMAGE_WIDTH);
		setHeight(DEFAULT_IMAGE_HEIGHT);
		
		//OS X fullscreen support:
		FullScreenUtilities.setWindowCanFullScreen(getImage(),true);
		//Allow esc key to... well... escape fullscreen
		getImage().addKeyListener(new KeyListener()
		{	
			@Override public void keyTyped(KeyEvent e) { }
			@Override public void keyReleased(KeyEvent e) { }
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					Simulation.this.setFullscreen(false);
			}
		});
	
		//Set up graphics.
		setUpGraphics();
	}
	

	private void setFullscreen(boolean fullscreen)
	{
		if (this.fullscreen != fullscreen)
		{
			if (Simulation.IS_MAC_OS_X)
				Application.getApplication().requestToggleFullScreen(getImage());
			
			this.fullscreen = fullscreen;
		}
	}
	
	public void toggleFullscreen()
	{	
		if (Simulation.IS_MAC_OS_X)
			Application.getApplication().requestToggleFullScreen(getImage());
		
		this.fullscreen = !this.fullscreen;
	}
	
	private void addMenuTo(JFrame frame)
	{
		Menu menu = new Menu(this, frame);
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
		image.setTitle(info.getName() + " – " + info.getAuthor() + " at " + (hertz == -1 ? "?" : hertz) + " Hz");
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
		setN(0);
		reset();
		combinedImage = permanentImage;
		getImage().paintImage();
	}
	
	private void calculateHertz()
	{
		calculating = true;
		calculateCount = 0;
	}
	
	private static final int changeInSpeed = 10; // TODO: maybe make this scale in some way to accommodate for the 1/x behavior.
	
	public File getDefaultFile()
	{
		return new File(getContentDirectory(), "frame_" + getTimestamp() + ".png");
	}
	
	public void saveAs()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
		chooser.setSelectedFile(getDefaultFile());
        if (chooser.showSaveDialog(getImage()) == JFileChooser.APPROVE_OPTION)
        {
        	String file = chooser.getSelectedFile().getAbsolutePath();
        	if (!chooser.getSelectedFile().getName().contains("."))
        		file += ".png";
            save(new File(file));
        }
	}
	
	public void save(File file)
	{
		try
		{
			ImageIO.write(combinedImage, "png", file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
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
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IIOException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		timerAnimation.start();
	}
	
	public void animationComplete()
	{
		System.out.println("Animation Completed!");
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
	}
	
	public void openPreferences()
	{
		System.out.println("open preferences");
	}
	
	public void draw()
	{
		temporaryImage = new BufferedImage(permanentImage.getWidth(), permanentImage.getHeight(), imgType);
		draw(permanentImage.createGraphics(), temporaryImage.createGraphics());
		combinedImage = new BufferedImage(permanentImage.getWidth(), permanentImage.getHeight(), imgType);
		
		Graphics2D combinedGraphics = combinedImage.createGraphics();
		combinedGraphics.drawImage(permanentImage, 0, 0, null, null);
		combinedGraphics.drawImage(temporaryImage, 0, 0, null, null);
		
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
		System.out.println("Image No Longer On Clipboard");
	}
	
	public void delete()
	{
		getImage().dispose();
		getControl().dispose();
		getHome().getConsole().getFrame().setVisible(false);
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
	
	public Control getControl()
	{
		return control;
	}
	
	public File getContentDirectory()
	{
		return contentDirectory;
	}
	
	/**
	 * Sets the width of the simulation so that the
	 * image becomes the specified size (not the window).
	 * @param width The desired image width, in pixels.
	 */
	public void setWidth(int width)
	{
		getImage().setSize(width, getImage().getHeight());
		this.width = width;
	}
	
	/**
	 * Gets the width of the underlying
	 * image for the simulation.
	 * @return The simulation image width, in pixels.
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Sets the height of the simulation so that the
	 * image becomes the specified size (not the window).
	 * @param width The desired image height, in pixels.
	 */
	public void setHeight(int height)
	{
		getImage().setSize(getImage().getWidth(), height);
		this.height = height;
	}
	
	/**
	 * Gets the height of the underlying
	 * image for the simulation.
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
		timerManual.setDelay(timerManual.getDelay() - changeInSpeed >= 1 ? timerManual.getDelay() - changeInSpeed : 1);
		hertz = -1;
		calculateHertz();	
	}
	
	public Resize getResize()
	{
		return resize;
	}
	
	//Mouse Adapter Capabilities – Same as java.awt.event.MouseAdapter
	/**
     * {@inheritDoc}
     */
    public void mouseClicked(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mousePressed(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mouseReleased(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mouseEntered(MouseEvent e) {}

    /**
     * {@inheritDoc}
     */
    public void mouseExited(MouseEvent e) {}

    /**
     * {@inheritDoc}
     * @since 1.6
     */
    public void mouseWheelMoved(MouseWheelEvent e){}

    /**
     * {@inheritDoc}
     * @since 1.6
     */
    public void mouseDragged(MouseEvent e){}

    /**
     * {@inheritDoc}
     * @since 1.6
     */
    public void mouseMoved(MouseEvent e){}
}