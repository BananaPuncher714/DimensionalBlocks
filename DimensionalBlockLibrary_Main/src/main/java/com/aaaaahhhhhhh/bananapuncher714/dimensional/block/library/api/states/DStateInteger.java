package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.states;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DState;
import com.google.common.collect.ImmutableSet;

/**
 * A DState implementation for a range of integers.
 */
public class DStateInteger extends DState< Integer > {
	private final int min, max;
	private final Collection< Integer > vals;
	
	protected DStateInteger( String id, int min, int max ) {
		super( id, Integer.class );

		if ( min < 0 ) {
			throw new IllegalArgumentException( "Min value of " + id + " must be 0 or greater" );
		} else if ( max <= min ) {
			throw new IllegalArgumentException( "Min value of " + id + " must be greater than min (" + min + ")" );
		}
		
		this.min = min;
		this.max = max;
		
		Set< Integer > vals = new HashSet< Integer >();
		for ( int i = min; i <= max; i++ ) {
			vals.add( i );
		}
		this.vals = ImmutableSet.copyOf( vals );
	}
	
	public final int getMin() {
		return min;
	}
	
	public final int getMax() {
		return max;
	}

	@Override
	public String convertToString( Integer value ) {
		return value.toString();
	}

	@Override
	public Optional< Integer > getFrom(String value) {
		try {
			Integer integer = Integer.valueOf( value );

			return vals.contains( integer ) ? Optional.of( integer ) : Optional.empty();
		} catch ( NumberFormatException numberformatexception ) {
			return Optional.empty();
		}
	}

	@Override
	public Collection< Integer > getValues() {
		return vals;
	}

	/**
	 * Create a new DStateInteger with a range from the min to max, inclusive.
	 * 
	 * @param id
	 * @param min
	 * The minimum number that this state can take on.
	 * @param max
	 * The maximum number that this state can take on.
	 * @return
	 */
	public static DStateInteger of( String id, int min, int max ) {
		return new DStateInteger( id, min, max );
	}
}
