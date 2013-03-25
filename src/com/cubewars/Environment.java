package com.cubewars;

import com.badlogic.gdx.graphics.Texture;

/**
 * Un elemento de decorado del escenario. Dentro de esta clase podremos encontrar puentes,
 * obstáculos, puertas, objetos, explosivos, etc.
 * 
 * @author pyrosphere3
 * 
 */
public abstract class Environment extends GameObject
{
	/* Prioridad base de la clase Environment. */
	private static final int priority = 1;

	public Environment (Texture textura, float posX, float posY)
	{
		super (priority, textura, posX, posY);
	}

}
