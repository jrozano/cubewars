package com.cubewars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Game;
import com.cubewars.characters.Character;
import com.cubewars.characters.CharacterNull;
import com.cubewars.characters.Cube;
import com.cubewars.characters.Triangle;
import com.cubewars.maps.Map;
import com.cubewars.players.ConsolePlayer;
import com.cubewars.players.Player;
import com.cubewars.players.Team;

/**
 * Controlador de Juego.
 * 
 * El controlador del juego se encarga de realizar las operaciones elementales de control de las
 * estructuras del juego y las reglas de juego.
 * 
 * @author pyrosphere3
 */
public class GameController extends Game
{
	private List<GameObject> elementos;
	private List<Player> jugadores;
	public Map mapa;
	private TurnController turnos;

	@Override
	public void create ()
	{
		System.out.println ("[CNTROL] Inicio de juego.");

		/* Construir Controlador. */
		elementos = new ArrayList<GameObject> ();
		turnos = new TurnController ();
		mapa = new Map ();
		ConsolePlayer cubos = new ConsolePlayer (this, Cube.class);
		ConsolePlayer triangulos = new ConsolePlayer (this, Triangle.class);
		
		/*
		 * TODO Construir controladores auxiliares (PlayerController, NetworkController,
		 * SoundController, etc.)
		 */
		
		while (status () != Response.FINISHED)
		{
			mapa.print ();
			turnos.newTurn (cubos);
			cubos.turn ();
			
			mapa.print ();
			turnos.newTurn (triangulos);
			triangulos.turn ();
		}
		
		System.out.println ("[CNTROL] Ganador: ");
		System.out.println ("[CNTROL] Fin de Juego.");
		System.exit (0);
	}

	public Class<? extends GameObject> what (Coordinates c)
	{
		return mapa.get (c).getClass ();
	}

	/**
	 * Muestra información sobre una celda.
	 * 
	 * Devuelve un {@link Response} indicando si en la celda hay un aliado, un enemigo, un
	 * objeto, está vacía, etc.
	 * 
	 * @param cell Celda de la que se desea obtener información.
	 * @param player Jugador que solicita información.
	 * @return
	 */
//	public Response select (Coordinates cell, Player player)
//	{
//		GameObject seleccionado = mapa.get (cell);
//
//		if (seleccionado instanceof Cube)
//		{
//			if (player.team () == Cube.class)
//				return Response.PLAYER;
//			else
//				return Response.ATTACK;
//		}
//
//		if (seleccionado instanceof Triangle)
//		{
//			if (player.team () == Triangle.class)
//				return Response.PLAYER;
//			else
//				return Response.ATTACK;
//		}
//
//		return Response.INVALID;
//	}

	/**
	 * Realiza un movimiento en el tablero de juego.
	 * 
	 * Mueve la entidad entre dos coordenadas, origen y destino, y actualiza su posición en pantalla
	 * siempre y cuando la entidad que realiza el movimiento pueda ser trasladada a su nueva
	 * ubicación (en el caso de un {@link Character}, comprobando si la distancia que separa origen
	 * y destino es igual o menos que la distancia que el personaje puede viajar en un turno).
	 * 
	 * @param source Coordenada origen.
	 * @param destination Coordenada destino.
	 * @return Una {@link Response} indicando si el movimiento se realizó
	 *         satisfactoriamente (OK) o no (VOID).
	 * @see Coodinates#distance
	 */
	public Response move (Coordinates source, Coordinates destination, Player player)
	{
		/* Si el jugador puede moverse... */
		if (turnos.canMove (player))
		{
			/*
			 * Si el personaje puede ir de su situación actual a las proporcionadas, moverlo, en caso
			 * contrario, excepción. Consideramos que sólo los personajes se pueden mover.
			 */
			Character personaje = (Character) mapa.get (source);
	
			if (Character.class.isAssignableFrom (what (source)) && source.distance (destination) <= personaje.getTravel ())
			{
				System.out.println ("[CNTROL] Moviendo " + personaje.toString () + ": " + source.toString () + " -> " + destination.toString () + ".");
				
				mapa.move (source, destination);
				turnos.move (player);
				
				personaje.area.x = destination.toPixel ().x;
				personaje.area.y = destination.toPixel ().y;
				
				return Response.OK;
			} 
			else
			{
				System.out.println ("[CNTROL] No se puede mover " + personaje.toString () + ".");
				throw new RuntimeException ("[CNTROL] No se puede mover " + personaje.toString () + ".");
			}
		}

		System.out.println ("[CNTROL] " + source.toString () + " no puede moverse a " + destination.toString ());
		return Response.INVALID;
	}
	
	public boolean moved (Player player)
	{
		return !turnos.canMove (player);
	}
	
	public boolean attacked (Player player)
	{
		return !turnos.canAttack (player);
	}

	/**
	 * Realiza una orden de ataque a una entidad en el tablero.
	 * 
	 * Localiza las entidades atacante y objetivo que se encuentran en las coordenadas
	 * proporcionadas y resta puntos de salud al objetivo, siempre y cuando el atacante se encuentre
	 * en rango de ataque.
	 * 
	 * @param source Coordenadas del personaje atacante.
	 * @param destination Coordenadas del personaje o entidad objetivo.
	 * @return Una {@link Response} indicando si el ataque se realizó satisfactoriamente
	 *         (OK) o no (VOID).
	 */
	public Response attack (Coordinates source, Coordinates destination, Player player)
	{
		/* Si el jugador puede atacar... */
		if (turnos.canAttack (player))
		{
			Character atacante = (Character) mapa.get (source);
			Character objetivo = (Character) mapa.get (destination);
	
			/* Comprobamos que la distancia de ataque sea inferior al rango del personaje. */
			if (source.distance (destination) > atacante.getAttackDistance ())
			{
				System.out.println ("[CNTROL] " + atacante.toString () + " está demasiado lejos de "
						+ destination.toString ());
				return Response.INVALID;
			}
	
			/* Si el objetivo se trata de un Character, lo atacamos. */
			if (Character.class.isAssignableFrom (objetivo.getClass ()))
			{
				System.out.println ("[CNTROL] " + atacante.toString () + " " + source.toString () + " atacando a " + objetivo.toString() + " "
						+ destination.toString () + " con " + atacante.getDamage () + " daño.");
	
				objetivo.addDamage (atacante.getDamage ());
				turnos.attack (player);
	
				/* Comprobar si muerto. */
				System.out.println ("[CNTROL] Salud restante: " + objetivo.getHealth ());
				if (objetivo.getHealth () <= 0)
					mapa.destroy (destination);
	
				return Response.OK;
			}
		}

		/* TODO Implementar ataque a entorno. */

		System.out.println ("[CNTROL] " + source.toString () + " no puede atacar a " + destination.toString ());
		return Response.INVALID;
	}

	/**
	 * Devuelve el estado actual del juego.
	 * 
	 * Comprueba si alguno de los dos jugadores ha ganado (ha eliminado a todos los personajes del
	 * equipo rival).
	 * 
	 * @return Una {@link Response} indicando si el juego ha finalizado (FINISHED) o no
	 *         (ACTIVE).
	 */
	public Response status ()
	{
		return Response.ACTIVE;
	}

	/**
	 * Añade un objeto al contenedor de elementos de pantalla del controlador.
	 * 
	 * TODO Deberíamos buscar la forma de hacer que esta función sólo sea accesible desde
	 * ScreenController. ¿Declararla como package?.
	 * 
	 * @param g El objeto a insertar en el contenedor.
	 */
	public void add (GameObject g)
	{
		this.elementos.add (g);
		Collections.sort (this.elementos);
	}

	/**
	 * Proporciona acceso al contenedor de elementos de pantalla del controlador.
	 * 
	 * TODO Deberíamos buscar la forma de hacer que esta función sólo sea accesible desde
	 * ScreenController. ¿Declararla como package?.
	 * 
	 * @return El contenedor de elementos.
	 */
	public List<GameObject> getDrawingContainer ()
	{
		return elementos;
	}
	

	/**
	 * Devuelve el {@link GameObject} que se encuentra en el {@lik Pixel} específico.
	 * 
	 * No debería usarse el código real, sólo para pruebas y prototipos, de forma que las clases
	 * Player no reciban ninguna referencia a objetos del juego, y se guien simplemente mediante
	 * {@link Response}. Será eliminada en un futuro.
	 * 
	 * @deprecated
	 * @param p Pixel de la celda donde se encuentra el objeto a obtener.
	 * @param priority Capa donde se debe buscar el objeto.
	 * @return Referencia a objeto de juego que ocupa la posición indicada.
	 */
	public GameObject getElementOn (Pixel p, int priority)
	{
		for (GameObject g : elementos)
		{
			if (g.getPriority () > priority && g.area.contains (p.x, p.y))
				return g;
		}

		return new CharacterNull ();
	}

	/**
	 * Devuelve el {@link GameObject} que se encuentra en las coordenadas especificaas.
	 * 
	 * No debería usarse el código real, sólo para pruebas y prototipos, de forma que las clases
	 * Player no reciban ninguna referencia a objetos del juego, y se guien simplemente mediante
	 * {@link Response}. Será eliminada en un futuro.
	 * 
	 * @deprecated
	 * @param c Coordenadas de la celda donde se encuentra el objeto a obtener.
	 * @param priority Capa donde se debe buscar el objeto.
	 * @return Referencia a objeto de juego que ocupa la posición indicada.
	 */
	public GameObject getElementOn (Coordinates c, int priority)
	{
		Pixel p;

		for (GameObject g : elementos)
		{
			p = new Pixel (c);

			if (g.getPriority () > priority && g.area.contains (p.x, p.y))
				return g;
		}

		return new CharacterNull ();
	}

	/**
	 * Obtiene las coordenadas donde se sitúa un gameObject.
	 * 
	 * No debería usarse el código real, sólo para pruebas y prototipos, de forma que las clases
	 * Player no reciban ninguna referencia a objetos del juego, y se guien simplemente mediante
	 * {@link Response}. Será eliminada en un futuro.
	 * @deprecated
	 * @param g El objeto.
	 * @return Un objeto Coordinates con la celda donde g está situado.
	 */
	public Coordinates getCoordinates (GameObject g)
	{
		return mapa.search (g);
	}

	/**
	 * Devuelve el {@link GameObject} que se encuentra en las coordenadas especificaas.
	 * 
	 * No debería usarse el código real, sólo para pruebas y prototipos, de forma que las clases
	 * Player no reciban ninguna referencia a objetos del juego, y se guien simplemente mediante
	 * {@link Response}. Será eliminada en un futuro.
	 * 
	 * @deprecated
	 * @param c Coordenadas de la celda donde se encuentra el objeto a obtener.
	 * @return Referencia a objeto de juego que ocupa la posición indicada.
	 */
	public GameObject getGridObject (Coordinates c)
	{
		return mapa.get (c);
	}


	@Override
	public void dispose ()
	{
		super.dispose ();
	}

	@Override
	public void render ()
	{
		super.render ();
	}

	@Override
	public void resize (int width, int height)
	{
		super.resize (width, height);
	}

	@Override
	public void pause ()
	{
		super.pause ();
	}

	@Override
	public void resume ()
	{
		super.resume ();
	}

	public void update ()
	{

	}
}
