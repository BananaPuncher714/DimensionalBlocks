package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import org.bukkit.block.data.BlockData;

public interface DBlockData {
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
}
