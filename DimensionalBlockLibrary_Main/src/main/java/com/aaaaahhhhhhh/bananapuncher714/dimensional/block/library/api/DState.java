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
			DState< ? > state = ( DState< ? > ) obj;
			
			return state.clazz == clazz && state.id.equals( id );
		}
		return false;
	}

	/**
	 * Convert the provided object to string.
	 * 
	 * @param value
	 * The value that needs converting.
	 * @return
	 * A string which may be used in serialization.
	 */
	public abstract String convertToString( T value );
	
	/**
	 * Get an optional from the provided string.
	 * 
	 * @param value
	 * A string representing a value of the object.
	 * @return
	 * An optional that may contain a valid object.
	 */
    public abstract Optional< T > getFrom( String value );
    
    /**
     * Get all the valid values that this state could take on.
     * 
     * @return
     * A collection of all valid states.
     */
    public abstract Collection< T > getValues();
}
