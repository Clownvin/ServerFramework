package com.git.cs309.mmoserver.connection;

import java.io.IOException;
import java.net.ServerSocket;

import com.git.cs309.mmoserver.Main;

public final class ConnectionAcceptor extends Thread {
    private static final ConnectionAcceptor SINGLETON = new ConnectionAcceptor();
    private static int port = 6667; // A default port.

    private ConnectionAcceptor() {
	// To prevent instantiation.
    }

    public static ConnectionAcceptor getSingleton() {
	return SINGLETON;
    }

    public static void startAcceptor(final int port) {
	ConnectionAcceptor.port = port;
	SINGLETON.start();
    }

    @Override
    public void run() {
	ServerSocket acceptorSocket;
	try {
	    acceptorSocket = new ServerSocket(port);
	} catch (IOException e) {
	    e.printStackTrace();
	    Main.requestExit();
	    return;
	}
	if (acceptorSocket != null && !acceptorSocket.isClosed()) {
	    while (Main.isRunning() && !acceptorSocket.isClosed()) {
		try {
		    Connection connection = new Connection(acceptorSocket.accept());
		    if (!ConnectionManager.full() && !ConnectionManager.ipAlreadyConnected(connection.getIP())) {
			ConnectionManager.addConnection(connection);
		    } else {
			connection.close();
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		    System.err.println("Failed to accept new connection...");
		}
	    }
	}
	try {
	    if (acceptorSocket != null) {
		acceptorSocket.close();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
