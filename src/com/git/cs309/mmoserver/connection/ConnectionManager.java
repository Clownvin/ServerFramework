package com.git.cs309.mmoserver.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.packets.Packet;
import com.git.cs309.mmoserver.packets.PacketHandler;
import com.git.cs309.mmoserver.packets.PacketType;
import com.git.cs309.mmoserver.util.TickReliant;

public final class ConnectionManager extends TickReliant {
	private static final ConnectionManager SINGLETON = new ConnectionManager();
	private static final List<Connection> connections = new ArrayList<>(Config.MAX_CONNECTIONS);
	private static final Map<String, Connection> connectionMap = new HashMap<>(); // Could hold both username -> connection and ip -> connection. But will probably only hold ip -> connection, since that's all that's needed.

	public static void addConnection(final Connection connection) {
		synchronized (connectionMap) {
			connectionMap.put(connection.getIP(), connection);
		}
		synchronized (connections) {
			connections.add(connection);
			System.out.println("Connection joined: " + connection.getIP());
		}
	}

	public static boolean full() {
		synchronized (connections) {
			return connections.size() == Config.MAX_CONNECTIONS;
		}
	}

	public static Connection getConnectionForIP(final String ip) {
		synchronized (connectionMap) {
			return connectionMap.get(ip);
		}
	}

	public static ConnectionManager getSingleton() {
		return SINGLETON;
	}

	public static boolean ipAlreadyConnected(String ip) {
		synchronized (connectionMap) {
			return connectionMap.containsKey(ip);
		}
	}

	public static Connection removeConnection(final Connection connection) {
		synchronized (connectionMap) {
			connectionMap.remove(connection.getIP());
		}
		synchronized (connections) {
			connections.remove(connection);
		}
		return connection;
	}

	public static Connection removeConnection(final String ip) {
		synchronized (connectionMap) {
			connectionMap.remove(ip);
		}
		synchronized (connections) {
			for (int i = 0; i < connections.size(); i++) {
				if (connections.get(i).getIP().equals(ip)) {
					return connections.remove(i);
				}
			}
		}
		return null;
	}

	public static void sendPacketToAllConnections(final Packet packet) {
		synchronized (connections) {
			for (Connection connection : connections) {
				connection.addOutgoingPacket(packet);
			}
		}
	}

	private ConnectionManager() {
		super("ConnectionManager");
	}

	@Override
	protected void tickTask() {
		final List<Packet> packets = new ArrayList<>(Config.MAX_CONNECTIONS);
		synchronized (connections) {
			for (int i = 0; i < connections.size(); i++) {
				if (connections.get(i).isDisconnected()) {
					System.out.println("Connection disconnected: " + removeConnection(connections.get(i--)).getIP());
					continue;
				}
				Packet packet = connections.get(i).getPacket();
				if (packet != null && packet.getPacketType() != PacketType.NULL_PACKET) {
					packets.add(packet);
				}
			}
		}
		synchronized (SINGLETON) {
			SINGLETON.notifyAll();
		}
		for (Packet packet : packets) {
			PacketHandler.handlePacket(packet);
		}
		packets.clear();
	}
}
