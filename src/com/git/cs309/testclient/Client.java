package com.git.cs309.testclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.git.cs309.mmoserver.packets.LoginPacket;
import com.git.cs309.mmoserver.packets.MessagePacket;
import com.git.cs309.mmoserver.packets.TestPacket;
import com.git.cs309.mmoserver.util.StreamUtils;

public class Client {
	//Just a basic client for various server/client interaction testing.
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Client client = new Client();
	}

	private final Socket socket;

	private Client() throws UnknownHostException, IOException, InterruptedException {
		socket = new Socket("localhost", 6667);
		//while (true) {
		LoginPacket packet = new LoginPacket(null, "Clowbn", "Clown");
		StreamUtils.writeBlockToStream(socket.getOutputStream(), packet.toBytes());
		Thread.sleep(500);
		int wait = 0;
		while (true) {
			if (wait++ == 100) {
				StreamUtils.writeBlockToStream(socket.getOutputStream(),
						new TestPacket(null, TestPacket.EXCEPTION_TEST).toBytes());
				wait = 0;
			} else {
				StreamUtils.writeBlockToStream(socket.getOutputStream(),
						new MessagePacket(null, (byte) 0, "Lol....").toBytes());
			}
			Thread.sleep(50);
		}
		//}

	}

}
