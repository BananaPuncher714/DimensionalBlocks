package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.states;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DState;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;

/**
 * A DState implementation for enums.
 * 
 * @param < T >
 * Some arbitrary enum 
 */
public class DStateEnum< T extends Enum< T > > extends DState< T > {
	private final Set< T > values;
	private final Map< String, T > mappedVals = new HashMap< String, T >();
	
	protected DStateEnum( String id, Class< T > clazz, Collection< T > values ) {
		super( id, clazz );

		this.values = ImmutableSet.copyOf( values );
		
		for ( T v : values ) {
			String name = v.name().toLowerCase();
			if ( mappedVals.put( v.name(), v ) != null ) {
				throw new IllegalArgumentException( "Multiple values have the same name '" + name + "'" );
			}
		}
	}

	@Override
	public String convertToString( T value ) {
		return value.name();
	}
	
	@Override
	public Optional< T > getFrom( String value ) {
		return Optional.ofNullable( mappedVals.get( value.toLowerCase() ) );
	}

	@Override
	public Collection< T > getValues() {
		return values;
	}

	public static < T extends Enum< T > > DStateEnum< T > of( String id, Class< T > clazz ) {
		return of( id, clazz, Predicates.alwaysTrue() );
	}

	public static < T extends Enum< T > > DStateEnum< T > of( String id, Class< T > clazz, Predicate< T > predicate ) {
		return of( id, clazz, Arrays.stream( clazz.getEnumConstants() ).filter( predicate ).collect( Collectors.toList() ) );
	}
	
	@SafeVarargs
	public static < T extends Enum< T > > DStateEnum< T > of( String id, Class< T > clazz, T... vals ) {
		return of( id, clazz, Arrays.asList( vals ) );
	}
	
	public static < T extends Enum< T > > DStateEnum< T > of( String id, Class< T > clazz, Collection< T > vals ) {
		return new DStateEnum< T >( id, clazz, vals );
	}
}
