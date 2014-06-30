package com.lindia.net.core.reciever;

import com.lindia.net.core.listener.NetCloseListener;
import com.lindia.net.core.listener.NetReceivedListener;
import com.lindia.net.core.listener.NetStatusListener;

public interface Reciever
{
	public void listen();
	
	public void receive();
	
	public void close();
	
	void addNetStatusListener(NetStatusListener listener);
	
	void addNetReceivedListener(NetReceivedListener listener);
	
	void addNetCloseListener(NetCloseListener listener);
	
}
