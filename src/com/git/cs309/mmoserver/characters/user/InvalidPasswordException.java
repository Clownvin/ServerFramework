package com.git.cs309.mmoserver.characters.user;

public class InvalidPasswordException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 904855812252022218L;

	public InvalidPasswordException(final String message) {
		super(message);
	}
}
