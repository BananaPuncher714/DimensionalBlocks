package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.util;

import org.bukkit.Bukkit;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.NMSHandler;

/**
 * Internal use only
 * 
 * @author BananaPuncher714
 */
public final class ReflectionUtil {
	public static final String VERSION;
	
	static {
		VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}

	private ReflectionUtil() {
	}
	
	/**
	 * Internal use only
	 * 
	 * @return
	 * Fetch a version specific NMS handler
	 */
	public static NMSHandler getNewNMSHandlerInstance() {
		try {
			Class< ? > clazz = Class.forName( "com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.implementation." + VERSION + ".NMSHandler" );
			return ( NMSHandler ) clazz.newInstance();
		} catch ( ClassNotFoundException | InstantiationException | IllegalAccessException e ) {
			e.printStackTrace();
			return null;
		}
	}
}
