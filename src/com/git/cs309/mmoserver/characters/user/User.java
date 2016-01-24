package com.git.cs309.mmoserver.characters.user;

import java.io.Serializable;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.characters.Character;
import com.git.cs309.mmoserver.connection.AbstractConnection;
import com.git.cs309.mmoserver.util.ClosedIDSystem;

public final class User extends Character implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9016268542066197274L;

	private final String username;
	private final String password;
	private transient AbstractConnection connection; // Transient means serialization will ignore this variable.

	public User(final String username, final String password) {
		super(Config.PLAYER_START_X, Config.PLAYER_START_Y, ClosedIDSystem.getTag());
		this.username = username;
		this.password = password;
	}

	@Override
	public void applyDamage(int damageAmount) {
		health -= damageAmount;
		if (health <= 0) {
			isDead = true;
		}
	}

	@Override
	public void applyRegen(int regenAmount) {
		if (health + regenAmount <= getMaxHealth()) {
			health += regenAmount;
		}
	}

	public AbstractConnection getConnection() {
		return connection;
	}

	@Override
	public int getMaxHealth() {
		return 100; // 100 for now. Change later if need be.
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public void process() {
		//System.out.println("Processing " + this);
	}

	public void setConnection(final AbstractConnection connection) {
		this.connection = connection;
	}

	@Override
	public String toString() {
		return username + ":" + getUniqueID();
	}
}
