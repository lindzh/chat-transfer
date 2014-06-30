package com.lindia.net.core.file.listener;

public interface FileDataSendedListener
{
	public void onSend(long id,long sendLength,long fileSize,long time);
}
