package com.git.cs309.mmoserver.packets;

import com.git.cs309.mmoserver.characters.user.InvalidPasswordException;
import com.git.cs309.mmoserver.characters.user.UserAlreadyLoggedInException;
import com.git.cs309.mmoserver.characters.user.UserManager;

public final class PacketHandler {
	public static void handlePacket(final Packet packet) {
		switch (packet.getPacketType()) {
		case MESSAGE_PACKET:
			System.out.println("Recieved message from connection \"" + packet.getConnection().getIP() + "\": "
					+ ((MessagePacket) packet).getMessage());
			break;
		case LOGIN_PACKET:
			LoginPacket loginPacket = (LoginPacket) packet;
			try {
				if (!UserManager.logIn(loginPacket)) {
					System.err.println("Failed to log in user \"" + loginPacket.getUsername() + "\".");
					loginPacket.getConnection().addOutgoingPacket(
							new ErrorPacket(loginPacket.getConnection(), ErrorPacket.LOGIN_ERROR, "Login failed."));
				} else {
					loginPacket.getConnection()
							.addOutgoingPacket(new EventPacket(loginPacket.getConnection(), EventPacket.LOGIN_SUCCESS));
				}
			} catch (UserAlreadyLoggedInException e) {
				System.err.println(e.getMessage());
				loginPacket.getConnection()
						.addOutgoingPacket(new ErrorPacket(loginPacket.getConnection(), ErrorPacket.LOGIN_ERROR,
								"User with username \"" + loginPacket.getUsername() + "\" is already logged in."));
			} catch (InvalidPasswordException e) {
				System.err.println(e.getMessage());
				loginPacket.getConnection()
						.addOutgoingPacket(new ErrorPacket(loginPacket.getConnection(), ErrorPacket.LOGIN_ERROR,
								"Password for user \"" + loginPacket.getUsername() + "\" does not match."));
			}
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
