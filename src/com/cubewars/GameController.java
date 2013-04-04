package com.cubewars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Game;
import com.cubewars.characters.Character;
import com.cubewars.characters.CharacterNull;
import com.cubewars.characters.Cube;
import com.cubewars.characters.CubeBoomer;
import com.cubewars.characters.CubeGunner;
import com.cubewars.characters.CubeSniper;
import com.cubewars.characters.Triangle;
import com.cubewars.characters.TriangleBoomer;
import com.cubewars.characters.TriangleGunner;
import com.cubewars.characters.TriangleSniper;
import com.cubewars.maps.Map;
import com.cubewars.players.ConsolePlayer;
import com.cubewars.players.LocalPlayer;
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
	private HashMap<Player, Double> money; /* The money balance of each player: */
	private HashMap<Class<? extends Character>, Double> prices; /* How much each character costs: */
	public Map map;
	private TurnController turns;

	
	public LocalPlayer cubes;
	public Player triangles;
	public ScreenController gamescreen;

	@Override
	public void create ()
	{
		System.out.println ("[CNTROL] Game Start.");

		restart();

		/* Show screen. */
		gamescreen = new ScreenController (this);
		setScreen (gamescreen);

		/*
		 * TODO Create additional Controllers (Sound, Network, etc.)
		 */
	}
	
	public void restart(){
		/* Create Controller internal attributes. */
		screenItems = new ArrayList<GameObject> ();
		turns = new TurnController ();
		map = new Map ();
		money = new HashMap<Player, Double> ();
		prices = new HashMap<Class<? extends Character>, Double> ();

		/* Populate the character cost map: */
		prices.put (CubeGunner.class, 100.0);
		prices.put (CubeBoomer.class, 300.0);
		prices.put (CubeSniper.class, 500.0);
		prices.put (TriangleGunner.class, 100.0);
		prices.put (TriangleBoomer.class, 300.0);
		prices.put (TriangleSniper.class, 500.0);

		/* Create players. */
		cubes = new LocalPlayer (this, Cube.class);
		triangles = new LocalPlayer (this, Triangle.class);

		/* Give players an initial amount of credits. */
		money.put (cubes, 1000.0);
		money.put (triangles, 1000.0);
		
		/* Add some test entities. */
		Coordinates c;
		c = new Coordinates (1, 1);
		addEntity (new CubeSniper (c), c);

		c = new Coordinates (4, 4);
		addEntity (new TriangleSniper (c), c);

		c = new Coordinates (3, 2);
		addEntity (new CubeBoomer (c), c);

		/*c = new Coordinates (4, 3);
		addEntity (new TriangleBoomer (c), c);*/

		screenItems.add (new Background ("grid.png"));
		Collections.sort (screenItems);
		/* End test entities. */

		turns.newTurn (cubes);
		cubes.turn ();
	}

	public void tick ()
	{
		if (turns.finishedTurn ())
		{
			if (turns.currentPlayer() == cubes && status () != Response.CUBEVICTORY && status () != Response.TRIANGLEVICTORY)
			{
				turns.newTurn (triangles);
				triangles.turn ();
			} else if (status () != Response.CUBEVICTORY && status () != Response.TRIANGLEVICTORY)
			{
				turns.newTurn (cubes);
				cubes.turn ();
			}

			if (status () == Response.CUBEVICTORY){
				System.out.println ("[CNTROL] Winner: Cubes.");
				restart();
			}

			if (status () == Response.TRIANGLEVICTORY){
				System.out.println ("[CNTROL] Winner: Triangles.");
				restart();
			}
		}
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
			/* If any coordinate is out of range, we'll assume the player wants to skip this stage. */
			if (source.x < 0 || source.y < 0 || destination.x < 0 || destination.y < 0)
			{
				turns.move (player);
				return Response.OK;
			}
			/*
			 * If our character can travel to the destination position, do it. We'll assume that
			 * only objects derived from the Character class can be moved across the board.
			 */
			Character character = (Character) map.get (source);

			if (Character.class.isAssignableFrom (select (source)))
			{
				/* We check again if the selected character is in the player's team */
				if (player.team ().isAssignableFrom (character.getClass ()))
				{
					if (source.distance (destination) <= character.getTravel ())
					{
						System.out.println ("[CNTROL] Moving " + character.toString () + ". Distance: " + source.distance (destination) + ", máx: "
								+ character.getTravel () + ".");

						map.move (source, destination);
						turns.move (player);

						character.area.x = destination.toPixel ().x;
						character.area.y = destination.toPixel ().y;

						return Response.OK;
					} else
						System.out.println ("[CNTROL] Cannot move: too far away.");
				} else
				{
					System.out.println ("[CNTROL] move: Wrong team " + player.team () + "." + character.getClass ().getSimpleName ());
				}
			} else
			{
				System.out.println ("[CNTROL] Cannot move " + character.toString () + ".");
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
			if (!player.team ().isAssignableFrom (attacker.getClass ()))
			{
				System.out.println ("[CNTROL] " + attacker.toString () + " is not in player's team " + player.team ().toString ());
				return Response.INVALID;
			}

			/* Attack it. */
			if (objective instanceof Character)
			{
				System.out.println ("[CNTROL] " + attacker.toString () + " " + source.toString () + " attacking " + objective.toString () + " "
						+ destination.toString () + " with " + attacker.getDamage () + " damage.");

				objective.addDamage (attacker.getDamage ());
				turns.attack (player);

				/* Death check. */
				System.out.println ("[CNTROL] Health left: " + objective.getHealth ());
				if (objective.getHealth () <= 0)
				{
					killEntity (destination);
				}

				return Response.OK;
			}
			/* Check boomer area's attack. */
			if((attacker instanceof CubeBoomer || attacker instanceof TriangleBoomer) && objective instanceof CharacterNull){
				/* We create the four near coordinates */
				System.out.println("Entra");
				Coordinates Up = new Coordinates (destination.x(),destination.y()+1);
				Coordinates Down = new Coordinates (destination.x(),destination.y()-1);
				Coordinates Right = new Coordinates (destination.x()+1,destination.y());
				Coordinates Left = new Coordinates (destination.x()-1,destination.y());
				
				/* First we must check if the coordinates are correct */
				if(Up.x>=0 && Up.x <=10 && Up.y>=0 && Up.y<=10){
					objective = (Character) map.get (Up);
					if(BoomerAttack(objective,attacker,source,Up)){
						turns.attack(player);
						return Response.OK;
					}
				}
				
				if(Down.x>=0 && Down.x<=10 && Down.y>=0 && Down.y<=10){
					objective = (Character) map.get (Down);
					if(BoomerAttack(objective,attacker,source,Down)){
						turns.attack(player);
						return Response.OK;
					}
				}
				
				if(Right.x>=0 && Right.x<=10 && Right.y>=0 && Right.y <=10){
					objective = (Character) map.get (Right);
					if(BoomerAttack(objective,attacker,source,Right)){
						turns.attack(player);
						return Response.OK;
					}
				}
				
				if(Left.x>=0 && Left.x<=10 && Left.y>=0 && Left.y<=10){
					objective = (Character) map.get (Left);
					if(BoomerAttack(objective,attacker,source,Left)){
						turns.attack(player);
						return Response.OK;
					}
				}	
			}
		}

		/* TODO Implement environment's attacks. */

		System.out.println ("[CNTROL] " + source.toString () + " cannot attack " + destination.toString ());
		return Response.INVALID;
	}
	/**
	 * This method is an internal function that help to check the boomer area attack.
	 * @param objective
	 * @param attacker
	 * @param source
	 * @param destination
	 * @return true or false depends if the attack is succesful or not
	 */
	private boolean BoomerAttack(Character objective, Character attacker, Coordinates source, Coordinates destination){
		if(objective instanceof Character){
			System.out.println ("[CNTROL] " + attacker.toString () + " " + source.toString () + " attacking " + objective.toString () + " "
					+ destination.toString () + " with " + attacker.getDamage ()/4 + " damage.");
			objective.addDamage (attacker.getDamage ()/4);

			/* Death check. */
			System.out.println ("[CNTROL] Health left: " + objective.getHealth ());
			if (objective.getHealth () <= 0)
			{
				killEntity (destination);
			}
			return true;
		}
		return false;
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
		boolean cube = false, triangle = false;
		/*
		 * We check if there is any triangle or any cube in the game, if it is not then the game is
		 * finished
		 */
		for (int i = 0; i != map.width; i++)
		{

			for (int j = 0; j != map.height; j++)
			{

				if (Cube.class.isAssignableFrom (map.get (new Coordinates (i, j)).getClass ()))
				{
					cube = true;
				}

				if (Triangle.class.isAssignableFrom (map.get (new Coordinates (i, j)).getClass ()))
				{
					triangle = true;
				}
			}

		}

		if (triangle == false)
		{
			return Response.CUBEVICTORY;
		}

		if (cube == false)
		{
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

	/**
	 * Returns the current amount of credits the player has.
	 * 
	 * @param player The player.
	 * @return A double with the amount of credits.
	 */
	public double getMoney (Player player)
	{
		return this.money.get (player);
	}

	/**
	 * Increments a player's amount of credits.
	 * 
	 * @param player The player.
	 * @param amount A number of credits greater than 0.
	 */
	public void addMoney (Player player, double amount)
	{
		if (amount > 0)
		{
			this.money.put (player, amount);
			System.out.println ("[CNTROL] Current balance: " + getMoney (player) + "c.");
		}
	}

	/**
	 * Decrements a player's amount of credits.
	 * 
	 * Only decreases the amount of credits if the value to subtract is less or equal than the
	 * current amount, since no player can have less that 0 credits.
	 * 
	 * @param player The player.
	 * @param amount A number of credits greater than his current balance.
	 */
	public Response takeMoney (Player player, double amount)
	{
		if ((getMoney (player) - amount) > 0.0)
		{
			this.money.put (player, getMoney (player) - amount);
			System.out.println ("[CNTROL] Current balance: " + getMoney (player) + "c.");
			return Response.OK;
		} else
		{
			System.out.println ("[CNTROL] Not enough credits.");
			return Response.INVALID;
		}
	}

	public Response buyCharacter (Player player, Class<? extends Character> type)
	{
		try
		{
			if (takeMoney (player, prices.get (type)) == Response.OK)
			{
				/* Check for an empty cell in the player's side of the map. */
				if (player.team () == Cube.class)
				{
					/* Check upper side. */
					for (int i = 0; i != 5; ++i)
					{
						Coordinates c = new Coordinates (0, i);

						if (map.get (c) instanceof CharacterNull)
						{
							Character newCharacter;

							/*
							 * TODO I should try java.lang.reflect to dynamically instantiate
							 * classes.
							 */
							if (type == CubeGunner.class)
							{
								newCharacter = new CubeGunner (c);
							} else if (type == CubeBoomer.class)
								newCharacter = new CubeBoomer (c);
							else if (type == CubeSniper.class)
								newCharacter = new CubeSniper (c);
							else
							{
								System.out.println ("[CNTROL] Error: class name not valid. " + type.toString ());
								return Response.ERROR;
							}

							System.out.println ("[CNTROL] Added new " + newCharacter.toString () + " " + c.toString ());
							addEntity (newCharacter, c);
							break;
						}
					}
				}

				if (player.team () == Triangle.class)
				{
					/* Check lower side. */
					for (int i = 4; i >= 0; --i)
					{
						Coordinates c = new Coordinates (4, i);

						if (map.get (c) instanceof CharacterNull)
						{
							Character newCharacter;

							/*
							 * TODO I should try java.lang.reflect to dynamically instantiate
							 * classes.
							 */
							if (type == TriangleGunner.class)
								newCharacter = new TriangleGunner (c);
							else if (type == TriangleBoomer.class)
								newCharacter = new TriangleBoomer (c);
							else if (type == TriangleSniper.class)
								newCharacter = new TriangleSniper (c);
							else
							{
								System.out.println ("[CNTROL] Error: class name not valid. " + type.toString ());
								return Response.ERROR;
							}

							System.out.println ("[CNTROL] Added new " + newCharacter.toString () + " " + c.toString ());
							addEntity (newCharacter, c);
							break;
						}
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace ();
		}

		return Response.INVALID;
	}

	public double getPrice (Class<? extends Character> item)
	{
		return prices.get (item);
	}

	private void addEntity (GameObject g, Coordinates c)
	{
		// if (c.x < 0 || c.x > map.width || c.y < 0 || c.y > map.height)
		// throw new RuntimeException ("Coordinates out of bounds.");

		map.grid[c.x][c.y] = g;
		screenItems.add (g);
		Collections.sort (screenItems);
	}

	private void killEntity (Coordinates c)
	{
		GameObject g = map.get (c);
		map.destroy (c);
		screenItems.remove (g);
	}

	public void skipTurn (Player p)
	{
		if(p==cubes){
			turns.newTurn(triangles);
			triangles.turn();
		}
		if(p==triangles){
			turns.newTurn(cubes);
			cubes.turn();
		}
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
