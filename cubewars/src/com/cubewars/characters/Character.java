package com.cubewars.characters;

import com.badlogic.gdx.graphics.Texture;
import com.cubewars.Coordinates;
import com.cubewars.GameObject;

/**
 * A generic character in the game. This class has the common attributes to all players characters
 * in the game characters: health, attack distance, movement distance, etc.
 * 
 * @author pyrosphere3
 */
public abstract class Character extends GameObject
{
	/* Base priority. */
	private static final int priority = 2;
	private float health;
	private float maxHealth;
	private float damage;
	private int damageDistance;
	private int maxTravel;
	private HealthBonus healthbonus;
	private AttackBonus attackbonus;

	

	/**
	 * Constructor.
	 * 
	 * @param texture Character texture.
	 * @param posX Horizontal position.
	 * @param posY Vertical position.
	 * @param health Starting health.
	 * @param maxHealth Maxmimum health points this character can have at any given time:
	 *            <code>health &lt;= maxHealth</code>.
	 */
	public Character (Texture texture, Coordinates c, float health, float maxHealth, float damage, int travel, int dmgDistance)
	{
		super (priority, texture, c);
		this.health = health;
		this.maxHealth = maxHealth;
		this.damage = damage;
		this.maxTravel = travel;
		this.damageDistance = dmgDistance;
		healthbonus= new HealthBonus (1,1);
		attackbonus= new AttackBonus(0,0);
	}

	/**
	 * Removes health from this character.
	 * 
	 * @param damage Daño recibido.
	 */
	public void addDamage (float damage)
	{
		if(healthbonus.value()!=1){
			System.out.println("[CHARAC] The character has a HealthBonus: "+ healthbonus.value() + "% of defense");
		}
		
		this.health -= (int) damage - ((damage * healthbonus.value())/100);

		if (health <= 0)
			System.out.println ("[CHARAC] " + this.toString () + " is dead.");
	}

	/**
	 * Add health to this character.
	 * 
	 * The amount of health points can never be grater than the maximum health points for this
	 * character class, and the amount of health added must be greater or equal to 0.
	 * 
	 * @param health Added health.
	 * @throws IllegalArgumentException Thrown when the amount of points added is below zero.
	 */
	public void addHealth (float health) throws IllegalArgumentException
	{
		if (health < 0)
			throw new IllegalArgumentException ("Cannot increase health with a negative number.");

		this.health = this.health + health;

		/* Check that this player does not end up with more health than the amount allowed. */
		if (this.health > maxHealth)
			this.health = maxHealth;
	}

	/**
	 * Returns the amount of damage points this character is capable of making.
	 * 
	 * @return A float with the damage value.
	 */
	public float getDamage ()
	{
		return damage;
	}

	/**
	 * Returns the number of cells this character can travel in a single turn.
	 * 
	 * @return A float with the attack distance.
	 */
	public int getAttackDistance ()
	{
		return damageDistance;
	}

	/**
	 * Returns the current health of this character.
	 * 
	 * @return A float with this character's health.
	 */
	public float getHealth ()
	{
		return health;
	}

	/**
	 * Returns the amount of cell this character can travel in a single turn.
	 * 
	 * @return An int with the travelling distance of this character.
	 */
	public int getTravel ()
	{
		return maxTravel;
	}
	
	/**
	 * Add a health bonus to this character.
	 * 
	 * @param healthbonus the bonus that will be add to the character.
	 */
	
	public void addHealthBonus(HealthBonus healthbonus){
		this.healthbonus=healthbonus;
	}
	
	/**
	 * Add a attack bonus to this character.
	 * 
	 * @param attackbonus the bonus that will be add to the character.
	 */
	
	public void addAttackBonus(AttackBonus attackbonus){
		this.attackbonus=attackbonus;
	}
	
	/**
	 * Returns the Attack Bonus of the main character
	 * 
	 * @return An int that represent the attack bonus of the character.
	 */
	
	public int getAttackBonus(){
		return attackbonus.value();
	}
	
	/**
	 * Returns the Health Bonus of the main character
	 * 
	 * @return An int that represent the health bonus of the character.
	 */
	
	public int getHealthBonus(){
		return healthbonus.value();
	}
}
