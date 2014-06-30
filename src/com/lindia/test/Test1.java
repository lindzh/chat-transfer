package com.lindia.test;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

import com.lindia.net.core.datagram.NetConfig;

public class Test1
{
	public static void main(String[] args)
	{
		NetConfig config = new NetConfig();
		config.setDestIp("127.0.0.1");
		int dest = 8888;
		int my = 9999;
		config.setDestPort(dest);
		config.setSelfPort(my);
		DatagramSocket socket;
		try {
			socket = new DatagramSocket(my);
			//通路才可以用
			config.setDataGramSocket(socket);
			MyNarqSocket s = new MyNarqSocket();
			s.setNetConfig(config);
			if(s.connect()){
				long index = 0;
				String str = "---------------1----------->  "+index;
				while(true)
				{
					str = "---------------1----------->  "+index;
					boolean sended = s.send(str.getBytes());
					try {
						Thread.currentThread().sleep(10);
						if(sended) index++;
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}else{
				s.close();
				System.out.println("Can't connect to "+config.getDestIp()+":"+config.getDestPort());
			}
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
