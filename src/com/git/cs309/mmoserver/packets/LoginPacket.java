package com.git.cs309.mmoserver.packets;

import com.git.cs309.mmoserver.connection.Connection;

public class LoginPacket extends Packet {
    private final String username, password;

    public LoginPacket(final byte[] bytes, final Connection connection) {
	super(connection);
	int index = 1;
	char[] charBuffer = new char[(bytes[index++] << 24) | (bytes[index++] << 16) | (bytes[index++] << 8)
		| bytes[index++]];
	for (int i = 0; i < charBuffer.length; i++) {
	    charBuffer[i] = (char) bytes[index++];
	}
	this.username = String.valueOf(charBuffer);
	charBuffer = new char[(bytes[index++] << 24) | (bytes[index++] << 16) | (bytes[index++] << 8) | bytes[index++]];
	for (int i = 0; i < charBuffer.length; i++) {
	    charBuffer[i] = (char) bytes[index++];
	}
	this.password = String.valueOf(charBuffer);
    }

    public LoginPacket(final Connection connection, final String username, final String password) {
	super(connection);
	this.username = username;
	this.password = password;
    }

    @Override
    public PacketType getPacketType() {
	return PacketType.LOGIN_PACKET;
    }

    public String getPassword() {
	return password;
    }

    public String getUsername() {
	return username;
    }

    @Override
    public byte[] toBytes() {
	byte[] bytes = new byte[username.length() + password.length() + 9]; // 2xIntLengths + 1 byte identifier
	int index = 0;
	bytes[index++] = getPacketType().getTypeByte();
	bytes[index++] = (byte) (username.length() >> 24);
	bytes[index++] = (byte) ((username.length() >> 16) & 0xFF);
	bytes[index++] = (byte) ((username.length() >> 8) & 0xFF);
	bytes[index++] = (byte) (username.length() & 0xFF);
	for (char c : username.toCharArray()) {
	    bytes[index++] = (byte) c;
	}
	bytes[index++] = (byte) (password.length() >> 24);
	bytes[index++] = (byte) ((password.length() >> 16) & 0xFF);
	bytes[index++] = (byte) ((password.length() >> 8) & 0xFF);
	bytes[index++] = (byte) (password.length() & 0xFF);
	for (char c : password.toCharArray()) {
	    bytes[index++] = (byte) c;
	}
	return bytes;
    }

}
