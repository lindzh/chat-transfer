package com.lindia.net.core.file.utils;

public class FilePieceIDUtils
{
	private static ThreadLocal<Long> ids = new ThreadLocal<Long>();
	private static ThreadLocal<Boolean> inited = new ThreadLocal<Boolean>();
	public static long getId()
	{
		Boolean init = inited.get();
		if(init == null)
		{
			init = true;
		}
		return ids.get();
	}
}
