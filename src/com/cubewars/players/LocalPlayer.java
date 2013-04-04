package com.cubewars.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.cubewars.Coordinates;
import com.cubewars.GameController;
import com.cubewars.GameObject;
import com.cubewars.Pixel;
import com.cubewars.Response;
import com.cubewars.characters.Character;
import com.cubewars.characters.CharacterNull;

public class LocalPlayer extends Player implements InputProcessor
{
	private Coordinates origin = null; 
	
	public LocalPlayer (GameController controlador, Class<? extends Character> equipo)
	{
		super (controlador, equipo);
		
	}
	
	public void turn ()
	{
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
		if (button == Buttons.RIGHT)
		{
			System.out.println ("[LOCAL ] Skipping Phase.");
			controller.skipTurn (this);
			origin=null;
			return true;
		}

		Coordinates destination = new Pixel (screenX, Gdx.graphics.getHeight() - screenY).toCoordinates ();
		System.out.println ("[LOCAL ] Touchdown on: " + screenX + ", " + (Gdx.graphics.getHeight() - screenY) + ": " + destination.toString ());
		Class<? extends GameObject> objective = controller.select (destination);
		
		if (origin == null){
			if(team().isAssignableFrom(objective)){
				origin = destination;
				System.out.println("[LOCAL ] Selected a character of your team");
			}
		}
		else
		{
			objective = controller.select (destination);
			
			if(controller.status() != Response.CUBEVICTORY && controller.status() != Response.TRIANGLEVICTORY){
				/* Check movement. */
				if (objective == CharacterNull.class && !controller.moved (this))
				{
					System.out.println ("[PLAYER] Movement Phase.");
					System.out.println ("[PLAYER] Objective: " + objective.getSimpleName ());

					Response response = controller.move (origin, destination, this);

					/*
					 * Check that the controller has indeed made the movement. If not, ask for another
					 * cell.
					 */
					if (response != Response.OK)
					{
						System.out.println ("[PLAYER] Choose another cell.");
						return false;
					}

					origin = destination;

					return true;
				}
				
			}else{
				origin=null;
				return false;
			}
				

			/* Check attack. */
			if (enemy ().isAssignableFrom (objective) && !controller.attacked (this))
			{
				System.out.println ("[PLAYER] Attack Phase.");
				System.out.println ("[PLAYER] Objective: " + objective.getSimpleName ());
				Response response = controller.attack (origin, destination, this);

				/*
				 * Check that the controller has indeed made the attack. If not, ask for another
				 * cell.
				 */
				if (response != Response.OK)
				{
					System.out.println ("[PLAYER] Choose another cell.");
					return false;
				}
				
				return true;
			}
			else{
				origin=null;
			}
			
			/* Check object. */
			/* TODO Implement objects. */
		}
		
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
