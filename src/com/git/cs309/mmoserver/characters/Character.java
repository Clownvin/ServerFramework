package com.git.cs309.mmoserver.characters;

public abstract class Character {

	protected volatile int health;
	protected volatile boolean isDead; //true is dead
	protected volatile int x, y;

	public Character(final int x, final int y) {
		this.x = x;
		this.y = y;
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

}
