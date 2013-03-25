package com.cubewars.maps;

import com.cubewars.Coordinates;
import com.cubewars.GameObject;
import com.cubewars.characters.CharacterNull;
import com.cubewars.characters.Cube;
import com.cubewars.characters.CubeBoomer;
import com.cubewars.characters.CubeGunner;
import com.cubewars.characters.CubeSniper;
import com.cubewars.characters.Triangle;
import com.cubewars.characters.TriangleBoomer;
import com.cubewars.characters.TriangleGunner;
import com.cubewars.characters.TriangleSniper;

public class Map
{
	public GameObject[][] grid;

	public Map ()
	{
		grid = new GameObject[5][5];

		for (int i = 0; i < grid.length; ++i)
			for (int j = 0; j < grid[0].length; ++j)
				grid[i][j] = new CharacterNull ();

		grid[0][0] = new CubeSniper (0, 0);
		grid[4][4] = new TriangleSniper (128, 128);
		grid[0][4] = new CubeBoomer (128, 0);
		grid[4][0] = new TriangleBoomer (0, 128);
		grid[2][2] = new CubeGunner (0, 0);
	}

	public void print ()
	{
		for (int i = 0; i < grid.length; ++i)
		{
			for (int j = 0; j < grid[0].length; ++j)
			{
				if (grid[i][j] instanceof CubeBoomer)
					System.out.print ("CB ");
				else if (grid[i][j] instanceof CubeSniper)
					System.out.print ("CS ");
				else if (grid[i][j] instanceof CubeGunner)
					System.out.print ("CG ");
				else if (grid[i][j] instanceof TriangleSniper)
					System.out.print ("TS ");
				else if (grid[i][j] instanceof TriangleBoomer)
					System.out.print ("TB ");
				else if (grid[i][j] instanceof TriangleGunner)
					System.out.print ("TG ");
				else
					System.out.print ("·· ");
			}

			System.out.println ();
		}
	}

	public void move (Coordinates origen, Coordinates destino)
	{
		if (grid[destino.x][destino.y] instanceof CharacterNull)
		{
			System.out.println ("[MAP   ] Moviendo " + origen.toString () + " a " + destino.toString ());
			grid[destino.x][destino.y] = grid[origen.x][origen.y];
			grid[origen.x][origen.y] = new CharacterNull ();
		}
	}

	public void move (GameObject g, Coordinates destino)
	{
		for (int i = 0; i < grid.length; ++i)
		{
			for (int j = 0; j < grid[0].length; ++j)
			{
				if (grid[i][j] == g)
					move (new Coordinates (i, j), destino);
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

		/* No se encuentra, devolvemos null. */
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
