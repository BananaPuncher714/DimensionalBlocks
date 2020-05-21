package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

public abstract class DBlock {
	private final NamespacedKey key;
	
	public DBlock( NamespacedKey key ) {
		this.key = key;
	}
	
	public final NamespacedKey getKey() {
		return key;
	}
	
	public void stepOn( Location location, Entity entity ) {}
	
	public abstract DInfo getBlockInfo();
	public abstract DState< ? >[] getStates();
}
