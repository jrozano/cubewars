package com.cubewars.backgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;

public class Dirt extends Environment
{
	private static final Texture texture = new Texture (Gdx.files.internal ("media/backgrounds/dirt.png"));

	public Dirt (Coordinates c)
	{
		super (texture, c);
	}

}
