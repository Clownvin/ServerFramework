package com.git.cs309.testclient;

import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.git.cs309.mmoserver.packets.ErrorPacket;
import com.git.cs309.mmoserver.packets.LoginPacket;
import com.git.cs309.mmoserver.packets.MessagePacket;
import com.git.cs309.mmoserver.util.StreamUtils;

public class Client {
    private final Socket socket;
    
    private Client() throws UnknownHostException, IOException, InterruptedException {
	socket = new Socket("localhost", 6667);
	while (true) {
	    LoginPacket packet = new LoginPacket(null, "Clowbn", "Clown");
	    StreamUtils.writeBlockToStream(socket.getOutputStream(), packet.toBytes());
	    Thread.sleep(5);
	}
	
    }

    //Just a basic client for various server/client interaction testing.
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
	Client client = new Client();
    }

}
