package com.lindia.net.core.cache;

public interface Queue
{
	public boolean add(Data data);
	
	public Data get();
	
	public int size();
	
	public boolean setSize(int size);
	
	public static final int MAX_SIZE = 20;
	
	public static final int MIN_SIZE = 5;
}
