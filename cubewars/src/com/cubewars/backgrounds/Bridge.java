package com.cubewars.backgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;
import com.cubewars.Response;

public class Bridge extends Destructible
{
	public Bridge (Texture texture, Coordinates c)
	{
		super (texture, c);
	}

	@Override
	public Response destroy() {
		// TODO Auto-generated method stub
		return Response.OK;
	}

}
