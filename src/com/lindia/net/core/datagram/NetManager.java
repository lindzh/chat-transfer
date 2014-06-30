package com.lindia.net.core.datagram;

import com.lindia.net.core.cache.Data;

public interface NetManager
{
	public void setNetConfig(NetConfig config);
	
	public boolean send(Data data);
	
	public boolean receive(Data data);
}
