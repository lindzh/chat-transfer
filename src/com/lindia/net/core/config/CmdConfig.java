package com.lindia.net.core.config;

public interface CmdConfig
{
	public static final long CMD_TICK = 0x99999999;
	public static final long CMD_TICK_ACK = 0x98989898;
	
	public static final long CMD_DATA = 0x76786765;
	public static final long CMD_DATA_ACK = 0x76434343;
	
	public static final long CMD_SHAKE_HANDS = 0x54232543;
	public static final long CMD_SHAKE_HANDS_ACK = 0x23213244;
	
	public static final long CMD_CLOSE = 0x98765432;
	public static final long CMD_CLOSE_ACK = 0x78912343;
	
	
	interface WaitConfig
	{
		public static final long WAIT_FIRST = 1000;
		
		public static final long WAIT_SECOND = 3000;
		
		public static final long WAIT_THIRD = 5000;
		
		public static final long WAIT_TICK = 5000;
	}
	
	interface SendSpeed
	{
		public static final int PACKET_MIN = 512;
		
		public static final int PACKET_MAX = 1024*50-100;
	}
}
