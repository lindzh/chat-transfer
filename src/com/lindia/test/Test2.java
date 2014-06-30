package com.lindia.test;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

import com.lindia.net.core.datagram.NetConfig;

public class Test2
{
	public static void main(String[] args)
	{
		NetConfig config = new NetConfig();
		config.setDestIp("127.0.0.1");
		int dest = 9999;
		int my = 8888;
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
				String str = "---------------2**********>>>>  "+index;
				while(true)
				{
					str = "---------------2**********>>>>  "+index;
				//	boolean sended = s.send(str.getBytes());
					try {
						Thread.currentThread().sleep(10);
					//	if(sended) index++;
						if(index >= 2000)
						{
							s.close();
							break;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}else{
				s.close();
				System.out.println("Can't connect to "+config.getDestIp()+":"+config.getDestPort());
			}
	
			System.exit(0);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
