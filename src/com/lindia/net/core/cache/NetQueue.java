package com.lindia.net.core.cache;

import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NetQueue implements Queue
{
	private LinkedList list = new LinkedList();
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private int size;
	
	public NetQueue(int size)
	{
		if(size>MAX_SIZE)
		{
			this.size = MAX_SIZE;
		}
		else{
			this.size = size;
		}
	}
	
	public NetQueue()
	{
		size = 10;
	}
	
	
	@Override
	public boolean add(Data data)
	{
		lock.writeLock().lock();
		if(size>=MAX_SIZE)
		{
			lock.writeLock().unlock();
			return false;
		}else{
			boolean result = list.offer(data);
			size++;
			lock.writeLock().unlock();
			return result;
		}		
	}

	@Override
	public Data get()
	{
		lock.readLock().lock();
		Data data = (Data)list.poll();
		size--;
		lock.readLock().unlock();
		return data;
	}

	@Override
	public int size()
	{
		lock.readLock().lock();
		int result = size;
		lock.readLock().unlock();
		return result;
	}

	@Override
	public boolean setSize(int size)
	{
		lock.writeLock().lock();
		if(size>MAX_SIZE)
		{
			this.size = MAX_SIZE;
			lock.writeLock().unlock();
			return false;
		}
		else{
			this.size = size;
			lock.writeLock().unlock();
			return true;
		}
	}

}
