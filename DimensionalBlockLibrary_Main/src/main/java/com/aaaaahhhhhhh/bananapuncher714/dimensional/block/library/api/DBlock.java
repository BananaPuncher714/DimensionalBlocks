package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import java.awt.Color;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.DimensionalBlocks;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.world.CollisionResultBlock;

public abstract class DBlock {
	private final DInfo info;
	
	public DBlock( DInfo info ) {
		this.info = info;
	}
	
	public final NamespacedKey getKey() {
		return info.getKey();
	}
	
	public final DInfo getInfo() {
		return info;
	}
	
	public final BlockData getData() {
		return info.getBlockData();
	}
	
	public final DBlockData getDefaultBlockData() {
		return DimensionalBlocks.getDefaultBlockDataFor( this );
	}
	
	/**
	 * Call the {@link DBlock#tick(DBlockData, Location, Random)} method after a given delay
	 * 
	 * @param location
	 * The location of the block to tick.
	 * @param delay
	 * The delay in ticks.
	 */
	public final void tickLater( Location location, int delay ) {
		DimensionalBlocks.tickLocation( location, this, delay );
	}
	
	/**
	 * Update the physics of the the nearby blocks surrounding the location, including the block.
	 * 
	 * @param location
	 * The location to update around.
	 */
	public final void applyPhysics( Location location ) {
		DimensionalBlocks.applyPhysics( this, location );
	}
	
	/**
	 * Manage all block related stuff here, such as setting the states and their various values.
	 * Gets called after being completely added to the internal registries.
	 */
	public void onRegister() {}
	
	/**
	 * Called when an entity is standing directly on top of the block.
	 * 
	 * @param location
	 * @param entity
	 */
	public void stepOn( Location location, Entity entity ) {}
	
	/**
	 * Called when a projectile, hits the block. Does not apply to snowballs for some reason.
	 * 
	 * @param data
	 * @param projectile
	 * Does not support all projectiles.
	 * @param ray
	 */
	public void onProjectileHit( DBlockData data, Entity projectile, CollisionResultBlock ray ) {}
	
	/**
	 * Called after this block gets placed in the world, either through code or command.
	 * 
	 * @param data
	 * @param location
	 */
	public void onPlace( DBlockData data, Location location ) {}
	
	/**
	 * Called after the block gets broken by the player, or removed somehow.
	 * 
	 * @param data
	 * @param location
	 */
	public void postBreak( DBlockData data, Location location ) {}
	
	/**
	 * Tick event. This is NOT called every tick, but only when called through @{link {@link DBlock#tickLater(Location, int)}.
	 * 
	 * @param data
	 * @param location
	 * @param random
	 */
	public void tick( DBlockData data, Location location, Random random ) {}
	
	/**
	 * Called when a neighboring block has been changed.
	 * 
	 * @param data
	 * @param blockloc
	 * @param neighbor
	 * @param direction
	 * The direction of the neighboring block.
	 */
	public void updateState( DBlockData data, Location blockloc, Location neighbor, BlockFace direction ) {}
	
	/**
	 * Called when physics are applied to this object. May include things like redstone, and regular physics checks.
	 * 
	 * @param data
	 * @param location
	 * @param otherBlock
	 * May not always be directly adjacent to the block.
	 */
	public void doPhysics( DBlockData data, Location location, Location otherBlock ) {}
	
	/**
	 * Called every once in a while when it's raining.
	 * 
	 * @param location
	 * Not sure what this location represents.
	 */
	public void handleRain( Location location ) {}
	
	/**
	 * Whether or not this block will give comparator output.
	 * 
	 * @param data
	 * @return
	 */
	public boolean isComplexRedstone( DBlockData data ) {
		return false;
	}

	/**
	 * Get the level of redstone that will be given to a comparator. @{link {@link DBlock#isComplexRedstone(DBlockData)} must
	 * return true for this to be called. Apply a physics update if the comparator level output changes.
	 * 
	 * @param data
	 * @param location
	 * @return
	 */
	public int getComparatorLevel( DBlockData data, Location location ) {
		return 0;
	}
	
	/**
	 * Whether or not this block will give off redstone power.
	 * 
	 * @param data
	 * @return
	 */
	public boolean isPowerSource( DBlockData data ) {
		return false;
	}

	/**
	 * Get the level of redstone that is emitted in the direction provided. @{link {@link DBlock#isPowerSource(DBlockData)} must
	 * return true for this to be called. Apply a physics update if the redstone level changes.
	 * 
	 * @param data
	 * @param location
	 * @param face
	 * @return
	 */
	public int getPowerSourceLevel( DBlockData data, Location location, BlockFace face ) {
		return 0;
	}
	
	/**
	 * Called when a HumanEntity right clicks this block, with either hand.
	 * 
	 * @param data
	 * @param location
	 * @param entity
	 * @param mainhand
	 * @param ray
	 * @return
	 * PASS signifies that nothing has occurred, and the item being held will receive the right click action.
	 * SUCCESS will cancel any item interaction events that may have occurred instead.
	 */
	public InteractionResult interact( DBlockData data, Location location, HumanEntity entity, boolean mainhand, CollisionResultBlock ray ) {
		return InteractionResult.PASS;
	}
	
	/**
	 * Whether or not the block can be destroyed by the fluid.
	 * 
	 * @param data
	 * @param fluid
	 * Takes on a string name, like "WATER", "FLOWING_WATER", "LAVA", etc.
	 * @return
	 */
	public boolean destroyedByFluid( DBlockData data, String fluid ) {
		return info.isDestroyableByFluid();
	}
	
	/**
	 * Does this block cause suffocation damage if the player's head is inside of it.
	 * 
	 * @param data
	 * @param location
	 * @return
	 */
	public boolean causesSuffocation( DBlockData data, Location location ) {
		return info.isCausesSuffocation();
	}
	
	/**
	 * Get the explosion resistance of this block. Find the vanilla values here: https://minecraft.gamepedia.com/Explosion#Blast_resistance
	 * 
	 * @return
	 */
	public float getExplosionResistance() {
		return info.getExplosionResistance();
	}
	
	/**
	 * Get the color that should show up on the map. It will be matched to the closest available Minecraft color.
	 * 
	 * @param data
	 * @return
	 */
	public Color getMapColor( DBlockData data ) {
		return info.getMapColor();
	}
	
	public PistonReaction getPistonReaction( DBlockData data ) {
		return info.getPistonReaction();
	}
	
	/**
	 * Return a list of all DStates that this block supports.
	 * 
	 * @return
	 */
	public abstract DState< ? >[] getStates();
	
	/**
	 * Set the provided {@link DBlockData} to the location, and apply physics.
	 * 
	 * @param block
	 * @param location
	 */
	public static void update( DBlockData block, Location location ) {
		DimensionalBlocks.setDBlockDataAt( block, location );
	}
	
	/**
	 * Set the provided {@link DBlockData} to the location, and apply physics if specified.
	 * 
	 * @param block
	 * @param location
	 * @param doPhysics
	 * Whether or not to apply physics.
	 */
	public static void update( DBlockData block, Location location, boolean doPhysics ) {
		DimensionalBlocks.setDBlockDataAt( block, location, doPhysics );
	}

	/**
	 * Get the {@link DTileEntity} at the given location, if it exists.
	 * 
	 * @param location
	 * @return
	 * Returns null if not found.
	 */
	public static DTileEntity getTileEntity( Location location ) {
		return DimensionalBlocks.getDTileEntityAt( location );
	}
}
