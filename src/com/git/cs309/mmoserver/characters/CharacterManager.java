package com.git.cs309.mmoserver.characters;

import java.util.HashSet;
import java.util.Set;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.util.TickReliant;

public final class CharacterManager extends Thread implements TickReliant {
	
	private static final Set<Character> characterSet = new HashSet<>();
	
	private static final CharacterManager SINGLETON = new CharacterManager();
	private volatile boolean tickFinished = true;
	
	public static synchronized void addCharacter(final Character character) {
		characterSet.add(character);
	}
	
	public static synchronized void removeCharacter(final Character character) {
		characterSet.remove(character);
	}
	
	private CharacterManager() {
		Main.addTickReliant(this);
		this.setName("CharacterManager");
		this.start();
	}
	
	@Override
	public void run() {
		final Object tickObject = Main.getTickObject();
		int tickCount = 0;
		boolean regenTick = false;
		while (Main.isRunning()) {
			synchronized (tickObject) {
				try {
					tickObject.wait();
				} catch (InterruptedException e) {
					// Don't care too much if it gets interrupted.
				}
			}
			tickFinished = false;
			regenTick = tickCount == Config.TICKS_PER_REGEN;
			if (regenTick) {
				tickCount = 0;
			}
			synchronized (characterSet) {
				for (Character character : characterSet) {
					if (regenTick) {
						character.applyRegen(Config.REGEN_AMOUNT);
					}
					character.process();
				}
			}
			tickFinished = true;
			tickCount++;
		}
	}

	@Override
	public boolean tickFinished() {
		return tickFinished;
	}

}
