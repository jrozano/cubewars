package com.cubewars.players;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.cubewars.Coordinates;
import com.cubewars.GameController;
import com.cubewars.GameObject;
import com.cubewars.Response;
import com.cubewars.characters.Character;
import com.cubewars.characters.CharacterNull;
import com.cubewars.characters.Cube;
import com.cubewars.characters.CubeBoomer;
import com.cubewars.characters.CubeGunner;
import com.cubewars.characters.CubeSniper;
import com.cubewars.characters.TriangleBoomer;
import com.cubewars.characters.TriangleGunner;
import com.cubewars.characters.TriangleSniper;

class InputGetter implements Runnable
{
	private Class<? extends GameObject> selected;
	BufferedReader input = new BufferedReader (new InputStreamReader (System.in));
	private GameController controller;
	private ConsolePlayer player;

	public InputGetter (GameController controller, ConsolePlayer player)
	{
		this.controller = controller;
		this.player = player;
	}

	@Override
	public void run ()
	{
		try
		{
			int originX, originY, destinationX, destinationY;
			Coordinates origin, destination;
			String action;
			boolean error;

			controller.map.print ();

			/* Ask the player if he wants to buy. */
			do
			{
				error = false;

				System.out.println ("[PLAYER] What do you want to do?");
				System.out.println ("[PLAYER] Options: buy, play");
				System.out.print ("> ");
				action = input.readLine ();

				/* Buy screen. */
				switch (action)
				{
					case "buy":
						buy ();
						break;
					case "play":
						break;
					default:
						System.out.println ("[PLAYER] Unknown option.");
						error = true;
				}

			} while (error);

			controller.map.print ();

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

			} while (!player.team ().isAssignableFrom (selected));

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
			if (controller.moved (player))
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
			System.out.println ("[PLAYER] Input error.");
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
		Class<? extends GameObject> objective = controller.select (destination);

		/* Check movement. */
		if (objective == CharacterNull.class && !controller.moved (player))
		{
			System.out.println ("[PLAYER] Movement Phase.");
			System.out.println ("[PLAYER] Objective: " + objective.getSimpleName ());

			Response response = controller.move (origin, destination, player);

			/* Check that the controller has indeed made the movement. If not, ask for another cell. */
			if (response != Response.OK)
			{
				System.out.println ("[PLAYER] Choose another cell.");
				return false;
			}

			return true;
		}

		/* Check attack. */
		if (player.enemy ().isAssignableFrom (objective) && !controller.attacked (player))
		{
			System.out.println ("[PLAYER] Attack Phase.");
			System.out.println ("[PLAYER] Objective: " + objective.getSimpleName ());
			Response response = controller.attack (origin, destination, player);

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

	private void buy ()
	{
		String action;
		boolean error = false;

		System.out.println ("[PLAYER] Shop menu:");
		System.out.println ("[PLAYER] Gunner :" + controller.getPrice (CubeGunner.class));
		System.out.println ("[PLAYER] Boomer :" + controller.getPrice (CubeBoomer.class));
		System.out.println ("[PLAYER] Sniper :" + controller.getPrice (CubeSniper.class));
		System.out.println ("[PLAYER] Current balance: " + controller.getMoney (player));

		do
		{
			System.out.println ("[PLAYER] What do you want to buy?");
			System.out.println ("[PLAYER] Options: gunner, boomer, sniper, nothing");
			System.out.print ("> ");

			try
			{
				action = input.readLine ();
			} catch (IOException e)
			{
				System.out.println ("[PLAYER] Input error.");
				break;
			}

			/* Buy screen. */
			switch (action)
			{
				case "gunner":
					if (player.team () == Cube.class)
						controller.buyCharacter (player, CubeGunner.class);
					else
						controller.buyCharacter (player, TriangleGunner.class);
					break;
				case "boomer":
					if (player.team () == Cube.class)
						controller.buyCharacter (player, CubeBoomer.class);
					else
						controller.buyCharacter (player, TriangleBoomer.class);
					break;
				case "sniper":
					if (player.team () == Cube.class)
						controller.buyCharacter (player, CubeSniper.class);
					else
						controller.buyCharacter (player, TriangleSniper.class);
					break;
				case "nothing":
					break;
				default:
					System.out.println ("[PLAYER] Unknown option.");
					error = true;
			}
		} while (error);
	}
}

/**
 * A console interacting via standard input/output (console).
 * 
 * @author pyrosphere3
 * 
 */
public class ConsolePlayer extends Player
{
	private GameController controller;

	public ConsolePlayer (GameController controller, Class<? extends Character> team)
	{
		super (controller, team);
		this.controller = controller;
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
		Runnable getter = new InputGetter (controller, this);
		Thread inputThread = new Thread (getter, "InputGetter");
		inputThread.start ();
	}

}
