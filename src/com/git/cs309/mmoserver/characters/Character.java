package com.git.cs309.mmoserver.characters;

import com.git.cs309.mmoserver.util.ClosedIDSystem.IDTag;

/**
 * 
 * @author Clownvin
 *
 *         An abstract class representing the basis for all in-game characters,
 *         such as player, npcs, bosses, etc.
 */
public abstract class Character {

	//Current health.
	protected volatile int health;
	protected volatile boolean isDead; //true is dead
	protected volatile int x, y; // Coordinates
	protected transient IDTag idTag; // Unique identifier

	public Character() { // This constructor exits solely so that deserialization can happen, which requires a contructor with no arguments.
		CharacterManager.addCharacter(this); // Ensure that all classes extending character get added to the CharacterManager
	}

	public Character(final int x, final int y, final IDTag idTag) {
		this.x = x;
		this.y = y;
		this.idTag = idTag;
		CharacterManager.addCharacter(this);
	}

	public abstract void applyDamage(int damageAmount); // Abstract so subclasses can implement in their own way.

	public abstract void applyRegen(int regenAmount); // Abstract so subclasses can implement in their own way.

	//Release this object and return id tag to system.
	public final void cleanUp() {
		CharacterManager.removeCharacter(this);
		idTag.returnTag();
	}

	public int getHealth() {
		return health;
	}

	public abstract int getMaxHealth();

	public final int getUniqueID() {
		return idTag.getID();
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public boolean isDead() {
		return isDead;
	}

	public void kill() {
		isDead = true;
	}

	public abstract void process(); // Force implementations to create their own process method.

	public void setIDTag(final IDTag idTag) {
		this.idTag = idTag;
	}

	public void setPosition(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
}
