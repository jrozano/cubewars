package com.cubewars;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.cubewars.backgrounds.Barrel;
import com.cubewars.backgrounds.Box;
import com.cubewars.backgrounds.Destructible;
import com.cubewars.backgrounds.Environment;
import com.cubewars.characters.CharacterNull;

/**
 * A generic class representing a game characters (known as a map).
 * 
 * @author pyrosphere3
 * 
 */
public class MapController
{
	private GameController controller;

	/* Map properties. */
	private int height;
	private int width;
	private String style;

	/* Map structure. */
	private GameObject[][] characters;
	private GameObject[][] objects;
	private Environment[][] terrain;
	private double[][] blockages;

	/* Constants. */
	private static final double infinity = Double.POSITIVE_INFINITY;

	@SuppressWarnings("unchecked")
	public MapController (GameController controller)
	{
		try
		{
			this.controller = controller;

			/* Read XML map. */
			XmlReader xmlReader = new XmlReader ();
			Element xmlNode = xmlReader.parse (Gdx.files.internal ("media/maps/map1.xml"));

			/* Read options. */
			Element options = xmlNode.getChildByName ("options");
			if (options != null)
			{
				System.out.println ("[OPTION] Parsing options...");
				Element xmlColor = options.getChildByName ("attackHighlight");
				if (xmlColor != null)
				{
					System.out.println ("[OPTION] Parsing attack color values...");
					System.out.println ("[OPTION] Color: " + xmlColor.getFloat ("red") + ", " + xmlColor.getFloat ("green") + ", "
							+ xmlColor.getFloat ("blue"));
					controller.setAttackHighlightColor (new Color (xmlColor.getFloat ("red"), xmlColor.getFloat ("green"),
							xmlColor.getFloat ("blue"), xmlColor.getFloat ("alpha")));
				}

				xmlColor = options.getChildByName ("moveHighlight");
				if (xmlColor != null)
				{
					System.out.println ("[OPTION] Parsing movement color values...");
					System.out.println ("[OPTION] Color: " + xmlColor.getFloat ("red") + ", " + xmlColor.getFloat ("green") + ", "
							+ xmlColor.getFloat ("blue"));
					controller.setMoveHighlightColor (new Color (xmlColor.getFloat ("red"), xmlColor.getFloat ("green"), xmlColor.getFloat ("blue"),
							xmlColor.getFloat ("alpha")));
				}
			}

			/* Read map dimensions and initialize entity arrays. */
			Element grid = xmlNode.getChildByName ("grid");

			if (grid != null)
			{
				height = grid.getIntAttribute ("height");
				width = grid.getIntAttribute ("width");

				try
				{
					style = xmlNode.getAttribute ("style");
				} catch (GdxRuntimeException e1)
				{
					/* XML file does not specify a theme. Use default. */
					style = "default";
				}

				characters = new GameObject[width][height];
				terrain = new Environment[width][height];
				objects = new GameObject[width][height];
				blockages = new double[width][height];

				/* Create empty character grid without any obstacles by default. */
				for (int i = 0; i < width; ++i)
				{
					for (int j = 0; j < height; ++j)
					{
						characters[i][j] = null;
						blockages[i][j] = 0;
					}
				}

				/* Look for a default cell class for the grid. */
				try
				{
					String defaultTerrain = grid.getAttribute ("default");

					/*
					 * Apply default class for all cells. We'll overwrite the specific cells in the
					 * next step.
					 */

					for (int i = 0; i < width; ++i)
					{
						for (int j = 0; j < height; ++j)
						{
							terrain[i][j] = new Environment (new Texture (
									Gdx.files.internal ("media/styles/" + style + "/" + defaultTerrain + ".png")), new Coordinates (i, j));
						}
					}

				} catch (GdxRuntimeException e)
				{
					/*
					 * This map XML file does not set any default class for the grid. That's OK, but
					 * if there's not a cell element for each cell in the grid, everything could end
					 * up blowing up.
					 */
				}

				/* Populate terrain cells. */
				for (int i = 0; i != grid.getChildCount (); ++i)
				{
					Element child = grid.getChild (i);
					int x = child.getIntAttribute ("x");
					int y = child.getIntAttribute ("y");
					terrain[x][y] = new Environment (new Texture (Gdx.files.internal ("media/styles/" + style + "/" + child.getAttribute ("class")
							+ ".png")), new Coordinates (x, y));

					try
					{
						blockages[x][y] = child.getFloat ("blockage");
					} catch (GdxRuntimeException e)
					{
						/*
						 * This cell XML element does not have blockage information, and that's OK.
						 * It's 0 by default.
						 */
					}
				}
			} else
			{
				System.out.println ("[MAP   ] ERROR: this map XML file does not have grid data. MapController not valid.");
				throw new RuntimeException ("This map XML file does not have grid data. MapController not valid.");
			}

			/* Place start characters. */
			Element characters = xmlNode.getChildByName ("characters");
			if (characters != null)
			{
				for (int i = 0; i != characters.getChildCount (); ++i)
				{
					Element child = characters.getChild (i);
					int x = child.getIntAttribute ("x");
					int y = child.getIntAttribute ("y");
					Coordinates c = new Coordinates (x, y);

					Class characterClass = Class.forName ("com.cubewars.characters." + child.getAttribute ("class"));
					Constructor constructor = characterClass.getConstructor (new Class[] { Coordinates.class });

					GameObject g = (GameObject) constructor.newInstance (c);
					this.characters[x][y] = g;
					this.controller.lifeBars.add (g);

					System.out.println ("[REFLEC] Placed character " + characterClass.getSimpleName () + " in " + c.toString ());

				}
			}
			

			/* Read predefined objects placed on the map. */
			Element objects = xmlNode.getChildByName ("objects");
			if (objects != null)
			{
				for (int i = 0; i != objects.getChildCount (); ++i)
				{
					Element child = objects.getChild (i);
					int x = child.getIntAttribute ("x");
					int y = child.getIntAttribute ("y");
					Coordinates c = new Coordinates (x, y);

					/* Instantiate an object of a given class and place it on the objects grid. */
					Class characterClass = Class.forName ("com.cubewars.backgrounds." + child.getAttribute ("class"));
					Texture tex = new Texture (Gdx.files.internal ("media/styles/" + style + "/" + child.getAttribute ("class").toLowerCase ()
							+ ".png"));
					Constructor constructor = characterClass.getConstructor (new Class[] { Texture.class, Coordinates.class });

					GameObject g = (GameObject) constructor.newInstance (tex, c);
					this.objects[x][y] = g;

					/* Should this object block character movement? */
					try
					{
						this.blockages[x][y] = child.getFloat ("blockage");
					} catch (GdxRuntimeException e)
					{
						/*
						 * This object's XML element does not have blockage information, and that's
						 * OK. It's 0 by default.
						 */
					}

					System.out.println ("[REFLEC] Placed object " + characterClass.getSimpleName () + " in " + c.toString ());

				}
				
				Box box= new Box(new Coordinates(4,4));
				this.objects[4][4]=box;
				this.blockages[4][4]=1;
				Barrel barrel = new Barrel(new Coordinates(5,5));
				this.objects[5][5]=barrel;
				this.blockages[5][5]=1;
			}

		} catch (IOException e)
		{
			System.out.println ("[MAP   ] ERROR: could not read map XML file.");
			throw new RuntimeException ("Could not read map file.");
		} catch (ClassNotFoundException e)
		{
			System.out.println ("[MAP   ] ERROR: could not instance class " + e.getMessage () + ". Class not found.");
			throw new RuntimeException ("Map file has errors.");
		} catch (GdxRuntimeException e)
		{
			System.out.println ("[MAP   ] ERROR: XML file is not well-formed.");
			throw new RuntimeException ("Map file has errors.");
		} catch (Exception e1)
		{
			System.out.println ("[MAP   ] ERROR: something really awful happened while generating the map.");
			throw new RuntimeException ("Unknown error.");
		}
	}

	public void add (GameObject character, Coordinates c)
	{
		/* Check for Coordinates within range. */
		if (c.x < 0 || c.x > width || c.y < 0 || c.y > height)
		{
			System.out.println ("[MAP   ] ERROR: inserting entity at out of bounds coordinates: " + c.toString ());
			throw new RuntimeException ("Inserting entity at out of bounds coordinates: " + c.toString ());
		}

		if (character != null)
		{
			characters[c.x][c.y] = character;
		} else
		{
			System.out.println ("[MAP   ] ERROR: inserting null entity at " + c.toString ());
			throw new RuntimeException ("Inserting null entity at " + c.toString ());
		}
	}

	public void move (Coordinates origin, Coordinates destination)
	{
		/* Check for Coordinates within range. */
		if (origin.x < 0 || origin.x > width || origin.y < 0 || origin.y > height)
		{
			System.out.println ("[MAP   ] ERROR: moving from coordinates out of bounds: " + origin.toString ());
			throw new RuntimeException ("Moving from coordinates out of bounds: " + origin.toString ());
		}

		if (destination.x < 0 || destination.x > width || destination.y < 0 || destination.y > height)
		{
			System.out.println ("[MAP   ] ERROR: moving to coordinates out of bounds: " + destination.toString ());
			throw new RuntimeException ("Moving to coordinates out of bounds: " + destination.toString ());
		}

		if (characters[destination.x][destination.y] == null)
		{
			System.out.println ("[MAP   ] Moving " + origin.toString () + " to " + destination.toString ());
			characters[destination.x][destination.y] = characters[origin.x][origin.y];
			characters[origin.x][origin.y] = null;
		}
	}

	public Coordinates search (GameObject g)
	{
		for (int i = 0; i < characters.length; ++i)
		{
			for (int j = 0; j < characters[0].length; ++j)
			{
				if (characters[i][j] == g)
					return new Coordinates (i, j);
			}
		}

		/* Not found, return null. */
		return null;
	}

	public void removeCharacter (Coordinates c)
	{
		GameObject g = this.get(c);
		characters[c.x][c.y] = null;
	}
	
	public void removeObject (Coordinates c){
		objects[c.x][c.y] = null;
		
		/* FIXME This is just a piece of shit. Needs more work. */
		if (blockages[c.x][c.y] == 0)
			blockages[c.x][c.y] = infinity;
		else if (blockages[c.x][c.y] != 0)
			blockages[c.x][c.y] = 0;
	}
	
	public GameObject get (Coordinates c)
	{
		if (characters[c.x][c.y] != null)
		{
			/* There's a character. */
			return characters[c.x][c.y];
		} else if (objects[c.x][c.y] != null)
		{
			/* There's an object. */
			return objects[c.x][c.y];
		} else
		{
			/* There's nothing :( */
			return new CharacterNull ();
		}

	}

	public ArrayList<Environment> getTerrain ()
	{
		ArrayList<Environment> env = new ArrayList<Environment> ();
		for (int i = 0; i != height; ++i)
			env.addAll (Arrays.asList (terrain[i]));

		env.removeAll (Collections.singleton (null));
		return env;
	}

	public ArrayList<GameObject> getCharacters ()
	{
		ArrayList<GameObject> chars = new ArrayList<GameObject> ();
		for (int i = 0; i != height; ++i)
			chars.addAll (Arrays.asList (characters[i]));

		chars.removeAll (Collections.singleton (null));
		return chars;
	}

	public ArrayList<GameObject> getObjects ()
	{
		ArrayList<GameObject> objs = new ArrayList<GameObject> ();
		for (int i = 0; i != height; ++i)
			objs.addAll (Arrays.asList (objects[i]));

		objs.removeAll (Collections.singleton (null));
		return objs;
	}

	public int height ()
	{
		return height;
	}

	public int width ()
	{
		return width;
	}

	public Set<Coordinates> getMoveArea (Coordinates a, int length)
	{
		Set<Coordinates> s = getMoveAreaR (a, length);
		//s.remove (a);

		for (int i = 0; i != width; ++i)
			for (int j = 0; j != height; ++j)
			{
				if (objects[i][j] != null)
				{
					s.remove (new Coordinates (i, j));
				}
			}
		return s;
	}

	public Set<Coordinates> getAttackArea (Coordinates a, int length)
	{
		Set<Coordinates> s = getAttackAreaR (a, length);

		return s;
	}

	private Set<Coordinates> getMoveAreaR (Coordinates a, int length)
	{
		HashSet<Coordinates> s = new HashSet<Coordinates> ();
		s.add (a);

		if (length > 0)
		{
			if (a.x < width - 1 && blockages[a.x + 1][a.y] == 0)
				s.addAll (getMoveAreaR (new Coordinates (a.x + 1, a.y), length - 1));
			if (a.x > 0 && blockages[a.x - 1][a.y] == 0)
				s.addAll (getMoveAreaR (new Coordinates (a.x - 1, a.y), length - 1));
			if (a.y > 0 && blockages[a.x][a.y - 1] == 0)
				s.addAll (getMoveAreaR (new Coordinates (a.x, a.y - 1), length - 1));
			if (a.y < height - 1 && blockages[a.x][a.y + 1] == 0)
				s.addAll (getMoveAreaR (new Coordinates (a.x, a.y + 1), length - 1));
		}

		return s;
	}

	private Set<Coordinates> getAttackAreaR (Coordinates a, int length)
	{
		HashSet<Coordinates> s = new HashSet<Coordinates> ();

		/*
		 * Only add if we can attack it: if it's an undestructible obstacle (water, holes, etc.), do
		 * not add it.
		 */
		/* TODO Cambiar la expresión. */
		if (!(blockages[a.x][a.y] > 0 && objects[a.x][a.y] == null))
			s.add (a);

		if (length > 0 && blockages[a.x][a.y] == 0)
		{
			if (a.x < width - 1)
				s.addAll (getAttackAreaR (new Coordinates (a.x + 1, a.y), length - 1));
			if (a.x > 0)
				s.addAll (getAttackAreaR (new Coordinates (a.x - 1, a.y), length - 1));
			if (a.y > 0)
				s.addAll (getAttackAreaR (new Coordinates (a.x, a.y - 1), length - 1));
			if (a.y < height - 1)
				s.addAll (getAttackAreaR (new Coordinates (a.x, a.y + 1), length - 1));
		}

		return s;
	}
}
