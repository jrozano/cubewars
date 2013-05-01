package com.cubewars.maps;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.cubewars.Coordinates;
import com.cubewars.GameObject;
import com.cubewars.backgrounds.Dirt;
import com.cubewars.backgrounds.Environment;
import com.cubewars.backgrounds.Grass;
import com.cubewars.characters.CharacterNull;

/**
 * A generic class representing a game characters (known as a map).
 * 
 * @author pyrosphere3
 * 
 */
public class Map
{
	private GameObject[][] characters;
	private Environment[][] terrain;
	private GameObject[][] objects;
	private int height;
	private int width;
	private String style;

	/* What could possibly go wrong? */
	@SuppressWarnings("unchecked")
	public Map ()
	{
		try
		{
			/* Read XML map. */
			XmlReader reader = new XmlReader ();
			Element e = reader.parse (Gdx.files.internal ("media/maps/map1.xml"));

			/* Read map dimensions and initialize entity arrays. */
			height = e.getIntAttribute ("height");
			width = e.getIntAttribute ("width");
			style = e.getAttribute ("style");

			characters = new GameObject[width][height];
			terrain = new Environment[width][height];
			objects = new GameObject[width][height];
			
			/* Create empty character grid. */
			for (int i = 0; i < width; ++i)
				for (int j = 0; j < height; ++j)
					characters[i][j] = null;

			/* Populate terrain cells. */
			for (int i = 0; i != e.getChildCount (); ++i)
			{
				Element child = e.getChild (i);
				Class c = Class.forName ("com.cubewars.backgrounds." + child.getAttribute ("type"));
				Constructor constructor = c.getConstructor (new Class[] { Coordinates.class });
				terrain[child.getIntAttribute ("x")][child.getIntAttribute ("y")] = (Environment) constructor.newInstance (new Coordinates (child
						.getIntAttribute ("x"), child.getIntAttribute ("y")));
			}
			
			/* TODO: read predefined objects placed on the map. */

		} catch (IOException e)
		{
			System.out.println ("[REFLEC] ERROR: could not read map XML file.");
			throw new RuntimeException ("Could not read map XML file.");
		} catch (ClassNotFoundException e1)
		{
			System.out.println ("[REFLEC] ERROR: the XML file specified a terrain class that does not exist.");
			throw new RuntimeException ("XML file specified a terrain class that does not exist.");
		} catch (Exception e1)
		{
			System.out.println ("[MAP   ] ERROR: something really awful happened while generating the map.");
			e1.printStackTrace ();
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

	public void destroy (Coordinates c)
	{
		characters[c.x][c.y] = null;
	}

	public GameObject get (Coordinates c)
	{
		if (characters[c.x][c.y] == null)
			return new CharacterNull ();
		else
			return characters[c.x][c.y];
	}

	public ArrayList<Environment> getTerrain ()
	{
		ArrayList<Environment> env = new ArrayList<Environment> ();
		for (int i = 0; i != height; ++i)
			env.addAll (Arrays.asList (terrain[i]));

		return env;
	}

	public int height ()
	{
		return height;
	}

	public int width ()
	{
		return width;
	}
}
