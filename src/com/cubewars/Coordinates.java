package com.cubewars;

import com.badlogic.gdx.Gdx;

/**
 * Represents a tuple of coordinates in the grid and converts tuples into pixels and vice versa.
 * 
 * @author pyrosphere3
 * 
 */
public class Coordinates
{
	public int x, y;

	/**
	 * Coordinates constructor using a Pixel.
	 * 
	 * Creates an instance of Coordinates with the values provided by a {@link Pixel} object,
	 * calculating the cell value in which that {@link Pixel} is located automatically.
	 * 
	 * @param p A valid {@link Pixel} object.
	 */
	public Coordinates (Pixel p)
	{
		this.x = (int) Math.ceil (p.x / Gdx.graphics.getWidth ());
		this.y = (int) Math.ceil (p.y / Gdx.graphics.getHeight ());
	}

	/**
	 * Coordinates constructor using two integers.
	 * 
	 * @param x Column.
	 * @param y Row.
	 */
	public Coordinates (int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Taxicab distance between two Coordinates.
	 * 
	 * @param c Destination.
	 * @return An int representing the distance between these two Coordinates.
	 */
	public float distance (Coordinates c)
	{
		return Math.abs (c.y - y) + Math.abs (c.x - x);
	}

	/**
	 * Returns a {@link Pixel} equivalent to this Coordinates.
	 * 
	 * By definition, due to the fact that a single cell may contain lots of pixels, the Pixel
	 * returned will be the one located at the bottom-left of the current cell, following the same
	 * reference framework as the Window to ease the task of locating objects in the screen.
	 * 
	 * @return A {link Pixel} equivalent to this Coordinates.
	 */
	public Pixel toPixel ()
	{
		return new Pixel (this);
	}

	public String toString ()
	{
		return new String ("(" + x + ", " + y + ")");
	}
}
