package com.cubewars.backgrounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;
import com.cubewars.GameController;
import com.cubewars.Response;

public class Box extends Destructible{

	private static Texture texture = new Texture (Gdx.files.internal ("media/items/boxclosed.png"));
	private boolean open=false;
	private int object;
	
	
	public Box(Coordinates c) {
		super(texture, c);
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
	
	public Response destroy(GameController controller){
		System.out.println("[BOX] Box destroyed");
		return Response.OK;
	}
	
	public int object(){
		return object;
	}

	@Override
	public Response destroy() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
