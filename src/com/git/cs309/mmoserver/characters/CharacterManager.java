package com.git.cs309.mmoserver.characters;

import java.util.HashSet;
import java.util.Set;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.util.TickProcess;

/**
 * 
 * @author Clownvin
 *
 *         TickReliant implementation that manages all Character objects.
 */
public final class CharacterManager extends TickProcess {

	private static final Set<Character> characterSet = new HashSet<>(); // All registered characters
	private static final CharacterManager SINGLETON = new CharacterManager(); // Single instance of this class

	/**
	 * Adds a character object to the character set. In theory, this should only
	 * need to be called from the Character constructor.
	 * 
	 * @param character
	 *            character to add to set.
	 */
	public static synchronized void addCharacter(final Character character) { // Add new character to characterSet
		characterSet.add(character);
	}

	/**
	 * Gets the single instance of this class.
	 * 
	 * @return the singleton for this class.
	 */
	public static CharacterManager getSingleton() { // Get the singleton
		return SINGLETON;
	}

	/**
	 * Removes character from the set.
	 * 
	 * @param character
	 *            character to remove from set.
	 */
	public static synchronized void removeCharacter(final Character character) { // Remove character from set
		characterSet.remove(character);
	}

	private CharacterManager() {
		super("CharacterManager");
	}

	/**
	 * Processes characters. Should be called each tick, from the tickTask
	 * method.
	 * 
	 * @param regenTick
	 *            determines whether or not to apply regeneration this tick.
	 */
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
	protected void tickTask() {
		processCharacters(Main.getTickCount() % Config.TICKS_PER_REGEN == 0); // Process characters.
	}

}
