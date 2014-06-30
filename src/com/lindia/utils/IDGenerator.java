package com.lindia.utils;

import java.util.Random;

public class IDGenerator
{
	private static long start = Math.abs(new Random().nextLong());
	
	public static long generateId()
	{
		return start++;
	}
}
