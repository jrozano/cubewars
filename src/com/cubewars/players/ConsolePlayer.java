package com.cubewars.players;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.cubewars.Response;
import com.cubewars.Coordinates;
import com.cubewars.GameController;
import com.cubewars.GameObject;
import com.cubewars.characters.CharacterNull;
import com.cubewars.characters.Character;
import com.cubewars.characters.Cube;
import com.cubewars.characters.Triangle;

/**
 * Clase que representa un jugador por consola.
 * 
 * En la clase ConsolePlayer, la interacción con el jugador se realiza mediante consola (E/S
 * estándar), solicitando coordenadas al usuario para realizar los movimientos.
 * 
 * @author pyrosphere3
 * 
 */
public class ConsolePlayer extends Player
{
	private Class<? extends GameObject> seleccionado;
	BufferedReader input = new BufferedReader (new InputStreamReader (System.in));

	public ConsolePlayer (GameController controlador, Class<? extends Character> equipo)
	{
		super (controlador, equipo);
	}

	/**
	 * Ejecuta las acciones necesarias para efectuar la jugada del jugador durante su turno.
	 * 
	 * Durante su turno, el jugador elegirá un personaje con el que desea jugar. Una jugada se
	 * compone de dos fases: ataque y movimiento. Durante la fase de ataque, el jugador podrá
	 * cambiar de posición el personaje seleccionado, siempre y cuando la distancia no sea superior
	 * a la capacidad de movimiento del personaje y la celda destino no esté ya ocupada. Durante la
	 * fase de ataque, el jugador podrá elegir un personaje enemigo o entidad neutral a la que
	 * infligir puntos de daño. El orden de la secuencia de turno es indiferente, pero sólo se podrá
	 * realizar un solo tipo de acción por turno. Tras realizar las dos acciones, el turno se da por
	 * finalizado.
	 * 
	 * En la clase ConsolePlayer, la interacción con el jugador se realiza mediante consola (E/S
	 * estándar), solicitando coordenadas al usuario para realizar los movimientos.
	 */
	@Override
	public void turn ()
	{
		try
		{
			int origenx, origeny, destinox, destinoy;
			Coordinates origen, destino;

			/* Seleccionamos qué personaje queremos mover. */
			do
			{
				System.out.println ("[PLAYER] Selecciona personaje:");

				System.out.print ("x > ");
				origenx = Integer.parseInt (input.readLine ());

				System.out.print ("y > ");
				origeny = Integer.parseInt (input.readLine ());

				origen = new Coordinates (origenx, origeny);
				seleccionado = controlador.what (origen);

			} while (! team ().isAssignableFrom (seleccionado));

			controlador.mapa.print ();

			/* Primera de las dos fases de las que se compone nuestro turno. */
			do
			{
				System.out.println ("[PLAYER] Seleccionado " + seleccionado.getSimpleName ());
				System.out.println ("[PLAYER] Primera fase, selecciona objetivo:");

				controlador.mapa.print ();

				System.out.print ("x > ");
				destinox = Integer.parseInt (input.readLine ());

				System.out.print ("y > ");
				destinoy = Integer.parseInt (input.readLine ());

				destino = new Coordinates (destinox, destinoy);

			} while (! play (origen, destino));

			/*
			 * Segunda fase. Hay que tener en cuenta que si en la primera fase nos movimos, "origen"
			 * ahora apunta a las coordenadas antiguas, por lo que hay que actualizarlas.
			 */
			if (controlador.moved (this))
				origen = destino;
			
			do
			{
				System.out.println ("[PLAYER] Seleccionado " + seleccionado.getSimpleName ());
				System.out.println ("[PLAYER] Segunda fase, selecciona objetivo:");

				controlador.mapa.print ();

				System.out.print ("x > ");
				destinox = Integer.parseInt (input.readLine ());

				System.out.print ("y > ");
				destinoy = Integer.parseInt (input.readLine ());

				destino = new Coordinates (destinox, destinoy);
			} while (! play (origen, destino));
		} catch (Exception e)
		{
			e.printStackTrace ();
		}

	}

	/**
	 * Función privada que mantiene la lógica de la secuencia.
	 * 
	 * play() detecta, en función de las coordenadas origen y destino, y el estado del turno del
	 * jugador, qué tipo de movimiento desea realizar el jugador. Si el jugador selecciona una
	 * casilla donde reside un personaje enemigo o entidad neutral, entenderá que desea atacar. Por
	 * contra, si se selecciona una casilla vacía, entenderá que desea realizar un movimiento.
	 * 
	 * @param origen Coordenadas origen.
	 * @param destino Coordenadas destino.
	 * @return Un {@link InternalStatus} indicando si la acción se llevó a cabo correctamente (MOVE,
	 *         ATTACK o FINISHED) o no (INVALID).
	 */
	private boolean play (Coordinates origen, Coordinates destino)
	{
		/* Si se introducen coordenadas fuera de rango, supondremos que se quiere pasar del turno. */
		if (origen.x < 0 || origen.y < 0 || destino.x < 0 || destino.y < 0)
		{
			System.out.println ("[PLAYER] Coordenadas fuera de rango: ignorando fase.");
			return true;
		}

		Class<? extends GameObject> objetivo = controlador.what (destino);

		/* Comprobar movimiento. */
		if (objetivo == CharacterNull.class && !controlador.moved (this))
		{
			System.out.println ("[PLAYER] Fase de movimiento.");
			System.out.println ("[PLAYER] Objetivo: " + objetivo.getSimpleName ());
			
			Response response = controlador.move (origen, destino, this);
			
			/* Comprobamos que, en efecto, el controlador ha movido. */
			if (response != Response.OK)
			{
				System.out.println ("[PLAYER] Selecciona otra celda.");
				return false;
			}
			
			return true;
		}
		
		/* Comprobar ataque. */
		if (enemy ().isAssignableFrom (objetivo) && !controlador.attacked (this))
		{
			System.out.println ("[PLAYER] Fase de ataque.");
			System.out.println ("[PLAYER] Objetivo: " + objetivo.getSimpleName ());
			Response response = controlador.attack (origen, destino, this);
			
			/* Si no se ha producido el ataque, pedimos repetición. */
			if (response != Response.OK)
			{
				System.out.println ("[PLAYER] Selecciona otra celda.");
				return false;
			}
			
			return true;
		}
		
		/* Si es un objeto, lo cogemos. */
		/* TODO Implementar objetos. */
		
		/* Nos hemos visto en un follón que no sabemos ni de dónde ha venío... */
		return false;
	}
}
