package com.cubewars;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * A game object.
 * 
 * This is an abstract class that it's used as a base to implement other object classes used in the
 * game.
 * 
 * @author Jesús Rozano
 */
public abstract class GameObject implements Comparable<GameObject>
{
	private static int nextID = 0;
	private int id;
	private int priority;
	private Texture texture;
	public Rectangle area = new Rectangle ();

	/**
	 * <code>GameObject</code> constructor.
	 * 
	 * @param priority Real priority of this object.
	 * @param texture Texture representing the object.
	 * @param posX Horizontal position.
	 * @param posY Vertical position.
	 */
	public GameObject (int priority, Texture texture, float posX, float posY)
	{
		/*
		 * FIXME Given a large number of objects created and destroyed, the IDs are not reused.
		 * Possible overflow?
		 */
		GameObject.nextID++;
		this.id = GameObject.nextID;

		this.priority = priority;
		this.texture = texture;
		this.area.x = posX;
		this.area.y = posY;

		TextureRegion region = new TextureRegion (texture);
		this.area.height = region.getRegionHeight ();
		this.area.width = region.getRegionWidth ();
	}

	/**
	 * <code>GameObject</code> constructor with coordinates.
	 * 
	 * @param priority Real priority of this object.
	 * @param texture Texture representing the object.
	 * @param c Position in the grid.
	 */
	public GameObject (int priority, Texture texture, Coordinates c)
	{
		/*
		 * FIXME Given a large number of objects created and destroyed, the IDs are not reused.
		 * Possible overflow?
		 */
		GameObject.nextID++;
		this.id = GameObject.nextID;

		this.priority = priority;
		this.texture = texture;
		this.area.x = c.toPixel ().x;
		this.area.y = c.toPixel ().y;

		TextureRegion region = new TextureRegion (texture);
		this.area.height = region.getRegionHeight ();
		this.area.width = region.getRegionWidth ();
	}

	/**
	 * Compares two (<code>GameObject</code>) priorities.
	 * 
	 * This method implements the {@link Comparable}, needed to be able to use generic sort with
	 * {@link java.util.Collections#sort}. Compares <code>this</code> element with another
	 * <code>GameObject</code> g passed as a parameter.
	 * 
	 * @see java.util.Collections#sort
	 * 
	 * @return <ul>
	 *         <li>-1, if this > g.</li>
	 *         <li>1, if this < g.</li>
	 *         <li>0, if this == g.</li>
	 * 
	 * @param g The object to be compared with.
	 */
	@Override
	public int compareTo (GameObject g)
	{
		return this.priority - g.getPriority ();
	}

	/**
	 * Returns the object's priority.
	 * 
	 * @return This object's priority.
	 */
	public int getPriority ()
	{
		return this.priority;
	}

	/**
	 * Returns the object's ID.
	 * 
	 * @return This object's ID.
	 */
	public int getID ()
	{
		return this.id;
	}

	/**
	 * Returns the object's texture.
	 * 
	 * @return This object's texture.
	 */
	public Texture getTexture ()
	{
		return texture;
	}

	public String toString ()
	{
		return this.getClass ().getSimpleName ();
	}
}
