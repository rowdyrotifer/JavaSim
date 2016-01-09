import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import com.marklalor.javasim.simulation.preset.BlankImageSimulation;

public class TripleAngleNephroid extends BlankImageSimulation
{
	@Override
	public void draw(Graphics2D permanent, Graphics2D temporary)
	{
		//Move the origin from the corner to the center.
		permanent.translate(getWidth() / 2, getHeight() / 2);
		
		//Choose a semi-transparent black color.
		permanent.setColor(new Color(0, 0, 0, 0.2f));
		
		//Get a radius that is 90% of the way to the edge of the screen.
		double radius = (Math.min(getWidth(), getHeight()) / 2) * 0.9;
		
		//Generate points on a circle using JavaSim's getN() to animate.
		Point point1 = getPoint(getN(), radius);
		Point point2 = getPoint(getN()*3, radius);
		
		//Draw a line between the two points.
		permanent.drawLine(point1.x, point1.y, point2.x, point2.y);
		
		//Stop after 360 lines.
		if (getN() == 360)
			breakpoint();
	}
	
	//Get a point on the radius of a circle.
	private Point getPoint(double theta, double radius)
	{
		int x = (int)(radius*Math.cos(Math.toRadians(theta)));
		int y = (int)(radius*Math.sin(Math.toRadians(theta)));
		return new Point(x, y);
	}
}