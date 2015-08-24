# Table of Contents
1. [Features](#features)
2. [Install](#install)
3. [Example](#example)

# JavaSim

Java simulation framework.

## Features

 * Easy to extend "Simulation" class
 * Simple program features
   * copy image
   * close window
   * resize window
   * reload simulation
 * Save created images
 * Create simple animations

## Install

See the [latest releases](https://github.com/MarkLalor/JavaSim/releases/latest) for downloads.

After running the application for the first time, a folder called `JavaSim` should appear in
your `Documents` folder. Jar files for JavaSim simulations placed here will appear on the application's
main screen. They can then be selected to load the simulation. 

## Example

The following program is designed to generate an image of a nephroid. Running `File → Create Animated Gif`
with the default settings saves this image:

![Nephroid Example](http://marklalor.com/wp-content/uploads/2015/08/NephroidExample.gif)

```java
public class Nephroid extends Simulation
{
	public Nephroid(SimulationInfo info)
	{
		super(info);
	}

	public static final float TRANSPARENCY = 0.1f;
	public static final double RADIUS_PERCENT = 0.8d;
	
	private static final int dTheta = 1;
	private static final Point text = new Point(15, 20);
	private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
	
	@Override
	public void initialize()
	{
		getImage().setVisible(true);
	}
	
	@Override
	public void reset(Graphics2D permanent)
	{
		permanent.setColor(Color.WHITE);
		permanent.fillRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public void draw(Graphics2D permanent, Graphics2D temporary)
	{
		permanent.translate(getWidth()/2, getHeight()/2);
		permanent.setColor(new Color(0.05f, 0.05f, 0.05f, TRANSPARENCY));	
		
		double radius = (Math.min(getWidth(), getHeight())/2) * RADIUS_PERCENT;
		
		Point p1 = getPoint(getN()*dTheta, radius);
		Point p2 = getPoint(getN()*3*dTheta, radius);
		
		permanent.drawLine(p1.x, p1.y, p2.x, p2.y);
		
		temporary.setColor(Color.BLACK);
		temporary.setFont(new Font("Monospaced", Font.BOLD, 20)); 
		temporary.drawString(" n = " + getN(), text.x, text.y);
		temporary.drawString(" θ = " + decimalFormat.format(getN()*dTheta), text.x, text.y + 25);
		temporary.drawString("dθ = " + decimalFormat.format(dTheta), text.x, text.y + 50);
		
		if (((getN())*dTheta) % 360 == 0 && getN() != 0)
			breakpoint();
	}
	
	private Point getPoint(double theta, double radius)
	{
		return new Point((int)(radius*Math.cos(theta * (Math.PI / 180d))), (int) (radius * Math.sin(theta * (Math.PI / 180d))));
	}
}
```

The program is simple:

 * `initialize()` is called when the simulation is first loaded. Here we set the image to be visible.
 * `reset(Graphics2D permanent)` is called once initially and any other time the image should be refreshed, such
as when the user resizes the window, or they select `Simulation → Reset`.
 * `draw(Graphics2D permanent, Graphics2D temporary)` is where most of the logic goes. First, the differences between `Graphics2D permanent` and `Graphics2D temporary`
are simple. Things drawn onto `permanent` persist throughout an animation, whereas drawings on `temporary` are cleared each time. This simulation
draws semitransparent black lines onto the permanent layer and strings onto the temporary layer.