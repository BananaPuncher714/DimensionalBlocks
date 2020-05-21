package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.implementation.v1_15_R1;

import org.bukkit.block.data.BlockData;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlock;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlockData;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DState;

import net.minecraft.server.v1_15_R1.IBlockData;

public class BananaBlockData implements DBlockData {
    private IBlockData data;
    private BananaBlock block;
    
    public BananaBlockData( IBlockData data ) {
        if ( !( data.getBlock() instanceof BananaBlock ) ) {
            throw new IllegalArgumentException( "Block is not a BananaBlock!" );
        }
        this.data = data;
        this.block = ( BananaBlock ) data.getBlock();
    }

    @Override
    public < T extends Comparable< T > > T get( DState< T > state ) {
        return block.get( state, data );
    }

    @Override
    public < T extends Comparable< T > > DBlockData set( DState< T > state, T value ) {
        data = block.set( state, value, data );
        return this;
    }

    @Override
    public DBlock getBlock() {
        return block.getBlock();
    }
    
    @Override
    public void setAsDefault() {
        block.setAsDefault( data );
    }
    
    @Override
    public void setClientBlock( BlockData craftData ) {
        NMSHandler.setRegistryBlockId( data, craftData );
    }
    
    protected IBlockData getData() {
        return data;
    }
}
