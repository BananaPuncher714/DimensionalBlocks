package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.world;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class CollisionResultBlock extends CollisionResult {
	protected Block block;
	
	public CollisionResultBlock( Location location, BlockFace direction, Block block ) {
		super( location, direction, CollisionType.BLOCK );
		this.block = block;
	}

	/**
	 * Get the block that was involved in the collision.
	 * 
	 * @return
	 * A solid block that was hit, if not a miss.
	 */
	public Block getBlock() {
		return block;
	}
	
	public CollisionResultBlock copyOf() {
		return new CollisionResultBlock( location.clone(), direction, block );
	}
}
