package com.lindia.net.core.socket;

import com.lindia.net.core.datagram.NetConfig;
import com.lindia.net.core.datagram.NetManager;
import com.lindia.net.core.datagram.NetManagerImpl;
import com.lindia.net.core.listener.NetReceivedListener;
import com.lindia.net.core.listener.NetStatusListener;
import com.lindia.net.core.reciever.NarqReceiverImpl;
import com.lindia.net.core.sender.PacketSizeHolder;
import com.lindia.net.core.sender.NarqSenderImpl;

public abstract class NarqSocketBase implements NetSocket,PacketSizeHolder,NetReceivedListener,NetStatusListener
{	
	private NarqSenderImpl sender = new NarqSenderImpl();
	private NarqReceiverImpl receiver = new NarqReceiverImpl();
	private NetConfig netConfig;
	private NetManager netManager;
	@Override
	public boolean connect()
	{
		netManager = new NetManagerImpl();
		netManager.setNetConfig(netConfig);
		sender.setNetManager(netManager);
		sender.addNetStatusListener(this);
		sender.setPacketSizeHolder(this);	
		receiver.setSender(sender);
		receiver.addNetReceivedListener(this);
		receiver.addNetStatusListener(this);
		receiver.setNetManager(netManager);
		receiver.addNetAckListener(sender);
		//sender.setReceiver(receiver);

		
		receiver.listen();
		if(sender.connect())
		{
			return true;
		}else{
			return false;
		}
	}
	@Override
	public void close()
	{
		sender.close();
		receiver.close();
	}
	public boolean send(byte[] data)
	{
		return sender.send(data);
	}
	
	public void setNetConfig(NetConfig config)
	{
		netConfig = config;
		sender.setNetConfig(netConfig);
		receiver.setNetConfig(netConfig);
	}
}
