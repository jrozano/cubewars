package com.cubewars.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Clase Boomer perteneciente a la facción de los Cubos.
 * 
 * @author pyrosphere3
 * 
 */
public class CubeBoomer extends Cube
{
	private static final Texture textura = new Texture (Gdx.files.internal ("media/characters/cube-boomer.png"));
	private static final float maxHealth = 60;
	private static final float damage = 90;
	private static final int damageDistance = 2;
	private static final int travel = 2;

	public CubeBoomer (float posX, float posY)
	{
		super (textura, posX, posY, maxHealth, maxHealth, damage, travel, damageDistance);
	}
}
