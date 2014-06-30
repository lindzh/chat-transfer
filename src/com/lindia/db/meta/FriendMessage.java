package com.lindia.db.meta;

public class FriendMessage
{
	private long id;
	private long fromId;
	private long toId;
	private int messageType;
	private long addTime;
	private String message;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getFromId()
	{
		return fromId;
	}

	public void setFromId(long fromId)
	{
		this.fromId = fromId;
	}

	public long getToId()
	{
		return toId;
	}

	public void setToId(long toId)
	{
		this.toId = toId;
	}

	public int getMessageType()
	{
		return messageType;
	}

	public void setMessageType(int messageType)
	{
		this.messageType = messageType;
	}

	public long getAddTime()
	{
		return addTime;
	}

	public void setAddTime(long addTime)
	{
		this.addTime = addTime;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

}
