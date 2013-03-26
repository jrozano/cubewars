package com.cubewars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Constituye un fondo de escenario. Los fondos tieen la prioridad más baja (0) de todas las capas
 * del escenario, y por tanto representan la imagen sobre la que se depositan el resto de elementos
 * del juego. Un Background, por definición, es una imagen que ocupa toda la pantalla colocada en el
 * origen de coordenadas.
 * 
 * @author pyrosphere3
 * 
 */
public class Background extends GameObject implements GameSettings // Comentariororlrllrlrlrlr
{
	/* Prioridad base del tipo Background. */
	private static final int priority = 0;

	/**
	 * Crea un objeto Background a partir de un nombre de fichero alojado en data.
	 * 
	 * @param fichero El nombre del fichero, con extensión, que aloja la imagen del Background.
	 */
	public Background (String fichero)
	{
		super (priority, new Texture (Gdx.files.internal ("media/backgrounds/" + fichero)), 0, 0);
	}
}
