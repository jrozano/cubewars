package com.cubewars.backgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;
import com.cubewars.GameController;
import com.cubewars.Response;
import com.cubewars.characters.AttackBonus;
import com.cubewars.characters.Bonus;
import com.cubewars.characters.HealthBonus;
import com.badlogic.gdx.math.MathUtils;

public class Box extends Destructible{

	private boolean open=false;
	private int object;
	private Bonus bonus;
	private MathUtils math;
	
	public Box(Coordinates c) {
		super(new Texture(Gdx.files.internal("media/items/boxclosed.png")), c);
		object=math.random(3,3);
		switch (object){
			case 1:
				bonus = new HealthBonus();
				break;
			case 2:
				bonus= new AttackBonus();
				break;
			case 3:
				bonus = null; //this is an explosion
		}
	}

	public boolean isOpen(){
		return open;
	}
	
	public Bonus open(){
		open=true;
		System.out.println("[BOX] Box opened");
		texture = new Texture(Gdx.files.internal("media/items/boxopen.png"));
		return bonus;
	}
	
	public Response destroy(GameController controller){
		System.out.println("[BOX] Box destroyed");
		return Response.OK;
	}

	@Override
	public Response destroy() {
		// TODO implement sound
		return null;
	}
	
}
