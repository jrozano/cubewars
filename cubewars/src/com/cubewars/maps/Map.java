package com.cubewars.maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.cubewars.Coordinates;
import com.cubewars.GameController;
import com.cubewars.GameObject;
import com.cubewars.backgrounds.Environment;
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
	private GameController controller;

	/* What could possibly go wrong? */
	@SuppressWarnings("unchecked")
	public Map (GameController controller)
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

				/* Create empty character grid. */
				for (int i = 0; i < width; ++i)
					for (int j = 0; j < height; ++j)
						characters[i][j] = null;

				/* Populate terrain cells. */
				for (int i = 0; i != grid.getChildCount (); ++i)
				{
					Element child = grid.getChild (i);
					terrain[child.getIntAttribute ("x")][child.getIntAttribute ("y")] = new Environment (new Texture (
							Gdx.files.internal ("media/styles/" + style + "/" + child.getAttribute ("type") + ".png")), new Coordinates (
							child.getIntAttribute ("x"), child.getIntAttribute ("y")));
				}

				/* TODO: read predefined objects placed on the map. */
			} else
			{
				System.out.println ("[MAP   ] ERROR: this map XML file does not have grid data. Map not valid.");
				throw new RuntimeException ("This map XML file does not have grid data. Map not valid.");
			}
		} catch (IOException e)
		{
			System.out.println ("[MAP   ] ERROR: could not read map XML file.");
			throw new RuntimeException ("Could not read map XML file.");
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
