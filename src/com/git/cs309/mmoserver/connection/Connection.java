package com.git.cs309.mmoserver.connection;

import java.io.IOException;
import java.net.Socket;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.Main;
import com.git.cs309.mmoserver.characters.user.User;
import com.git.cs309.mmoserver.characters.user.UserManager;
import com.git.cs309.mmoserver.packets.PacketFactory;
import com.git.cs309.mmoserver.util.CorruptDataException;
import com.git.cs309.mmoserver.util.EndOfStreamReachedException;
import com.git.cs309.mmoserver.util.StreamUtils;

/**
 * 
 * @author Clownvin
 *
 * 
 */
public class Connection extends AbstractConnection {
	private volatile boolean closeRequested = false;

	public Connection(Socket socket) throws IOException {
		super(socket);
	}

	@Override
	public synchronized void close() {
		try {
			if (input != null)
				input.close();
			if (output != null)
				output.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		disconnected = true;
		closeRequested = true;
		synchronized (outgoingPackets) {
			outgoingPackets.notifyAll();
		}
	}

	@Override
	public void run() {
		int packetsThisTick;
		//ConnectionManager singleton to wait on.
		final ConnectionManager connectionManager = ConnectionManager.getSingleton();
		while (!socket.isClosed() && !closeRequested) {
			synchronized (connectionManager) {
				try {
					connectionManager.wait(); // Wait for connection manager to notify us of new tick.
				} catch (InterruptedException e) {
					// We shouldn't care too much if it gets interrupted.
				}
			}
			packet = null;
			packetsThisTick = 0;
			try {
				do {
					try {
						packet = PacketFactory.buildPacket(StreamUtils.readBlockFromStream(input), this);
					} catch (CorruptDataException | NegativeArraySizeException | ArrayIndexOutOfBoundsException e) { // Just general exception that might occur for bad packets.
						System.err.println(e.getMessage());
					} catch (EndOfStreamReachedException e) { // End of the stream was reached, meaning there's no more data, ever.
						System.err.println(e.getMessage());
						closeRequested = true;
						break;
					} catch (IOException e) { // Should only be Connection reset
						System.err.println(e.getMessage());
						closeRequested = true;
						break;
					}
					if (!Main.wasPaused() && ++packetsThisTick == Config.PACKETS_PER_TICK_BEFORE_KICK) {
						System.out.println(
								this + " exceeded the maximum packets per tick limit. Packets: " + packetsThisTick);
						closeRequested = true;
						break;
					}
				} while (!socket.isClosed() && input.available() != 0 && !closeRequested);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		User user = UserManager.getUserForIP(ip);
		if (user != null) {
			UserManager.logOut(user.getUsername());
		}
		close();
		disconnected = true;
	}
}
