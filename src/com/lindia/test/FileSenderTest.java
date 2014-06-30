package com.lindia.test;

import java.io.File;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.lindia.net.core.datagram.NetConfig;
import com.lindia.net.core.file.config.FileConfig;
import com.lindia.net.core.file.listener.ConnectionStateListener;
import com.lindia.net.core.file.listener.FileDataReceivedListener;
import com.lindia.net.core.file.listener.FileDataSendedListener;
import com.lindia.net.core.file.socket.NarqFileSocketBase;
import com.lindia.utils.IDGenerator;

public class FileSenderTest implements ConnectionStateListener,FileDataReceivedListener,FileDataSendedListener
{
	private NarqFileSocketBase socket = NarqFileSocketBase.createNarqFileSocket();
	
	private long lastReceivedLength = 0;
	@Override
	public void onSend(long id, long sendLength, long fileSize, long time)
	{
		String str = ((int)(sendLength*100/fileSize))+"%";
		System.out.println("SEND:"+id+"   "+str);
	}

	@Override
	public void onReceived(long fileId, long receivedLength, long fileLength,
			long time)
	{
		long speed = 0;
		String str = ((int)(receivedLength*100/fileLength))+"%";
		if(time!=0)
		{
			speed = (receivedLength-lastReceivedLength)/time;
		}
		else{
			speed = (receivedLength-lastReceivedLength)*2;
		}
		lastReceivedLength = receivedLength;
		System.out.println("REC:"+fileId+"   "+str+"  Speed :"+speed+" Kb/s");
//		if(receivedLength == fileLength)
//		{
//			socket.close();
//			System.exit(0);
//		}
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
		netConfig.setDestPort(7777);
		netConfig.setSelfPort(7776);
		netConfig.setSpeed(200);
		try {
			netConfig.setDataGramSocket(new DatagramSocket(netConfig.getSelfPort()));
			FileConfig sendConfig = new FileConfig();
			File sendFile = new File("E:\\通信课件.rar");
			sendConfig.setFile(sendFile.getAbsolutePath());
			sendConfig.setFileSize(sendFile.length());
			System.out.println(sendFile.length());
			sendConfig.setFileId(523243243432L);
		
			FileSenderTest testSocket = new FileSenderTest();
			testSocket.init(netConfig, sendConfig, null);
			boolean connect = testSocket.connect();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	
}
