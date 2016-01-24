package com.git.cs309.mmoserver.characters;

import com.git.cs309.mmoserver.util.ClosedIDSystem.IDTag;

public abstract class Character {

	protected volatile int health;
	protected volatile boolean isDead; //true is dead
	protected volatile int x, y;
	protected transient IDTag idTag;
	
	public Character() {
		CharacterManager.addCharacter(this);
	}

	public Character(final int x, final int y, final IDTag idTag) {
		this.x = x;
		this.y = y;
		this.idTag = idTag;
	}
	
	public void setIDTag(final IDTag idTag) {
		this.idTag = idTag;
	}

	public abstract void applyDamage(int damageAmount);

	public abstract void applyRegen(int regenAmount);
	
	public abstract int getMaxHealth();

	public void cleanUp() {
		CharacterManager.removeCharacter(this);
		idTag.returnTag();
	}

	public int getHealth() {
		return health;
	}

	public int getUniqueID() {
		return idTag.getID();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isDead() {
		return isDead;
	}

	public void kill() {
		isDead = true;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void process();
}
