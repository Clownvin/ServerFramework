package com.git.cs309.testclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.git.cs309.mmoserver.packets.ErrorPacket;
import com.git.cs309.mmoserver.packets.LoginPacket;
import com.git.cs309.mmoserver.packets.MessagePacket;
import com.git.cs309.mmoserver.util.StreamUtils;

public class Client {

    //Just a basic client for various server/client interaction testing.
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
	for (int i = 0; i < 2000; i++) {
	Socket socket = new Socket("localhost", 6667);
	LoginPacket loginPacket = new LoginPacket(null, "joke", "youare");
	MessagePacket messagePacket = new MessagePacket(null, "This is a message packet.");
	ErrorPacket errorPacket = new ErrorPacket(null, 1, "This is an error packet.");
	byte[] randomBlock = { 0, 0, 52, 51, 32 };
	//			   	     1    {               2}  {               3}
	//   1: Checksum
	//   2: Length block
	//   3: Data block
	byte[] invalidChecksum = new byte[] { 0xA, 0x0, 0x0, 0x0, 0x4, 0xF, 0xF, 0xF, 0xF };
	socket.getOutputStream().write(randomBlock);
	socket.getOutputStream().flush();
	Thread.sleep(110);
	socket.getOutputStream().write(invalidChecksum);
	socket.getOutputStream().flush();
	Thread.sleep(110);
	StreamUtils.writeBlockToStream(socket.getOutputStream(), loginPacket.toBytes());
	Thread.sleep(110);
	StreamUtils.writeBlockToStream(socket.getOutputStream(), messagePacket.toBytes());
	Thread.sleep(110);
	StreamUtils.writeBlockToStream(socket.getOutputStream(), errorPacket.toBytes());
	socket.close();
	Thread.sleep(1100);
	}
    }

}
