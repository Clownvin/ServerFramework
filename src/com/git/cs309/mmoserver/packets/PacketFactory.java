package com.git.cs309.mmoserver.packets;

import com.git.cs309.mmoserver.connection.Connection;

public final class PacketFactory {
    
    private PacketFactory() {
	// To prevent instantiation.
    }
    
    public static Packet buildPacket(final byte[] bytes, final Connection source) {
	switch (bytes[0]) { // First byte should ALWAYS be the type byte.
	case 0: // Null packet
	    return null;
	case 1: // Message packet
	    return new MessagePacket(bytes, source);
	default:
	    return null;
	}
    }
}
