package com.lindia.db.meta;

public class Group
{
	private long groupId;
	private long creator;
	private long admin;
	private String name;
	private String information;

	public long getGroupId()
	{
		return groupId;
	}

	public void setGroupId(long groupId)
	{
		this.groupId = groupId;
	}

	public long getCreator()
	{
		return creator;
	}

	public void setCreator(long creator)
	{
		this.creator = creator;
	}

	public long getAdmin()
	{
		return admin;
	}

	public void setAdmin(long admin)
	{
		this.admin = admin;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getInformation()
	{
		return information;
	}

	public void setInformation(String information)
	{
		this.information = information;
	}

}
