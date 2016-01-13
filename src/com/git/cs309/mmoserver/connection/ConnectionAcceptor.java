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
	try {
	    ServerSocket acceptorSocket = new ServerSocket(port);
	    while (Main.isRunning()) {
		 ConnectionManager.addConnection(new Connection(acceptorSocket.accept()));   
	    }
	    acceptorSocket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
