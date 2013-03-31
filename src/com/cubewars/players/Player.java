package com.cubewars.players;

import com.cubewars.GameController;
import com.cubewars.characters.Character;
import com.cubewars.characters.Cube;
import com.cubewars.characters.Triangle;

/**
 * A generic plyer in the game.
 * 
 * All specific Player implementations (local, consola, network, AI, etc.) must inherit from this
 * class to ensure similar behaviour and compatibility across the program.
 * 
 * @author pyrosphere3
 * 
 */
public abstract class Player
{
	protected GameController controller;
	protected Class<? extends Character> team;

	/**
	 * Constructor.
	 * 
	 * The Player's team must be assigned prior to the object instantiation.
	 * 
	 * @param controller The game controller.
	 * @param team The new player's team.
	 * @see GameController
	 * @see Team
	 */
	public Player (GameController controller, Class<? extends Character> team)
	{
		this.controller = controller;
		this.team = team;
	}

	/**
	 * This method will be called by the controller each player turn.
	 * 
	 * Each player implementation must overwrite this method to adapt its behaviour to the specifics
	 * of its nature.
	 */
	public void turn ()	{}

	/**
	 * Returns this player's team.
	 * @return This player's team.
	 */
	public Class<? extends Character> team ()
	{
		return team;
	}

	/**
	 * Returns this player's enemy team.
	 * @return This player's enemy team.
	 */
	public Class<? extends Character> enemy ()
	{
		if (Cube.class.isAssignableFrom (team))
			return Triangle.class;
		else
			return Cube.class;
	}

	public String toString ()
	{
		return this.getClass ().getSimpleName () + " (" + team.getSimpleName () + ")";
	}
}
