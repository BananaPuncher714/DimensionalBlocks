package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import java.util.Collection;
import java.util.Optional;

public abstract class DState< T extends Comparable< T > > {
	private final String id;
	private final Class< T > clazz;
	
	public DState( String id, Class< T > clazz ) {
		this.id = id;
		this.clazz = clazz;
	}
	
	public final String getId() {
		return id;
	}
	
	public final Class< T > getType() {
		return clazz;
	}
	
	
	@Override
	public final boolean equals( Object obj ) {
		if ( this == obj ) {
			return true;
		}
		
		if ( obj instanceof DState ) {
			DState state = ( DState ) obj;
			
			return state.clazz == clazz && state.id.equals( id );
		}
		return false;
	}

	public abstract String convertToString( T value );
    public abstract Optional< T > getFrom( String value );
    public abstract Collection< T > getValues();
}
