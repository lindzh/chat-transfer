package com.lindia.db.meta;

public class FriendGroup
{
	private long friendGroupId;
	private long userId;
	private String groupName;
	private int friendCount;

	public long getFriendGroupId()
	{
		return friendGroupId;
	}

	public void setFriendGroupId(long friendGroupId)
	{
		this.friendGroupId = friendGroupId;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public int getFriendCount()
	{
		return friendCount;
	}

	public void setFriendCount(int friendCount)
	{
		this.friendCount = friendCount;
	}

}
