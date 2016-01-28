package com.git.cs309.mmoserver.connection;

import java.io.IOException;
import java.net.ServerSocket;

import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.packets.ErrorPacket;

/**
 * 
 * @author Clownvin
 *
 *         Connection acceptor. Waits for connections, then encapsulates the
 *         sockets into Connection containers.
 */
public final class ConnectionAcceptor implements Runnable {
	private static final ConnectionAcceptor SINGLETON = new ConnectionAcceptor();
	private static Thread connectionAcceptorThread;
	private static int port = 6667; // A default port.

	public static ConnectionAcceptor getSingleton() {
		return SINGLETON;
	}

	/**
	 * Creates a new thread that runs the singleton object, and starts it.
	 * 
	 * @param port
	 *            port to start acceptor on.
	 */
	public static void startAcceptor(final int port) {
		ConnectionAcceptor.port = port; // Set port
		connectionAcceptorThread = new Thread(SINGLETON); // New thread.
		connectionAcceptorThread.setName("ConnectionAcceptor"); // Set name so it can be identified easily.
		connectionAcceptorThread.start(); // Start thread.
	}

	private ConnectionAcceptor() {
		// Can only be instantiated internally.
	}

	/**
	 * Run method.
	 */
	@Override
	public void run() {
		ServerSocket acceptorSocket; // Declare new ServerSocket variable.
		try {
			acceptorSocket = new ServerSocket(port); // Try and create socket.
		} catch (IOException e) {
			e.printStackTrace();
			Main.requestExit(); // It failed, so might as well just exit.
			return;
		}
		if (acceptorSocket != null && !acceptorSocket.isClosed()) { // If it's not null and not closed, proceed.
			System.out.println("Acceptor running on port: " + port);
			while (Main.isRunning() && !acceptorSocket.isClosed()) { // While open and server is running..
				try {
					Connection connection = new Connection(acceptorSocket.accept()); // Accept new socket, and immediately encapsulate.
					if (ConnectionManager.ipConnected(connection.getIP())) { // Is a socket with same IP already connected?
						connection.forceOutgoingPacket(new ErrorPacket(null, ErrorPacket.GENERAL_ERROR,
								"Failed to connect because your ip is already logged in.")); // Send error packet.
						connection.close(); // Close connection.
						continue;
					}
					if (ConnectionManager.full()) { // Are we at max connections?
						connection.forceOutgoingPacket(new ErrorPacket(null, ErrorPacket.GENERAL_ERROR,
								"Failed to connect because server is full.")); // Send error packet
						connection.close(); // Close
						continue;
					}
					ConnectionManager.addConnection(connection); // Made it to end, so add to manager.
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
