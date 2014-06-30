package com.lindia.test;

import com.lindia.net.core.cache.Data;
import com.lindia.net.core.socket.NarqSocketBase;

public class MyNarqSocket extends NarqSocketBase
{
	private int size = 300;
	@Override
	public void onReceived(Data data)
	{
		String str = new String(data.getData());
		System.out.println("receive:"+data.getId()+" data:"+str);
	}

	@Override
	public void adjustPacketSize(int size)
	{
		this.size = size;
	}

	@Override
	public int getPacketSize()
	{
		return size;
	}

	@Override
	public void onStatus(boolean isConnected, long id, boolean isSended)
	{
		System.out.println("ÍøÂçÖÐ¶Ï");
		super.close();
	}

}
