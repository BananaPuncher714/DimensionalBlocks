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
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlock;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlockData;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DInfo;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DTileEntity;

import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.RegistryBlockID;
import net.minecraft.server.v1_15_R1.TileEntityTypes;
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
    
    private Map< DTileEntity, Set< Block > > tileEntityBlocks = new HashMap< DTileEntity, Set< Block > >();
    
    @Override
    public boolean register( DBlock block, DTileEntity tileEntity ) {
        // Add a new tile entity
        Supplier< BananaTileEntity > supplier = () -> { return new BananaTileEntity( IRegistry.BLOCK_ENTITY_TYPE.get( new MinecraftKey( tileEntity.getId() ) ), tileEntity ); };
        BananaBlock nmsBlock = new BananaBlockTileEntity( block, supplier );

        Set< Block > blocks = tileEntityBlocks.getOrDefault( tileEntity, new HashSet< Block >() );
        tileEntityBlocks.put( tileEntity, blocks );

        blocks.add( nmsBlock );

        TileEntityTypes< BananaTileEntity > tileEntityType = new TileEntityTypes< BananaTileEntity >( supplier, blocks, null );
        IRegistry.a( IRegistry.BLOCK_ENTITY_TYPE, tileEntity.getId(), tileEntityType );
	    
	    return register( nmsBlock, block );
	}

	@Override
	public boolean register( DBlock block ) {
	    return register( new BananaBlock( block ), block );
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

        return true;
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
}
