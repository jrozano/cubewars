package com.cubewars.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.cubewars.Coordinates;
import com.cubewars.GameController;
import com.cubewars.GameObject;
import com.cubewars.Obtainable;
import com.cubewars.Response;
import com.cubewars.backgrounds.Destructible;
import com.cubewars.characters.Character;
import com.cubewars.characters.CharacterNull;

public class LocalPlayer extends Player implements InputProcessor
{
	private Coordinates origin;
	private static int numberOfFingers = 0;

	public LocalPlayer (GameController controlador, Class<? extends Character> equipo)
	{
		super (controlador, equipo);
	}

	public void turn ()
	{
		origin = null;
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
		numberOfFingers++;
		
		if (button == Buttons.RIGHT || numberOfFingers == 2)
		{
			System.out.println ("[LOCAL ] Skipping Turn.");
			System.out.println(numberOfFingers);
			controller.skipTurn (this);
			return true;
		}

		Coordinates destination = controller.obtenerCoordenadas (screenX, screenY);
		// System.out.println ("[LOCAL ] Touchdown on: (" + destination.toPixel().x + ", " +
		// (destination.toPixel().y) + ") px: " + destination.toString ()
		// + ": " );

		/* Check if we already have a origin coordinates for our movement. */
		if (origin == null)
		{
			if (team ().isAssignableFrom (controller.select (destination)))
				origin = destination;

			/* Highligh cells only if a character has been selected. */
			if (origin != null)
				controller.choose (origin, this);
		} else
		{
			Class<? extends GameObject> objective = controller.select (destination);
			Class<? extends GameObject> selected = controller.select (origin);

			/* Highligh cells. */
			controller.choose (origin, this);

			/* Only obey if the game has not ended. */
			if (controller.status () == Response.ACTIVE && team ().isAssignableFrom (selected))
			{
				/* Check movement. */
				if (objective == CharacterNull.class && !controller.moved (this))
				{
					System.out.println ("[LOCAL ] Movement Phase.");
					System.out.println ("[LOCAL ] Objective: " + objective.getSimpleName ());

					Response response = controller.move (origin, destination, this);

					/*
					 * Check that the controller has indeed made the movement. If not, ask for
					 * another cell.
					 */
					if (response != Response.OK)
					{
						System.out.println ("[LOCAL ] Choose another cell.");
						return false;
					}

					origin = destination;
					return true;
				}
			}

			/* Check Boomer area's attack */
			if (objective == CharacterNull.class && !controller.attacked (this))
			{
				if (origin.x < destination.x + 1 || origin.x > destination.y - 1 || origin.y < destination.y + 1 || origin.y > destination.y - 1)
				{
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
			}

			/* Obtain loot. */
			if (Obtainable.class.isAssignableFrom (objective) && origin.distance (destination) == 1)
			{
				System.out.println ("[PLAYER] Object gathering.");
				Response response = controller.reclaim (origin, destination, this);
			}

			/* Check attack. */
			if ((enemy ().isAssignableFrom (objective) || Destructible.class.isAssignableFrom (objective))&& !controller.attacked (this))
			{
				System.out.println ("[LOCAL ] Attack Phase.");
				System.out.println ("[LOCAL ] Objective: " + objective.getSimpleName ());
				Response response = controller.attack (origin, destination, this);

				/*
				 * Check that the controller has indeed made the attack. If not, ask for another
				 * cell.
				 */
				if (response != Response.OK)
				{
					System.out.println ("[LOCAL ] Choose another cell.");
					return false;
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button)
	{
		numberOfFingers--;
		 
		if(numberOfFingers<0){
	        numberOfFingers = 0;
		}
		 
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
