package com.cubewars.characters;

import com.badlogic.gdx.math.MathUtils;

public class HealthBonus extends Bonus{
	
	private MathUtils math;
	private final int min=50;
	private final int max=200;
	
	public HealthBonus(){
		value= (int) math.random(min,max);
	}
	
}
