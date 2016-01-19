package com.git.cs309.mmoserver;

public final class Config {
	//This class is just temporary storage for configuration stuff, like maxes and mins

	//Max bytes per packet.
	public static final int MAX_PACKET_BYTES = 1000;

	//Max packets/tick before automatically closes connection.
	public static final int PACKETS_PER_TICK_BEFORE_KICK = 30;

	//Desired tick delay
	public static final long TICK_DELAY = 100; // 100MS (runescape runs around 575MS/tick)

	//Maximum connections allowed.
	public static final int MAX_CONNECTIONS = 1000;

	public static final byte EOF_CHARACTER = -1;

	public static final String USER_FILE_PATH = "./data/users/";

	public static final int MILLISECONDS_PER_MINUTE = 60000;

	public static final int TICKS_PER_AUTO_SAVE = (int) ((1 * MILLISECONDS_PER_MINUTE) / TICK_DELAY);
}
