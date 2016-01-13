package com.git.cs309.mmoserver.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.packets.Packet;
import com.git.cs309.mmoserver.packets.PacketFactory;

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
	this.start();
    }

    //TODO Consider changing packet formats, so that first 4 bytes represent packet length. Will solve some potential issues.
    @Override
    public void run() {
	byte[] buffer;
	int bufferIndex = 0;
	int packetsThisTick = 0;
	boolean success = true;
	final ConnectionManager connectionManager = ConnectionManager.getSingleton();
	while (socket.isConnected() && !logoutRequested) {
	    synchronized (connectionManager) {
		try {
		    connectionManager.wait(); // Wait for connection manager to notify us of new tick.
		} catch (InterruptedException e) {
		    // We shouldn't care too much if it gets interrupted.
		}
	    }
	    while (outgoingPackets.size() > 0) {
		try {
		    output.write(outgoingPackets.remove(0).toBytes());
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    packet = null;
	    packetsThisTick = 0;
	    try {
		do {
		    success = true;
		    bufferIndex = 0;
		    buffer = new byte[Config.MAX_PACKET_BYTES];
		    int packetLength = 2;
		    if ((buffer[bufferIndex++] = (byte) input.read()) == -1) { // We're getting EOF character.
			int count = 0;
			packetLength++;
			while ((buffer[bufferIndex++] = (byte) input.read()) == -1) {
			    packetLength++;
			    if (count++ == 20) {
				logoutRequested = true;
				success = false;
				break;
			    }
			}
		    }
		    while (success && (buffer[bufferIndex++] = (byte) input.read()) != '\n') {
			packetLength++;
			if (bufferIndex == buffer.length) {
			    success = false;
			}
		    }
		    if (success) {
			packetsThisTick++;
			packet = PacketFactory.buildPacket(Arrays.copyOfRange(buffer, 0, packetLength), this);
		    }
		    if (packetsThisTick > Config.PACKETS_PER_TICK_BEFORE_KICK) {
			logoutRequested = true;
			break;
		    }
		} while (input.available() != 0 && !logoutRequested);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	close();
	disconnected = true;
    }

    public void close() {
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

    public void addOutgoingPacket(Packet packet) {
	outgoingPackets.add(packet);
    }

    //Since packet is volatile, shouldn't need synchronized method block.
    public Packet getPacket() {
	return packet;
    }

    public boolean isDisconnected() {
	return disconnected;
    }

    public String getIP() {
	return ip;
    }

    @Override
    public String toString() {
	return "Connection:" + ip;
    }

    @Override
    public boolean equals(Object other) {
	return other instanceof Connection && ((Connection) other).ip.equals(ip);
    }
}
