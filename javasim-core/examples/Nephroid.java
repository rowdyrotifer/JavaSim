import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import com.marklalor.javasim.simulation.preset.BlankImageSimulation;

//Last updated with JavaSim 1.3.0
public class Nephroid extends BlankImageSimulation
{
    @Override
    public void draw(Graphics2D graphics)
    {
        //Move the origin from the corner to the center.
        graphics.translate(getImage().getWidth()/2, getImage().getHeight()/2);
        
        //Choose a semi-transparent black color.
        graphics.setColor(new Color(0f, 0f, 0f, 0.2f));
        
        //Get a radius that is 90% of the way to the edge of the screen.
        double radius = (Math.min(getImage().getWidth(), getImage().getHeight())/2) * 0.9;
        
        //Generate points on a circle using JavaSim's built-in "n", which automatically increments.
        Point point1 = getPoint(n, radius);
        Point point2 = getPoint(n*3, radius);
        
        //Draw a line between the two points.
        graphics.drawLine(point1.x, point1.y, point2.x, point2.y);
        
        //Stop after frame 360.
        if (n == 360)
            breakpoint();
    }
    
    //Get a point on the radius of a circle.
    private static Point getPoint(double angle, double radius)
    {
        int x = (int)(radius*Math.cos(Math.toRadians(angle)));
        int y = (int)(radius*Math.sin(Math.toRadians(angle)));
        return new Point(x, y);
    }
}