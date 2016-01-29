package com.marklalor.javasim;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.LogManager;

import apple.dts.samplecode.osxadapter.OSXAdapter;

import com.marklalor.javasim.misc.FileDropManager;
import com.marklalor.javasim.misc.FileDropOverlay;
import com.marklalor.javasim.misc.FileDropManager.Listener;
import com.marklalor.javasim.preferences.ApplicationPreferences;
import com.marklalor.javasim.simulation.HomeMenu;
import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.SimulationInfo;
import com.marklalor.javasim.simulation.frames.Minimizable;
import com.marklalor.javasim.text.Console;
import com.marklalor.javasim.text.JavaSimConsoleAppender;

public class Home extends JFrame implements ListSelectionListener, Minimizable
{
	private static final long serialVersionUID = 6321955323521519657L;
	
	private ApplicationPreferences preferences;
	
	private List<SimulationInfo> simulations;
	private JList<SimulationInfo> simulationList;
	private JPanel simulationInfoPanel;
	
	private List<Simulation> activeSimulations; 
	
	private JLabel name, date, author, version, description;
	private JButton run;
	
	private HomeMenu menu;
	private Console console;
	
	private JLayeredPane layeredPane; 
	private JPanel main;
    private FileDropOverlay fileDropOverlay;

	private boolean fileDropVisible;
	
	public Home(ApplicationPreferences preferences)
	{
		this.preferences = preferences;
		
		this.setUpConsole();
		this.loadSimulations();
		this.setUpLayout();
		if(getApplicationPreferences().isMacOSX())
		    this.setUpOSXMenuHandlers();
		this.setUpMenu();
	}

	//Set up operations.

    private void setUpConsole()
	{
		console = new Console(this);
		
		//Bind Log4j info messages to the console.
		if (getApplicationPreferences().getConsoleBind())
		{
			JavaSimConsoleAppender consoleAppender = new JavaSimConsoleAppender(console);
			consoleAppender.setThreshold(getApplicationPreferences().getPreferences().getLogLevel());
			LogManager.getRootLogger().addAppender(consoleAppender);
		}
		
		JavaSim.getLogger().info("Console started.");
		JavaSim.getLogger().info("JavaSim version: {}", JavaSim.getVersion());	
	}
    
    private void setUpOSXMenuHandlers()
    {
        //Set up OSX preferences handler
        try
        {
            OSXAdapter.setPreferencesHandler(this, this.getClass().getMethod("openPreferences", (Class[]) null));
        }
        catch(SecurityException e)
        {
            JavaSim.getLogger().error("SecurityException while trying to set OSXAdapter preferences handler.", e);
        }
        catch(NoSuchMethodException e)
        {
            JavaSim.getLogger().error("NoSuchMethodException while trying to set OSXAdapter preferences handler.", e);
        }
        
        try
        {
            OSXAdapter.setQuitHandler(this, this.getClass().getMethod("quit", (Class[]) null));
        }
        catch(SecurityException e)
        {
            JavaSim.getLogger().error("SecurityException while trying to set OSXAdapter preferences handler.", e);
        }
        catch(NoSuchMethodException e)
        {
            JavaSim.getLogger().error("NoSuchMethodException while trying to set OSXAdapter preferences handler.", e);
        }
    }
	
	private static final FilenameFilter jarFilter = new FilenameFilter()
	{
		@Override public boolean accept(File dir, String name) { return name.toLowerCase().endsWith("jar"); }
	};
	
	public void loadSimulations()
	{
	    JavaSim.getLogger().debug("Loading simulations.");
	    
		simulations = new ArrayList<SimulationInfo>();
		activeSimulations = new ArrayList<Simulation>();
		
		for (File jar : getApplicationPreferences().getSimulationsDirectory().listFiles(jarFilter))
		{
			SimulationInfo info = new SimulationInfo(jar);
			simulations.add(info);
		}
	}

	private void setUpLayout()
	{
	    JavaSim.getLogger().debug("Setting up Home layout.");
	    
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Java Simulation Home");

        layeredPane = getLayeredPane();
		//Seperate into two layers: a main pane and a glass pane for the FileDropOverlay
		main = new JPanel();
		fileDropOverlay = new FileDropOverlay(this);
		
		fileDropOverlay.setSize(800, 500);
		fileDropOverlay.setLocation(0, 0);
		
		
		main.setLayout(new GridBagLayout());
		main.setSize(800, 500);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		
		DefaultListModel<SimulationInfo> model = new DefaultListModel<SimulationInfo>();
		if (simulations != null)
			for(SimulationInfo info : simulations)
				model.addElement(info);
		
		simulationList = new JList<SimulationInfo>(model);
		Font listFont = simulationList.getFont();
		simulationList.setFont(new Font(listFont.getName(), listFont.getStyle(), (int) (listFont.getSize()*1.50)));
		simulationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		simulationList.setLayoutOrientation(JList.VERTICAL);
		simulationList.setVisibleRowCount(-1);
		simulationList.addListSelectionListener(this);
		simulationList.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 4, new Color(0.25f, 0.10f, 0.10f)));
		
		JScrollPane simulationListScrollPane = new JScrollPane(simulationList);
		simulationListScrollPane.setPreferredSize(new Dimension(200, 100));
		simulationListScrollPane.setMinimumSize(new Dimension(200, 100));
		simulationListScrollPane.setBorder(BorderFactory.createEmptyBorder());
		main.add(simulationListScrollPane, constraints);
		
		simulationList.addMouseListener(new MouseAdapter()
		{
		    public void mouseClicked(MouseEvent evt)
		    {
		        if (evt.getClickCount() == 2)
		            runSelected();
		    }
		});
		
		simulationList.addKeyListener(new KeyListener()
		{	
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					runSelected();
			}

            @Override public void keyTyped(KeyEvent e) { }  
            @Override public void keyReleased(KeyEvent e) { }
		});
		
		simulationInfoPanel = new JPanel();
		simulationInfoPanel.setLayout(new BoxLayout(simulationInfoPanel, BoxLayout.Y_AXIS));
		simulationInfoPanel.setBackground(new Color(0.90f, 0.85f, 0.85f));
		simulationInfoPanel.setBorder(BorderFactory.createMatteBorder(2, 6, 0, 0, new Color(0.90f, 0.85f, 0.85f)));
		constraints.weightx = 0.75;
		
		JScrollPane simulationInfoPanelScrollPane = new JScrollPane(simulationInfoPanel);
		simulationInfoPanelScrollPane.setBorder(BorderFactory.createEmptyBorder());
		main.add(simulationInfoPanelScrollPane, constraints);
		
		this.name = new JLabel();
		this.name.setFont(this.name.getFont().deriveFont(26f).deriveFont(Font.BOLD));
		this.date = new JLabel();
		this.author = new JLabel();
		this.version = new JLabel();
		this.description = new JLabel();
		simulationInfoPanel.add(this.name);
		simulationInfoPanel.add(this.date);
		simulationInfoPanel.add(this.author);
		simulationInfoPanel.add(this.version);
		simulationInfoPanel.add(this.description);
		
		this.run = new JButton("Run");
		this.run.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				runSelected();
			}
		});
		this.run.setVisible(false);
		simulationInfoPanel.add(this.run);
		
		simulationList.setSelectedIndex(0);
		
		new FileDropManager(this, new Listener()
        {   
            @Override
            public void filesDropped(File[] files)
            {
                for (File file : files)
                    JavaSim.getLogger().info(file.getAbsolutePath());
            }
            
        });
		
		layeredPane.add(main, Integer.valueOf(1));
		layeredPane.add(fileDropOverlay, Integer.valueOf(2));
		
        layeredPane.addComponentListener(new ComponentListener()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                Home.this.fileDropOverlay.setSize(Home.this.getWidth(), Home.this.getHeight());
                Home.this.main.setSize(Home.this.getWidth(), Home.this.getHeight());
            }

            @Override public void componentMoved(ComponentEvent e) { }
            @Override public void componentShown(ComponentEvent e) { }
            @Override public void componentHidden(ComponentEvent e) { }
        });
	}
	
	private void setUpMenu()
    {
        JavaSim.getLogger().debug("Setting up Home menu bar.");
        menu = new HomeMenu(this);
        setJMenuBar(menu.getMenuBar());
    }
	
	//End setup.
	
	private void runSelected()
	{
		this.run((SimulationInfo) simulationList.getSelectedValue());
	}
	
	public void run(SimulationInfo info)
	{
		JavaSim.getLogger().info("Running {}", info.getName());
		
		Class<? extends Simulation> simClass = null;
		
		try
		{
			simClass = SimulationInfo.loadSimulationClass(info.getFile(), info.getMain()==null?null:(info.getMain()==""?null:info.getMain()));
		}
		catch(ClassNotFoundException e)
		{
		    if (info.getMain() == null)
		        JavaSim.getLogger().error("Could not automatically find a simulation class.", e);
		    else
		        JavaSim.getLogger().error("Could not find class {}", info.getMain(), e);
		}
		catch(IOException e)
		{
		    JavaSim.getLogger().error("Could not load the simulation class", e);
		}
		
		if (simClass == null)
		{
			JavaSim.getLogger().error("Simulation class was not loaded. {}", info);
			return;
		}
		
        try
        {
            //Create a new instance of a simulation.
            JavaSim.getLogger().info("Initializing {}", simClass.getName());
            Simulation simulation = simClass.newInstance();
            activeSimulations.add(simulation);
            
            //Run all the initialization steps.
            simulation.preInitialize(this, info);
            simulation.initialize();
            simulation.postInitialize();
            
            //Reset it the begin.
            simulation.resetAction();
        }
        catch(InstantiationException | IllegalAccessException e)
        {
            JavaSim.getLogger().error("Could not instantiate an instance of " + simClass.getSimpleName());
        }
	}
	
	public Console getConsole()
	{
		return console;
	}
	
	public ApplicationPreferences getApplicationPreferences()
	{
		return preferences;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.run.setVisible(true);
		
		SimulationInfo info = (SimulationInfo) simulationList.getSelectedValue();
		this.name.setText(info.getName());
		this.date.setText(info.getDate());
		this.author.setText(info.getAuthor());
		this.version.setText("Version " + info.getVersion());
		this.description.setText("<html><p>" + info.getDescription() + "</p></html>");
	}
	
	@Override
	public void minimize()
	{
		if (isVisible())
			setState(ICONIFIED);
	}
    
	public boolean getFileDropVisible()
	{
	    return this.fileDropVisible;
	}
	
    public void setFileDropVisible(boolean fileDropVisible)
    {
        this.fileDropVisible = fileDropVisible;
        this.fileDropOverlay.repaint();
    }

    public void openPreferences()
    {
        JavaSim.getLogger().info("Open Preferences");
        
        try
        {
            Desktop.getDesktop().open(getApplicationPreferences().getFile().getParentFile());
        }
        catch(IOException e)
        {
            JavaSim.getLogger().error("Could not open preferences file on native system.", e);
        }
    }
    
    //Close any simulations and then dispose of the home window itself.
    public void quit()
    {
        Iterator<Simulation> iterator = activeSimulations.iterator();
        while(iterator.hasNext())
        {
            Simulation simulation = iterator.next();
            simulation.close();
            iterator.remove();
        }
        this.dispose();
    }
    
    public void removeSimulation(Simulation simulation)
    {
        simulation.close();
        activeSimulations.remove(simulation);
    }
}
