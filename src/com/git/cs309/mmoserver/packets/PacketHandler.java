package com.git.cs309.mmoserver.packets;

public final class PacketHandler {
    public static void handlePacket(final Packet packet) {
	switch (packet.getPacketType()) {
	case MESSAGE_PACKET:
	    System.out.println("Recieved message from connection \"" + packet.getConnection().getIP() + "\": "
		    + ((MessagePacket) packet).getMessage());
	    break;
	case LOGIN_PACKET:
	    LoginPacket loginPacket = (LoginPacket) packet;
	    System.out.println("Recieved login from connection \"" + packet.getConnection().getIP() + "\".");
	    System.out.println("Username: " + loginPacket.getUsername());
	    System.out.println("Password: " + loginPacket.getPassword());
	    break;
	case ERROR_PACKET:
	    ErrorPacket errorPacket = (ErrorPacket) packet;
	    System.out.println("Recieved error packet from connection \"" + packet.getConnection().getIP() + "\".");
	    System.out.println("Error code: " + errorPacket.getErrorCode());
	    System.out.println("Error message: " + errorPacket.getErrorMessage());
	    break;
	default:
	    System.out.println("No case for type: " + packet.getPacketType());
	}
    }
}
