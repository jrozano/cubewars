package com.cubewars.characters;

import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;

public class Cube extends Character
{
	public Cube (Texture texture, Coordinates c, float health, float maxHealth, float damage, int travel, int damageDistance)
	{
		super (texture, c, health, maxHealth, damage, travel, damageDistance);
	}
}
