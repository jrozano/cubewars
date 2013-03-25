package com.cubewars;

/**
 * Clase que almacena una tupla de coordenadas en el tablero. Esta clase proporciona métodos de
 * conversión entre tuplas y píxeles y viceversa, necesarios para diversas acciones.
 * 
 * @author pyrosphere3
 * 
 */
public class Coordinates implements GameSettings
{
	public int x, y;

	/**
	 * Construye un par de coordenadas de tablero en función del {@link Pixel} que se especifique.
	 * Dado un pixel p, construye un objeto Coordinates con el par (x,y) de la casilla del tablero
	 * en la que se encuentra dicho pixel.
	 * 
	 * @param p Un {@link Pixel} de la pantalla.
	 */
	public Coordinates (Pixel p)
	{
		this.x = (int) Math.ceil (p.x / GameSettings.CellWidth);
		this.y = (int) Math.ceil (p.y / GameSettings.CellHeight);
	}

	/**
	 * Construye un par de coordenadas de tablero a partir de dos números reales.
	 * 
	 * @param x Columna.
	 * @param y Fila.
	 */
	public Coordinates (int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Distancia en métrica Taxicab de unas coordenadas a otra.
	 * 
	 * @param c Coordenadas objetivo.
	 * @return Un entero con la distancia entre los dos puntos.
	 */
	public int distance (Coordinates c)
	{
		return (c.y - y) + (c.x - x);
	}

	/**
	 * Devuelve un {@link Pixel} que se correponde con esta casilla.
	 * 
	 * Por definición, dado que una casilla puede contener gran cantidad de píxeles, se devolverá el
	 * pixel de la esquina inferior izquierda, siguiendo el mismo marco de referencia usado para la
	 * ventana, de modo que facilite la labor de colocar una textura en esta casilla.
	 * 
	 * @return Un {link Pixel} equivalente a esta casilla.
	 */
	public Pixel toPixel ()
	{
		// return new Pixel (this.x * GameSettings.CellWidth, this.y * GameSettings.CellHeight);
		return new Pixel (this);
	}

	public String toString ()
	{
		return new String ("(" + x + ", " + y + ")");
	}
}
