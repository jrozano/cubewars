package com.cubewars.backgrounds;

import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;
import com.cubewars.Response;

public abstract class Destructible extends Environment
{
	private double health;
	private double maxHealth;

	public Destructible (Texture texture, Coordinates c)
	{
		super (texture, c);
		health = 1;
		maxHealth = 1;
	}

	public boolean attack (double damage)
	{
		health -= damage;
		System.out.println ("[OBJECT] Damaging " + this.getClass ().getSimpleName () + " with " + damage + " points. " + health + " left.");
		if(health<=0){
			this.destroy();
			return true;
		}else{
			return false;
		}
	}
	
	public double getHealth ()
	{
		return health;
	}
	
	public abstract Response destroy();
	
	public abstract int damage();
}
