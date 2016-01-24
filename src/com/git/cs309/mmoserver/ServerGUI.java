package com.git.cs309.mmoserver;

import javax.swing.JFrame;

public class ServerGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3687402786121828571L;
	private static final ServerGUI SINGLETON = new ServerGUI();

	public static ServerGUI getSingleton() {
		return SINGLETON;
	}

	private ServerGUI() {

	}
}
