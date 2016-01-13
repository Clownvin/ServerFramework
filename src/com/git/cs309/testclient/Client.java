package com.git.cs309.testclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.git.cs309.mmoserver.packets.LoginPacket;

public class Client {

    //This demonstrates the server's function of ignoring all but the last packet per tick.
    //The message packet "Hello, World!" will be ignored entirely, while the login packet will be processed.
    //Not sending the login packet will make server process message packet instead. Sending them with a delay between will also
    //Cause them both to be processed.
    public static void main(String[] args) throws UnknownHostException, IOException {
	Socket socket = new Socket("localhost", 6667);
	String message = "Hello, world!";
	byte[] bytes = new byte[message.length() + 2];
	int index = 0;
	bytes[index++] = (byte) 1;
	for (char c : message.toCharArray()) {
	    bytes[index++] = (byte) c;
	}
	bytes[index] = '\n';
	socket.getOutputStream().write(bytes);
	socket.getOutputStream().flush();
	LoginPacket loginPacket = new LoginPacket(null, "joke", "youare");
	socket.getOutputStream().write(loginPacket.toBytes());
	socket.getOutputStream().flush();
	socket.close();
    }

}
