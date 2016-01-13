package com.git.cs309.mmoserver.packets;

import com.git.cs309.mmoserver.connection.Connection;
import com.git.cs309.mmoserver.util.ByteFormatted;

public abstract class Packet implements ByteFormatted {
    protected final Connection source;
    /*
     * Packet "toByte" convention: first byte: packetType final byte: '\n' In
     * between: Adequate representation of needed data.
     */

    public Packet(final Connection source) {
	this.source = source;
    }

    public Connection getConnection() {
	return source;
    }

    //Harharhar, forgot to make this abstract.
    public abstract PacketType getPacketType();
}
