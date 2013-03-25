package com.cubewars;

import com.badlogic.gdx.graphics.Texture;

/**
 * Clase que implementa una capa o fundido para menús y demás interfaces.
 * 
 * @author pyrosphere3
 * 
 */
public class Overlay extends GameObject
{
	private static final int priority = 20;

	public Overlay ()
	{
		super (priority, new Texture ("data/overlay.png"), 0, 0);
	}

}
