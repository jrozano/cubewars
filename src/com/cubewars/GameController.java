package com.cubewars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Game;
import com.cubewars.characters.Character;
import com.cubewars.characters.Cube;
import com.cubewars.characters.Triangle;
import com.cubewars.maps.Map;
import com.cubewars.players.ConsolePlayer;
import com.cubewars.players.Player;

/**
 * The game controller.
 * 
 * The game controller implements the game rules and actions. It keeps track of the player turns and
 * actions so they are not able to repeat movements in the same turn.
 * 
 * @author pyrosphere3
 */
public class GameController extends Game
{
	private List<GameObject> screenItems;
	private List<Player> playerList;
	public Map map;
	private TurnController turns;

	@Override
	public void create ()
	{
		System.out.println ("[CNTROL] Game Start.");

		/* Create Controller internal attributes. */
		screenItems = new ArrayList<GameObject> ();
		turns = new TurnController ();
		map = new Map ();
		ConsolePlayer cubes = new ConsolePlayer (this, Cube.class);
		ConsolePlayer triangles = new ConsolePlayer (this, Triangle.class);

		/*
		 * TODO Create additional Controllers (Sound, Network, etc.)
		 */

		while (status () != Response.CUBEVICTORY && status () != Response.TRIANGLEVICTORY)
		{
			map.print ();
			turns.newTurn (cubes);
			cubes.turn ();
			
			if(status () != Response.CUBEVICTORY && status () != Response.TRIANGLEVICTORY){
				map.print ();
				turns.newTurn (triangles);
				triangles.turn ();
			}
			
		}
		
		if(status () == Response.CUBEVICTORY){
			System.out.println ("[CNTROL] Winner: " + "Cubes" );
		}
		
		if(status () == Response.TRIANGLEVICTORY){
			System.out.println("[CNTROL] Winner: " + "Cubes" );
		}
		
		System.out.println ("[CNTROL] Game End.");
		System.exit (0);
	}

	/**
	 * Gets information about the given Coordinates.
	 * 
	 * {@link #select(Coordinates)} can be used to obtain the class of the current game entity
	 * placed in a given cell without exposing the actual GameObject.
	 * 
	 * @param c The grid {@link Coordinates} to the cell.
	 * @return The entity's Class.
	 */
	public Class<? extends GameObject> select (Coordinates c)
	{
		return map.get (c).getClass ();
	}

	/**
	 * Issues a character movement order in the game grid.
	 * 
	 * Places the entity placed in origin in the destination and updates its current screen
	 * position, provided that the path is not longer than the character's travel distance.
	 * 
	 * @param source Source {@link Coordinates}
	 * @param destination Destination {@link Coordinates}
	 * @return A {@link Response} telling whether the movement has completed successfully (OK) or
	 *         not (INVALID).
	 * @see Coodinates#distance
	 */
	public Response move (Coordinates source, Coordinates destination, Player player)
	{
		/* Check if the player has permission to move... */
		if (turns.canMove (player))
		{
			/*
			 * If our character can travel to the destination position, do it. We'll assume that
			 * only objects derived from the Character class can be moved across the board.
			 */
			Character character = (Character) map.get (source);

			if (Character.class.isAssignableFrom (select (source)))
			{
				/* We check again if the selected character is in the player's team */
				if(player.team().isAssignableFrom (character.getClass())){
					if (source.distance (destination) <= character.getTravel ())
					{
						System.out.println ("[CNTROL] Moving " + character.toString () + ". Distance: "
								+ source.distance (destination) + ", máx: " + character.getTravel () + ".");

						map.move (source, destination);
						turns.move (player);

						character.area.x = destination.toPixel ().x;
						character.area.y = destination.toPixel ().y;

						return Response.OK;
					} else
						System.out.println ("[CNTROL] Cannot move: too far away.");
				}
				else{
					System.out.println ("[CNTROL] Wrong team " + player.team().toString () + "." + character.getClass());
				}
			} else
			{
				System.out.println ("[CNTROL] Cannot move " + character.toString () + ".");
				// throw new RuntimeException ("[CNTROL] Cannot move " + personaje.toString () +
				// ".");
			}
		} else
			System.out.println ("[CNTROL] Cannot move: you have already made a move.");

		System.out.println ("[CNTROL] " + source.toString () + " cannot move to " + destination.toString ());
		return Response.INVALID;
	}

	/**
	 * Checks if a player has made a Movement in his current turn.
	 * 
	 * @param player The player.
	 * @return True if the player has already made a move, false in other case.
	 */
	public boolean moved (Player player)
	{
		return !turns.canMove (player);
	}

	/**
	 * Checks if a player has made an Attack in his current turn.
	 * 
	 * @param player The player.
	 * @return True if the player has already made an attack, false in other case.
	 */
	public boolean attacked (Player player)
	{
		return !turns.canAttack (player);
	}

	/**
	 * Issues a character attack order to another entity.
	 * 
	 * If the objective is inside our attack radius, deal points of damage. This method takes care
	 * of character deletion when its health decreases below 0, and should work with characters and
	 * game objects.
	 * 
	 * @param source Attacker's coordinates.
	 * @param destination Objective's coordinates.
	 * @return A {@link Response} telling whether the attack has completed successfully (OK) or not
	 *         (INVALID).
	 */
	public Response attack (Coordinates source, Coordinates destination, Player player)
	{
		/* Check if the player has permission to attack... */
		if (turns.canAttack (player))
		{
			Character attacker = (Character) map.get (source);
			Character objective = (Character) map.get (destination);

			/* Check attack distance. */
			if (source.distance (destination) > attacker.getAttackDistance ())
			{
				System.out.println ("[CNTROL] " + attacker.toString () + " is too far from " + destination.toString ());
				return Response.INVALID;
			}
			
			/* We check again if the attacker character is in the player's team */
			if(!player.team().isAssignableFrom (attacker.getClass())){
				System.out.println ("[CNTROL] " + attacker.toString () + " is not in player's team " + player.team().toString ());
				return Response.INVALID;
			}

			/* Attack it. */
			if (objective instanceof Character)
			{
				System.out.println ("[CNTROL] " + attacker.toString () + " " + source.toString () + " attacking "
						+ objective.toString () + " " + destination.toString () + " with " + attacker.getDamage ()
						+ " damage.");

				objective.addDamage (attacker.getDamage ());
				turns.attack (player);

				/* Death check. */
				System.out.println ("[CNTROL] Health left: " + objective.getHealth ());
				if (objective.getHealth () <= 0)
					map.destroy (destination);

				return Response.OK;
			}
		}

		/* TODO Implement environment's attacks. */

		System.out.println ("[CNTROL] " + source.toString () + " cannot attack " + destination.toString ());
		return Response.INVALID;
	}

	/**
	 * Returns game current status
	 * 
	 * Checks game end condition: whether one of the players has won the game or not.
	 * 
	 * @return A {@link Response} telling if the game has ended (FINISHED) or not (ACTIVE).
	 */
	public Response status ()
	{
		boolean cube=false, triangle=false;
		/* We check if there is any triangle or any cube in the game, if it is not then the game is finished */
		for(int i=0; i!=map.size();i++){
			
			for(int j=0; j!=map.size();j++){
				
				if(Cube.class.isAssignableFrom (map.get(new Coordinates(i,j)).getClass())){
					cube=true;
				}
				
				if(Triangle.class.isAssignableFrom (map.get(new Coordinates(i,j)).getClass())){
					triangle=true;
				}
			}
		
		}
		
		if(triangle==false){
			return Response.CUBEVICTORY;
		}
		
		if(cube==false){
			return Response.TRIANGLEVICTORY;
		}
		
		return Response.ACTIVE;
	}

	/**
	 * Adds a new drawable object to the drawing container.
	 * 
	 * @param g The object.
	 */
	public void add (GameObject g)
	{
		this.screenItems.add (g);
		Collections.sort (this.screenItems);
	}

	/**
	 * Grants direct access to the drawing container.
	 * 
	 * @return The drawing container.
	 */
	public List<GameObject> getDrawingContainer ()
	{
		return screenItems;
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
