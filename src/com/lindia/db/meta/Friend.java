package com.lindia.db.meta;

public class Friend
{
	private long userId;
	private long groupId;
	private long friendId;
	private String name;

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public long getGroupId()
	{
		return groupId;
	}

	public void setGroupId(long groupId)
	{
		this.groupId = groupId;
	}

	public long getFriendId()
	{
		return friendId;
	}

	public void setFriendId(long friendId)
	{
		this.friendId = friendId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
