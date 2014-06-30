package com.lindia.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomFileTest
{
	public static void main(String[] args)
	{
		try {
			RandomAccessFile file = new RandomAccessFile("D:\\test.jpg","rw");

			long len = file.length();
			System.out.println(file.getFilePointer());
			File file2 = new File("D:\\test1.jpg");
			RandomAccessFile fileaccess;
			if(!file2.exists())
			{
				file2.createNewFile();
				fileaccess = new RandomAccessFile("D:\\test1.jpg","rw");
				byte[] bytes = new byte[1024*512];
				long len2 = 0;
				while(len2+1024*512<len)
				{
					fileaccess.write(bytes);
					len2 += 1024*512;
				}
				int toWrite = (int)(len-len2);
				fileaccess.write(bytes,0,toWrite);
				
				long read = 0;
				int rst = 1;
				fileaccess.seek(0);
				while(rst>0)
				{
					byte[] buffer = new byte[1024*512];
					rst = file.read(buffer);
					fileaccess.seek(read);
					if(rst>0)
					{
						fileaccess.write(buffer, 0, rst);
					}
					read += rst;
				}
				file.close();
				fileaccess.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
