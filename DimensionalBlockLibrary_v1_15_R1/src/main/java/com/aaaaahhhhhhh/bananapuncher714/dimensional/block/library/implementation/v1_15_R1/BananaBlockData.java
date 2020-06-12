package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.implementation.v1_15_R1;

import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;
import org.bukkit.util.BoundingBox;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlock;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlockData;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DState;

import net.minecraft.server.v1_15_R1.IBlockData;

public class BananaBlockData implements DBlockData {
    private IBlockData data;
    private BananaBlock block;
    private boolean locked = false;
    
    public BananaBlockData( IBlockData data ) {
        if ( !( data.getBlock() instanceof BananaBlock ) ) {
            throw new IllegalArgumentException( "Block is not a BananaBlock!" );
        }
        this.data = data;
        this.block = ( BananaBlock ) data.getBlock();
    }
    
    @Override
    public BoundingBox[] getCollisionShape() {
        return NMSHandler.convertFrom( data.getCollisionShape( null, null ) );
    }
    
    @Override
    public BoundingBox[] getHitbox() {
        return NMSHandler.convertFrom( data.getShape( null, null ) );
    }
    
    @Override
    public < T extends Comparable< T > > T get( DState< T > state ) {
        return block.get( state, data );
    }

    @Override
    public < T extends Comparable< T > > DBlockData set( DState< T > state, T value ) {
        if ( locked ) {
            throw new IllegalArgumentException( "Unable to modify locked data!" );
        }
        data = block.set( state, value, data );
        return this;
    }
    
    @Override
    public < T extends Comparable< T > > T increment( DState< T > state ) {
        if ( locked ) {
            throw new IllegalArgumentException( "Unable to modify locked data!" );
        }
        data = block.increment( state, data );
        return block.get( state, data );
    }
    
    @Override
    public DBlock getBlock() {
        return block.getBlock();
    }

    @Override
    public BlockData getBlockData() {
        IBlockData representation = NMSHandler.getFor( data );
        return CraftBlockData.fromData( representation );
    }
    
    @Override
    public void setAsDefault() {
        if ( locked ) {
            throw new IllegalArgumentException( "Unable to modify locked data!" );
        }
        block.setAsDefault( data );
    }
    
    protected IBlockData getData() {
        return data;
    }
    
    protected BananaBlockData lock() {
        locked = true;
        return this;
    }
}
