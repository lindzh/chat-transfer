package com.lindia.net.core.datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map;

import com.lindia.net.core.cache.Data;
import com.lindia.net.core.exception.NetException;
import com.lindia.utils.NetUtils;

public class NetManagerImpl implements NetManager
{
	private NetConfig netConfig;
	private InetSocketAddress address;
	
	@Override
	public void setNetConfig(NetConfig config)
	{
		this.netConfig = config;
		this.address = new InetSocketAddress(this.netConfig.getDestIp(),this.netConfig.getDestPort());
		String test = "echo test";
		byte[] bytes = test.getBytes();
		try {
			new DatagramPacket(bytes,bytes.length,address);
		} catch (SocketException e) {
			throw new NetException("wrong host ip port");
		}
	}

	@Override
	public boolean send(Data data)
	{
		byte[] bytes = NetUtils.toBytes(data.getId(), data.getCmd(), data.getVersion(), data.getLength(), data.getData());
		try {
			DatagramPacket packet = new DatagramPacket(bytes,bytes.length,address);
			netConfig.getDataGramSocket().send(packet);
			return true;
		} catch (SocketException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		
	}

	@Override
	public boolean receive(Data data)
	{
		byte[] buffer = new byte[NetConfig.MAX_PACKET_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer,NetConfig.MAX_PACKET_SIZE);
		try {
			netConfig.getDataGramSocket().receive(packet);
			Map map = NetUtils.getObject(buffer);
			data.setId((Long)map.get("id"));
			data.setCmd((Long)map.get("cmd"));
			data.setVersion((Long)map.get("version"));
			data.setLength((Integer)map.get("length"));
			data.setData((byte[])map.get("data"));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
