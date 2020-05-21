package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import java.awt.Color;

import org.bukkit.block.data.BlockData;

public class DInfo {
	private BlockData material;
	private boolean burnable = false;
	private Color mapColor;
	private PistonReaction pistonReaction = PistonReaction.NORMAL;
	private float explosionStrength;
	
	public DInfo( BlockData material ) {
		this.material = material;
		explosionStrength = material.getMaterial().getBlastResistance();
		mapColor = new Color( 0xFF00FF );
	}
	
	public final BlockData getBlockData() {
		return material;
	}
	
	public final boolean isBurnable() {
		return burnable;
	}
	
	public final DInfo setBurnable( boolean burnable ) {
		this.burnable = burnable;
		return this;
	}
	
	public final Color getMapColor() {
		return mapColor;
	}
	
	public final DInfo setMapColor( Color mapColor ) {
		this.mapColor = mapColor;
		return this;
	}
	
	public final PistonReaction getPistonReaction() {
		return pistonReaction;
	}
	
	public final DInfo setPistonReaction( PistonReaction pistonReaction ) {
		this.pistonReaction = pistonReaction;
		return this;
	}
	
	public final float getExplosionResistance() {
		return explosionStrength;
	}
	
	public final DInfo setExplosionStrength( float explosionStrength ) {
		this.explosionStrength = explosionStrength;
		return this;
	}
}
