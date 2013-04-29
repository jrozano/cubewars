package com.cubewars;

import com.badlogic.gdx.graphics.Texture;

public class Lifebar extends GameObject {
	static Texture t = new Texture("media/items/new-health-bar.png");

	public Lifebar(int priority, int x, int y) {
		super(priority, t, x, y);
		area.height=3;
	}
	
	public void setWidth(float width){
		area.width=width;
	}

}
