package com.cubewars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Box extends Item{

	private Texture texture = new Texture (Gdx.files.internal ("media/items/boxclosed.png"));
	private boolean open=false;
	private int object;
	
	
	public Box(int priority, Texture texture, Coordinates c) {
		super(priority, texture, c);
		object=1;
		// TODO Auto-generated constructor stub
	}

	public boolean isOpen(){
		return open;
	}
	
	public void open(){
		open=true;
		texture = new Texture(Gdx.files.internal("media/items/boxopen.ong"));
	}
	
	public int object(){
		return object;
	}
	
}
