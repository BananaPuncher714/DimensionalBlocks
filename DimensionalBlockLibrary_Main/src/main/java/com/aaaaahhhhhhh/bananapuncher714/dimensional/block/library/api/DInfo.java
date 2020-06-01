package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api;

import java.awt.Color;

import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;

public class DInfo {
	private final BlockData material;
	private final NamespacedKey key;
	private boolean burnable = false;
	private boolean destroyableByFluid = false;
	private boolean causesSuffocation = true;
	private boolean canMobsSpawnOn = true;
	private boolean occluding = true;
	private Color mapColor;
	private PistonReaction pistonReaction = PistonReaction.NORMAL;
	private float explosionResistance;
	
	public DInfo( NamespacedKey key, BlockData material ) {
		this.key = key;
		this.material = material;
		explosionResistance = material.getMaterial().getBlastResistance();
		mapColor = new Color( 0xFF00FF );
	}
	
	public final NamespacedKey getKey() {
		return key;
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
		return explosionResistance;
	}
	
	public final DInfo setExplosionResistance( float explosionStrength ) {
		this.explosionResistance = explosionStrength;
		return this;
	}

	public boolean isDestroyableByFluid() {
		return destroyableByFluid;
	}

	public final DInfo setDestroyableByFluid( boolean destroyableByFluid ) {
		this.destroyableByFluid = destroyableByFluid;
		return this;
	}

	public boolean isCausesSuffocation() {
		return causesSuffocation;
	}

	public final DInfo setCausesSuffocation( boolean causesSuffocation ) {
		this.causesSuffocation = causesSuffocation;
		return this;
	}

	public boolean isCanMobsSpawnOn() {
		return canMobsSpawnOn;
	}

	public DInfo setCanMobsSpawnOn( boolean canMobsSpawnOn ) {
		this.canMobsSpawnOn = canMobsSpawnOn;
		return this;
	}

	public boolean isOccluding() {
		return occluding;
	}

	public DInfo setOccluding( boolean occluding ) {
		this.occluding = occluding;
		return this;
	}
}
