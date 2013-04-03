package com.cubewars.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class CubeGunner extends Cube
{
	private static final Texture texture = new Texture (Gdx.files.internal ("media/characters/cube-gunner.png"));
	private static final float maxHealth = 60;
	private static final float damage = 60;
	private static final int damageDistance = 1;
	private static final int travel = 4;

	public CubeGunner (float posX, float posY)
	{
		super (texture, posX, posY, maxHealth, maxHealth, damage, travel, damageDistance);
		
	}
}
