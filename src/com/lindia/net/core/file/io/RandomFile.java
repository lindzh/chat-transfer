package com.lindia.net.core.file.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.lindia.net.core.file.config.FileConfig;

public class RandomFile implements FileAccess
{
	private boolean read;
	private FileConfig config;
	private RandomAccessFile randomFile;
	private boolean inited = false;

	@Override
	public void setFileConfig(FileConfig fileConfig, boolean read)
	{
		config = fileConfig;
		this.read = read;
	}

	@Override
	public void init()
	{
		if (read) {
			try {
				randomFile = new RandomAccessFile(config.getFile(), "rw");
				inited = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				
				File file = new File(config.getFile());
				if(!file.exists())
				{
					file.createNewFile();
					randomFile = new RandomAccessFile(config.getFile(), "rw");
					long writeLength = 0;
					int bufferSize = 1024*1024;
					byte[] buffer = new byte[bufferSize];
					while(writeLength+bufferSize<config.getFileSize())
					{
						randomFile.write(buffer,0,buffer.length);
						writeLength += bufferSize;
					}
					int len = (int)(config.getFileSize()-writeLength);
					randomFile.write(buffer, 0, len);
					randomFile.seek(0);
				}
				else{
					randomFile = new RandomAccessFile(config.getFile(), "rw");
					randomFile.seek(0);
				}
				inited = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void write(long id, long position, byte[] data)
	{
		if (!read && inited) {
			if (id == config.getFileId()) {
				try {
					randomFile.seek(position);
					randomFile.write(data,0,data.length);
				} catch (IOException e) {
					System.out.println("文件写失败");
				}
			}
		}
	}

	@Override
	public int read(long id, long position, byte[] data)
	{
		if (read && inited) {
			if (id == config.getFileId()) {
				try {
					randomFile.seek(position);
					return randomFile.read(data,0,data.length);
				} catch (IOException e) {
					System.out.println("文件读失败");
				}
			}
		}
		return 0;
	}

	@Override
	public void close()
	{
		try {
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
