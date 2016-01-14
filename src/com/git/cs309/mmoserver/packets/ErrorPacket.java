package com.git.cs309.mmoserver.packets;

import com.git.cs309.mmoserver.connection.Connection;

public class ErrorPacket extends Packet {

    private final int errorCode;
    private final String errorMessage;
    
    public ErrorPacket(final byte[] bytes, final Connection source) {
	super(source);
	int index = 1;
	errorCode = (bytes[index++] << 24) | (bytes[index++] << 16) | (bytes[index++] << 8) | bytes[index++];
	String buffer = "";
	for (; index < bytes.length; index++) {
	    buffer += (char) bytes[index];
	}
	errorMessage = buffer;
    }
    
    public ErrorPacket(final Connection destination, final int errorCode, final String errorMessage) {
	super(destination);
	this.errorCode = errorCode;
	this.errorMessage = errorMessage;
    }
    
    public String getErrorMessage() {
	return errorMessage;
    }
    
    public int getErrorCode() {
	return errorCode;
    }

    @Override
    public byte[] toBytes() {
	byte[] bytes = new byte[5 + errorMessage.length()]; // 4 bytes error code, 1 byte packet type, rest errorMessage
	int index = 0;
	bytes[index++] = getPacketType().getTypeByte();
	bytes[index++] = (byte) (errorCode >> 24);
	bytes[index++] = (byte) ((errorCode >> 16) & 0xFF);
	bytes[index++] = (byte) ((errorCode >> 8) & 0xFF);
	bytes[index++] = (byte) (errorCode & 0xFF);
	for (char c : errorMessage.toCharArray()) {
	    bytes[index++] = (byte) c;
	}
	return bytes;
    }

    @Override
    public PacketType getPacketType() {
	return PacketType.ERROR_PACKET;
    }

}
