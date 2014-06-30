package com.lindia.net.core.cache;

import com.lindia.net.core.config.CmdConfig;

public class Data
{
	private long id;
	private long cmd;
	private long version;
	private int length;
	private byte[] data;
	private int sendTime = 0;
	private long waitTime = 0;
	
	public Data(long id, long cmd, long version, int length, byte[] data)
	{
		this.id = id;
		this.cmd = cmd;
		this.version = version;
		this.length = length;
		this.data = data;
		sendTime = 0;
		waitTime = 0;
	}
	
	public Data()
	{
		
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getCmd()
	{
		return cmd;
	}

	public void setCmd(long cmd)
	{
		this.cmd = cmd;
	}

	public long getVersion()
	{
		return version;
	}

	public void setVersion(long version)
	{
		this.version = version;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public byte[] getData()
	{
		return data;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}
	
	public void incrSendTime()
	{
		waitTime = 0;
		sendTime++;
	}
	
	public int getSendTime()
	{
		return sendTime;
	}
	
	public void incrWaitTime()
	{
		waitTime += 500;
	}
	
	public long getWaitTime()
	{
		return waitTime;
	}
	

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if(this.cmd == CmdConfig.CMD_DATA)
		{
			sb.append("DATA  ");
		}
		if(this.cmd == CmdConfig.CMD_DATA_ACK)
		{
			sb.append("DATA_ACK  ");
		}
		if(this.cmd == CmdConfig.CMD_TICK)
		{
			sb.append("TICK  ");
		}
		if(this.cmd == CmdConfig.CMD_TICK_ACK)
		{
			sb.append("TICK_ACK  ");
		}
		if(this.cmd == CmdConfig.CMD_SHAKE_HANDS)
		{
			sb.append("SHAKE_HANDS  ");
		}
		if(this.cmd == CmdConfig.CMD_SHAKE_HANDS_ACK)
		{
			sb.append("SHAKE_HANDS_ACK  ");
		}
		sb.append(" ID:"+this.getId());
		sb.append(" 	TIME:"+this.getVersion());
		sb.append(" TXT:"+new String(this.data));
		return sb.toString();
	}
}
