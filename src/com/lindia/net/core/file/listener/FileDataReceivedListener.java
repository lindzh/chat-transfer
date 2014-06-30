package com.lindia.net.core.file.listener;

public interface FileDataReceivedListener
{
	public void onReceived(long fileId,long receivedLength,long fileLength,long time);
}
