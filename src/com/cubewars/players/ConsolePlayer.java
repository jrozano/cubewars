package com.cubewars.players;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.cubewars.Response;
import com.cubewars.Coordinates;
import com.cubewars.GameController;
import com.cubewars.GameObject;
import com.cubewars.characters.CharacterNull;
import com.cubewars.characters.Character;

/**
 * A console interacting via standard input/output (console).
 * 
 * @author pyrosphere3
 * 
 */
public class ConsolePlayer extends Player
{
	private Class<? extends GameObject> selected;
	BufferedReader input = new BufferedReader (new InputStreamReader (System.in));

	public ConsolePlayer (GameController controller, Class<? extends Character> team)
	{
		super (controller, team);
	}

	/**
	 * Interacts with the user and executes his actions during his turn.
	 * 
	 * During his turn, a player can choose one of his characters to play. His turn consists of two
	 * stages: a movement and an attack. Those actions are limited by the number of cells the
	 * character is able to traverse.
	 */
	@Override
	public void turn ()
	{
		try
		{
			int originX, originY, destinationX, destinationY;
			Coordinates origin, destination;

			/* Let the player choose who he wants to play with. */
			do
			{
				System.out.println ("[PLAYER] Choose a character:");

				System.out.print ("x > ");
				originX = Integer.parseInt (input.readLine ());

				System.out.print ("y > ");
				originY = Integer.parseInt (input.readLine ());

				origin = new Coordinates (originX, originY);
				selected = controller.select (origin);

			} while (!team ().isAssignableFrom (selected));

			controller.map.print ();

			/* First Stage. */
			do
			{
				System.out.println ("[PLAYER] Selected " + selected.getSimpleName ());
				System.out.println ("[PLAYER] Stage One: choose an objective.");

				controller.map.print ();

				System.out.print ("x > ");
				destinationX = Integer.parseInt (input.readLine ());

				System.out.print ("y > ");
				destinationY = Integer.parseInt (input.readLine ());

				destination = new Coordinates (destinationX, destinationY);

			} while (!play (origin, destination));

			/*
			 * Second stage. Note that, if the player chose to move during the first stage, now
			 * "origin" is pointing to an outdated coordinates, so we must refrest it.
			 */
			if (controller.moved (this))
				origin = destination;

			do
			{
				System.out.println ("[PLAYER] Selected " + selected.getSimpleName ());
				System.out.println ("[PLAYER] Stage Two: choose an objective.");

				controller.map.print ();

				System.out.print ("x > ");
				destinationX = Integer.parseInt (input.readLine ());

				System.out.print ("y > ");
				destinationY = Integer.parseInt (input.readLine ());

				destination = new Coordinates (destinationX, destinationY);
			} while (!play (origin, destination));
		} catch (Exception e)
		{
			e.printStackTrace ();
		}

	}

	/**
	 * Private method implementing the sequence logic.
	 * 
	 * play() sends orders to the game controllers depending on the origin and destination
	 * coordinates. If the objective is an empty cell, play() understands he wants to move to than
	 * position. On the other hand, if the chosen cell is an enemy, play() will issue an order
	 * attack.
	 * 
	 * Función privada que mantiene la lógica de la secuencia.
	 * 
	 * @param origin Origin coordinates.
	 * @param destination Destination coordinates.
	 * @return True if the action was completed successfully, flase in any other case.
	 */
	private boolean play (Coordinates origin, Coordinates destination)
	{
		/* If any coordinate is out of range, we'll assume the player wants to skip this stage. */
		if (origin.x < 0 || origin.y < 0 || destination.x < 0 || destination.y < 0)
		{
			System.out.println ("[PLAYER] Coordinates out of range: ignoring stage.");
			return true;
		}

		Class<? extends GameObject> objective = controller.select (destination);

		/* Check movement. */
		if (objective == CharacterNull.class && !controller.moved (this))
		{
			System.out.println ("[PLAYER] Movement Phase.");
			System.out.println ("[PLAYER] Objective: " + objective.getSimpleName ());

			Response response = controller.move (origin, destination, this);

			/* Check that the controller has indeed made the movement. If not, ask for another cell. */
			if (response != Response.OK)
			{
				System.out.println ("[PLAYER] Choose another cell.");
				return false;
			}

			return true;
		}

		/* Check attack. */
		if (enemy ().isAssignableFrom (objective) && !controller.attacked (this))
		{
			System.out.println ("[PLAYER] Attack Phase.");
			System.out.println ("[PLAYER] Objective: " + objective.getSimpleName ());
			Response response = controller.attack (origin, destination, this);

			/* Check that the controller has indeed made the attack. If not, ask for another cell. */
			if (response != Response.OK)
			{
				System.out.println ("[PLAYER] Choose another cell.");
				return false;
			}

			return true;
		}

		/* Check object. */
		/* TODO Implement objects. */

		return false;
	}
}
