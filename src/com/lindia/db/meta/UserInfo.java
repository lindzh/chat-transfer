package com.lindia.db.meta;

import java.sql.Date;

public class UserInfo
{
	private long userId;
	private String nickname;
	private String email;
	private String cellphone;
	private Date birthday;
	private long faceId;
	private String sign;
	private int friendGroupCount;
	private int groupCount;
	private int messageCount;
	private long registerTime;
	private long lastLoginTime;

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getCellphone()
	{
		return cellphone;
	}

	public void setCellphone(String cellphone)
	{
		this.cellphone = cellphone;
	}

	public Date getBirthday()
	{
		return birthday;
	}

	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}

	public long getFaceId()
	{
		return faceId;
	}

	public void setFaceId(long faceId)
	{
		this.faceId = faceId;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}

	public int getFriendGroupCount()
	{
		return friendGroupCount;
	}

	public void setFriendGroupCount(int friendGroupCount)
	{
		this.friendGroupCount = friendGroupCount;
	}

	public int getGroupCount()
	{
		return groupCount;
	}

	public void setGroupCount(int groupCount)
	{
		this.groupCount = groupCount;
	}

	public int getMessageCount()
	{
		return messageCount;
	}

	public void setMessageCount(int messageCount)
	{
		this.messageCount = messageCount;
	}

	public long getRegisterTime()
	{
		return registerTime;
	}

	public void setRegisterTime(long registerTime)
	{
		this.registerTime = registerTime;
	}

	public long getLastLoginTime()
	{
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime)
	{
		this.lastLoginTime = lastLoginTime;
	}

}
