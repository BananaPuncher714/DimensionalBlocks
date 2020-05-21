package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library;

import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.MagicalPlanks.TileEntityMagicStorage;
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
		
		register( new MagicalPlanks(), TileEntityMagicStorage::new, "magic_storage" );
	}
	
	public static void register( DBlock block ) {
		handler.register( block );
	}
	
	public static void register( DBlock block, Supplier< DTileEntity > tileEntity, String id ) {
		handler.register( block, tileEntity, id );
	}
	
	public static DBlockData getDBlockDataAt( Location location ) {
		return handler.getBlockDataAt( location );
	}
	
	public static DBlockData setDBlockAt( DBlock block, Location location ) {
		return handler.setDBlockAt( block, location);
	}
	
	public static void setDBlockDataAt( DBlockData data, Location location ) {
		handler.setDBlockDataAt( data, location );
	}
	
	public static DBlockData getDefaultBlockDataFor( DBlock block ) {
		return handler.getDefaultBlockDataFor( block );
	}
	
	public static DTileEntity getDTileEntityAt( Location location ) {
		return handler.getDTileEntityAt( location );
	}
}
