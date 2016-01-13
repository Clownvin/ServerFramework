package com.git.cs309.mmoserver.packets;

public final class PacketFactory {
    
    private PacketFactory() {
	// To prevent instantiation.
    }
    
    public static Packet buildPacket(byte[] bytes) {
	switch (bytes[0]) { // First byte should ALWAYS be the type byte.
	case 0: // Null packet
	    return null;
	case 1: // Message packet
	    return new MessagePacket(bytes);
	default:
	    return null;
	}
    }
}
