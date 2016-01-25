package com.git.cs309.mmoserver.connection;

import java.io.IOException;
import java.net.ServerSocket;

import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.packets.ErrorPacket;

public final class ConnectionAcceptor implements Runnable {
	private static final ConnectionAcceptor SINGLETON = new ConnectionAcceptor();
	private static Thread connectionAcceptorThread;
	private static int port = 6667; // A default port.

	public static ConnectionAcceptor getSingleton() {
		return SINGLETON;
	}

	public static void startAcceptor(final int port) {
		ConnectionAcceptor.port = port;
		connectionAcceptorThread = new Thread(SINGLETON);
		connectionAcceptorThread.setName("ConnectionAcceptor");
		connectionAcceptorThread.start();
	}

	private ConnectionAcceptor() {

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
			System.out.println("Acceptor running on port: " + port);
			while (Main.isRunning() && !acceptorSocket.isClosed()) {
				try {
					Connection connection = new Connection(acceptorSocket.accept());
					if (ConnectionManager.ipAlreadyConnected(connection.getIP())) {
						connection.forceOutgoingPacket(new ErrorPacket(null, ErrorPacket.GENERAL_ERROR,
								"Failed to connect because your ip is already logged in."));
						connection.close();
						continue;
					}
					if (ConnectionManager.full()) {
						connection.forceOutgoingPacket(new ErrorPacket(null, ErrorPacket.GENERAL_ERROR,
								"Failed to connect because server is full."));
						connection.close();
						continue;
					}
					ConnectionManager.addConnection(connection);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Failed to accept new connection...");
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
