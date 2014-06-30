package com.lindia.net.core.file.utils;

import java.util.HashMap;
import java.util.Map;

import com.lindia.utils.ChatByteUtils;

public class NetFileUtils
{
	public static byte[] toBytes(long fileId,long pieceId,long position,int length,byte[] data)
	{
		byte[] prefix = new byte[28];
		System.arraycopy(ChatByteUtils.longToBytes(fileId), 0, prefix, 0, 8);
		System.arraycopy(ChatByteUtils.longToBytes(pieceId), 0, prefix, 8, 8);
		System.arraycopy(ChatByteUtils.longToBytes(position), 0, prefix, 16, 8);
		System.arraycopy(ChatByteUtils.intToBytes(length), 0, prefix, 24, 4);
		byte[] array = new byte[28+length];
		System.arraycopy(prefix, 0, array, 0, 28);
		System.arraycopy(data, 0, array, 28, length);
		return array;
	}
	
	public static Map getObject(byte[] bytes)
	{
		Map map = new HashMap();
		byte[] fileId = new byte[8];
		byte[] pieceId = new byte[8];
		byte[] position = new byte[8];
		byte[] lengthd = new byte[4];
		System.arraycopy(bytes, 0, fileId, 0, 8);
		System.arraycopy(bytes, 8, pieceId, 0, 8);
		System.arraycopy(bytes, 16, position, 0, 8);
		System.arraycopy(bytes, 24, lengthd, 0, 4);
		long id = ChatByteUtils.bytesToLong(fileId);
		long cmd = ChatByteUtils.bytesToLong(pieceId);
		long version = ChatByteUtils.bytesToLong(position);
		int length = ChatByteUtils.bytesToInt(lengthd);
		byte[] d = new byte[length];
		System.arraycopy(bytes, 28, d, 0, length);
		map.put("fileId", id);
		map.put("pieceId", cmd);
		map.put("position", version);
		map.put("length", length);
		map.put("data", d);
		return map;
	}
}
