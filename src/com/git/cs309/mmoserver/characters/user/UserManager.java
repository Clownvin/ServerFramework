package com.git.cs309.mmoserver.characters.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.cycle.CycleProcess;
import com.git.cs309.mmoserver.cycle.CycleProcessManager;
import com.git.cs309.mmoserver.packets.LoginPacket;

public final class UserManager {
	private static final Hashtable<String, User> USER_TABLE = new Hashtable<>();
	private static final Hashtable<String, User> IP_TABLE = new Hashtable<>();

	static {
		CycleProcessManager.addProcess(new CycleProcess() {
			private int tick = 0;

			private final Thread AUTO_SAVE_THREAD = new Thread() {
				@Override
				public void run() {
					while (Main.isRunning()) {
						synchronized (AUTO_SAVE_THREAD) {
							try {
								AUTO_SAVE_THREAD.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						long start = System.currentTimeMillis();
						System.out.println("Saved " + USER_TABLE.size() + " users in "
								+ (System.currentTimeMillis() - start) + "ms.");
					}
				}
			};

			@Override
			public void end() {
				System.out.println("User save processes ended.");
			}

			@Override
			public boolean finished() {
				return !Main.isRunning();
			}

			@Override
			public void process() {
				if (!AUTO_SAVE_THREAD.isAlive()) {
					AUTO_SAVE_THREAD.start();
				}
				if (++tick == Config.TICKS_PER_AUTO_SAVE) {
					tick = 0;
					synchronized (AUTO_SAVE_THREAD) {
						AUTO_SAVE_THREAD.notifyAll();
					}
				}
			}

		});
	}

	public static void saveAllUsers() {
		for (String key : USER_TABLE.keySet()) {
			try {
				saveUser(USER_TABLE.get(key));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static File getUserFile(final String username) {
		return new File(Config.USER_FILE_PATH + username.toLowerCase() + ".user");
	}

	public static User getUserForIP(final String ip) {
		return IP_TABLE.get(ip);
	}

	public static User getUserForUsername(final String username) {
		return USER_TABLE.get(username);
	}

	public static boolean isLoggedIn(final String username) {
		return USER_TABLE.containsKey(username.toLowerCase());
	}

	private static User loadUser(final File userFile) throws FileNotFoundException, IOException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(userFile));
		User user = null;
		try {
			user = (User) in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		in.close();
		return user;
	}

	public static boolean logIn(final LoginPacket loginPacket)
			throws UserAlreadyLoggedInException, InvalidPasswordException {
		if (isLoggedIn(loginPacket.getUsername())) {
			throw new UserAlreadyLoggedInException(
					"The user \"" + loginPacket.getUsername() + "\" is already logged in.");
		}
		File userFile = getUserFile(loginPacket.getUsername());
		User user;
		if (userFile.exists()) {
			try {
				user = loadUser(userFile);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			if (!user.getPassword().equals(loginPacket.getPassword())) {
				throw new InvalidPasswordException("The password does not match the password registered to the user \""
						+ user.getUsername() + "\".");
			}
			user.setConnection(loginPacket.getConnection());
			USER_TABLE.put(user.getUsername().toLowerCase(), user);
			IP_TABLE.put(loginPacket.getConnection().getIP(), user);
		} else {
			user = new User(loginPacket.getUsername(), loginPacket.getPassword());
			user.setConnection(loginPacket.getConnection());
			USER_TABLE.put(user.getUsername().toLowerCase(), user);
			IP_TABLE.put(loginPacket.getConnection().getIP(), user);
		}
		System.out.println("User " + user + " logged in.");
		return true;
	}

	public static boolean logOut(final String username) {
		if (isLoggedIn(username)) {
			try {
				saveUser(USER_TABLE.get(username.toLowerCase()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			User user = USER_TABLE.remove(username.toLowerCase());
			IP_TABLE.remove(user.getConnection().getIP());
<<<<<<< HEAD
			user.cleanUp();
=======
>>>>>>> bd014c8b8f92a308a091b0a131d5455c0a4447be
			System.out.println("User " + user + " logged out.");
		}
		return true;
	}

	private static void saveUser(final User user) throws FileNotFoundException, IOException {
		File userSaveDirectory = new File(Config.USER_FILE_PATH);
		if (!userSaveDirectory.exists()) {
			userSaveDirectory.mkdirs();
		}
		File userFile = getUserFile(user.getUsername());
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userFile));
		out.writeObject(user);
		out.close();
	}

	private UserManager() {
		//To prevent instantiation, since this is a static utility class
	}
}
