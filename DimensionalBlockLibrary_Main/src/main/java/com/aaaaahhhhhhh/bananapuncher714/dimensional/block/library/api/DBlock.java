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
	
	public final void tickLater( Location location ) {
		DimensionalBlocks.tickLocation( location, this );
	}
	
	public final void tickLater( Location location, int delay ) {
		DimensionalBlocks.tickLocation( location, this, delay );
	}
	
	public final void applyPhysics( Location location ) {
		DimensionalBlocks.applyPhysics( this, location );
	}
	
	public void onRegister() {}
	public void stepOn( Location location, Entity entity ) {}
	public void onProjectileHit( DBlockData data, Entity projectile, CollisionResultBlock ray ) {}
	public void onPlace( DBlockData data, Location location ) {}
	public void postBreak( DBlockData data, Location location ) {}
	public void tick( DBlockData data, Location location, Random random ) {}
	public void updateState( DBlockData data, Location blockloc, Location neighbor, BlockFace direction ) {}
	public void doPhysics( DBlockData data, Location location, Location otherBlock ) {}
	public void handleRain( Location location ) {}
	
	public boolean isComplexRedstone( DBlockData data ) {
		return false;
	}

	public int getComparatorLevel( DBlockData data, Location location ) {
		return 0;
	}
	
	public boolean isPowerSource( DBlockData data ) {
		return false;
	}

	public int getPowerSourceLevel( DBlockData data, Location location, BlockFace face ) {
		return 0;
	}
	
	public InteractionResult interact( DBlockData data, Location location, HumanEntity entity, boolean mainhand, CollisionResultBlock ray ) {
		return InteractionResult.PASS;
	}
	
	public boolean destroyedByFluid( DBlockData data, String fluid ) {
		return info.isDestroyableByFluid();
	}
	
	public boolean causesSuffocation( DBlockData data, Location location ) {
		return info.isCausesSuffocation();
	}
	
	public float getExplosionResistance() {
		return info.getExplosionResistance();
	}
	
	public Color getMapColor( DBlockData data ) {
		return info.getMapColor();
	}
	
	public PistonReaction getPistonReaction( DBlockData data ) {
		return info.getPistonReaction();
	}
	
	public abstract DState< ? >[] getStates();
	
	public static void update( DBlockData block, Location location ) {
		DimensionalBlocks.setDBlockDataAt( block, location );
	}
	
	public static void update( DBlockData block, Location location, boolean doPhysics ) {
		DimensionalBlocks.setDBlockDataAt( block, location, doPhysics );
	}

	public static DTileEntity getTileEntity( Location location ) {
		return DimensionalBlocks.getDTileEntityAt( location );
	}
}
