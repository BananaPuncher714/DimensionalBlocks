package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import java.util.function.Supplier;

import org.bukkit.Location;

public interface NMSHandler {
	boolean register( DBlock block, Supplier< DTileEntity > tileEntity, String id );
	boolean register( DBlock block );
	
	DBlockData getBlockDataAt( Location location );
	DBlockData setDBlockAt( DBlock block, Location location );
	void setDBlockDataAt( DBlockData data, Location location );
	DBlockData getDefaultBlockDataFor( DBlock block );
	DTileEntity getDTileEntityAt( Location location );
}
