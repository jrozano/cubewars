package com.cubewars;

import com.badlogic.gdx.Gdx;

/**
 * Represents a tuple of coordinates in the characters and converts tuples into pixels and vice versa.
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
	 * Return the x coordinate
	 * 
	 * @return x
	 */

	public int x ()
	{
		return x;
	}

	/**
	 * Return the y coordinate
	 * 
	 * @return y
	 */
	public int y ()
	{
		return y;
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
		return new Pixel ((128) * x, (80) * y);
	}

	public String toString ()
	{
		return new String ("(" + x + ", " + y + ")");
	}

	/**
	 * Compares two objects, and returns if they are equivalent or not.
	 * 
	 * Needed to compare objects and delete duplicates in a HashSet.
	 * 
	 * @param o The object to be compared to.
	 * @returns "true" if both coordinates are the same, "false" in any other case.
	 */
	@Override
	public boolean equals (Object o)
	{
		if (o instanceof Coordinates)
		{
			Coordinates c = (Coordinates) o;
			return (x == c.x && y == c.y);
		} else
			return false;
	}

	/**
	 * Needed to comply with the HashSet contract.
	 * 
	 * HashSet will use hashCode() to "presort" the objects before comparing them. The contract of
	 * hashCode() says that whenever a.equals(b) is true, a.hashCode() == b.hashCode() will be true.
	 * 
	 * @return An int with the answer to the Ultimate Question of Life, the Universe, and
	 *         Everything, which will serve as the Coordinates class' hash code.
	 */
	@Override
	public int hashCode ()
	{
		return 42;
	}
}
