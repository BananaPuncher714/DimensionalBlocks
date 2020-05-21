package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlock;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlockData;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DTileEntity;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.NMSHandler;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.util.ReflectionUtil;

public class DimensionalBlocks extends JavaPlugin {
	private static NMSHandler handler;
	
	@Override
	public void onEnable() {
		handler = ReflectionUtil.getNewNMSHandlerInstance();
		
		register( new MagicalPlanks() );
	}
	
	public static void register( DBlock block ) {
		handler.register( block );
	}
	
	public static void register( DBlock block, DTileEntity tileEntity ) {
		handler.register( block, tileEntity );
	}
	
	public static DBlockData getDBlockDataAt( Location location ) {
		return handler.getBlockDataAt( location );
	}
	
	public static DBlockData setDBlockAt( DBlock block, Location location ) {
		return handler.setDBlockAt( block, location);
	}
}
