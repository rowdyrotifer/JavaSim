package com.marklalor.javasim.simulation;

import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import org.json.JSONObject;

import apple.dts.samplecode.osxadapter.OSXAdapter;

import com.marklalor.javasim.Home;
import com.marklalor.javasim.imaging.GifSequenceWriter;
import com.marklalor.javasim.imaging.TransferableImage;
import com.marklalor.javasim.simulation.frames.Image;
import com.marklalor.javasim.simulation.frames.subframes.Animate;
import com.marklalor.javasim.simulation.frames.subframes.Control;

public abstract class Simulation implements ActionListener, ClipboardOwner
{
	public static final int DEFAULT_IMAGE_WIDTH = 500, DEFAULT_IMAGE_HEIGHT = 500;
	public static final int DEFAULT_CONTROL_WIDTH = 100, DEFAULT_CONTROL_HEIGHT = 500;
	public int imgType = BufferedImage.TYPE_INT_ARGB;
	
	public static boolean IS_MAC_OS_X = System.getProperty("os.name").toLowerCase().startsWith("mac os x");
	
	private Home home;
	private SimulationInfo info;
	
	private File contentDirectory;
	
	// Main Frames
	private Image image;
	private Control control;
	
	// Other Frames
	private Animate animate;
	
	// Relating to the image
	private BufferedImage permanentImage, temporaryImage, combinedImage;
	private int width, height;
	
	// Timer
	private Timer timer;
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
	private Timer animationTimer;
	private ImageOutputStream animationOut;
	private GifSequenceWriter animationWriter;
	
	public abstract void initialize();
	public abstract void reset(Graphics2D permanent);
	public abstract void draw(Graphics2D permanent, Graphics2D temporary);
	public abstract void click(Point point);
	
	private int n = 0;
	
	public int getN()
	{
		return n;
	}
	
	public void setN(int n)
	{
		this.n = n;
	}
	
	public void incrementN()
	{
		this.n++;
	}
	
	public Simulation(SimulationInfo info)
	{
		this.info = info;
		
		contentDirectory = new File(Home.homeDirectory, info.getName());
		contentDirectory.mkdirs();
		
		image = new Image(this);
		refreshTitle();
		image.setSize(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
		image.setLocationRelativeTo(null);
		
		control = new Control(image);
		control.setSize(DEFAULT_CONTROL_WIDTH, DEFAULT_CONTROL_HEIGHT);
		control.setLocationRelativeTo(this.getImage());
		control.setLocation(this.getControl().getLocation().x - (this.getImage().getWidth() / 2) - (this.getControl().getWidth() / 2), this.getImage().getY());
		
		animate = new Animate(image);
		
		timer = new Timer(10, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				incrementN();
				draw();
				hertzCheck();
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
				setupGraphics();
				resetAction();
			}
		});
		
		animationTimer = new Timer(0, new ActionListener()
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
				if  (getN() == animate.getStopFrame() || !animationTimer.isRunning())
					delay = animate.getStopDelay();
				
				if (getN() % animate.getSaveEvery() == 0 || 
						getN() == animate.getStartFrame() || 
						getN() == animate.getStopFrame() || 
						!animationTimer.isRunning())
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
		
		setupMenu();
		
		setWidth(DEFAULT_IMAGE_WIDTH);
		setHeight(DEFAULT_IMAGE_HEIGHT);
		setupGraphics();
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
	
	private void setupGraphics()
	{
		permanentImage = new BufferedImage(width, height, imgType);
	}
	
	// Menu Items
	private JMenuBar menuBar;
	private JMenu file;
	private JMenuItem newSimulation, saveImage, saveImageAs, createGif, openHomeFolder, openContentFolder, closeSimulation, openProperties;
	private JMenu edit;
	private JMenuItem copy;
	private JMenu animation;
	private JMenuItem play, playUntilBreakpoint, stop, nextFrame, decreaseSpeed, increaseSpeed;
	private JMenu simulation;
	private JMenuItem reset, resize;
	
	public void setupMenu()
	{
		menuBar = new JMenuBar();
		
		file = new JMenu("File");
		
		// New Simulation – Command + N
		newSimulation = new JMenuItem("New " + info.getName());
		newSimulation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		newSimulation.addActionListener(this);
		file.add(newSimulation);
		
		file.addSeparator();
		
		// Save – Command + S
		saveImage = new JMenuItem("Save Current Image");
		saveImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		saveImage.addActionListener(this);
		file.add(saveImage);
		
		// Save As – Command + Shift + S
		saveImageAs = new JMenuItem("Save Current Image As…");
		saveImageAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK)));
		saveImageAs.addActionListener(this);
		file.add(saveImageAs);
		
		// Create Animated Gif – Command + I
		createGif = new JMenuItem("Created Animated Gif");
		createGif.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		createGif.addActionListener(this);
		file.add(createGif);
		
		file.addSeparator();
		
		// Open Content Folder – Command + M
		openContentFolder = new JMenuItem("Open Content Folder");
		openContentFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		openContentFolder.addActionListener(this);
		file.add(openContentFolder);
		
		// Open Home Folder – Command + Shift + M
		openHomeFolder = new JMenuItem("Open Home Folder");
		openHomeFolder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK)));
		openHomeFolder.addActionListener(this);
		file.add(openHomeFolder);
		
		file.addSeparator();
		
		// Close – Command + W
		closeSimulation = new JMenuItem("Close Simulation");
		closeSimulation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		closeSimulation.addActionListener(this);
		file.add(closeSimulation);
		
		if(IS_MAC_OS_X)
		{
			try
			{
				// for (Method f : getClass().getMethods())
				// System.out.println(f.getName());
				OSXAdapter.setPreferencesHandler(this, getClass().getMethod("openPreferences", (Class[]) null));
				OSXAdapter.setQuitHandler(this, getClass().getMethod("delete", (Class[]) null));
			}
			catch(SecurityException e)
			{
				e.printStackTrace();
			}
			catch(NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			file.addSeparator();
			
			// Properties – No Shortcut
			openProperties = new JMenuItem("Properties");
			// openProperties.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
			// Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() ));
			openProperties.addActionListener(this);
			file.add(openProperties);
		}
		
		menuBar.add(file);
		
		edit = new JMenu("Edit");
		
		// Copy – Command + C
		copy = new JMenuItem("Copy");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		copy.addActionListener(this);
		edit.add(copy);
		
		menuBar.add(edit);
		
		animation = new JMenu("Animation");
		
		// Play – Command + P
		play = new JMenuItem("Play");
		play.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		play.addActionListener(this);
		animation.add(play);
		
		// Play Until Breakpoint – Command + Shift + P
		playUntilBreakpoint = new JMenuItem("Play Until Breakpoint");
		playUntilBreakpoint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.SHIFT_MASK)));
		playUntilBreakpoint.addActionListener(this);
		animation.add(playUntilBreakpoint);
		
		// Pause – Command + .
		stop = new JMenuItem("Stop");
		stop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		stop.addActionListener(this);
		animation.add(stop);
		
		animation.addSeparator();
		
		// Next Frame – Space
		nextFrame = new JMenuItem("Next Frame");
		nextFrame.setAccelerator(KeyStroke.getKeyStroke(' '));
		nextFrame.addActionListener(this);
		animation.add(nextFrame);
		
		animation.addSeparator();
		
		// Decrease Speed – [
		decreaseSpeed = new JMenuItem("Decrease Speed");
		decreaseSpeed.setAccelerator(KeyStroke.getKeyStroke('['));
		decreaseSpeed.addActionListener(this);
		animation.add(decreaseSpeed);
		
		// Increase Speed – ]
		increaseSpeed = new JMenuItem("Increase Speed");
		increaseSpeed.setAccelerator(KeyStroke.getKeyStroke(']'));
		increaseSpeed.addActionListener(this);
		animation.add(increaseSpeed);
		
		menuBar.add(animation);
		
		simulation = new JMenu("Simulation");
		
		// Reset – Command + R
		reset = new JMenuItem("Reset");
		reset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		reset.addActionListener(this);
		simulation.add(reset);
		
		simulation.addSeparator();
		
		// Resize – No Shortcut
		resize = new JMenuItem("Resize");
		// resize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
		// Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		resize.addActionListener(this);
		simulation.add(resize);
		
		menuBar.add(simulation);
		
		getImage().setJMenuBar(menuBar);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println(((JMenu) ((JPopupMenu) ((JMenuItem) e.getSource()).getParent()).getInvoker()).getText() + " → " + ((JMenuItem) e.getSource()).getText());
		
		if(e.getSource() == newSimulation)
		{
			getHome().run(info);
		}
		else if(e.getSource() == saveImage)
		{
			save(new File(getContentDirectory(), "frame_" + getTimestamp() + ".png"));
		}
		else if(e.getSource() == saveImageAs)
		{
			
		}
		else if(e.getSource() == createGif)
		{
			animate.setLocationRelativeTo(getImage());
			animate.setVisible(true);
		}
		else if(e.getSource() == openContentFolder)
		{
			try
			{
				Desktop.getDesktop().open(contentDirectory);
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == openHomeFolder)
		{
			try
			{
				Desktop.getDesktop().open(contentDirectory.getParentFile());
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == closeSimulation)
		{
			delete();
		}
		else if(e.getSource() == openProperties)
		{
			openPreferences();
		}
		else if(e.getSource() == copy)
		{
			copyImageToClipboard();
		}
		else if(e.getSource() == play)
		{
			stopForBreakpoint = false;
			play();
		}
		else if(e.getSource() == playUntilBreakpoint)
		{
			stopForBreakpoint = true;
			play();
		}
		else if(e.getSource() == stop)
		{
			stop();
		}
		else if(e.getSource() == nextFrame)
		{
			incrementN();
			draw();
		}
		else if(e.getSource() == decreaseSpeed)
		{
			timer.setDelay(timer.getDelay() + changeInSpeed);
			hertz = -1;
			calculateHertz();
			System.out.println(timer.getDelay());
		}
		else if(e.getSource() == increaseSpeed)
		{
			timer.setDelay(timer.getDelay() - changeInSpeed >= 1 ? timer.getDelay() - changeInSpeed : 1);
			hertz = -1;
			calculateHertz();
			System.out.println(timer.getDelay());
		}
		else if(e.getSource() == reset)
		{
			resetAction();
		}
	}
	
	private void copyImageToClipboard()
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
		getImage().paintImage(permanentImage);
	}
	
	private void calculateHertz()
	{
		calculating = true;
		calculateCount = 0;
	}
	
	private static final int changeInSpeed = 10; // TODO: maybe make this scale in some way to accommodate for the 1/x behavior.
	
	private void save(File file)
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
		
		animationTimer.start();
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
		if(!timer.isRunning())
			timer.start();
	}
	
	public void stop()
	{
		//Things for both normal animation and saving
		stopForBreakpoint = false;
		hertz = 0;
		refreshTitle();
		//Things for the user-controlled timer.
		if(timer.isRunning())
			timer.stop();
		//Things for the animator-controlled timer.
		if (animationTimer.isRunning())
		{
			animationTimer.stop();
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
		
		image.paintImage(combinedImage);
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
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d_H-m-s-S");
	
	public static String getTimestamp()
	{
		return dateFormat.format(new Date());
	}
}