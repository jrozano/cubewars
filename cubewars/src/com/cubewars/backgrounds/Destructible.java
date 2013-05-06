package com.cubewars.backgrounds;

import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;

public class Destructible extends Environment
{
	private double health;
	private double maxHealth;

	public Destructible (Texture texture, Coordinates c)
	{
		super (texture, c);
		health = 50;
		maxHealth = 50;
	}

	public void attack (double damage)
	{
		health -= damage;
		System.out.println ("[OBJECT] Damaging " + this.getClass ().getSimpleName () + " with " + damage + " points. " + health + " left.");
	}
	
	public double getHealth ()
	{
		return health;
	}
}
