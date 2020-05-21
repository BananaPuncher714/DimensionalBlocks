package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.util.NBTEditor.NBTCompound;

public abstract class DTileEntity {
	public abstract void load( NBTCompound compound );
	public abstract void save( NBTCompound compound );
}
