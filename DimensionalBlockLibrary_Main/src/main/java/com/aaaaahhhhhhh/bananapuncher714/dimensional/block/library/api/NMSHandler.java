package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import org.bukkit.Location;

public interface NMSHandler {
	boolean register( DBlock block, DTileEntity tileEntity );
	boolean register( DBlock block );
	
	DBlockData getBlockDataAt( Location location );
	DBlockData setDBlockAt( DBlock block, Location location );
	void setDBlockDataAt( DBlockData data, Location location );
}
