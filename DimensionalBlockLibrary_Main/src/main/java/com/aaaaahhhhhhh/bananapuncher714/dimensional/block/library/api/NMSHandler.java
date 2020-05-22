package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import java.io.File;
import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.block.Block;

public interface NMSHandler {
	boolean register( DBlock block, Supplier< DTileEntity > tileEntity, String id );
	boolean register( DBlock block );
	
	DBlockData getDBlockDataAt( Location location );
	DBlockData setDBlockAt( DBlock block, Location location );
	DBlockData setDBlockAt( DBlock block, Location location, boolean doPhysics );
	void setDBlockDataAt( DBlockData data, Location location );
	void setDBlockDataAt( DBlockData data, Location location, boolean doPhysics );
	DBlockData getDefaultBlockDataFor( DBlock block );
	DBlockData getDBlockDataFrom( Block block );
	DTileEntity getDTileEntityAt( Location location );
	
	Location getLocationOf( DTileEntity entity );
	DBlockData getDBlockDataOf( DTileEntity entity );
	
	void tick( Location location, DBlock block, int delay );
	void applyPhysics( DBlock block, Location location );
	
	void createDumpDataFile( File dump );
}
