package com.cubewars;

/**
 * Clase que representa un pixel cualquiera de la ventana. Se entiende que un pixel se compone de
 * dos coordenadas, vertical y horizontal, que indican su posición en la pantalla.
 * 
 * @author pyrosphere3
 * 
 */
public class Pixel implements GameSettings
{
	public float x, y;

	/**
	 * Construye un pixel a partir de una {@link Coordinates}.
	 * 
	 * @param c
	 */
	public Pixel (Coordinates c)
	{
		this.x = (c.x - 1) * GameSettings.CellWidth;
		this.y = (c.y - 1) * GameSettings.CellHeight;
	}

	/**
	 * Construye un Pixel a partir de dos reales que indican su ubicación.
	 * 
	 * @param x
	 * @param y
	 */
	public Pixel (float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Convierte un Pixel a una tupla de coordenadas, {@link Coordinates}
	 * 
	 * @return Un objeto con las coordenadas de este pixel.
	 */
	public Coordinates toCoordinates ()
	{
		return new Coordinates (this);
	}
}
