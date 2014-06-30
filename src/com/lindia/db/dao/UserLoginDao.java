package com.lindia.db.dao;

public interface UserLoginDao
{
	public long login(long userId,String password);
	
	public long changePassword(long userId,String oldPassword,String newPassword);
	
	public long registerUser(long userId,String password);
	
	public long generateUserId();
}
