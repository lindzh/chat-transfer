package com.lindia.net.core.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NetCache implements Cache
{
	private Map<Long,Data> map;
	private int max;
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public NetCache()
	{
		max = 10;
		map = Collections.synchronizedMap(new HashMap<Long,Data>());
	}
	
	public void setSize(int size)
	{
		lock.writeLock().lock();
		max = size;
		lock.writeLock().unlock();
	}
	
	@Override
	public boolean put(long key, Data value)
	{
		lock.writeLock().lock();
		int size = map.size();
		if(size>max)
		{
			lock.writeLock().unlock();
			return false;
		}else
		{
			map.put(key, value);
			lock.writeLock().unlock();
			return true;
		}
		
	}

	@Override
	public boolean remove(long key)
	{
		lock.writeLock().lock();
		if(map.containsKey(key))
		{
			map.remove(key);
			lock.writeLock().unlock();
			return true;
		}
		lock.writeLock().unlock();
		return false;
	}

	@Override
	public int getSize()
	{
		lock.readLock().lock();
		int size = map.size();
		lock.readLock().unlock();
		return size;
	}

	@Override
	public Data get(long key)
	{
		lock.readLock().lock();
		Data data = map.get(key);
		lock.readLock().unlock();
		return data;
	}

	@Override
	public Set<Long> keySet()
	{
		lock.readLock().lock();
		Set<Long> set = map.keySet();
		lock.readLock().unlock();
		return set;
	}

	@Override
	public void clear()
	{
		lock.writeLock().lock();
		map.clear();
		lock.writeLock().unlock();
	}

	@Override
	public void setMax(int max)
	{
		if(max>ALLOW_MAX)
		{
			lock.writeLock().lock();
			this.max = ALLOW_MAX;
			lock.writeLock().unlock();
		}else{
			lock.writeLock().lock();
			this.max = max;
			lock.writeLock().unlock();
		}
	}

	@Override
	public boolean canPutData()
	{
		lock.readLock().lock();
		boolean result = map.size()<max-1;
		lock.readLock().unlock();
		return result;
	}

	@Override
	public boolean canPutTick()
	{
		lock.readLock().lock();
		boolean result = map.size()<max;
		lock.readLock().unlock();
		return result;
	}
	
	

}
