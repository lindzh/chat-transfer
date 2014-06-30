package com.lindia.net.core.sender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.lindia.net.core.cache.Cache;
import com.lindia.net.core.cache.Data;
import com.lindia.net.core.cache.NetCache;
import com.lindia.net.core.cache.NetQueue;
import com.lindia.net.core.cache.Queue;
import com.lindia.net.core.config.CmdConfig;
import com.lindia.net.core.config.CmdConfig.SendSpeed;
import com.lindia.net.core.config.CmdConfig.WaitConfig;
import com.lindia.net.core.datagram.NetConfig;
import com.lindia.net.core.datagram.NetManager;
import com.lindia.net.core.listener.NetAckListener;
import com.lindia.net.core.listener.NetCloseListener;
import com.lindia.net.core.listener.NetSendedListener;
import com.lindia.net.core.listener.NetStatusListener;
import com.lindia.net.core.listener.TimerListener;
import com.lindia.net.core.reciever.NarqReceiverImpl;
import com.lindia.net.core.timer.NetTimer;
import com.lindia.net.core.timer.Timer;
import com.lindia.utils.IDGenerator;

public class NarqSenderImpl implements Sender,TimerListener,NetAckListener,NetCloseListener
{
	private Cache cacheManager = new NetCache();
	private Queue queueManager = new NetQueue();
	private Timer tickTimer = new NetTimer("tick");
	private Timer ackTimer = new NetTimer("send");
	private Timer waitTimer = new NetTimer("wait");
	private NetConfig netConfig;
	private NetManager netManager;
	private List<NetStatusListener> netStatusListeners = new ArrayList<NetStatusListener>();
	private List<NetSendedListener> netSendedListeners = new ArrayList<NetSendedListener>();
	private byte[] echo = "echo".getBytes();
	private PacketSizeHolder sizeHolder;
	private boolean closed = false;
	private boolean connected = false;
	private boolean shakeHandsTimesUp = false;
	private long lastSendTick = 0;
	private boolean startTick = false;
	private boolean firstClose = true;
	private boolean hasCleared = false;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	@Override
	public void doTimesUp(String type, long interval)//¶¨Ê±Æ÷
	{
		if(type.equals("tick"))
		{
			Data data = new Data();
			data.setId(IDGenerator.generateId());
			data.setCmd(CmdConfig.CMD_TICK);
			data.setVersion(System.currentTimeMillis());
			data.setLength(echo.length);
			data.setData(echo);
			data.incrSendTime();
			if(cacheManager.canPutTick())
			{
				if(!startTick)
				{
					lastSendTick = data.getId();
					cacheManager.put(data.getId(), data);
					netManager.send(data);
				}else{
					if(cacheManager.get(lastSendTick)!=null)
					{
						fireStatusListeners(false,lastSendTick,false);
					}else{
						lastSendTick = data.getId();
						cacheManager.put(data.getId(), data);
						netManager.send(data);
					}
				}
			}
			else{
				fireStatusListeners(false,lastSendTick,false);
			}
		}else if(type.equals("send"))
		{
			if(cacheManager.canPutData())
			{
				Data data = queueManager.get();
				if(data != null)
				{
					data.setVersion(System.currentTimeMillis());
					data.incrSendTime();
					Data oData = cacheManager.get(data.getId());
					if(oData == null)
					{
						cacheManager.put(data.getId(), data);
					}
					netManager.send(data);
				}
			}
		}else if(type.equals("wait"))
		{
			int size = cacheManager.getSize();
			if(size>0)
			{
				//-------------------------------------------------
				lock.readLock().lock();
				System.out.println("--------------------start------------------------");
				//---------------------------------------------
				Set<Long> keys=  cacheManager.keySet();
				for(long key:keys)
				{
					Data data = cacheManager.get(key);
					
					System.out.println("id:     "+data.getId());
					
					if(data.getSendTime() == 1)
					{
						if(data.getWaitTime() >= WaitConfig.WAIT_FIRST)
						{
							send(data);
						}else{
							data.incrWaitTime();
						}
					}else if(data.getSendTime() == 2)
					{
						if(data.getWaitTime() >= WaitConfig.WAIT_SECOND)
						{
							send(data);
						}
						else
						{
							data.incrWaitTime();
						}
					}else if(data.getSendTime() == 3)
					{
						if(data.getWaitTime() >= WaitConfig.WAIT_THIRD)
						{
							if(data.getCmd() == CmdConfig.CMD_SHAKE_HANDS)
							{
								shakeHandsTimesUp = true;
								return ;
							}
							else if(data.getCmd() == CmdConfig.CMD_TICK)
							{
								connected = false;
								fireStatusListeners(false,0,false);
							}else
							{
								connected = false;
								fireStatusListeners(false,data.getId(),false);
							}
						}
						else
						{
							data.incrWaitTime();
						}
					}else{
						
						System.out.println("sendTime:"+data.getSendTime());
						connected = false;
						//cacheManager.remove(data.getId());
					}
				}
				//--------------------------------------------------------------------
				lock.readLock().unlock();
				System.out.println("--------------------end------------------------");
				//-------------------------------------------------------------------
			}
		}
	}
	
	public boolean send(byte[] data)
	{
		if(!connected) return false;
		if(closed) return false;
		Data dat = new Data();
		dat.setCmd(CmdConfig.CMD_DATA);
		dat.setId(IDGenerator.generateId());
		dat.setLength(data.length);
		dat.setData(data);
		return send(dat);
	}
	
	private boolean send(Data data)
	{
		if(!connected&&data.getCmd() == CmdConfig.CMD_SHAKE_HANDS)
		{
			return queueManager.add(data);
		}
		if(!connected) return false;
		if(closed) return false;
		if(!cacheManager.canPutData())
		{
			return false;
		}
		return queueManager.add(data);
	}

	@Override
	public void addNetStatusListener(NetStatusListener listener)
	{
		netStatusListeners.add(listener);
	}

	@Override
	public void addNetSendedListener(NetSendedListener listener)
	{
		netSendedListeners.add(listener);
	}
	
	@Override
	public void onRecieved(long id, long version)
	{
		Data data = cacheManager.get(id);
		if(data == null) return;
		//-----------------------------------------
		lock.writeLock().lock();
		cacheManager.remove(id);
		lock.writeLock().unlock();
		//--------------------------------------------
		if(data.getCmd() == CmdConfig.CMD_SHAKE_HANDS)
		{
			connected = true;
			System.out.println("Connected");
			return ;
		}
		else if(data.getCmd() == CmdConfig.CMD_TICK)
		{
			connected = true;
			if(System.currentTimeMillis()-version<90000)
			return ;
		}
		fireSendedListeners(id);
		int length = data.getLength()+28;
		long time = (System.currentTimeMillis()-version)/1000;
		if(time == 0) time = 1;
		long speed = length/time;
		if(Math.abs(speed-netConfig.getSpeed())>1024)
		{
			double adjust = speed/netConfig.getSpeed();
			adjustDataSzie((int)(sizeHolder.getPacketSize()*adjust));
			adjustQueue((int)(queueManager.size()*adjust));
			netConfig.setSpeed(speed);
		}
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
	
	protected void init()
	{
		netManager.setNetConfig(netConfig);
		tickTimer.addTimerListener(this);
		ackTimer.addTimerListener(this);
		ackTimer.start(20);
		waitTimer.addTimerListener(this);
		waitTimer.start(500);
	}
	
	public void setReceiver(NarqReceiverImpl receiver)
	{
		receiver.addNetAckListener(this);
	}
	
	private void fireStatusListeners(boolean connect,long id,boolean sended)
	{
		for(NetStatusListener listener:netStatusListeners)
		{
			listener.onStatus(connect, id, sended);
		}
		netStatusListeners.clear();
	}
	
	private void fireSendedListeners(long id)
	{
		for(NetSendedListener listener:netSendedListeners)
		{
			listener.dataSend(true, id);
		}
	}
	
	private void adjustDataSzie(int size)
	{
		if(size>SendSpeed.PACKET_MAX)
		{
			sizeHolder.adjustPacketSize(SendSpeed.PACKET_MAX);
		}else if(size<SendSpeed.PACKET_MIN)
		{
			sizeHolder.adjustPacketSize(SendSpeed.PACKET_MIN);
		}else{
			sizeHolder.adjustPacketSize(size);
		}
	}
	
	private void adjustQueue(int size)
	{
		if(size>Queue.MAX_SIZE)
		{
			queueManager.setSize(Queue.MAX_SIZE);
		}else if(size<Queue.MIN_SIZE)
		{
			queueManager.setSize(Queue.MIN_SIZE);
		}else{
			queueManager.setSize(size);
		}
	}
	
	public void setPacketSizeHolder(PacketSizeHolder sizeHolder)
	{
		this.sizeHolder = sizeHolder;
	}

	@Override
	public boolean connect()
	{
		init();
		Data data = new Data();
		data.setCmd(CmdConfig.CMD_SHAKE_HANDS);
		data.setId(IDGenerator.generateId());
		data.setVersion(System.currentTimeMillis());
		byte[] str = "shake hands".getBytes();
		data.setLength(str.length);
		data.setData(str);
		send(data);
		while(!shakeHandsTimesUp&&!connected);
		if(shakeHandsTimesUp) return false;
		if(connected)
		{
			tickTimer.start(10000);
			return true;
		}
		return false;
	}

	@Override
	public boolean close()
	{
		if(firstClose)
		{
			Data data = new Data();
			data.setCmd(CmdConfig.CMD_CLOSE);
			data.setVersion(System.currentTimeMillis());
			data.setId(IDGenerator.generateId());
			byte[] close = "close".getBytes();
			data.setLength(close.length);
			data.setData(close);
			netManager.send(data);
		}
		if(!hasCleared)
		{
			closed = true;
			connected = false;
			tickTimer.stop();
			ackTimer.stop();
			waitTimer.stop();
			tickTimer = null;
			ackTimer = null;
			waitTimer = null;
			cacheManager = null;
			queueManager = null;
			netSendedListeners.clear();
			hasCleared = true;
		}
		return true;
	}

	@Override
	public void onClose()
	{
		firstClose = false;
		close();
	}
}
