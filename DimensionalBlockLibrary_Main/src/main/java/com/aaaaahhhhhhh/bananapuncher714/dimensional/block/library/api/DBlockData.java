package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

public interface DBlockData {
	DBlock getBlock();
	< T extends Comparable< T > > T get( DState< T > state );
	< T extends Comparable< T > > void set( DState< T > state, T value );
}
