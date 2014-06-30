package com.lindia.net.core.reciever;

import java.util.ArrayList;
import java.util.List;

import com.lindia.net.core.cache.Data;
import com.lindia.net.core.config.CmdConfig;
import com.lindia.net.core.datagram.NetConfig;
import com.lindia.net.core.datagram.NetManager;
import com.lindia.net.core.listener.NetAckListener;
import com.lindia.net.core.listener.NetCloseListener;
import com.lindia.net.core.listener.NetReceivedListener;
import com.lindia.net.core.listener.NetStatusListener;
import com.lindia.net.core.listener.TimerListener;
import com.lindia.net.core.sender.NarqSenderImpl;
import com.lindia.net.core.timer.NetTimer;
import com.lindia.net.core.timer.Timer;

public class NarqReceiverImpl implements Reciever,Runnable,TimerListener
{
	private List<NetAckListener> netAckListeners = new ArrayList<NetAckListener>();
	private List<NetReceivedListener> netReceivedListeners = new ArrayList<NetReceivedListener>();
	private List<NetStatusListener> netStatusListeners = new ArrayList<NetStatusListener>();
	private List<NetCloseListener> netCloseListeners = new ArrayList<NetCloseListener>();
	private Timer netTimer = new NetTimer("tick");
	private NetManager netManager;
	private NetConfig netConfig;
	private boolean connected = true;
	private boolean closed = false;
	private long lastTickId = 1;
	private long tickId = 0;
	private boolean hasCleared = false;
	
	@Override
	public void receive()
	{
		new Thread(this).start();
	}
	
	public void setNetConfig(NetConfig netConfig)
	{
		this.netConfig = netConfig;
	}
	
	public NetConfig getNetConfig()
	{
		return netConfig;
	}
	
	public void setNetManager(NetManager netManager)
	{
		this.netManager = netManager;
	}
	
	public NetManager getNetManager()
	{
		return netManager;
	}
	
	public void init()
	{
		netManager.setNetConfig(netConfig);
		netTimer.start(15000);
	}

	@Override
	public void addNetStatusListener(NetStatusListener listener)
	{
		netStatusListeners.add(listener);
	}

	@Override
	public void addNetReceivedListener(NetReceivedListener listener)
	{
		netReceivedListeners.add(listener);
	}

	@Override
	public void run()
	{
		while(!closed&&connected)
		{
			Data data = new Data();
			netManager.receive(data);
			if(data.getCmd() == CmdConfig.CMD_SHAKE_HANDS)
			{
				Data ack = new Data();
				ack.setCmd(CmdConfig.CMD_SHAKE_HANDS_ACK);
				ack.setId(data.getId());
				ack.setVersion(data.getVersion());
				ack.setLength(1);
				byte[] b = new byte[]{0x08};
				ack.setData(b);
				netManager.send(ack);
				connected = true;
				tickId = data.getId();
			}else if(data.getCmd() == CmdConfig.CMD_SHAKE_HANDS_ACK)
			{
				fireNetAckListeners(data.getId(),data.getVersion());
				tickId = data.getId();
				connected = true;
			}
			else if(data.getCmd() == CmdConfig.CMD_TICK_ACK)
			{
				fireNetAckListeners(data.getId(),data.getVersion());
				tickId = data.getId();
				connected = true;
			}
			else if(data.getCmd() == CmdConfig.CMD_TICK)
			{
				Data ack = new Data();
				ack.setCmd(CmdConfig.CMD_TICK_ACK);
				ack.setId(data.getId());
				ack.setVersion(data.getVersion());
				ack.setLength(1);
				byte[] b = new byte[]{0x08};
				ack.setData(b);
				netManager.send(ack);
				tickId = data.getId();
				connected = true;
			}
			else if(data.getCmd() == CmdConfig.CMD_DATA_ACK)
			{
				fireNetAckListeners(data.getId(),data.getVersion());
				tickId = data.getId();
				connected = true;
			}else if(data.getCmd() == CmdConfig.CMD_DATA)
			{
				Data ack = new Data();
				ack.setCmd(CmdConfig.CMD_DATA_ACK);
				ack.setId(data.getId());
				ack.setVersion(data.getVersion());
				ack.setLength(1);
				byte[] b = new byte[]{0x08};
				ack.setData(b);
				netManager.send(ack);
				fireNetReceivedListeners(data);
				tickId = data.getId();
				connected = true;
			}
			else if(data.getCmd() == CmdConfig.CMD_CLOSE){
				Data ack = new Data();
				ack.setCmd(CmdConfig.CMD_CLOSE_ACK);
				ack.setId(data.getId());
				ack.setVersion(data.getVersion());
				byte[] close = "close".getBytes();
				ack.setLength(close.length);
				ack.setData(close);
				netManager.send(ack);
				fireNetCloseListeners();
				fireNetStatusListener(false);
			}
			else if(data.getCmd() == CmdConfig.CMD_CLOSE_ACK){
				fireNetStatusListener(false);
			}

		}
	}
	
	@Override
	public void doTimesUp(String type, long interval)
	{
		if(type.equals("tick"))
		{
			if(!connected || this.tickId == this.lastTickId)
			{
				fireNetStatusListener(false);
			}else{
				this.lastTickId = this.tickId;
			}
		}
	}
	
	public void setSender(NarqSenderImpl sender)
	{
		//addNetAckListener(sender);
		//Ìí¼Ó½øÈ¥
	}
	
	private void fireNetReceivedListeners(Data data)
	{
		for(NetReceivedListener listener:netReceivedListeners)
		{
			listener.onReceived(data);
		}
	}
	
	private void fireNetAckListeners(long id,long version)
	{
		try
		{
			for(NetAckListener listener:netAckListeners)
			{
				listener.onRecieved(id, version);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void fireNetStatusListener(boolean connected)
	{
		for(NetStatusListener listener:netStatusListeners)
		{
			listener.onStatus(connected, 0, true);
		}
		netStatusListeners.clear();
	}
	
	private void fireNetCloseListeners()
	{
		for(NetCloseListener listener:netCloseListeners)
		{
			listener.onClose();
		}
	}
	
	public void addNetCloseListener(NetCloseListener listener)
	{
		netCloseListeners.add(listener);
	}
	
	public void addNetAckListener(NetAckListener listener)
	{
		netAckListeners.add(listener);
	}

	@Override
	public void listen()
	{
		init();
		receive();
	}

	@Override
	public void close()
	{
		if(!hasCleared)
		{
			closed = true;
			connected = false;
			netTimer.stop();
			netTimer = null;
			netReceivedListeners.clear();
			netAckListeners.clear();
		}
		return ;
	}
}
