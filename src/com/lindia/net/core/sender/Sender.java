package com.lindia.net.core.sender;

import com.lindia.net.core.cache.Data;
import com.lindia.net.core.listener.NetSendedListener;
import com.lindia.net.core.listener.NetStatusListener;

public interface Sender
{	
	boolean connect();
	
	boolean send(byte[] data);
	
	boolean close();
	
	void addNetStatusListener(NetStatusListener listener);
	
	void addNetSendedListener(NetSendedListener listener);
}
