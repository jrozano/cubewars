package com.cubewars.characters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;

public class TriangleSniper extends Triangle
{
	private static final Texture texture = new Texture (Gdx.files.internal ("media/characters/triangle-sniper.png"));
	private static final float maxHealth = 60;
	private static final float damage = 60;
	private static final int damageDistance = 3;
	private static final int travel = 1;
	
	public TriangleSniper (Coordinates c)
	{
		super (texture, c, maxHealth, maxHealth, damage, travel, damageDistance);
	}
}
