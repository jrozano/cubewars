package com.cubewars.characters;

import com.badlogic.gdx.math.MathUtils;

public class AttackBonus extends Bonus {

	private MathUtils math;
	private final int min=25;
	private final int max=50;
	
	public AttackBonus(){
		value= (int) math.random(min,max);
	}
	
}
