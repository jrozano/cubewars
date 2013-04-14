package com.cubewars;

import com.badlogic.gdx.graphics.Texture;

/**
 * An entity serving as decoration on the screen.
 * 
 * It could be a rock, walls, doors, bridges, etc.
 * 
 * @author pyrosphere3
 * 
 */
public abstract class Environment extends GameObject
{
	/* Basse priority. */
	private static final int priority = 1;

	public Environment (Texture texture, int posX, int posY)
	{
		super (priority, texture, posX, posY);
	}

}
