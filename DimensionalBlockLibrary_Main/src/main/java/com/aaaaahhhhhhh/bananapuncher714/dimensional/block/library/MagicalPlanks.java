package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlock;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DInfo;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DState;

public class MagicalPlanks extends DBlock {

	public MagicalPlanks() {
		super( NamespacedKey.minecraft( "magical_wood" ) );
	}

	@Override
	public void stepOn( Location location, Entity entity ) {
		entity.sendMessage( "Hello!" );
	}
	
	@Override
	public DInfo getBlockInfo() {
		return new DInfo( Material.BOOKSHELF.createBlockData() );
	}

	@Override
	public DState< ? >[] getStates() {
		return new DState< ? >[] {};
	}

}
