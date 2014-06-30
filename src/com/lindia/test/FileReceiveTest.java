package com.lindia.test;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.lindia.net.core.datagram.NetConfig;
import com.lindia.net.core.file.config.FileConfig;
import com.lindia.net.core.file.listener.ConnectionStateListener;
import com.lindia.net.core.file.listener.FileDataReceivedListener;
import com.lindia.net.core.file.listener.FileDataSendedListener;
import com.lindia.net.core.file.socket.NarqFileSocketBase;

public class FileReceiveTest implements ConnectionStateListener,FileDataReceivedListener,FileDataSendedListener
{
	private NarqFileSocketBase socket = NarqFileSocketBase.createNarqFileSocket();
	private boolean started = false;
	private long lastReceivedLength = 0;
	private Logger logger = Logger.getLogger("data");
	private long lastSpeed = 0;
	
	@Override
	public void onSend(long id, long sendLength, long fileSize, long time)
	{
		String str = ((int)(sendLength*100/fileSize))+"%";
		System.out.println("SEND:"+id+"   "+str);
	}

	@Override
	public void onReceived(long fileId, long receivedLength, long fileLength,long time)
	{
		if(fileLength!=0)
		{
			long speed = 0;
			String str = ((int)(receivedLength*100/fileLength))+"%";
			if(time!=0)
			{
				speed = (receivedLength-lastReceivedLength)/time*1000/1024;
			}
			else{
				speed = (receivedLength-lastReceivedLength)*1000/1024;
			}
			lastReceivedLength = receivedLength;
			logger.info("REC:"+fileId+"   "+str+"  Speed :"+speed+" Kb/s");
		}
		
		if(!started)
		{
			logger.info("start:"+new Date());
			started = true;
		}
		
		if(receivedLength == fileLength)
		{
			logger.info("end:"+new Date());
//			socket.close();
//			System.exit(0);
		}
	}

	@Override
	public void onNetClose(long id)
	{
		System.out.println("连接中断");
		socket.close();
	}
	
	public <ReceiveConfig> void init(NetConfig netConfig,FileConfig sendConfig,FileConfig receiveConfig)
	{
		socket.addConnectionStateListener(this);
		socket.addFileDataReceivedListener(this);
		socket.addFileDataSendedListener(this);
		socket.setNetConfig(netConfig);
		socket.setSendFile(sendConfig);
		socket.setReceiveFile(receiveConfig);
	}
	
	public boolean connect()
	{
		return socket.connect();
	}
	
	public void start(boolean send)
	{
		if(send)
		{
			socket.startSend();
		}else{
			socket.startReceive();
		}
	}
	
	public static void main(String[] args)
	{
		NetConfig netConfig = new NetConfig();
		netConfig.setDestIp("127.0.0.1");
		netConfig.setDestPort(7776);
		netConfig.setSelfPort(7777);
		netConfig.setSpeed(200);
		try {
			netConfig.setDataGramSocket(new DatagramSocket(netConfig.getSelfPort()));
			FileConfig receiveConfig = new FileConfig();
			File receiveFile = new File("D:\\eclipse\\chatDir\\通信课件.rar");
			receiveConfig.setFile(receiveFile.getAbsolutePath());
			receiveConfig.setFileSize(172585345);
			receiveConfig.setFileId(523243243432L);
			FileReceiveTest testSocket = new FileReceiveTest();
			testSocket.init(netConfig, null, receiveConfig);
			testSocket.connect();
			boolean connect = testSocket.connect();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

