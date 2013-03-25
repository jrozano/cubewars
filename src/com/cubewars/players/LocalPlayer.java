package com.cubewars.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.cubewars.GameController;
import com.cubewars.characters.Character;

public class LocalPlayer extends Player implements InputProcessor
{
	public LocalPlayer (GameController controlador, Class<? extends Character> equipo)
	{
		super (controlador, equipo);
		Gdx.input.setInputProcessor (this);
	}

	@Override
	public boolean keyDown (int keycode)
	{
		return true;
	}

	@Override
	public boolean keyUp (int keycode)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped (char character)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved (int screenX, int screenY)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled (int amount)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
