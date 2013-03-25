package com.cubewars.players;

import com.cubewars.GameController;
import com.cubewars.characters.Character;
import com.cubewars.characters.Cube;
import com.cubewars.characters.Triangle;

/**
 * Clase genérica que representa un jugador de la partida.
 * 
 * Todas las implementaciones de jugador (local, consola, remoto, IA, etc.) deberán heredar de esta clase para garantizar un comportamiento similar y compatibilidad con el resto de la estructura del juego.
 * @author pyrosphere3
 *
 */
public class Player
{
	protected GameController controlador;
	protected Class<? extends Character> equipo;

	/**
	 * Constructor de un jugador.
	 * 
	 * Debe especificarse el equipo con el que juega el jugador.
	 * @param controlador Controlador del juego.
	 * @param equipo Equipo del jugador.
	 * @see GameController
	 * @see Team
	 */
	public Player (GameController controlador, Class<? extends Character> equipo)
	{
		this.controlador = controlador;
		this.equipo = equipo;
	}

	public void turn ()
	{
	}

	public Class<? extends Character> team ()
	{
		return equipo;
	}

	public Class<? extends Character> enemy ()
	{
		if (Cube.class.isAssignableFrom (equipo))
			return Triangle.class;
		else
			return Cube.class;
	}
}
