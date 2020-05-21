package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.implementation.v1_15_R1;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlock;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlockData;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DInfo;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DTileEntity;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.world.CollisionResultBlock;

import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.MovingObjectPositionBlock;
import net.minecraft.server.v1_15_R1.RegistryBlockID;
import net.minecraft.server.v1_15_R1.TileEntity;
import net.minecraft.server.v1_15_R1.TileEntityTypes;
import net.minecraft.server.v1_15_R1.Vec3D;
import net.minecraft.server.v1_15_R1.World;

public class NMSHandler implements com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.NMSHandler {
    private static Map< Block, Material > BLOCK_MATERIAL;
    private static IdentityHashMap< IBlockData, Integer > BLOCK_ID_MAP;
    
    static {
        try {
            Field map = CraftMagicNumbers.class.getDeclaredField( "BLOCK_MATERIAL" );
            map.setAccessible( true );
            BLOCK_MATERIAL = ( Map< Block, Material > ) map.get( null );

            Field internalMap = RegistryBlockID.class.getDeclaredField( "b" );
            internalMap.setAccessible( true );
            BLOCK_ID_MAP = ( IdentityHashMap< IBlockData, Integer > ) internalMap.get( Block.REGISTRY_ID );
        } catch ( NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e ) {
            e.printStackTrace();
        }
    }
    
    private Map< String, Set< Block > > tileEntityBlocks = new HashMap< String, Set< Block > >();
    
    @Override
    public boolean register( DBlock block, Supplier< DTileEntity > tileEntity, String id ) {
        // Add a new tile entity
        Supplier< BananaTileEntity > supplier = () -> { return new BananaTileEntity( id, tileEntity.get() ); };
        // Set the DBlock for this thread while we call the constructor
        long threadId = Thread.currentThread().getId();
        BananaBlock.GLOBAL_BLOCK_MAP.put( threadId, block );
        BananaBlock nmsBlock = new BananaBlockTileEntity( block, supplier );
        BananaBlock.GLOBAL_BLOCK_MAP.remove( threadId );

        Set< Block > blocks = tileEntityBlocks.getOrDefault( tileEntity, new HashSet< Block >() );
        tileEntityBlocks.put( id, blocks );

        blocks.add( nmsBlock );

        TileEntityTypes< BananaTileEntity > tileEntityType = new TileEntityTypes< BananaTileEntity >( supplier, blocks, null );
        IRegistry.a( IRegistry.BLOCK_ENTITY_TYPE, id, tileEntityType );
	    
	    return register( nmsBlock, block );
	}

	@Override
	public boolean register( DBlock block ) {
        long threadId = Thread.currentThread().getId();
        BananaBlock.GLOBAL_BLOCK_MAP.put( threadId, block );
        BananaBlock nmsBlock = new BananaBlock( block );
        BananaBlock.GLOBAL_BLOCK_MAP.remove( threadId );
	    return register( nmsBlock, block );
	}
	
	private boolean register( BananaBlock nmsBlock, DBlock block ) {
	    // Register it with the block registry
        IRegistry.a( IRegistry.BLOCK, block.getKey().toString(), nmsBlock );

        // Add it to the NMS -> Material dictionary
        DInfo info = block.getBlockInfo();
        CraftBlockData data = ( CraftBlockData ) info.getBlockData();
        BLOCK_MATERIAL.put( nmsBlock, data.getMaterial() );
        
        // Register all the different states possible
        int id = Block.REGISTRY_ID.getId( data.getState() );
        for ( IBlockData state : nmsBlock.getStates().a() ) {
            state.c();
            
            Block.REGISTRY_ID.b( state );
            
            BLOCK_ID_MAP.put( state, id );
        }

        // Let the block do whatever steps it needs
        block.onRegister();
        
        return true;
	}
	
	protected static void setRegistryBlockId( IBlockData data, BlockData craftData ) {
	    IBlockData state = ( ( CraftBlockData ) craftData ).getState();
	    BLOCK_ID_MAP.put( data, BLOCK_ID_MAP.get( state ) );
	}
	
	protected static IBlockData getFor( IBlockData data ) {
	    return Block.REGISTRY_ID.fromId( Block.REGISTRY_ID.getId( data ) );
	}
	
	protected static CollisionResultBlock getResultFrom( World world, MovingObjectPositionBlock position ) {
	    CraftBlock block = CraftBlock.at( world, position.getBlockPosition() );
	    BlockFace face = CraftBlock.notchToBlockFace( position.getDirection() );
	    Vec3D fin = position.getPos();
	    Location interception = new Location( world.getWorld(), fin.getX(), fin.getY(), fin.getZ() );
	    return new CollisionResultBlock( interception, face, block );
	}
	
	@Override
	public DBlockData getBlockDataAt( Location location ) {
	    IBlockData data = ( ( CraftBlock ) location.getBlock() ).getNMS();
	    Block block = data.getBlock();
	    if ( block instanceof BananaBlock ) {
	        return new BananaBlockData( data );
	    }
	    return null;
	}
	
	@Override
	public DBlockData setDBlockAt( DBlock block, Location location ) {
	    World world = ( ( CraftWorld ) location.getWorld() ).getHandle();
	    
	    BlockPosition position = new BlockPosition( location.getBlockX(), location.getBlockY(), location.getBlockZ() );
	    Block nmsBlock = IRegistry.BLOCK.get( new MinecraftKey( block.getKey().toString() ) );
	    world.setTypeUpdate( position, nmsBlock.getBlockData() );
	    return getBlockDataAt( location );
	}
	
	@Override
	public void setDBlockDataAt( DBlockData data, Location location ) {
	    World world = ( ( CraftWorld ) location.getWorld() ).getHandle();
	    BlockPosition position = new BlockPosition( location.getBlockX(), location.getBlockY(), location.getBlockZ() );
	    if ( data instanceof BananaBlockData ) {
	        world.setTypeUpdate( position, ( ( BananaBlockData ) data ).getData() );
	    }
	}
	
	@Override
	public DBlockData getDefaultBlockDataFor( DBlock block ) {
	    Block nmsBlock = IRegistry.BLOCK.get( new MinecraftKey( block.getKey().toString() ) );
	    if ( nmsBlock == null ) {
	        throw new IllegalArgumentException( block.getKey().toString() + " has not been registered!" );
	    }
	    
	    return new BananaBlockData( nmsBlock.getBlockData() );
	}
	
	@Override
	public DTileEntity getDTileEntityAt( Location location ) {
	    World world = ( ( CraftWorld ) location.getWorld() ).getHandle();
	    BlockPosition position = new BlockPosition( location.getBlockX(), location.getBlockY(), location.getBlockZ() );
	    TileEntity ent = world.getTileEntity( position );
	    if ( ent instanceof BananaTileEntity ) {
	        return ( ( BananaTileEntity ) ent ).getTileEntity();
	    }
	    return null;
	}
}
