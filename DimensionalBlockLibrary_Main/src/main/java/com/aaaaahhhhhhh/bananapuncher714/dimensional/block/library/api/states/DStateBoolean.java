package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.states;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DState;
import com.google.common.collect.ImmutableSet;

/**
 * A DState implementation for booleans.
 */
public class DStateBoolean extends DState< Boolean > {
	private final Set< Boolean > values;
	
	protected DStateBoolean( String id ) {
		super( id, Boolean.class );
		
		values = ImmutableSet.of( true, false );
	}

	@Override
	public String convertToString( Boolean value ) {
		return value.toString();
	}

	@Override
	public Optional< Boolean > getFrom( String value ) {
		if ( "true".equals( value ) || "false".equals( value ) ) {
			return Optional.of( Boolean.valueOf( value ) );
		}
		return Optional.empty();
	}

	@Override
	public Collection< Boolean > getValues() {
		return values;
	}
	
	/**
	 * Create a new DStateBoolean with the provided id.
	 * 
	 * @param id
	 * @return
	 */
	public DStateBoolean of( String id ) {
		return new DStateBoolean( id );
	}
}
