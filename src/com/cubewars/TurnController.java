package com.cubewars;

import java.util.HashMap;
import java.util.Map;

import com.cubewars.players.Player;

/**
 * Controla las acciones que un jugador puede realizar en su turno.
 * @author pyrosphere3
 *
 */
public class TurnController
{
	private Map<Player, boolean[]> estados = new HashMap<Player, boolean[]> ();
	
	public TurnController () {}
	
	public void newTurn (Player p)
	{
		System.out.println ("[TURNOS] Empezando nuevo turno para " + p.toString ());
		estados.put (p, new boolean[2]);
	}
	
	public void move (Player p)
	{
		if (canMove (p))
			estados.get (p)[0] = true;
		else
			System.out.println ("[TURNOS] " + p.toString () + " no puede moverse: " + estados.get (p)[0] + ", " + estados.get (p)[1]);
	}
	
	public void attack (Player p)
	{
		if (canAttack (p))
			estados.get (p)[1] = true;
		else
			System.out.println ("[TURNOS] " + p.toString () + " no puede atacar: " + estados.get (p)[0] + ", " + estados.get (p)[1]);
	}
	
	public boolean canMove (Player p)
	{
		return !estados.get (p)[0];

	}
	
	public boolean canAttack (Player p)
	{
		return !estados.get (p)[1];
	}
}
