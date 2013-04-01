package com.cubewars.characters;

import com.badlogic.gdx.graphics.Texture;

public class Cube extends Character
{
	public Cube (Texture texture, float posX, float posY, float health, float maxHealth, float damage, int travel,
			int damageDistance)
	{
		super (texture, posX, posY, health, maxHealth, damage, travel, damageDistance);
	}
}
