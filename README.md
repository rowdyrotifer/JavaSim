# Table of Contents
1. [Features](#features)
2. [Installation](#installation)
3. [Example](#example)
4. [Building From Source](#building-from-source)

# JavaSim

Java simulation framework.

## Features

 * Easy to extend Simulation class.
   * provides a sort of canvas to draw on.
   * easy animation with repeatedly-called "draw" function.
 * Program that reads your extended class can:
   * save generated images.
   * create animations.
   * run at variable rates.
   * run frame-by-frame.
   * view in full screen.

## Installation

See the [latest releases](https://github.com/MarkLalor/JavaSim/releases/latest) for downloads.

After running the application for the first time, a folder called `JavaSim` should appear in
your `Documents` folder. Jar files for JavaSim simulations placed here will appear on the application's
main screen. They can then be selected to load the simulation. 

## Example

The following program is designed to generate an image of a nephroid. Running `File → Create Animated Gif`
with the default settings saves this image:

![Nephroid Example](https://raw.githubusercontent.com/MarkLalor/JavaSim/master/examples/Nephroid.gif)

```java
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import com.marklalor.javasim.simulation.preset.BlankImageSimulation;

//Last tested with JavaSim 1.1.1
public class Nephroid extends BlankImageSimulation
{	
	@Override
	public void draw(Graphics2D permanent, Graphics2D temporary)
	{
		//Move the origin from the corner to the center.
		permanent.translate(getWidth()/2, getHeight()/2);
		
		//Choose a semi-transparent black color.
		permanent.setColor(new Color(0, 0, 0, 0.2f));
		
		//Get a radius that is 90% of the way to the edge of the screen.
		double radius = (Math.min(getWidth(), getHeight())/2) * 0.9;
		
		//Generate points on a circle using JavaSim's built-in "n", which automatically increments.
		Point point1 = getPoint(n, radius);
		Point point2 = getPoint(n*3, radius);
		
		//Draw a line between the two points.
		permanent.drawLine(point1.x, point1.y, point2.x, point2.y);
		
		//Stop after frame 360.
		if (n == 360)
			breakpoint();
	}
	
	//Get a point on the radius of a circle.
	private Point getPoint(double angle, double radius)
	{
		int x = (int)(radius*Math.cos(Math.toRadians(angle)));
		int y = (int)(radius*Math.sin(Math.toRadians(angle)));
		return new Point(x, y);
	}
}
```

This bare bones program works by overriding `draw(Graphics2D permanent, Graphics2D temporary)`.
The differences between `Graphics2D permanent` and `Graphics2D temporary`
are simple. Things drawn onto `permanent` persist throughout an animation, whereas objects drawn on to `temporary`
are cleared each frame.
This simulation draws one semitransparent black line onto the `permanent` graphics each frame, connecting each angle to the angle three times it.

See the [getting started](https://github.com/MarkLalor/JavaSim/wiki/Getting-Started) page for a full introduction on how and what to use.

## Building From Source

JavaSim uses [Apache Ant](http://ant.apache.org) for building and Ant Task [JarBundler (Tobias Fischer's fork)](https://github.com/tofi86/Jarbundler) for packaging for OS X.

Building requires at least Ant and a Java 7 JDK.

#### Quick build (only runnable jar):

```shell
git clone https://github.com/MarkLalor/JavaSim.git
cd JavaSim/build
ant create_jar
```

This will create `JavaSim.jar` in the `dist` directory.

#### Full build (runnable jar, OSX .app, and .dmg)
Requires [JarBundler](https://github.com/tofi86/Jarbundler) and osascript (will certainly not build .dmg on Windows because it relies on applescript. JarBundler for .app should work on Windows, though!) 

```shell
git clone https://github.com/MarkLalor/JavaSim.git
cd JavaSim/build
ant
```

This will create `JavaSim.app`, `JavaSim-x.y.z.dmg`, and `JavaSim-x.y.z.jar` in the `dist` directory.