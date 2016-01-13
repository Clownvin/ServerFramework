package com.git.cs309.mmoserver.packets;

import com.git.cs309.mmoserver.util.ByteFormatted;

public abstract class Packet implements ByteFormatted {
    
    /*
     * Packet "toByte" convention:
     * first byte: packetType
     * final byte: '\n'
     * In between: Adequate representation of needed data.
     */
    
    public Packet(final byte[] buffer) {
	// Don't need to do anything here. It's abstract. Just forcing all implementing types to abide by this.
    }
    
    //Override this
    public PacketType getPacketType() {
	return PacketType.NULL_PACKET;
    }
}
