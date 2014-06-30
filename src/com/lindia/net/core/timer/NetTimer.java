package com.lindia.net.core.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import com.lindia.net.core.listener.TimerListener;

public class NetTimer implements Timer
{
	private List<TimerListener> listeners = new ArrayList<TimerListener>();
	private java.util.Timer timer = new java.util.Timer();
	private String type;
	
	public NetTimer(String type)
	{
		this.type = type;
	}
	
	@Override
	public void start(final long interval)
	{
		timer.schedule(new TimerTask(){
			@Override
			public void run()
			{
				for(TimerListener listener:listeners)
				{
					listener.doTimesUp(type,interval);
				}
			}
		}, 10, interval);
	}

	@Override
	public void addjust(final long interval)
	{
		timer.cancel();
		timer.schedule(new TimerTask(){
			@Override
			public void run()
			{
				for(TimerListener listener:listeners)
				{
					listener.doTimesUp(type,interval);
				}
			}
		}, 10, interval);		
	}

	@Override
	public void stop()
	{
		timer.cancel();
	}

	@Override
	public void addTimerListener(TimerListener listener)
	{
		listeners.add(listener);
	}
	
}
