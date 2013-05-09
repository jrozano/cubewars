package com.cubewars.characters;

import com.badlogic.gdx.math.MathUtils;

public class AttackBonus extends Bonus {

	private MathUtils math;
	
	public AttackBonus(int min, int max){
		value= (int) math.random(min,max);
	}
	
}
