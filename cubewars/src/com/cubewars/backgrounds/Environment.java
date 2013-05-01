package com.cubewars.backgrounds;

import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;
import com.cubewars.GameObject;

/**
 * An entity serving as decoration on the screen.
 * 
 * It could be a rock, walls, doors, bridges, etc.
 * 
 * @author pyrosphere3
 * 
 */
public class Environment extends GameObject
{
	/* Base priority. */
	private static final int priority = 1;

	public Environment (Texture texture, Coordinates c)
	{
		super (priority, texture, c);
	}

}
