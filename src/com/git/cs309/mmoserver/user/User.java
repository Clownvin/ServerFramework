package com.git.cs309.mmoserver.user;

import java.io.Serializable;

import com.git.cs309.mmoserver.connection.Connection;

public final class User implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 9016268542066197274L;

    private final String username;
    private final String password;
    private transient Connection connection; // Transient means serialization will ignore this variable.

    public User(final String username, final String password) {
	this.username = username;
	this.password = password;
    }

    public Connection getConnection() {
	return connection;
    }

    public String getPassword() {
	return password;
    }

    public String getUsername() {
	return username;
    }

    public void setConnection(final Connection connection) {
	this.connection = connection;
    }

    @Override
    public String toString() {
	return username;
    }
}
