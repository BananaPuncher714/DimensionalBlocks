package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.DimensionalBlocks;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.world.CollisionResultBlock;

public abstract class DBlock {
	private final NamespacedKey key;
	
	public DBlock( NamespacedKey key ) {
		this.key = key;
	}
	
	public final NamespacedKey getKey() {
		return key;
	}
	
	public final DBlockData getDefaultBlockData() {
		return DimensionalBlocks.getDefaultBlockDataFor( this );
	}
	
	public void onRegister() {}
	public void stepOn( Location location, Entity entity ) {}
	public InteractionResult interact( DBlockData data, Location location, HumanEntity entity, boolean mainhand, CollisionResultBlock ray ) { return InteractionResult.PASS; }
	public void onProjectileHit( DBlockData data, Entity projectile, CollisionResultBlock ray ) {}
	public void onPlace( DBlockData data, Location location ) {}
	public void postBreak( DBlockData data, Location location ) {}
	public void tick( DBlockData data, Location location, Random random ) {}
	public void updateState( DBlockData data, Location blockloc, Location neighbor, BlockFace direction ) {}
	public abstract DInfo getBlockInfo();
	public abstract DState< ? >[] getStates();
	
	public static void update( DBlockData block, Location location ) {
		DimensionalBlocks.setDBlockDataAt( block, location );
	}
	
	public static DTileEntity getTileEntity( Location location ) {
		return DimensionalBlocks.getDTileEntityAt( location );
	}
}
