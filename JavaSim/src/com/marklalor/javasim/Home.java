package com.marklalor.javasim;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.marklalor.javasim.simulation.Simulation;

public class Home extends JFrame implements ListSelectionListener
{
	private static final long serialVersionUID = 6321955323521519657L;
	
	public static File homeDirectory;
	
	private List<Simulation> simulations;
	private JList simulationList;
	private JPanel simulationInfoPanel;
	
	private JLabel name, date, author, version, description;
	private JButton run;
	
	public Home(File location)
	{
		Home.homeDirectory = location;
		loadSimulations();
		setupLayout();
		simulationList.setSelectedIndex(0);
	}

	private static final FilenameFilter jarFilter = new FilenameFilter()
	{
		@Override
		public boolean accept(File dir, String name) { return name.toLowerCase().endsWith("jar"); }
	};
	
	private void loadSimulations()
	{
		simulations = new ArrayList<Simulation>();
		for (File file : homeDirectory.listFiles(jarFilter))
		{
			try
			{
				Class<? extends Simulation> simClass = findSimClassFromJar(file);
				try
				{
					simulations.add(simClass.newInstance());
				}
				catch(InstantiationException e)
				{
					e.printStackTrace();
				}
				catch(IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Simulation> findSimClassFromJar(File file) throws IOException, ClassNotFoundException
	{
		JarFile jarFile = new JarFile(file);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + file.getAbsolutePath() +"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

	    while (e.hasMoreElements())
	    {
	        JarEntry entry = (JarEntry) e.nextElement();
	        if(entry.isDirectory() || !entry.getName().endsWith(".class"))
	            continue;
		    // -6 because of .class
		    String className = entry.getName().substring(0,entry.getName().length()-6);
		    className = className.replace('/', '.');
		    
		    
		    //Load it and return it if it's a simulation.
		    Class<?> simClass = cl.loadClass(className);
		    if (Simulation.class.isAssignableFrom(simClass))
		    	return (Class<? extends Simulation>) simClass;
	    }
	    
	    
	    System.out.println(file.getAbsolutePath() + " did not contain a Simulation!");
	    jarFile.close();
		return null;
	}

	private void setupLayout()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Java Simulation Home");
		
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		constraints.gridwidth = 2;
		
		DefaultListModel model = new DefaultListModel();
		if (simulations != null)
			for(Simulation simulation : simulations)
				model.addElement(simulation);
		
		simulationList = new JList(model);
		Font listFont = simulationList.getFont();
		simulationList.setFont(new Font(listFont.getName(), listFont.getStyle(), (int) (listFont.getSize()*1.50)));
		simulationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		simulationList.setLayoutOrientation(JList.VERTICAL);
		simulationList.setVisibleRowCount(-1);
		simulationList.addListSelectionListener(this);
		constraints.weightx = 0.25;
		getContentPane().add(simulationList, constraints);
		
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
			public void keyTyped(KeyEvent e) { }
			
			@Override
			public void keyReleased(KeyEvent e) { }
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					runSelected();
			}
		});
		
		simulationInfoPanel = new JPanel();
		simulationInfoPanel.setLayout(new BoxLayout(simulationInfoPanel, BoxLayout.Y_AXIS));
		simulationInfoPanel.setBackground(new Color(0.8f, 0.8f, 0.8f));
		simulationInfoPanel.setBorder(BorderFactory.createMatteBorder(0, 8, 0, 0, new Color(0.15f, 0.15f, 0.15f)));
		constraints.weightx = 0.75;
		getContentPane().add(simulationInfoPanel, constraints);
		
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
	}
	
	private void runSelected()
	{
		run((Simulation) simulationList.getSelectedValue());
	}
	
	public void run(Simulation s)
	{
		System.out.println("Running " + s.getName());
		Class<? extends Simulation> c = s.getClass();
		Simulation sim;
		try
		{
			System.out.println("Initializing " + c.toString());
			sim = c.newInstance();
			//sim.setContentDirectory(new File(location, sim.getName()));
			sim.setHome(this);
			sim.initialize();
			sim.resetAction();
		}
		catch(InstantiationException e)
		{
			e.printStackTrace();
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		this.run.setVisible(true);
		Simulation s = (Simulation) simulationList.getSelectedValue();
		this.name.setText(s.getName());
		this.date.setText(s.getDate());
		this.author.setText(s.getAuthor());
		this.version.setText("v" + s.getVersion());
		//this.description.setMaximumSize(new Dimension(100, 1000));
		this.description.setText("<html><p>" + s.getDescription() + "</p></html>");
	}
}
