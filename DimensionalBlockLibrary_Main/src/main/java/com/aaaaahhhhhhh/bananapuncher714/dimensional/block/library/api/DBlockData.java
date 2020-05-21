package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import org.bukkit.block.data.BlockData;

public interface DBlockData {
	DBlock getBlock();
	void setAsDefault();
	void setClientBlock( BlockData craftData );
	< T extends Comparable< T > > T get( DState< T > state );
	< T extends Comparable< T > > DBlockData set( DState< T > state, T value );
}
