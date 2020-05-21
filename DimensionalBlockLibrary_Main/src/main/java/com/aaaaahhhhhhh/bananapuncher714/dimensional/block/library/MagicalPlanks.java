package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlock;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlockData;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DInfo;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DState;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DTileEntity;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.InteractionResult;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.world.CollisionResultBlock;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.util.NBTEditor;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.util.NBTEditor.NBTCompound;

public class MagicalPlanks extends DBlock {
	private static final ArbitraryInteger STATE = new ArbitraryInteger( "test" );
	
	public MagicalPlanks() {
		super( NamespacedKey.minecraft( "magical_wood" ) );
	}
	
	@Override
	public void onRegister() {
		DBlockData defData = getDefaultBlockData();
		defData.set( STATE, 4 );
		defData.setAsDefault();

		defData.set( STATE, 1 );
		defData.setClientBlock( Material.KELP_PLANT.createBlockData() );
		
		defData.set( STATE, 2 );
		defData.setClientBlock( Material.ACACIA_FENCE.createBlockData() );
	}
	
	@Override
	public InteractionResult interact( DBlockData data, Location location, HumanEntity entity, boolean mainhand, CollisionResultBlock ray ) {
		DTileEntity ent = getTileEntity( location );
		if ( ent instanceof TileEntityMagicStorage ) {
			TileEntityMagicStorage storage = ( TileEntityMagicStorage ) ent;
			entity.sendMessage( "Incremented to " + storage.increment() );
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
	
	@Override
	public void stepOn( Location location, Entity entity ) {
		DBlockData data = DimensionalBlocks.getDBlockDataAt( location );
		entity.sendMessage( "Hello! " + data.get( STATE ) );
		data.set( STATE, ( int ) ( Math.random() * 4 ) + 1 );
		update( data, location );
	}
	
	@Override
	public DInfo getBlockInfo() {
		return new DInfo( Material.BOOKSHELF.createBlockData() ).setExplosionStrength( 600 ).setMapColor( Color.BLACK );
	}

	@Override
	public DState< ? >[] getStates() {
		return new DState< ? >[] { STATE };
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
	
	public static class TileEntityMagicStorage extends DTileEntity {
		int tick = 0;

		public int increment() {
			return tick++;
		}

		@Override
		public void load( NBTCompound compound ) {
			tick = NBTEditor.getInt( compound, "custom-tag" );
		}

		@Override
		public void save( NBTCompound compound ) {
			compound.set( tick, "custom-tag" );
		}
	}
}
