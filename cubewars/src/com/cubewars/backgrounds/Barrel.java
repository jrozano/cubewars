package com.cubewars.backgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cubewars.Coordinates;
import com.cubewars.Response;

public class Barrel extends Destructible{
	private static Texture texture = new Texture(Gdx.files.internal ("media/items/barrel.png"));
	
	public Barrel(Coordinates c) {
		super(texture, c);
		TextureRegion region = new TextureRegion (texture);
		this.area.height = region.getRegionHeight ();
		this.area.width = region.getRegionWidth ();
	}

	public Response destroy(){
		System.out.println("[BARREL] Barrel destroyed");
		return Response.AREAATACK;
		// TODO Add explosion sound
	}
	
}
