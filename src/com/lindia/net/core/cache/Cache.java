package com.lindia.net.core.cache;

import java.util.Set;

public interface Cache
{
	public boolean put(long key,Data value);
	
	public boolean remove(long key);
	
	public Data get(long key);
	
	public Set<Long> keySet();
	
	public void clear();
	
	public int getSize();
	
	public void setMax(int max);
	
	public boolean canPutData();
	
	public boolean canPutTick();
	
	public static final int ALLOW_MAX = 20;
}
