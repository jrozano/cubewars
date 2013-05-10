package com.cubewars.backgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;
import com.cubewars.Response;

public class Rocks extends Destructible
{
	public Rocks (Texture texture, Coordinates c)
	{
		super (texture, c);
	}

	@Override
	public Response destroy() {
		// TODO Auto-generated method stub
		return Response.OK;
	}
	
	public int damage(){
		return 0;
	}

}
