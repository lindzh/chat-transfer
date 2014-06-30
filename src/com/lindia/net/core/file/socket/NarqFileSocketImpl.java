package com.lindia.net.core.file.socket;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.lindia.net.core.cache.Data;
import com.lindia.net.core.file.config.FileConfig;
import com.lindia.net.core.file.io.FileAccess;
import com.lindia.net.core.file.io.RandomFile;
import com.lindia.net.core.file.utils.NetFileUtils;
import com.lindia.utils.ChatByteUtils;

public class NarqFileSocketImpl extends NarqFileSocketBase
{
	private boolean startSend = false;
	private boolean startReceive = true;
	private FileAccess sendFile;
	private FileAccess receive;
	private FileConfig SendConfig;
	private FileConfig receiveConfig;
	private long receivceLength = 0;
	private int sendSize = 1024*20; 
	private Map<Long,Boolean> receivedData = new HashMap<Long,Boolean>();
	
	@Override
	public void onReceived(Data data)
	{
		if(startReceive)
		{
			byte[] bytes = data.getData();
			long time = System.currentTimeMillis()-data.getVersion();
			Map map = NetFileUtils.getObject(bytes);
			Boolean received = receivedData.get(map.get("pieceId"));
			if(received==null)
			{
				byte[] fileData = (byte[])map.get("data");
				receive.write((Long)map.get("fileId"), (Long)map.get("position"),fileData);
				int length = (Integer)map.get("length");
				receivceLength += length;
				fireFileDataReceivedListeners((Long)map.get("fileId"),receivceLength,receiveConfig.getFileSize(),time);
				receivedData.put((Long)map.get("pieceId"), true);
				if(receivceLength>=receiveConfig.getFileSize())
				{
					System.out.println("close");
					startReceive = false;
					receivedData.clear();
					receive.close();
				}
			}
		}
	}

	/**
	 * size 不能大于1024*50否则就可能接收不到，因为UDP分包
	 */
	@Override
	public synchronized void adjustPacketSize(int size)
	{
		sendSize = size;
	}

	@Override
	public synchronized int getPacketSize()
	{
		return sendSize;
	}

	@Override
	public void onStatus(boolean isConnected, long id, boolean isSended)
	{
		if(!isConnected){
			super.fireConnectionStateListeners(id);
		}
	}

	@Override
	public void setReceiveFile(FileConfig config)
	{
		receiveConfig = config;
		receive  = new RandomFile();
		receive.setFileConfig(receiveConfig, false);	
	}

	@Override
	public void setSendFile(FileConfig config)
	{
		SendConfig = config;
		sendFile  = new RandomFile();
		sendFile.setFileConfig(SendConfig, true);	
	}

	@Override
	public void run()
	{
		long sendedLength = 0;
		long position = 0;
		long pieceId = 0;
		while(startSend)
		{			
			int size = getPacketSize();
			byte[] data = new byte[size];
			int len = sendFile.read(SendConfig.getFileId(), position, data);
			if(len>0)
			{
				byte[] sendData = NetFileUtils.toBytes(SendConfig.getFileId(), pieceId, position, len, data);
				boolean sended = false;
				while(startSend&&!sended)
				{
					sended = super.send(sendData);
				}
				pieceId++;
				position += len;
				sendedLength = position;
				super.fireFileDataSendedListeners(SendConfig.getFileId(), sendedLength, SendConfig.getFileSize(), 0);
				if(sendedLength>=SendConfig.getFileSize())
				{
					sendFile.close();
				}
			}else{
				startSend = false;
				sendFile.close();
			}
		}
	}

	@Override
	public void startSend()
	{
		startSend = true;
		sendFile.init();
		new Thread(this).start();
	}

	@Override
	public void startReceive()
	{
		receive.init();
		startReceive = true;
	}
	
	

	@Override
	public boolean connect()
	{
		if(receiveConfig!=null)
		{
			receive.init();
		}
		if(	SendConfig!=null)
		{
			sendFile.init();
		}
		boolean result = super.connect();
		if(result&&SendConfig!=null)
		{
			startSend = true;
			new Thread(this).start();
		}
		return result;
	}

	@Override
	public void stopSend()
	{
		startSend = false;		
	}

	@Override
	public void stopReceive()
	{
		startReceive = false;
	}
}
