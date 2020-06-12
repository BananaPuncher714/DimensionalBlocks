package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import org.bukkit.block.data.BlockData;
import org.bukkit.util.BoundingBox;

public interface DBlockData {
	/**
	 * Get the BlockData associated with this state.
	 * 
	 * @return
	 */
	BlockData getBlockData();
	
	/**
	 * Get the {@link DBlock} representing this data.
	 * 
	 * @return
	 */
	DBlock getBlock();
	
	/**
	 * Set this as the default data when the block gets placed.
	 */
	void setAsDefault();

	/**
	 * Get the bounding boxes that make up the collidable shape of the block.
	 * 
	 * @return
	 */
	BoundingBox[] getCollisionShape();
	
	/**
	 * Get the bounding boxes that make up the visible hitbox of the block.
	 * 
	 * @return
	 */
	BoundingBox[] getHitbox();
	
	/**
	 * Get the value of one of the states of this data. The {@link DState} must be included in the {@link DBlock#getStates()} method.
	 * 
	 * @param <T>
	 * @param state
	 * @return
	 */
	< T extends Comparable< T > > T get( DState< T > state );
	
	/**
	 * Set the value of one of the states of this data. The {@link DState} must be included in the {@link DBlock#getStates()} method.
	 * 
	 * @param <T>
	 * @param state
	 * @param value
	 * @return
	 */
	< T extends Comparable< T > > DBlockData set( DState< T > state, T value );
	
	/**
	 * Increment the DBlockData to the next available state.
	 * 
	 * @param <T>
	 * @param state
	 * @return
	 */
	< T extends Comparable< T > > T increment( DState< T > state );
}
