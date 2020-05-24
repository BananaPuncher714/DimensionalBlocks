package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library;

import java.io.File;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlock;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlockData;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DTileEntity;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.NMSHandler;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.util.ReflectionUtil;

/**
 * Contains various methods for getting/setting custom block data.
 */
public class DimensionalBlocks extends JavaPlugin {
	private static NMSHandler handler;
	
	@Override
	public void onEnable() {
		handler = ReflectionUtil.getNewNMSHandlerInstance();
		
		if ( !Bukkit.getWorlds().isEmpty() ) {
			Bukkit.getScheduler().runTask( this, this::cleanAllTheOldBlocksAwayBecauseSomeNoobDecidedItWasAGoodIdeaToReloadTheServer );
		}
	}
	
	@Override
	public void onDisable() {
		handler.abandonShip();
	}
	
	@Override
	public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
		if ( sender.hasPermission( "dimensionalblocklibrary.admin" ) ) {
			File dump = new File( getDataFolder() + "/" + "dump.txt" );
			createBlockStateDumpFile( dump );
			sender.sendMessage( ChatColor.BLUE + "Dumped block states to file!" );
		} else {
			sender.sendMessage( ChatColor.RED + "You do not have permission to run this command!" );
		}
		return true;
	}
	
	private void cleanAllTheOldBlocksAwayBecauseSomeNoobDecidedItWasAGoodIdeaToReloadTheServer() {
		getLogger().severe( "Looks like someone decided it was a good idea to reload the server..." );
		getLogger().severe( "Cleaning the worlds..." );
		for ( World world : Bukkit.getWorlds() ) {
			getLogger().severe( "Cleaning world '" + world.getName() + "'" );
			for ( Chunk chunk : world.getLoadedChunks() ) {
				int amount = handler.cleanChunk( chunk );
				if ( amount > 0 ) {
					getLogger().severe( "Detected " + amount + " old block(s) in chunk (" + chunk.getX() + ", " + chunk.getZ() + ")" );
				}
			}
		}
		getLogger().severe( "Cleaning complete!" );
	}
	
	/**
	 * Register a custom {@link DBlock}. This should only be called once for each block.
	 * 
	 * @param block
	 * A custom {@link DBlock}.
	 */
	public static void register( DBlock block ) {
		handler.register( block );
	}
	
	/**
	 * Register a custom {@link DBlock} and associate it with a given {@link DTileEntity}. This
	 * can be called with the same {@link DTileEntity} and id more than once, if you want
	 * to assign it to mulitple blocks.
	 * 
	 * @param block
	 * The DBlock to register and associate with.
	 * @param tileEntity
	 * A supplier that provides a new instance of the DTileEntity when required
	 * @param id
	 * A unique id that will be used to identify the DTileEntity.
	 */
	public static void register( DBlock block, Supplier< DTileEntity > tileEntity, String id ) {
		handler.register( block, tileEntity, id );
	}
	
	/**
	 * Get the {@link DBlockData} of a {@link DBlock} at the specified location.
	 * 
	 * @param location
	 * The location to fetch.
	 * @return
	 * A {@link DBlockData} instance, or null if it isn't a custom {@link DBlock}.
	 */
	public static DBlockData getDBlockDataAt( Location location ) {
		return handler.getDBlockDataAt( location );
	}
	
	/**
	 * Get the {@link DBlockData} of a {@link DBlock} from a given block.
	 * 
	 * @param block
	 * The Bukkit block to convert.
	 * @return
	 * A {@link DBlockData} instance, or null if the block isn't a {@link DBlock}.
	 */
	public static DBlockData getDBlockDataFrom( Block block ) {
		return handler.getDBlockDataFrom( block );
	}
	
	/**
	 * Set a {@link DBlock} with the default {@link DBlockData} at the given location and update with physics.
	 * 
	 * @param block
	 * The {@link DBlock} must be registered.
	 * @param location
	 * The location to set.
	 * @return
	 * The {@link DBlockData} of the {@link DBlock} created at the new location.
	 */
	public static DBlockData setDBlockAt( DBlock block, Location location ) {
		return handler.setDBlockAt( block, location);
	}
	
	/**
	 * Set a {@link DBlock} with the default {@link DBlockData} at the given location.
	 * 
	 * @param block
	 * The {@link DBlock} must be registered.
	 * @param location
	 * The location to set.
	 * @param doPhysics
	 * Whether or not nearby blocks should be updated.
	 * @return
	 * The {@link DBlockData} of the {@link DBlock} created at the new location.
	 */
	public static DBlockData setDBlockAt( DBlock block, Location location, boolean doPhysics ) {
		return handler.setDBlockAt( block, location, doPhysics );
	}
	
	/**
	 * Set a {@link DBlockData} to a current location and update with physics.
	 * 
	 * @param data
	 * The {@link DBlock} of the data must be registered.
	 * @param location
	 * The location to set.
	 */
	public static void setDBlockDataAt( DBlockData data, Location location ) {
		handler.setDBlockDataAt( data, location );
	}
	
	/**
	 * Set the block at a current location to the provided {@link DBlockData}.
	 * 
	 * @param data
	 * The {@link DBlock} of the {@link DBlockData} must be registered.
	 * @param location
	 * The location to set.
	 * @param doPhysics
	 * Whether or not nearby blocks should be updated.
	 */
	public static void setDBlockDataAt( DBlockData data, Location location, boolean doPhysics ) {
		handler.setDBlockDataAt( data, location, doPhysics );
	}
	
	/**
	 * Get a {@link DBlockData} representing the default state of the given {@link DBlock}.
	 * 
	 * @param block
	 * The {@link DBlock} to fetch the default state for. It must be registered.
	 * @return
	 * The default state of the block.
	 */
	public static DBlockData getDefaultBlockDataFor( DBlock block ) {
		return handler.getDefaultBlockDataFor( block );
	}
	
	/**
	 * Fetch a {@link DTileEntity} from a given location, if it exists.
	 * 
	 * @param location
	 * The location to fetch a {@link DTileEntity}
	 * @return
	 * A {@link DTileEntity} if it exists, or null.
	 */
	public static DTileEntity getDTileEntityAt( Location location ) {
		return handler.getDTileEntityAt( location );
	}
	
	/**
	 * Get the location associated with the given tile entity.
	 * 
	 * @param entity
	 * @return
	 */
	public static Location getLocationOf( DTileEntity entity ) {
		return handler.getLocationOf( entity );
	}
	
	/**
	 * Get the {@link DBlockData} associated with the given tile entity.
	 * 
	 * @param entity
	 * @return
	 */
	public static DBlockData getDBlockDataOf( DTileEntity entity ) {
		return handler.getDBlockDataOf( entity );
	}

	/**
	 * Call the {@link DBlock#tick(DBlockData, Location, java.util.Random)} method after a certain amount of ticks.
	 * 
	 * @param location
	 * The location to tick.
	 * @param block
	 * The {@link DBlock} that requires ticking. It must be registered.
	 * @param delay
	 * Delay in amount of ticks.
	 */
	public static void tickLocation( Location location, DBlock block, int delay ) {
		handler.tick( location, block, delay );
	}
	
	/**
	 * Updates the physics for the given block at the given location, if it exists. This will update the redstone level and such.
	 * 
	 * @param block
	 * The {@link DBlock} to update. It must be registered.
	 * @param location
	 * The location to update
	 */
	public static void applyPhysics( DBlock block, Location location ) {
		handler.applyPhysics( block, location );
	}
	
	/**
	 * Create a dump file with all the current blockstates and their assigned ids.
	 * 
	 * @param dump
	 * The file to dump in. It will be cleared and overwritten if it already exists.
	 */
	public static void createBlockStateDumpFile( File dump ) {
		handler.createDumpDataFile( dump );
	}
}
