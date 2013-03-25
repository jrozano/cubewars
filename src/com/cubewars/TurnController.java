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
	Map<Player, boolean[]> estados = new HashMap<Player, boolean[]> ();
	
	public TurnController () {}
	
	public void newTurn (Player p)
	{
		System.out.println ("[TURNOS] Empezando nuevo turno para " + p.toString ());
		estados.put (p, new boolean[2]);
	}
	
	public void move (Player p)
	{
		if (canMove (p))
		{
			estados.get (p)[0] = true;
			System.out.println ("[TURNOS] Estado de turno para " + p.toString () + ": " + estados.get (p)[0] + ", " + estados.get (p)[1]);
		}
		else
			throw new RuntimeException ("No puedes moverte.");
	}
	
	public void attack (Player p)
	{
		if (canAttack (p))
		{
			estados.get (p)[1] = true;
			System.out.println ("[TURNOS] Estado de turno para " + p.toString () + ": " + estados.get (p)[0] + ", " + estados.get (p)[1]);
		}
		else
			throw new RuntimeException ("No puedes atacar.");
	}
	
	public boolean canMove (Player p)
	{
		if (!estados.get (p)[0])
		{
			System.out.println ("[TURNOS] " + p.toString () + " puede moverse.");
			return true;
		}
		else
		{
			System.out.println ("[TURNOS] " + p.toString () + " no puede moverse.");
			return false;
		}
	}
	
	public boolean canAttack (Player p)
	{
		if (!estados.get (p)[1])
		{
			System.out.println ("[TURNOS] " + p.toString () + " puede atacar.");
			return true;
		}
		else
		{
			System.out.println ("[TURNOS] " + p.toString () + " no puede atacar.");
			return false;
		}
	}
}
