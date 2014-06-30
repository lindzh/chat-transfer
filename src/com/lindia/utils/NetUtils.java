package com.lindia.utils;

import java.util.HashMap;
import java.util.Map;

public class NetUtils
{
	public static byte[] toBytes(long id,long cmd,long version,int length,byte[] data)
	{
		byte[] prefix = new byte[28];
		System.arraycopy(ChatByteUtils.longToBytes(id), 0, prefix, 0, 8);
		System.arraycopy(ChatByteUtils.longToBytes(cmd), 0, prefix, 8, 8);
		System.arraycopy(ChatByteUtils.longToBytes(version), 0, prefix, 16, 8);
		System.arraycopy(ChatByteUtils.intToBytes(length), 0, prefix, 24, 4);
		byte[] array = new byte[28+length];
		System.arraycopy(prefix, 0, array, 0, 28);
		System.arraycopy(data, 0, array, 28, length);
		return array;
	}
	
	public static Map getObject(byte[] bytes)
	{
		Map map = new HashMap();
		byte[] idb = new byte[8];
		byte[] cmdd = new byte[8];
		byte[] versiond = new byte[8];
		byte[] lengthd = new byte[4];
		System.arraycopy(bytes, 0, idb, 0, 8);
		System.arraycopy(bytes, 8, cmdd, 0, 8);
		System.arraycopy(bytes, 16, versiond, 0, 8);
		System.arraycopy(bytes, 24, lengthd, 0, 4);
		long id = ChatByteUtils.bytesToLong(idb);
		long cmd = ChatByteUtils.bytesToLong(cmdd);
		long version = ChatByteUtils.bytesToLong(versiond);
		int length = ChatByteUtils.bytesToInt(lengthd);
		byte[] d = new byte[length];
		System.arraycopy(bytes, 28, d, 0, length);
		map.put("id", id);
		map.put("cmd", cmd);
		map.put("version", version);
		map.put("length", length);
		map.put("data", d);
		return map;
	}
	
	public static void main(String[] args)
	{
		String test = "this is a test";
		String ss = "hello";
		byte[] te = test.getBytes();
		byte[] p = ss.getBytes();
		byte[] data = toBytes(123456,4567890,1234567890,te.length,te);
		byte[] add = new byte[data.length+p.length];
		System.arraycopy(data, 0, add, 0, data.length);
		System.arraycopy(p, 0, add, data.length, p.length);
		Map map = getObject(add);
		
	}
}
