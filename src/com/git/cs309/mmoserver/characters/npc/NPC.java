package com.git.cs309.mmoserver.characters.npc;

import com.git.cs309.mmoserver.characters.Character;
import com.git.cs309.mmoserver.util.ClosedIDSystem.IDTag;

public class NPC extends Character {
	private final NPCDefinition definition;

	public NPC(int x, int y, final NPCDefinition definition, final IDTag idTag) {
		super(x, y, idTag);
		this.definition = definition;
	}

	@Override
	public void applyDamage(int damageAmount) {
		health -= damageAmount;
		if (health <= 0) {
			isDead = true;
		}
	}

	@Override
	public void applyRegen(int regenAmount) {
		if (health + regenAmount <= getMaxHealth()) {
			health += regenAmount;
		}
	}

	@Override
	public int getMaxHealth() {
		return definition.getMaxHealth();
	}

	@Override
	public void process() {
		//System.out.println("Processing " + this);
	}

	@Override
	public String toString() {
		return definition.getName() + ":" + getUniqueID();
	}

}
