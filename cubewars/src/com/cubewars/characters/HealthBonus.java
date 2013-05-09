package com.cubewars.characters;

import com.badlogic.gdx.math.MathUtils;

public class HealthBonus extends Bonus{
	
	private MathUtils math;
	
	public HealthBonus(int min, int max){
		value= (int) math.random(min,max);
	}
	
}
