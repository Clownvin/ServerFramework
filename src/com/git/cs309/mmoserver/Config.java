package com.git.cs309.mmoserver;

public final class Config {
    //This class is just temporary storage for configuration stuff, like maxes and mins
    
    //Max bytes per packet.
    public static final int MAX_PACKET_BYTES = 1000;
    
    //Max packets/tick before automatically closes connection.
    public static final int PACKETS_PER_TICK_BEFORE_KICK = 10000;
    
    //Desired tick delay
    public static final long TICK_DELAY = 400; // 400MS
    
    //Maximum connections allowed.
    public static final int MAX_CONNECTIONS = 1000;
    
    public static final byte EOF_CHARACTER = -1;
}
