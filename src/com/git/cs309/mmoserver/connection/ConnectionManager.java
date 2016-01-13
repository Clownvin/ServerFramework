package com.git.cs309.mmoserver.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.packets.Packet;
import com.git.cs309.mmoserver.packets.PacketHandler;
import com.git.cs309.mmoserver.packets.PacketType;
import com.git.cs309.mmoserver.util.TickReliant;

public final class ConnectionManager extends Thread implements TickReliant {
    private static final ConnectionManager SINGLETON = new ConnectionManager();
    private static volatile boolean tickFinished = false;
    private static final List<Connection> connections = new ArrayList<>();
    private static final Map<String, Connection> connectionMap = new HashMap<>(); // Can hold both username -> connection and ip -> connection.
    
    private ConnectionManager() {
	// Private so that this class can only be instantiated from within.
	Main.addTickReliant(this);
	this.start();
    }
    
    public static ConnectionManager getSingleton() {
	return SINGLETON;
    }
    
    public static void addConnection(final Connection connection) {
	synchronized (connectionMap) {
	    connectionMap.put(connection.getIP(), connection);
	}
	synchronized (connections) {
	    connections.add(connection);
	    System.out.println("Connection joined: "+connection.getIP());
	}
    }
    
    public static void sendPacketToAllConnections(final Packet packet) {
	synchronized (connections) {
	    for (Connection connection : connections) {
		connection.addOutgoingPacket(packet);
	    }
	}
    }
    
    public static Connection getConnectionForIP(final String ip) {
	synchronized (connections) {
	    for (Connection connection : connections) {
		if (connection.getIP().equals(ip)) {
		    return connection;
		}
	    }
	}
	return null;
    }
    
    @Override
    public void run() {
	final Object tickObject = Main.getTickObject();
	final List<Packet> packets = new ArrayList<>(Config.MAX_CONNECTIONS);
	while (Main.isRunning()) {
	    tickFinished = false;
	    synchronized (connections) {
		for (int i = 0; i < connections.size(); i++) {
		    Packet packet = connections.get(i).getPacket();
		    if (packet != null && packet.getPacketType() != PacketType.NULL_PACKET) {
			packets.add(packet);
		    }
		    if (connections.get(i).isDisconnected()) {
			connectionMap.remove(connections.get(i).getIP());
			System.out.println("Connection disconnected: "+connections.remove(i--).getIP());
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
	    tickFinished = true;
	    synchronized (tickObject) {
		try {
		    tickObject.wait();
		} catch (InterruptedException e) {
		    // We don't care too much if it gets interrupted.
		}
	    }
	}
    }

    @Override
    public boolean tickFinished() {
	return tickFinished;
    }
}
