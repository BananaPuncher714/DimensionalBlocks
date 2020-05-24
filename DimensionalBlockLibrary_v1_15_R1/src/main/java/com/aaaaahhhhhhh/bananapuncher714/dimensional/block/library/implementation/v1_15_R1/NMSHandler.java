package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.implementation.v1_15_R1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_15_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_15_R1.util.CraftMagicNumbers;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlock;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlockData;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DInfo;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DTileEntity;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.world.CollisionResultBlock;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.v1_15_R1.ArgumentBlock;
import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.Chunk;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.MovingObjectPositionBlock;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
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
    private Map< NamespacedKey, BananaBlock > blockMap = new HashMap< NamespacedKey, BananaBlock >();
    
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
	    blockMap.put( block.getKey(), nmsBlock );
	    DInfo info = block.getInfo();
	    
	    // Register it with the block registry
        IRegistry.a( IRegistry.BLOCK, info.getKey().toString(), nmsBlock );

        // Add it to the NMS -> Material dictionary
        CraftBlockData data = ( CraftBlockData ) info.getBlockData();
        BLOCK_MATERIAL.put( nmsBlock, data.getMaterial() );
        
        // Let the block do whatever steps it needs
        block.onRegister();

        return true;
	}

	protected static void register( BananaTileEntity entity ) {
	}
	
	protected static void setRegistryBlockId( IBlockData data, IBlockData clientData ) {
	    BLOCK_ID_MAP.put( data, BLOCK_ID_MAP.get( clientData ) );
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
	public DBlockData getDBlockDataAt( Location location ) {
	    return getDBlockDataFrom( location.getBlock() );
	}
	
	@Override
	public DBlockData getDBlockDataFrom( org.bukkit.block.Block bBlock ) {
	    IBlockData data = ( ( CraftBlock ) bBlock ).getNMS();
	    Block block = data.getBlock();
	    if ( block instanceof BananaBlock ) {
	        return new BananaBlockData( data );
	    }
	    return null;
	}
	
	@Override
	public DBlockData setDBlockAt( DBlock block, Location location ) {
	    return setDBlockAt( block, location, true );
	}

	@Override
	public DBlockData setDBlockAt( DBlock block, Location location, boolean doPhysics ) {
	    World world = ( ( CraftWorld ) location.getWorld() ).getHandle();

	    BlockPosition position = new BlockPosition( location.getBlockX(), location.getBlockY(), location.getBlockZ() );
	    Block nmsBlock = IRegistry.BLOCK.get( new MinecraftKey( block.getInfo().getKey().toString() ) );
	    world.setTypeAndData( position, nmsBlock.getBlockData(), doPhysics ? 3 : 2 );
	    return getDBlockDataAt( location );
	}

	@Override
	public void setDBlockDataAt( DBlockData data, Location location ) {
	    setDBlockDataAt( data, location, true );
	}

	@Override
	public void setDBlockDataAt( DBlockData data, Location location, boolean doPhysics ) {
	    World world = ( ( CraftWorld ) location.getWorld() ).getHandle();
	    BlockPosition position = new BlockPosition( location.getBlockX(), location.getBlockY(), location.getBlockZ() );
	    if ( data instanceof BananaBlockData ) {
	        world.setTypeAndData( position, ( ( BananaBlockData ) data ).getData(), doPhysics ? 3 : 2 );
	    }
	}
	
	@Override
	public DBlockData getDefaultBlockDataFor( DBlock block ) {
	    BananaBlock nmsBlock = blockMap.get( block.getKey() );
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
	
	@Override
	public Location getLocationOf( DTileEntity entity ) {
	    WeakReference< BananaTileEntity > bananaTileRef = BananaTileEntity.TILE_ENTITY_MAP.get( entity );
	    if ( bananaTileRef != null ) {
	        BananaTileEntity bananaTile = bananaTileRef.get();
	        if ( bananaTile != null ) {
	            BlockPosition blockposition = bananaTile.getPosition();
	            World world = bananaTile.getWorld();
	            return new Location( world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ() );
	        }
	    }
	    return null;
	}
	
	@Override
	public DBlockData getDBlockDataOf( DTileEntity entity ) {
	    WeakReference< BananaTileEntity > bananaTileRef = BananaTileEntity.TILE_ENTITY_MAP.get( entity );
	    if ( bananaTileRef != null ) {
	        BananaTileEntity bananaTile = bananaTileRef.get();
	        if ( bananaTile != null ) {
	            IBlockData nmsData = bananaTile.getBlock();
	            return new BananaBlockData( nmsData );
	        }
	    }
	    return null;
	}
	
	@Override
	public void tick( Location location, DBlock block, int delay ) {
	    World world = ( ( CraftWorld ) location.getWorld() ).getHandle();
	    BlockPosition position = new BlockPosition( location.getBlockX(), location.getBlockY(), location.getBlockZ() );
	    Block nmsBlock = IRegistry.BLOCK.get( new MinecraftKey( block.getInfo().getKey().toString() ) );
	    // It's up to the implementation to decide how many tick laters they want. I'm not going to stop them.
	    world.getBlockTickList().a( position, nmsBlock, delay );
	}
	
	@Override
	public void applyPhysics( DBlock block, Location location ) {
	    World world = ( ( CraftWorld ) location.getWorld() ).getHandle();
	    BlockPosition position = new BlockPosition( location.getBlockX(), location.getBlockY(), location.getBlockZ() );
	    Block nmsBlock = IRegistry.BLOCK.get( new MinecraftKey( block.getInfo().getKey().toString() ) );
	    world.applyPhysics( position, nmsBlock );
	}
	
	@Override
	public void abandonShip() {
	    BananaTileEntity.TILE_ENTITY_MAP.values().forEach( t -> { t.get().saveTheCurrentCompoundBecauseSomeNoobDecidedToReloadTheServer(); } );
	}
	
	@Override
	public int cleanChunk( org.bukkit.Chunk craftChunk ) {
	    Chunk chunk = ( ( CraftChunk ) craftChunk ).getHandle();
	    
	    int relX = craftChunk.getX() << 4;
	    int relZ = craftChunk.getZ() << 4;
	    
	    int old = 0;
	    for ( int h = 0; h < 16; h++ ) {
	        int relY = h << 4;
	        for ( int x = 0; x < 16; x++ ) {
	            for ( int y = 0; y < 16; y++ ) {
	                for ( int z = 0; z < 16; z++ ) {
	                    BlockPosition blockPos = new BlockPosition( relX + x, relY + y, relZ + z );
	                    
	                    IBlockData blockData = chunk.getType( blockPos );
	                    
	                    Block block = blockData.getBlock();
	                    
	                    TileEntity entity = chunk.getTileEntity( blockPos );
	                    
	                    String name = block.getClass().getSimpleName();
	                    if ( name.contains( "BananaBlock" ) && !( block instanceof BananaBlock ) ) {
                            try {
                                Method getNamespacedKeyMethod = block.getClass().getMethod( "getKey" );
                                NamespacedKey key = ( NamespacedKey ) getNamespacedKeyMethod.invoke( block );
                                
                                // Convert the old block to a new block
                                String extraData = blockData.toString();
                                StringReader reader = new StringReader( key + extraData.substring( extraData.indexOf( '[' ) ) );
                                ArgumentBlock argBlock = new ArgumentBlock( reader, false ).a( false );
                                
                                IBlockData newData = argBlock.getBlockData();

                                NBTTagCompound compound = new NBTTagCompound();
                                if ( entity != null ) {
                                    String tileName = entity.getClass().getSimpleName();
                                    if ( tileName.equals( "BananaTileEntity" ) ) {
                                        Method getSavedNBTTagCompoundMethod = entity.getClass().getMethod( "getSavedNBTTagCompound" );
                                        Optional< NBTTagCompound > optionalCompound = ( Optional< NBTTagCompound > ) getSavedNBTTagCompoundMethod.invoke( entity );
                                        if ( optionalCompound.isPresent() ) {
                                            compound = optionalCompound.get();
                                        }
                                    }
                                }

                                chunk.setType( blockPos, newData, false );
                                
                                entity = chunk.getTileEntity( blockPos );
                                
                                if ( entity != null ) {
                                    entity.load( compound );
                                }
                                
                                old++;
                            } catch ( NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | CommandSyntaxException e ) {
                                e.printStackTrace();
                            }
	                    }
	                }
	            }
	        }
	    }
	    return old;
	}
	
	@Override
	public void createDumpDataFile( File dump ) {
	    try {
	        dump.mkdirs();
	        dump.delete();
	        dump.createNewFile();
	        FileWriter writer = new FileWriter( dump );
	        for ( IBlockData data : net.minecraft.server.v1_15_R1.Block.REGISTRY_ID ) {
	            writer.write( net.minecraft.server.v1_15_R1.Block.REGISTRY_ID.getId( data ) + "" );
	            writer.write( "\t:\t" );
	            writer.write( data.toString() );
	            writer.write( '\n' );
	        }
	        writer.close();
	    } catch ( IOException e ) {
	        e.printStackTrace();
	    }
	}
}
