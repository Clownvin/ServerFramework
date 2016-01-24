package com.git.cs309.mmoserver.characters.npc;

public final class NPCDefinition {
	private final String name;
	private final int id;
	private final int maxHealth;
	private final int strength;
	private final int accuracy;
	private final int defence;
	private final int level;

	public NPCDefinition(final String name, final int id, final int maxHealth, final int strength, final int accuracy,
			final int defence, final int level) {
		this.name = name;
		this.id = id;
		this.maxHealth = maxHealth;
		this.strength = strength;
		this.accuracy = accuracy;
		this.defence = defence;
		this.level = level;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public int getDefence() {
		return defence;
	}

	public int getID() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public String getName() {
		return name;
	}

	public int getStrength() {
		return strength;
	}
}
