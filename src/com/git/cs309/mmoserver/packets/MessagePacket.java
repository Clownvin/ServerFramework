package com.git.cs309.mmoserver.packets;

import com.git.cs309.mmoserver.connection.Connection;

public class MessagePacket extends Packet {
    private final String message;

    public MessagePacket(final byte[] buffer, final Connection source) {
	super(source);
	char[] chars = new char[buffer.length - 1];
	for (int i = 1, j = 0; i < buffer.length; i++, j++) {
	    chars[j] = (char) buffer[i];
	}
	message = String.valueOf(chars);
    }

    public MessagePacket(final Connection destination, final String message) {
	super(destination);
	this.message = message;
    }

    public String getMessage() {
	return message;
    }

    @Override
    public PacketType getPacketType() {
	return PacketType.MESSAGE_PACKET;
    }

    @Override
    public byte[] toBytes() {
	byte[] bytes = new byte[message.length() + 1];
	int index = 0;
	bytes[index++] = getPacketType().getTypeByte();
	for (char c : message.toCharArray()) {
	    bytes[index++] = (byte) c;
	}
	return bytes;
    }
}
