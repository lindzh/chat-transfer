package com.lindia.net.core.listener;

public interface NetStatusListener
{
	public void onStatus(boolean isConnected,long id,boolean isSended);
}
