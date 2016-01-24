package com.git.cs309.mmoserver.characters.npc;

import com.git.cs309.mmoserver.characters.Character;
import com.git.cs309.mmoserver.util.ClosedIDSystem.IDTag;

public class NPC extends Character {
	private final NPCDefinition definition;

	public NPC(int x, int y, final NPCDefinition definition, final IDTag idTag) {
		super(x, y, idTag);
		this.definition = definition;
	}

}
