package com.git.cs309.mmoserver.packets;

public class MessagePacket extends Packet {
    private final String message;
    
    public MessagePacket(byte[] buffer) {
	super(buffer);
	char[] chars = new char[buffer.length - 2];
	for (int i = 1, j = 0; i < buffer.length; i++, j++) {
	    chars[j] = (char) buffer[i];
	}
	message = String.valueOf(chars);
    }
    
    public String getMessage() {
	return message;
    }

    @Override
    public byte[] toBytes() {
	byte[] bytes = new byte[message.length() + 2];
	int index = 0;
	bytes[index++] = getPacketType().getTypeByte();
	for (char c : message.toCharArray()) {
	    bytes[index++] = (byte) c;
	}
	bytes[index] = '\n';
	return bytes;
    }
    
    @Override
    public PacketType getPacketType() {
	return PacketType.MESSAGE_PACKET;
    }
}
