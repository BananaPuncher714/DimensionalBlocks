package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.implementation.v1_15_R1;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlock;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DInfo;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DState;

import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.BlockStateList;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EnumPistonReaction;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.Material;
import net.minecraft.server.v1_15_R1.MaterialMapColor;
import net.minecraft.server.v1_15_R1.World;

public class BananaBlock extends Block {
	private static Field MATERIAL_ENUM_PISTON_REACTION;
	private static Field BLOCK_INFO_MATERIAL;
	private static Field BLOCK_INFO_STRENGTH;
	
	protected static Map< Long, DBlock > GLOBAL_BLOCK_MAP = new ConcurrentHashMap< Long, DBlock >();
	
	static {
		try {
			MATERIAL_ENUM_PISTON_REACTION = Material.a.class.getDeclaredField( "a" );
			MATERIAL_ENUM_PISTON_REACTION.setAccessible( true );
			
			BLOCK_INFO_MATERIAL = Info.class.getDeclaredField( "a" );
			BLOCK_INFO_MATERIAL.setAccessible( true );
			
			BLOCK_INFO_STRENGTH = Info.class.getDeclaredField( "f" );
			BLOCK_INFO_STRENGTH.setAccessible( true );
		} catch ( NoSuchFieldException | SecurityException e ) {
			e.printStackTrace();
		}
	}
	
	private DBlock block;

	private Map< String, BananaState< ? > > states;
	
	public BananaBlock( DBlock block ) {
	    // Construct the info from the block
		super( getInfoFrom( block ) );
		
		this.block = block;
		
		initializeStates( block );
	}
	
	@Override
	public void stepOn( World world, BlockPosition position, Entity entity ) {
	    Location location = new Location( world.getWorld(), position.getX(), position.getY(), position.getZ() );
	    block.stepOn( location, entity.getBukkitEntity() );
	}
	
    @Override
    protected void a( BlockStateList.a< Block, IBlockData > blockstatelist ) {
        initializeStates( block == null ? GLOBAL_BLOCK_MAP.get( Thread.currentThread().getId() ) : block );
        blockstatelist.a( states.values().toArray( new BananaState[ states.size() ] ) );
    }
    
	public DBlock getBlock() {
	    return block;
	}
	
	public < T extends Comparable< T > > T get( DState< T > state, IBlockData data ) {
	    String id = state.getId();
	    BananaState bState = states.get( id );
	    if ( bState == null ) {
	        throw new IllegalArgumentException( "State not part of this block!" );
	    }
	    DState dState = bState.getState();
	    if ( dState.equals( state ) ) {
	        return ( T ) data.get( bState );
	    } else {
	        throw new IllegalArgumentException( "Requested Invalid State" );
	    }
	}
	
	public < T extends Comparable< T > > IBlockData set( DState< T > state, T value, IBlockData data ) {
	    String id = state.getId();
        BananaState bState = states.get( id );
        if ( bState == null ) {
            throw new IllegalArgumentException( "State not part of this block!" );
        }
	    DState dState = bState.getState();
	    if ( dState.equals( state ) ) {
	        return data.set( bState, value );
	    } else {
	        throw new IllegalArgumentException( "Requested Invalid State" );
	    }
	}
	
	private void initializeStates( DBlock block ) {
	    // Dumb generics and their type erasure
	    if ( states == null ) {
	        states = new HashMap< String, BananaState< ? > >();
	        for ( DState< ? > state : block.getStates() ) {
	            BananaState< ? > bState = new BananaState( state );
	            states.put( state.getId(), bState );
	        }
	    }
	}
	
	private static Info getInfoFrom( DBlock block ) {
		DInfo info = block.getBlockInfo();

		Color color = info.getMapColor();
		Material.a material = new Material.a( computeNearest( color.getRed(), color.getGreen(), color.getBlue() ) );

		try {
			MATERIAL_ENUM_PISTON_REACTION.set( material, EnumPistonReaction.valueOf( info.getPistonReaction().name() ) );
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
			e.printStackTrace();
		}
		
		CraftBlockData cData = ( CraftBlockData ) info.getBlockData();
		Info blockInfo = Info.a( cData.getState().getBlock() );
		try {
		    BLOCK_INFO_MATERIAL.set( blockInfo, material.i() );
		    BLOCK_INFO_STRENGTH.set( blockInfo, info.getExplosionStrength() );
		} catch ( IllegalArgumentException | IllegalAccessException e ) {
            e.printStackTrace();
        }
		
		return blockInfo;
	}
	
	private static MaterialMapColor computeNearest( int red, int green, int blue ) {
		float best_distance = Float.MAX_VALUE;
		MaterialMapColor best = null;
		MaterialMapColor val = null;
		for ( int i = 0; ( val = MaterialMapColor.a[ i ] ) != null && i < 64; i++ ) {
			int col = val.rgb;
			float distance = getDistance(red, green, blue, col >> 16 & 0xFF, col >> 8 & 0xFF, col & 0xFF);
			if ( best == null || distance < best_distance ) {
				best_distance = distance;
				best = val;
			}
		}
		return best;
	}
	
	private static float getDistance( int red, int green, int blue, int red2, int green2, int blue2 ) {
		float red_avg = ( red + red2 ) * .5f;
		int r = red - red2;
		int g = green - green2;
		int b = blue - blue2;
		float weight_red = 2.0f + red_avg * ( 1f / 256f );
		float weight_green = 4.0f;
		float weight_blue = 2.0f + ( 255.0f - red_avg ) * ( 1f / 256f );
		return weight_red * r * r + weight_green * g * g + weight_blue * b * b;
	}
}
