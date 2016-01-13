package com.git.cs309.mmoserver.connection;

import java.util.ArrayList;
import java.util.List;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.packets.Packet;
import com.git.cs309.mmoserver.util.TickReliant;

public final class ConnectionManager extends Thread implements TickReliant {
    private static final ConnectionManager SINGLETON = new ConnectionManager();
    private static volatile boolean tickFinished = false;
    private static final List<Connection> connections = new ArrayList<>();
    
    private ConnectionManager() {
	// Private so that this class can only be instantiated from within.
	Main.addTickReliant(this);
	this.start();
    }
    
    public static ConnectionManager getSingleton() {
	return SINGLETON;
    }
    
    public static void addConnection(final Connection connection) {
	synchronized (connections) {
	    connections.add(connection);
	    System.out.println("Connection joined: "+connection.getIP());
	}
    }
    
    @Override
    public void run() {
	final Object tickObject = Main.getTickObject();
	final List<Packet> packets = new ArrayList<>(Config.MAX_CONNECTIONS);
	while (Main.isRunning()) {
	    tickFinished = false;
	    synchronized (connections) {
		for (int i = 0; i < connections.size(); i++) {
		    packets.add(connections.get(i).getPacket());
		    if (connections.get(i).isDisconnected()) {
			System.out.println("Connection disconnected: "+connections.remove(i--).getIP());
		    }
		}
	    }
	    synchronized (SINGLETON) {
		SINGLETON.notifyAll();
	    }
	    //TODO Packet stuff.
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
