package com.cubewars.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Clase Gunner perteneciente a la facción de los Triangulos.
 * 
 * @author pyrosphere3
 * 
 */
public class TriangleGunner extends Triangle
{
	private static final Texture textura = new Texture (Gdx.files.internal ("media/characters/triangle-gunner.png"));
	private static final float maxHealth = 100;
	private static final float damage = 60;
	private static final int damageDistance = 1;
	private static final int travel = 4;

	public TriangleGunner (float posX, float posY)
	{
		super (textura, posX, posY, maxHealth, maxHealth, damage, travel, damageDistance);
	}
}
