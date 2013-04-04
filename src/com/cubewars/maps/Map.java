package com.cubewars.maps;

import com.cubewars.Coordinates;
import com.cubewars.GameObject;
import com.cubewars.characters.CharacterNull;
import com.cubewars.characters.CubeBoomer;
import com.cubewars.characters.CubeGunner;
import com.cubewars.characters.CubeSniper;
import com.cubewars.characters.TriangleBoomer;
import com.cubewars.characters.TriangleGunner;
import com.cubewars.characters.TriangleSniper;

/**
 * A generic class representing a game grid (known as a map).
 * 
 * @author pyrosphere3
 * 
 */
public class Map
{
	public GameObject[][] grid;
	public final int height = 10;
	public final int width = 10;

	public Map ()
	{
		grid = new GameObject[width][height];

		for (int i = 0; i < width; ++i)
			for (int j = 0; j < height; ++j)
				grid[i][j] = new CharacterNull ();
	}

	public void print ()
	{
		for (int i = 0; i < width; ++i)
		{
			for (int j = 0; j < height; ++j)
			{
				if (grid[width-1-i][j] instanceof CubeBoomer)
					System.out.print ("CB ");
				else if (grid[width-1-i][j] instanceof CubeSniper)
					System.out.print ("CS ");
				else if (grid[width-1-i][j] instanceof CubeGunner)
					System.out.print ("CG ");
				else if (grid[width-1-i][j] instanceof TriangleSniper)
					System.out.print ("TS ");
				else if (grid[width-1-i][j] instanceof TriangleBoomer)
					System.out.print ("TB ");
				else if (grid[width-1-i][j] instanceof TriangleGunner)
					System.out.print ("TG ");
				else
					System.out.print ("-- ");
			}

			System.out.println ();
		}
	}

	public void add (GameObject character, Coordinates c)
	{
		grid[c.x][c.y] = character;
	}

	public void move (Coordinates origin, Coordinates destination)
	{
		if (grid[destination.x][destination.y] instanceof CharacterNull)
		{
			System.out.println ("[MAP   ] Moving " + origin.toString () + " to " + destination.toString ());
			grid[destination.x][destination.y] = grid[origin.x][origin.y];
			grid[origin.x][origin.y] = new CharacterNull ();
		}
	}

	public void move (GameObject g, Coordinates destination)
	{
		for (int i = 0; i < grid.length; ++i)
		{
			for (int j = 0; j < grid[0].length; ++j)
			{
				if (grid[i][j] == g)
					move (new Coordinates (i, j), destination);
			}
		}
	}

	public Coordinates search (GameObject g)
	{
		for (int i = 0; i < grid.length; ++i)
		{
			for (int j = 0; j < grid[0].length; ++j)
			{
				if (grid[i][j] == g)
					return new Coordinates (i, j);
			}
		}

		/* Not found, return null. */
		return null;
	}

	public void destroy (Coordinates c)
	{
		grid[c.x][c.y] = new CharacterNull ();
	}

	public GameObject get (Coordinates c)
	{
		return grid[c.x][c.y];
	}
}
