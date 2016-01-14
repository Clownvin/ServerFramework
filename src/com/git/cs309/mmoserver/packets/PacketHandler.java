package com.git.cs309.mmoserver.packets;

import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.cycle.CycleProcess;
import com.git.cs309.mmoserver.cycle.CycleProcessManager;

public final class PacketHandler {
    public static void handlePacket(final Packet packet) {
	switch (packet.getPacketType()) {
	case MESSAGE_PACKET:
	    System.out.println("Recieved message from connection \"" + packet.getConnection().getIP() + "\": "
		    + ((MessagePacket) packet).getMessage());
	    CycleProcessManager.addProcess(new CycleProcess() {
		long startTick = Main.getTickCount();

		@Override
		public void end() {
		    System.out.println("Ending process");
		}

		@Override
		public boolean finished() {
		    return Main.getTickCount() - startTick >= 5;
		}

		@Override
		public void process() {
		    System.out.println("Handling process");
		}

	    });
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
