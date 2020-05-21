package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.DimensionalBlocks;

public abstract class DBlock {
	private final NamespacedKey key;
	
	public DBlock( NamespacedKey key ) {
		this.key = key;
	}
	
	public final NamespacedKey getKey() {
		return key;
	}
	
	public static void update( DBlockData block, Location location ) {
		DimensionalBlocks.setDBlockDataAt( block, location );
	}
	
	public void stepOn( Location location, Entity entity ) {}
	
	public abstract DInfo getBlockInfo();
	public abstract DState< ? >[] getStates();
}
