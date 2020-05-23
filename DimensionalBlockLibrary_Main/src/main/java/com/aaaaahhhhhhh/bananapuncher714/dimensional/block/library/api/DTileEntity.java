package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import org.bukkit.Location;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.DimensionalBlocks;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.util.NBTEditor.NBTCompound;

public abstract class DTileEntity {
	/**
	 * Called when loading from file. Initialize your values here.
	 * 
	 * @param compound
	 */
	public abstract void load( NBTCompound compound );
	
	/**
	 * Called when saving to file. Save your persistent data here.
	 * 
	 * @param compound
	 */
	public abstract void save( NBTCompound compound );
	
	/**
	 * Called once a tick.
	 */
	public void tick() {};
	
	/**
	 * Get the location associated with this tile entity.
	 * 
	 * @return
	 */
	public final Location getLocation() {
		return DimensionalBlocks.getLocationOf( this );
	}
	
	/**
	 * Get the DBlockData associated with this tile entity.
	 * 
	 * @return
	 */
	public final DBlockData getBlockData() {
		return DimensionalBlocks.getDBlockDataOf( this );
	}
}
