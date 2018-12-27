package com.huawei.rest.client.hpsdemo.util;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;


public class PathUtils
{
	
	private static final String CHARSET = "UTF-8";
	
	private static BitSet URI_UNRESERVED_CHARACTERS = new BitSet();
	
	private static String[] PERCENT_ENCODED_STRINGS = new String[256];
	
	static 
	{
		for (int i = 97; i <= 122; i++)
		{
			PathUtils.URI_UNRESERVED_CHARACTERS.set(i);
		}
		for (int i = 65; i <= 90; i++) 
		{
			PathUtils.URI_UNRESERVED_CHARACTERS.set(i);
		}
		for (int i = 48; i <= 57; i++)
		{
			PathUtils.URI_UNRESERVED_CHARACTERS.set(i);
		}
		PathUtils.URI_UNRESERVED_CHARACTERS.set(45);
		PathUtils.URI_UNRESERVED_CHARACTERS.set(46);
		PathUtils.URI_UNRESERVED_CHARACTERS.set(95);
		PathUtils.URI_UNRESERVED_CHARACTERS.set(126);
		
		for (int i = 0; i < PathUtils.PERCENT_ENCODED_STRINGS.length; i++)
		{
			PathUtils.PERCENT_ENCODED_STRINGS[i] = String.format("%%%02X", new Object[] {Integer.valueOf(i)});
		}
	}
	
	public static String normalizePath(String path) 
	{
		return normalize(path).replace("%2F", "/");
	}
	
	
	public static String normalize(String value)
	{
		try {
			StringBuilder builder = new StringBuilder();
			for (byte b : value.getBytes(PathUtils.CHARSET))
			{
				if (PathUtils.URI_UNRESERVED_CHARACTERS.get(b & 0xFF))
				{
					builder.append((char) b);
				}
				else 
				{
					builder.append(PathUtils.PERCENT_ENCODED_STRINGS[(b & 0xFF)]);
				}
			}
			return builder.toString();
		} 
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	
	
}
