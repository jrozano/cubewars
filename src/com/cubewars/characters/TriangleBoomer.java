package com.cubewars.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;

public class TriangleBoomer extends Triangle
{
	private static final Texture texture = new Texture (Gdx.files.internal ("media/characters/triangle-boomer.png"));
	private static final float maxHealth = 200;
	private static final float damage = 90;
	private static final int damageDistance = 2;
	private static final int travel = 2;

	public TriangleBoomer (Coordinates c)
	{
		super (texture, c, maxHealth, maxHealth, damage, travel, damageDistance);
	}
}
