package com.cubewars.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;

public class TriangleGunner extends Triangle
{
	private static final Texture texture = new Texture (Gdx.files.internal ("media/characters/triangle-gunner.png"));
	private static final float maxHealth = 100;
	private static final float damage = 60;
	private static final int damageDistance = 1;
	private static final int travel = 4;

	public TriangleGunner (Coordinates c)
	{
		super (texture, c, maxHealth, maxHealth, damage, travel, damageDistance);
	}
}
