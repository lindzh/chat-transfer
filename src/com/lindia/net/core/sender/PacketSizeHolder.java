package com.lindia.net.core.sender;

public interface PacketSizeHolder
{
	public void adjustPacketSize(int size);
	
	public int getPacketSize();
}
