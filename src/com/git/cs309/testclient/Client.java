package com.git.cs309.testclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) throws UnknownHostException, IOException {
	Socket socket = new Socket("localhost", 6667);
	String message = "Hello, world!";
	byte[] bytes = new byte[message.length() + 2];
	int index = 0;
	bytes[index++] = '1';
	for (char c : message.toCharArray()) {
	    bytes[index++] = (byte) c;
	}
	bytes[index] = '\n';
	socket.getOutputStream().write(bytes);
	socket.getOutputStream().flush();
    }

}
