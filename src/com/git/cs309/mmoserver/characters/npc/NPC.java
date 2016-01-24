package com.git.cs309.mmoserver.characters.npc;

import com.git.cs309.mmoserver.characters.Character;
<<<<<<< HEAD
import com.git.cs309.mmoserver.util.ClosedIDSystem.IDTag;
=======
>>>>>>> bd014c8b8f92a308a091b0a131d5455c0a4447be

public class NPC extends Character {
	private final NPCDefinition definition;
	
<<<<<<< HEAD
	public NPC(int x, int y, final NPCDefinition definition, final IDTag idTag) {
		super(x, y, idTag);
=======
	public NPC(int x, int y, final NPCDefinition definition) {
		super(x, y);
>>>>>>> bd014c8b8f92a308a091b0a131d5455c0a4447be
		this.definition = definition;
	}

}
