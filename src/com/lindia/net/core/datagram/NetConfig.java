package com.lindia.net.core.datagram;

import java.net.DatagramSocket;

import com.lindia.net.core.cache.Data;

public class NetConfig
{
	public static final int MAX_PACKET_SIZE = 1024 * 50;

	private String destIp;
	private int destPort;
	private int selfPort;
	private DatagramSocket dataGramSocket;
	private long speed;
	
	public String getDestIp()
	{
		return destIp;
	}

	public void setDestIp(String destIp)
	{
		this.destIp = destIp;
	}

	public int getDestPort()
	{
		return destPort;
	}

	public void setDestPort(int destPort)
	{
		this.destPort = destPort;
	}

	public int getSelfPort()
	{
		return selfPort;
	}

	public void setSelfPort(int selfPort)
	{
		this.selfPort = selfPort;
	}

	public DatagramSocket getDataGramSocket()
	{
		return dataGramSocket;
	}

	public void setDataGramSocket(DatagramSocket dataGramSocket)
	{
		this.dataGramSocket = dataGramSocket;
	}

	public void close()
	{
		try {
			this.dataGramSocket.close();
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}

	public long getSpeed()
	{
		return speed;
	}

	public void setSpeed(long speed)
	{
		this.speed = speed;
	}
	
	public static final long SPEED_MIN = 1024;
	
	public static final long SPEED_MAX = 1024*1024*10;

}
