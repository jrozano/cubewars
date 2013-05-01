package com.cubewars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.cubewars.backgrounds.Environment;
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
	public List<GameObject> lifeBars;
	private List<Player> playerList;
	private HashMap<Player, Double> money; /* The money balance of each player: */
	private HashMap<Class<? extends Character>, Double> prices; /* How much each character costs: */
	private Set<Coordinates> highlightedMovement;
	private Set<Coordinates> highlightedAttack;
	private MapController map;
	private TurnController turns;
	public Player cubes;
	public Player triangles;
	public ScreenController gamescreen;

	@Override
	public void create ()
	{
		System.out.println ("[CNTROL] Game Start.");

		/* Show screen. */
		gamescreen = new ScreenController (this);
		setScreen (gamescreen);
		restart ();
	}

	/********************************************************************************
	 * 1. PUBLIC METHODS. *
	 ********************************************************************************/

	/********************************************************************************
	 * 1.1 GAME CONTROL. *
	 ********************************************************************************/

	/**
	 * Restarts a game.
	 * 
	 * Re-creates all GameController attributes to start a new game.
	 */
	public void restart ()
	{
		/* Create Controller internal attributes. */
		lifeBars = new ArrayList<GameObject> ();
		map = new MapController (this);
		money = new HashMap<Player, Double> ();
		prices = new HashMap<Class<? extends Character>, Double> ();
		playerList = new ArrayList<Player> ();
		highlightedMovement = null;
		highlightedAttack = null;

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
		playerList.add (cubes);
		playerList.add (triangles);
		turns = new TurnController (playerList);

		/* Give players an initial amount of credits. */
		money.put (cubes, 1000.0);
		money.put (triangles, 1000.0);
	}

	/**
	 * Checks the current game state every frame.
	 * 
	 * Thus method is called every frame by the ScreenController to check for a win condition or a
	 * player turn change. If the game has finished, it restarts it and prints on stdio the winner.
	 */
	public void tick ()
	{
		if (status () == Response.CUBEVICTORY)
		{
			System.out.println ("[CNTROL] Winner: Cubes.");
			restart ();
		}

		if (status () == Response.TRIANGLEVICTORY)
		{
			System.out.println ("[CNTROL] Winner: Triangles.");
			restart ();
		}

		/*
		 * Here we check if the player currently playing has finished his two moves. If he has,
		 * change turns.
		 */

		manageLifebars ();

		if (turns.finishedTurn ())
		{

			if (turns.currentPlayer () == cubes && status () != Response.CUBEVICTORY && status () != Response.TRIANGLEVICTORY)
			{
				turns.newTurn (triangles);
			} else if (status () != Response.CUBEVICTORY && status () != Response.TRIANGLEVICTORY)
			{
				turns.newTurn (cubes);
			}
		}
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
		for (int i = 0; i != map.width (); i++)
		{

			for (int j = 0; j != map.height (); j++)
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
			return Response.CUBEVICTORY;

		if (cube == false)
			return Response.TRIANGLEVICTORY;

		return Response.ACTIVE;
	}

	/********************************************************************************
	 * 1.2 SCREEN CONTROL. *
	 ********************************************************************************/

	/**
	 * Adds a new drawable object to the drawing container.
	 * 
	 * @param g The object.
	 */
	public void add (GameObject g)
	{
		this.lifeBars.add (g);
		Collections.sort (this.lifeBars);
	}

	/**
	 * Grants direct access to the drawing container.
	 * 
	 * @return The drawing container.
	 */
	public List<GameObject> getCharacterContainer ()
	{
		return map.getCharacters ();
	}

	public List<Environment> getTerrainContainer ()
	{
		return map.getTerrain ();
	}

	public void manageLifebars ()
	{
		// first we delete the bars of the enemies
		for (int i = 0; i < lifeBars.size (); i++)
		{
			GameObject g = lifeBars.get (i);
			if (Lifebar.class.isAssignableFrom (g.getClass ()))
			{
				lifeBars.remove (i);
			}
		}

		// then we checkout which player is the current player
		if (turns.currentPlayer () == cubes)
		{
			for (int i = 0; i < lifeBars.size (); i++)
			{
				GameObject g = lifeBars.get (i);
				if (Cube.class.isAssignableFrom (g.getClass ()))
				{
					Lifebar lb = new Lifebar (3, (int) g.area.x, (int) g.area.y);
					lb.setWidth (((Character) g).getHealth ());
					this.add (lb);
				}
			}
		} else if (turns.currentPlayer () == triangles)
		{
			for (int i = 0; i < lifeBars.size (); i++)
			{
				GameObject g = lifeBars.get (i);
				if (Triangle.class.isAssignableFrom (g.getClass ()))
				{
					Lifebar lb = new Lifebar (3, (int) g.area.x, (int) g.area.y);
					lb.setWidth (((Character) g).getHealth ());
					this.add (lb);
				}
			}
		}
	}

	public Set<Coordinates> getMoveHighlightArea ()
	{
		return highlightedMovement;
	}

	public Set<Coordinates> getAttackHighlightArea ()
	{
		return highlightedAttack;
	}

	public List<GameObject> getLifebarContainer ()
	{
		return lifeBars;
	}

	public void setAttackHighlightColor (Color c)
	{
		this.gamescreen.setAttackHighlightColor (c);
	}

	public void setMoveHighlightColor (Color c)
	{
		this.gamescreen.setMoveHighlightColor (c);
	}

	/********************************************************************************
	 * 1.3 PLAYER ACTIONS. *
	 ********************************************************************************/

	/**
	 * Gets information about the given Coordinates.
	 * 
	 * {@link #select(Coordinates)} can be used to obtain the class of the current game entity
	 * placed in a given cell without exposing the actual GameObject.
	 * 
	 * @param c The characters {@link Coordinates} to the cell.
	 * @return The entity's Class.
	 */
	public Class<? extends GameObject> select (Coordinates c)
	{
		return map.get (c).getClass ();
	}

	public void choose (Coordinates source, Player player)
	{
		Character character = (Character) map.get (source);
		int totalArea = 0;

		if (turns.canMove (player))
		{
			totalArea += character.getTravel ();
			highlightedMovement = getArea (source, character.getTravel ());
		}

		if (turns.canAttack (player))
		{
			totalArea += character.getAttackDistance ();
			highlightedAttack = getArea (source, totalArea);
		}
	}

	/**
	 * Issues a character movement order in the game characters.
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

						/* Clear highlightedMovement cells. */
						highlightedAttack = null;
						highlightedMovement = null;

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
			if (source.distance (destination) >= attacker.getAttackDistance ())
			{
				System.out.println ("[CNTROL] " + attacker.toString () + " is too far from " + destination.toString ());
				return Response.INVALID;
			}

			/* Check if it's not a Character Null */
			if (objective instanceof CharacterNull && (!(attacker instanceof CubeBoomer) && !(attacker instanceof TriangleBoomer)))
			{
				System.out.println ("[CNTROL] Selected terrain, only Boomers can attack at terrain.");
				return Response.INVALID;
			}

			/* We check again if the attacker character is in the player's team */
			if (!player.team ().isAssignableFrom (attacker.getClass ()))
			{
				System.out.println ("[CNTROL] " + attacker.toString () + " is not in player's team " + player.team ().toString ());
				return Response.INVALID;
			}

			/* Check boomer's area attack. */
			if (attacker instanceof CubeBoomer || attacker instanceof TriangleBoomer)
			{

				Set<Coordinates> s = getArea (destination, 1);
				Iterator<Coordinates> i = s.iterator ();

				while (i.hasNext ())
				{
					Coordinates c = (Coordinates) i.next ();
					objective = (Character) map.get (c);

					if (!(objective instanceof CharacterNull))
					{
						if (c != destination)
						{
							System.out.println ("[CNTROL] " + attacker.toString () + " " + source.toString () + "Area attacking "
									+ objective.toString () + " " + destination.toString () + " with " + attacker.getDamage () / 4 + " damage.");
							objective.addDamage (attacker.getDamage () / 4);
						} else
						{
							System.out.println ("[CNTROL] " + attacker.toString () + " " + source.toString () + " attacking " + objective.toString ()
									+ " " + destination.toString () + " with " + attacker.getDamage () + " damage.");
							objective.addDamage (attacker.getDamage ());
						}

						/* Clear highlightedMovement cells. */
						highlightedAttack = null;
						highlightedMovement = null;

						/* Death check. */
						System.out.println ("[CNTROL] Health left: " + objective.getHealth ());
						if (objective.getHealth () <= 0)
						{
							killEntity (destination);
						}
					}
				}

				turns.attack (player);
				return Response.OK;
			}

			/* Attack it. */
			else if (objective instanceof Character)
			{
				System.out.println ("[CNTROL] " + attacker.toString () + " " + source.toString () + " attacking " + objective.toString () + " "
						+ destination.toString () + " with " + attacker.getDamage () + " damage.");

				objective.addDamage (attacker.getDamage ());
				turns.attack (player);

				/* Clear highlightedMovement cells. */
				highlightedAttack = null;
				highlightedMovement = null;

				/* Death check. */
				System.out.println ("[CNTROL] Health left: " + objective.getHealth ());
				if (objective.getHealth () <= 0)
				{
					killEntity (destination);
				}

				return Response.OK;
			}
		}

		System.out.println ("[CNTROL] " + source.toString () + " cannot attack " + destination.toString ());
		return Response.INVALID;
	}

	/**
	 * This method is an internal function that help to check the boomer area attack.
	 * 
	 * @param objective
	 * @param attacker
	 * @param source
	 * @param destination
	 * @return true or false depends if the attack is successful or not
	 */

	private Set<Coordinates> getArea (Coordinates c, int area)
	{
		HashSet<Coordinates> s = new HashSet<Coordinates> ();
		s.add (c);

		if (area == 0)
		{
			return s;
		} else
		{
			if (c.x < map.height ())
				s.addAll (getArea (new Coordinates (c.x + 1, c.y), area - 1));
			if (c.x > 0)
				s.addAll (getArea (new Coordinates (c.x - 1, c.y), area - 1));
			if (c.y > 0)
				s.addAll (getArea (new Coordinates (c.x, c.y - 1), area - 1));
			if (c.y < map.width ())
				s.addAll (getArea (new Coordinates (c.x, c.y + 1), area - 1));
			return s;
		}
	}

	/********************************************************************************
	 * 1.4 CHARACTER SHOPPING. *
	 ********************************************************************************/

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

	/********************************************************************************
	 * 2.0 PRIVATE METHODS. *
	 ********************************************************************************/

	/********************************************************************************
	 * 2.1 ENTITY ACTIONS. *
	 ********************************************************************************/

	private void addEntity (GameObject g, Coordinates c)
	{
		// if (c.x < 0 || c.x > map.width || c.y < 0 || c.y > map.height)
		// throw new RuntimeException ("Coordinates out of bounds.");

		map.add (g, c);
		lifeBars.add (g);
		Collections.sort (lifeBars);
	}

	private void killEntity (Coordinates c)
	{
		GameObject g = map.get (c);
		map.destroy (c);
		lifeBars.remove (g);
	}

	public void skipTurn (Player p)
	{
		turns.skip (p);
		highlightedMovement = null;
		highlightedAttack = null;
	}

	/********************************************************************************
	 * 3.0 LIBGDX METHODS. *
	 ********************************************************************************/

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

	public Coordinates obtenerCoordenadas (int screenx, int screeny)
	{
		Vector3 vector = gamescreen.unproject (screenx, screeny);
		Coordinates c = new Coordinates ((int) vector.x / 128, (int) vector.y / 80);
		// System.out.println ("Convirtiendo " + screenx + ", " + screeny + " (real); " + vector.x +
		// ", " + vector.y + " (camara); " + c.toString() + " (coordenada)");
		return c;
	}
}
