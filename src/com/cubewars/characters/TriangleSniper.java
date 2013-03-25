package com.cubewars.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Clase Sniper perteneciente a la facción de los Triangulos.
 * 
 * @author pyrosphere3
 * 
 */
public class TriangleSniper extends Triangle
{
	private static final Texture textura = new Texture (Gdx.files.internal ("media/characters/triangle-sniper.png"));
	private static final float maxHealth = 80;
	private static final float damage = 200;
	private static final int damageDistance = 3;
	private static final int travel = 1;

	public TriangleSniper (float posX, float posY)
	{
		super (textura, posX, posY, maxHealth, maxHealth, damage, travel, damageDistance);
	}
}
