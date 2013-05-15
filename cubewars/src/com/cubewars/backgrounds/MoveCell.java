package com.cubewars.backgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;
import com.cubewars.GameObject;

public class MoveCell extends GameObject {
	
	private static Texture texture = new Texture ((Gdx.files.internal ("media/styles/default/movecell.png")));
	
	public MoveCell(int priority, Coordinates c) {
		super(priority, texture, c);
		// TODO Auto-generated constructor stub
	}
	
}
