package com.git.cs309.mmoserver.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.packets.Packet;
import com.git.cs309.mmoserver.packets.PacketFactory;
import com.git.cs309.mmoserver.util.CorruptDataException;
import com.git.cs309.mmoserver.util.EndOfStreamReachedException;
import com.git.cs309.mmoserver.util.StreamUtils;

public class Connection extends Thread {
    private final OutputStream output;
    private final InputStream input;
    private final Socket socket;
    private final String ip;
    private volatile boolean logoutRequested = false;
    private volatile boolean disconnected = false;
    private volatile Packet packet; // Making this volatile should allow for other threads to access it properly, as well as be changed by this thread properly.
    private volatile List<Packet> outgoingPackets = new ArrayList<>(10);

    //Throwing IOException because if the exception occurs here, there's not much we can really do.
    //It's better to let the ConnectionAcceptor try and handle this issue.
    public Connection(final Socket socket) throws IOException {
	this.output = socket.getOutputStream();
	this.input = socket.getInputStream();
	this.socket = socket;
	this.ip = socket.getInetAddress().getHostAddress();
	this.setName(this.toString());
	this.start();
    }

    public void addOutgoingPacket(Packet packet) {
	outgoingPackets.add(packet);
    }

    public synchronized void close() {
	try {
	    if (input != null)
		input.close();
	    if (output != null)
		output.close();
	    if (socket != null)
		socket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	logoutRequested = true;
    }

    @Override
    public boolean equals(Object other) {
	return other instanceof Connection && ((Connection) other).ip.equals(ip);
    }

    public String getIP() {
	return ip;
    }

    //Since packet is volatile, shouldn't need synchronized method block.
    public Packet getPacket() {
	return packet;
    }

    public boolean isDisconnected() {
	return disconnected;
    }

    @Override
    public void run() {
	int packetsThisTick;
	//ConnectionManager singleton to wait on.
	final ConnectionManager connectionManager = ConnectionManager.getSingleton();
	while (!socket.isClosed() && !logoutRequested) {
	    synchronized (connectionManager) {
		try {
		    connectionManager.wait(); // Wait for connection manager to notify us of new tick.
		} catch (InterruptedException e) {
		    // We shouldn't care too much if it gets interrupted.
		}
	    }
	    while (outgoingPackets.size() > 0) {
		try {
		    StreamUtils.writeBlockToStream(output, outgoingPackets.remove(0).toBytes());
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    packet = null;
	    packetsThisTick = 0;
	    try {
		do {
		    try {
			packet = PacketFactory.buildPacket(StreamUtils.readBlockFromStream(input), this);
		    } catch (CorruptDataException e) {
			System.err.println(e.getMessage());
		    } catch (EndOfStreamReachedException e) {
			System.err.println(e.getMessage());
			logoutRequested = true;
			break;
		    } catch (IOException e) {
			System.err.println(e.getMessage());
		    }
		    if (++packetsThisTick == Config.PACKETS_PER_TICK_BEFORE_KICK) {
			System.out.println(
				this + " exceeded the maximum packets per tick limit. Packets: " + packetsThisTick);
			logoutRequested = true;
			break;
		    }
		} while (!socket.isClosed() && input.available() != 0 && !logoutRequested);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	close();
	disconnected = true;
    }

    @Override
    public String toString() {
	return "Connection: " + ip;
    }
}
