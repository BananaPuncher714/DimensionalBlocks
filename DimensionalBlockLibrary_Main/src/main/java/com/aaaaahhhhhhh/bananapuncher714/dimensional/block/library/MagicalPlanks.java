package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlock;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlockData;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DInfo;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DState;

public class MagicalPlanks extends DBlock {

	public MagicalPlanks() {
		super( NamespacedKey.minecraft( "magical_wood" ) );
	}

	@Override
	public void stepOn( Location location, Entity entity ) {
		DBlockData data = DimensionalBlocks.getDBlockDataAt( location );
		DState< Integer > state = new ArbitraryInteger( "test" );
		entity.sendMessage( "Hello! " + data.get( state ) );
		data.set( state, ( int ) ( Math.random() * 4 ) + 1 );
		update( data, location );
	}
	
	@Override
	public DInfo getBlockInfo() {
		return new DInfo( Material.BOOKSHELF.createBlockData() );
	}

	@Override
	public DState< ? >[] getStates() {
		return new DState< ? >[] { new ArbitraryInteger( "test" ) };
	}

	public static class ArbitraryInteger extends DState< Integer > {
		public ArbitraryInteger( String id ) {
			super( id, Integer.class );
		}

		@Override
		public String convertToString( Integer value ) {
			return value + "";
		}

		@Override
		public Optional< Integer > getFrom( String value ) {
			if ( value.matches( "-?\\d+" ) ) {
				return Optional.of( Integer.valueOf( value ) );
			}
			return Optional.empty();
		}

		@Override
		public Collection< Integer > getValues() {
			return Arrays.asList( 1, 2, 3, 4, 5 );
		}
		
	}
}
