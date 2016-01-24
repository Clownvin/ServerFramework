package com.git.cs309.mmoserver.characters.user;

public class UserAlreadyLoggedInException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -672269689273689788L;

	public UserAlreadyLoggedInException(final String message) {
		super(message);
	}
}
