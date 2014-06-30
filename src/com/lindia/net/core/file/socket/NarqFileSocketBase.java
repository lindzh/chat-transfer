package com.lindia.net.core.file.socket;

import java.util.ArrayList;
import java.util.List;

import com.lindia.net.core.file.config.FileConfig;
import com.lindia.net.core.file.listener.ConnectionStateListener;
import com.lindia.net.core.file.listener.FileDataReceivedListener;
import com.lindia.net.core.file.listener.FileDataSendedListener;
import com.lindia.net.core.socket.NarqSocketBase;

public abstract class NarqFileSocketBase extends NarqSocketBase implements Runnable
{	
	public abstract void setReceiveFile(FileConfig config);
	public abstract void setSendFile(FileConfig config);
	public abstract void startSend();
	public abstract void startReceive();
	
	public abstract void stopSend();
	public abstract void stopReceive();
	
	private List<FileDataReceivedListener> fileReceiveListeners = new ArrayList<FileDataReceivedListener>();
	private List<FileDataSendedListener> fileSendListeners = new ArrayList<FileDataSendedListener>();
	private List<ConnectionStateListener> connectionListeners = new ArrayList<ConnectionStateListener>();
	
	public static NarqFileSocketBase createNarqFileSocket()
	{
		return new NarqFileSocketImpl();
	}
	
	public void addFileDataSendedListener(FileDataSendedListener listener)
	{
		fileSendListeners.add(listener);
	}
	
	protected void fireFileDataSendedListeners(long fileId,long sendedLength,long fileLength,long time)
	{
		for(FileDataSendedListener listener:fileSendListeners)
		{
			listener.onSend(fileId, sendedLength, fileLength, time);
		}
	}
	
	public void addFileDataReceivedListener(FileDataReceivedListener listener)
	{
		fileReceiveListeners.add(listener);
	}
	
	protected  void fireFileDataReceivedListeners(long fileId,long receivedLength,long fileLength,long time)
	{
		for(FileDataReceivedListener listener:fileReceiveListeners)
		{
			listener.onReceived(fileId, receivedLength, fileLength, time);
		}
	}
	
	public void addConnectionStateListener(ConnectionStateListener listener)
	{
		connectionListeners.add(listener);
	}
	
	protected  void fireConnectionStateListeners(long id)
	{
		for(ConnectionStateListener listener:connectionListeners)
		{
			listener.onNetClose(id);
		}
	}
	
}
