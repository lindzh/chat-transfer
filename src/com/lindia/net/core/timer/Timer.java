package com.lindia.net.core.timer;

import com.lindia.net.core.listener.TimerListener;

public interface Timer
{
	public void start(long interval);
	
	public void addjust(long interval);
	
	public void stop();
	
	public void addTimerListener(TimerListener listener);
	
	public static final long INTERVAL_MIN = 10;
	
	public static final long INVERVAL_MAX = 1000;
}
