package com.cubewars.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Clase Sniper perteneciente a la facción de los Cubos.
 * 
 * @author pyrosphere3
 * 
 */
public class CubeSniper extends Cube
{
	private static final Texture textura = new Texture (Gdx.files.internal ("media/characters/cube-sniper.png"));
	private static final float maxHealth = 60;
	private static final float damage = 120;
	private static final int damageDistance = 3;
	private static final int travel = 1;

	public CubeSniper (float posX, float posY)
	{
		super (textura, posX, posY, maxHealth, maxHealth, damage, travel, damageDistance);
	}
}
