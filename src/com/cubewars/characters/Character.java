package com.cubewars.characters;

import com.badlogic.gdx.graphics.Texture;
import com.cubewars.GameController;
import com.cubewars.GameObject;

/**
 * Clase abstracta que representa un personaje genérico de juego. Contiene los atributos y métodos
 * comunes a todos los personajes que se posicionarán en la parrilla de juego, la prioridad, salud,
 * y salud máxima.
 * 
 * @author pyrosphere3
 */
public abstract class Character extends GameObject
{
	/* Prioridad base de la clase Character. */
	private static final int priority = 2;
	private float health;
	private float maxHealth;
	private float damage;
	private int damageDistance;
	private int maxTravel;

	/**
	 * Contruye un personaje.
	 * 
	 * @param textura Textura del personaje.
	 * @param posX Posición horizontal del personaje.
	 * @param posY Posición vertical del personaje.
	 * @param health Salud con la que cuenta el personaje al comienzo de su vida.
	 * @param maxHealth Salud máxima que el personaje puede tener.
	 *            <code>health &lt;= maxHealth</code>.
	 */
	public Character (Texture textura, float posX, float posY, float health, float maxHealth, float damage, int travel,
			int dmgDistance)
	{
		super (priority, textura, posX, posY);
		this.health = health;
		this.maxHealth = maxHealth;
		this.damage = damage;
		this.maxTravel = travel;
		this.damageDistance = dmgDistance;
	}

	/**
	 * Resta salud al personaje. Si el daño causado ocasiona que la salud sea igual o inferior a
	 * cero, el personaje muere y llama a {@link GameController} para eliminar al personaje de la
	 * parrilla.
	 * 
	 * @param damage Daño recibido.
	 */
	public void addDamage (float damage)
	{
		this.health -= damage;

		if (health <= 0)
		{
			// TODO Muerto. Pedir al controlador que elimine el muñeco
			System.out.println ("[CHARACTER] Entidad " + this.toString () + " muerta.");
		}
	}

	/**
	 * Cura un personaje. La cantidad de vida añadida nunca será superior al máximo de salud
	 * permitida por este personaje. La cantidad de vida siempre debe ser igual o superior a cero.
	 * 
	 * @param health Salud recibida.
	 * @throws IllegalArgumentException Lanzada cuando se recube un parámetro inferior a cero.
	 */
	public void addHealth (float health) throws IllegalArgumentException
	{
		if (health < 0)
			throw new IllegalArgumentException ("No se puede incrementar la salud con un número negativo.");

		this.health = this.health + health;

		/* Comprobamos que el personaje no acabe con más salud de la máxima permitida para su clase. */
		if (this.health > maxHealth)
			this.health = maxHealth;
	}

	public float getDamage ()
	{
		return damage;
	}

	public float getAttackDistance ()
	{
		return damageDistance;
	}

	public float getHealth ()
	{
		return health;
	}

	public int getTravel ()
	{
		return maxTravel;
	}
}
