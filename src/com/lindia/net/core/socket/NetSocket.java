package com.lindia.net.core.socket;

import com.lindia.net.core.cache.Data;
import com.lindia.net.core.datagram.NetConfig;

public interface NetSocket
{
	public void setNetConfig(NetConfig config);
	
	public boolean connect();
	
	public boolean send(byte[] data);
	
	public void onReceived(Data data);
	
	public void close();
}
