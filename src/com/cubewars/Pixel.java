package com.cubewars;

import com.badlogic.gdx.Gdx;

/**
 * Represents a pixel in the window.
 * 
 * @author pyrosphere3
 * 
 */
public class Pixel
{
	public float x, y;

	/**
	 * Construye un pixel a partir de una {@link Coordinates}.
	 * 
	 * @param c
	 */
	public Pixel (Coordinates c)
	{
		this.x = (c.x - 1) * Gdx.graphics.getWidth();
		this.y = (c.y - 1) * Gdx.graphics.getHeight();
	}

	/**
	 * Constructor using two real numbers representing its position on the screen.
	 * 
	 * @param x Horizontal position.
	 * @param y Vertical position.
	 */
	public Pixel (float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns a {@link Coordinates} object equivalent to this Pixel.
	 * 
	 * @return A Coordinates object,
	 */
	public Coordinates toCoordinates ()
	{
		return new Coordinates (this);
	}
}
