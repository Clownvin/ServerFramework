package com.git.cs309.mmoserver.characters;

import com.git.cs309.mmoserver.util.ClosedIDSystem.IDTag;

public abstract class Character {

	protected volatile int health;
	protected volatile boolean isDead; //true is dead
	protected volatile int x, y;
	protected final IDTag idTag;

	public Character(final int x, final int y, final IDTag idTag) {
		this.x = x;
		this.y = y;
		this.idTag = idTag;
	}
	
	public int getUniqueID() {
		return idTag.getID();
	}

	public int getHealth() {
		return health;
	}

	public void applyDamage(int damage) {
		health -= damage;
		if (health <= 0) {
			isDead = true;
		}
	}
         
	public void applyRegen(int regenAmount) {
		health += regenAmount;
	}

	public void kill() {
		isDead = true;
	}

	public boolean isDead() {
		return isDead;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void cleanUp() {
		idTag.returnTag();
	}
}
