package com.marklalor.javasim.sample.nephroid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.marklalor.javasim.data.annotations.SimFunction;
import com.marklalor.javasim.data.annotations.SimInput;
import com.marklalor.javasim.data.annotations.SimInterface;
import com.marklalor.javasim.data.annotations.SimOutput;
import com.marklalor.javasim.data.input.InputInt32;
import com.marklalor.javasim.data.output.OutputImage;

@SimInterface(group = "com.marklalor.nephriod")
public class Nephroid {

	@SimFunction(group = "com.marklalor.nephriod", name = "draw")
	public void draw(
			@SimInput(group = "javasim", name = "frame") InputInt32 frame,
			@SimOutput(group = "com.marklalor.nephriod", name = "nephriod") OutputImage output) {
		final int width = 400;
		final int height = 400;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = image.createGraphics();

		// Move the origin from the corner to the center.
		graphics.translate(width / 2, height / 2);

		// Choose a semi-transparent black color.
		graphics.setColor(new Color(0f, 0f, 0f, 0.2f));

		// Get a radius that is 90% of the way to the edge of the screen.
		double radius = Math.min(width, height) / 2d * 0.9;

		final int lines = 10 + frame.get();

		for(int i = 0; i < lines; i++) {
			final double position = i / (double) lines * Math.PI * 2;
			Point point1 = getPoint(position, radius);
			Point point2 = getPoint(position * 3, radius);
			graphics.drawLine(point1.x, point1.y, point2.x, point2.y);
		}

		output.put(image);
	}

	// Get a point on the radius of a circle.
	private static Point getPoint(double angle, double radius) {
		int x = (int) Math.round(radius * Math.cos(angle));
		int y = (int) Math.round(radius * Math.sin(angle));
		return new Point(x, y);
	}
}
