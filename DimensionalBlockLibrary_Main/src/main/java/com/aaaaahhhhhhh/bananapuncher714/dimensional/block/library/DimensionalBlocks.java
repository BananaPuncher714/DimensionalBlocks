package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library;

import java.io.File;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
	}
	
	@Override
	public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
		if ( sender.hasPermission( "dimensionalblocklibrary.admin" ) ) {
			File dump = new File( getDataFolder() + "/" + "dump.txt" );
			createBlockStateDumpFile( dump );
			sender.sendMessage( ChatColor.BLUE + "Dumped block state files!" );
		} else {
			sender.sendMessage( ChatColor.RED + "You do not have permission to run this command!" );
		}
		return true;
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
	
	public static DBlockData getDBlockDataFrom( Block block ) {
		return handler.getBlockDataFrom( block );
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
	
	public static void createBlockStateDumpFile( File dump ) {
		handler.createDumpDataFile( dump );
	}
}
