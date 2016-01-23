package com.git.cs309.mmoserver.characters.npc;

import com.git.cs309.mmoserver.characters.Character;

public class NPC extends Character {
	private final NPCDefinition definition;
	
	public NPC(int x, int y, final NPCDefinition definition) {
		super(x, y);
		this.definition = definition;
	}

}
