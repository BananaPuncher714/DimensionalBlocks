package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

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
}
