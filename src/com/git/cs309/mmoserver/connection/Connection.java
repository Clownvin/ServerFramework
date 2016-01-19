package com.git.cs309.mmoserver.connection;

import java.io.IOException;
import java.net.Socket;

import com.git.cs309.mmoserver.Config;
import com.git.cs309.mmoserver.packets.PacketFactory;
import com.git.cs309.mmoserver.user.User;
import com.git.cs309.mmoserver.user.UserManager;
import com.git.cs309.mmoserver.util.CorruptDataException;
import com.git.cs309.mmoserver.util.EndOfStreamReachedException;
import com.git.cs309.mmoserver.util.StreamUtils;

public class Connection extends AbstractConnection {
	public Connection(Socket socket) throws IOException {
		super(socket);
		// TODO Auto-generated constructor stub
	}

	private volatile boolean closeRequested = false;

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
					} catch (CorruptDataException e) {
						System.err.println(e.getMessage());
					} catch (EndOfStreamReachedException e) {
						System.err.println(e.getMessage());
						closeRequested = true;
						break;
					} catch (IOException e) {
						System.err.println(e.getMessage());
					}
					if (++packetsThisTick == Config.PACKETS_PER_TICK_BEFORE_KICK) {
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
		try {
			sleep(Config.TICK_DELAY); // Sleeping to ensure that managers are up to date.
		} catch (InterruptedException e) {
		}
		User user = UserManager.getUserForIP(ip);
		if (user != null) {
			UserManager.logOut(user.getUsername());
		}
		close();
		disconnected = true;
	}
}
