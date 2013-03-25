package com.cubewars.players;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.cubewars.ControllerResponse;
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
	private InternalStatus estadoAnterior;
	private Class<? extends GameObject> seleccionado;
	BufferedReader input = new BufferedReader (new InputStreamReader (System.in));

	public ConsolePlayer (GameController controlador, Class<? extends Character> equipo)
	{
		super (controlador, equipo);
		estadoAnterior = InternalStatus.READY;
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
			estadoAnterior = InternalStatus.READY;

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

			} while (!Cube.class.isAssignableFrom (seleccionado));

			controlador.mapa.print ();

			/* Primera de las dos fases de las que se compone nuestro turno. */
			do
			{
				System.out.println ("[PLAYER] Seleccionado " + seleccionado.toString ());
				System.out.println ("[PLAYER] Primera fase, selecciona objetivo:");

				controlador.mapa.print ();

				System.out.print ("x > ");
				destinox = Integer.parseInt (input.readLine ());

				System.out.print ("y > ");
				destinoy = Integer.parseInt (input.readLine ());

				destino = new Coordinates (destinox, destinoy);

			} while (play (origen, destino) == InternalStatus.INVALID);

			/*
			 * Segunda fase. Hay que tener en cuenta que si en la primera fase nos movimos, "origen"
			 * ahora apunta a las coordenadas antiguas, por lo que hay que actualizarlas.
			 */
			if (estadoAnterior == InternalStatus.MOVE)
				origen = destino;
			
			do
			{
				System.out.println ("[PLAYER] Seleccionado " + seleccionado.toString ());
				System.out.println ("[PLAYER] Segunda fase, selecciona objetivo:");

				controlador.mapa.print ();

				System.out.print ("x > ");
				destinox = Integer.parseInt (input.readLine ());

				System.out.print ("y > ");
				destinoy = Integer.parseInt (input.readLine ());

				destino = new Coordinates (destinox, destinoy);
			} while (play (origen, destino) == InternalStatus.INVALID);
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
	private InternalStatus play (Coordinates origen, Coordinates destino)
	{
		/* Si se introducen coordenadas fuera de rango, supondremos que se quiere pasar del turno. */
		if (origen.x < 0 || origen.y < 0 || destino.x < 0 || destino.y < 0)
		{
			System.out.println ("[PLAYER] Coordenadas fuera de rango: ignorando fase.");
			return InternalStatus.FINISHED;
		}

		ControllerResponse response = controlador.select (destino, this);
		Class<? extends GameObject> objetivo = controlador.what (destino);

		/*
		 * Fase de ataque/movimiento. Aquí sólo entraremos si se dan las siguientes condiciones:
		 * 
		 * - Nuestro estado anterior era el inicial o de ataque (es decir, no hemos movido el
		 * personaje y no ha acabado nuestro turno).
		 * 
		 * - La celda objetivo está vacía (objeto CharacterNull).
		 * 
		 */

		if (objetivo == CharacterNull.class
				&& (estadoAnterior == InternalStatus.ATTACK || estadoAnterior == InternalStatus.READY))
		{
			System.out.println ("[PLAYER] Fase de movimiento.");
			System.out.println ("[DEBUG] Objetivo: " + objetivo.toString ());
			System.out.println ("[PLAYER] Moviendo " + seleccionado.toString () + " a " + destino.toString ());

			response = controlador.move (origen, destino, this);

			/* Comprobamos que, en efecto, el controlador ha movido. */
			if (response != ControllerResponse.OK)
			{
				System.out.println ("[PLAYER] Selecciona otra celda.");
				return InternalStatus.INVALID;
			}

			/*
			 * Si venimos del estado de ataque, hemos completado nuestros dos movimientos.
			 * Terminamos el turno.
			 */
			if (estadoAnterior == InternalStatus.ATTACK)
			{
				System.out.println ("[PLAYER] Fin de turno.");
				estadoAnterior = InternalStatus.FINISHED;
				return InternalStatus.MOVE;
			}

			/*
			 * Si venimos del estado de inicio, es que aún no hemos atacado, cambiamos a estado de
			 * ataque.
			 */
			if (estadoAnterior == InternalStatus.READY)
			{
				System.out.println ("[PLAYER] Cambio a estado Movimiento.");
				estadoAnterior = InternalStatus.MOVE;
				return InternalStatus.MOVE;
			}
		}

		/* Si es un enemigo, atacamos. */
		/* FIXME Mejorar y generalizar la identificación de enemigos. */
		if ((estadoAnterior == InternalStatus.MOVE || estadoAnterior == InternalStatus.READY)
				&& Triangle.class.isAssignableFrom (objetivo))
		{
			System.out.println ("[PLAYER] " + seleccionado.toString () + " atacando a " + objetivo.toString ());

			response = controlador.attack (origen, destino, this);

			/* Si no se ha producido el ataque, pedimos repetición. */
			if (response == ControllerResponse.VOID)
			{
				System.out.println ("[PLAYER] Selecciona otra celda.");
				return InternalStatus.INVALID;
			}

			/*
			 * Si venimos del estado de movimiento, hemos completado nuestros dos movimientos.
			 * Terminamos el turno.
			 */
			if (estadoAnterior == InternalStatus.MOVE)
			{
				System.out.println ("[PLAYER] Fin de turno.");
				estadoAnterior = InternalStatus.FINISHED;
				return InternalStatus.ATTACK;
			}

			/*
			 * Si venimos del estado de inicio, es que aún no no hemos movido, cambiamos a estado
			 * MOVE.
			 */
			if (estadoAnterior == InternalStatus.READY)
			{
				System.out.println ("[PLAYER] Cambio de estado Ataque.");
				estadoAnterior = InternalStatus.MOVE;
				return InternalStatus.ATTACK;
			}
		}

		/* Si es un objeto, lo cogemos. */
		/* TODO Implementar objetos. */

		/* Nos hemos visto en un follón que no sabemos ni de dónde ha venío... */
		return InternalStatus.INVALID;
	}
}
