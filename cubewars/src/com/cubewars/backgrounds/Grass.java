package com.cubewars.backgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;

public class Grass extends Environment
{
	private static final Texture texture = new Texture (Gdx.files.internal ("media/backgrounds/grass.png"));

	public Grass (Coordinates c)
	{
		super (texture, c);
	}

}
