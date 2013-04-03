package com.cubewars;

import java.util.HashMap;
import java.util.Map;

import com.cubewars.players.Player;

/**
 * Keeps track of the different actions that the players can do during their turn.
 * 
 * The turn controller remembers if a player has already made an attack or a movement, so only one
 * action of each kind can be executed in the same turn.
 * 
 * @author pyrosphere3 hola esto es un commit
 */
public class TurnController
{
	private Map<Player, boolean[]> statuses = new HashMap<Player, boolean[]> ();
	private Player currentPlayer;

	public TurnController ()
	{
	}

	/**
	 * Creates a new turn for a player. All actions are available when a turn starts.
	 * 
	 * @param p The player.
	 */
	public void newTurn (Player p)
	{
		System.out.println ("[TURN  ] New turn for " + p.toString ());
		statuses.put (p, new boolean[2]);
		currentPlayer = p;
	}

	/**
	 * Tells the turn controller that a specific player has executed a movement, so no forward
	 * actions of this kind can be done in this turn.
	 * 
	 * @param p
	 */
	public void move (Player p)
	{
		if (canMove (p))
			statuses.get (p)[0] = true;
		else
			System.out.println ("[TURN  ] " + p.toString () + " cannot move: " + statuses.get (p)[0] + ", "
					+ statuses.get (p)[1]);
	}

	/**
	 * Tells the turn controller that a specific player has executed an attack, so no forward
	 * actions of this kind can be done in this turn.
	 * 
	 * @param p
	 */
	public void attack (Player p)
	{
		if (canAttack (p))
			statuses.get (p)[1] = true;
		else
			System.out.println ("[TURN  ] " + p.toString () + " cannot attack: " + statuses.get (p)[0] + ", "
					+ statuses.get (p)[1]);
	}

	/**
	 * Returns if the player has permission to make a movement or not.
	 * 
	 * @param p The player.
	 * @return True if the player can make a movement (has not made it yet) or false in any other
	 *         case.
	 */
	public boolean canMove (Player p)
	{
		return !statuses.get (p)[0];

	}

	/**
	 * Returns if the player has permission to attack or not.
	 * 
	 * @param p The player.
	 * @return True if the player can attack (has not made it yet) or false in any other case.
	 */
	public boolean canAttack (Player p)
	{
		return !statuses.get (p)[1];
	}
	
	public boolean finishedTurn ()
	{
		return statuses.get (currentPlayer)[0] && statuses.get (currentPlayer)[1];
	}
	
	public Player currentPlayer ()
	{
		return currentPlayer;
	}
}
