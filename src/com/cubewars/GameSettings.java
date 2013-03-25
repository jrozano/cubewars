/**
 * 
 */
package com.cubewars;

/**
 * Prueba de configuración global del juego.
 * 
 * @author pyrosphere3
 */
public interface GameSettings
{
	/*
	 * TODO El tamaño de pantalla supongo que se podrá obtener con libGDX, y será incluso mejor
	 * hacerlo así por si la pantalla se redimentsiona o estamos en un móvil o tablet, pero el
	 * tamaño de las celdas debería ir en una clase Mapa para poder cambiar el tamaño de cada nivel.
	 * 
	 * Esto es un apaño para ir probando que todo vaya bien.
	 * 
	 * Afecta a clases Pixel, Coordinates y Background.
	 */
	public static final int GridRows = 8;
	public static final int GridColumns = 10;
	public static final int CellHeight = 100;
	public static final int CellWidth = 128;
}
