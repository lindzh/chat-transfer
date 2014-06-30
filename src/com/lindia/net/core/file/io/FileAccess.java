package com.lindia.net.core.file.io;

import com.lindia.net.core.file.config.FileConfig;

public interface FileAccess
{
	public void setFileConfig(FileConfig fileConfig,boolean read);
	
	public void init();
	
	public void write(long id,long position,byte[] data);
	
	public int read(long id,long position,byte[] data);
	
	public void close();
}
