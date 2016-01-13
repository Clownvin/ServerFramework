package com.git.cs309.mmoserver.packets;

public enum PacketType {
    MESSAGE_PACKET((byte) 1), NULL_PACKET((byte) 0);
    
    private final byte typeByte;
    
    private PacketType(final byte typeByte) {
	this.typeByte = typeByte;
    }
    
    public byte getTypeByte() {
	return typeByte;
    }
}
