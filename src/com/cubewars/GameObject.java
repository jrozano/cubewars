package com.cubewars;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Objeto de juego. Es una clase genérica y abstracta que sirve como base para implementar el resto
 * de clases de objetos que se usan en el juego.
 * 
 * @author Jesús Rozano
 */
public abstract class GameObject implements Comparable<GameObject>
{
	private static int nextID = 0;
	private int id;
	private int priority;
	private Texture textura;
	public Rectangle area = new Rectangle ();

	/**
	 * Constructor de <code>GameObject</code>. Crea un <code>GameObject</code> con los parámetros
	 * básicos de todo objeto en el juego: textura, prioridad de dibujado, tamaño y posición en la
	 * parrilla.
	 * 
	 * @param priority Prioridad real del objeto.
	 * @param textura Textura que representa al objeto.
	 * @param posX Posición horizontal donde se dibujará el objeto.
	 * @param posY Posición vertical donde se dibujará el objeto.
	 */
	public GameObject (int priority, Texture textura, float posX, float posY)
	{
		/*
		 * Establecemos la ID del nuevo objeto como la siguiente al último objeto creado. FIXME Si
		 * se van destruyendo objetos, las ID no se reutilizan. ¿Desbordamiento tras muchos objetos
		 * creados?
		 */
		GameObject.nextID++;
		this.id = GameObject.nextID;

		this.priority = priority;
		this.textura = textura;
		this.area.x = posX;
		this.area.y = posY;

		/* Obtenemos automáticamente alto y ancho de la textura. */
		TextureRegion region = new TextureRegion (textura);
		this.area.height = region.getRegionHeight ();
		this.area.width = region.getRegionWidth ();
	}

	/**
	 * Compara las prioridades de dos elementos de juego (<code>GameObject</code>).
	 * 
	 * Método que implementa la interfaz {@link Comparable}, necesaria para poder usar ordenación
	 * genérica con {@link java.util.Collections#sort}. Compara el elemento actual (
	 * <code>this</code>) con un <code>GameObject</code> g que recibe como parámetro.
	 * 
	 * @see java.util.Collections#sort
	 * 
	 * @return <ul>
	 *         <li>-1, si este elemento es mayor que el parámetro.</li>
	 *         <li>1, si el parámetro es mayor que este objeto.</li>
	 *         <li>0, si ambos objetos son iguales.</li>
	 * 
	 * @param g El objeto con el que se desea establecer la comparación.
	 */
	@Override
	public int compareTo (GameObject g)
	{
		return this.priority - g.getPriority ();
	}

	/**
	 * Devuelve la prioridad del objeto actual.
	 * 
	 * @return La prioridad del objeto actual.
	 */
	public int getPriority ()
	{
		return this.priority;
	}

	/**
	 * Devuelve la ID del objeto actual.
	 * 
	 * @return La ID del objeto actual.
	 */
	public int getID ()
	{
		return this.id;
	}

	/**
	 * Devuelve la textura del objeto actual.
	 * 
	 * @return La textura del objeto actual.
	 */
	public Texture getTexture ()
	{
		return textura;
	}
	
	public String toString ()
	{
		return this.getClass ().getSimpleName ();
	}
}
