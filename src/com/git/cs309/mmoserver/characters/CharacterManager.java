package com.git.cs309.mmoserver.characters;

import java.util.HashSet;
import java.util.Set;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.util.TickReliant;

public final class CharacterManager extends TickReliant {

	private static final Set<Character> characterSet = new HashSet<>();

	private static final CharacterManager SINGLETON = new CharacterManager();

	public static synchronized void addCharacter(final Character character) {
		characterSet.add(character);
	}

	public static CharacterManager getSingleton() {
		return SINGLETON;
	}

	public static synchronized void removeCharacter(final Character character) {
		characterSet.remove(character);
	}

	private CharacterManager() {
		super("CharacterManager");
	}

	private void processCharacters(final boolean regenTick) {
		synchronized (characterSet) {
			for (Character character : characterSet) {
				if (regenTick) {
					character.applyRegen(Config.REGEN_AMOUNT);
				}
				character.process();
			}
		}
	}

	@Override
	public void run() {
		final Object tickObject = Main.getTickObject();
		int tickCount = 0;
		boolean regenTick = false;
		isStopped = false;
		while (Main.isRunning()) {
			synchronized (tickObject) {
				try {
					tickObject.wait();
				} catch (InterruptedException e) {
					// Don't care too much if it gets interrupted.
				}
				tickFinished = false;
			}
			setChanged();
			notifyObservers();
			regenTick = tickCount == Config.TICKS_PER_REGEN;
			if (regenTick) {
				tickCount = 0;
			}
			processCharacters(regenTick);
			tickCount++;
			tickFinished = true;
			setChanged();
			notifyObservers();
		}
		isStopped = true;
		setChanged();
		notifyObservers();
	}

}
