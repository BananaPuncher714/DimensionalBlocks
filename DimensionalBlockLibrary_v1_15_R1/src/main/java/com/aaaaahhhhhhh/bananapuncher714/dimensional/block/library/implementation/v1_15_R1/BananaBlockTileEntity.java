package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.implementation.v1_15_R1;

import java.util.function.Supplier;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlock;

import net.minecraft.server.v1_15_R1.IBlockAccess;
import net.minecraft.server.v1_15_R1.ITileEntity;
import net.minecraft.server.v1_15_R1.TileEntity;

public class BananaBlockTileEntity extends BananaBlock implements ITileEntity {
    private Supplier< BananaTileEntity > tileEntity;
    
    public BananaBlockTileEntity( DBlock block, Supplier< BananaTileEntity > tileEntity ) {
        super( block );
        
        this.tileEntity = tileEntity;
    }

    @Override
    public TileEntity createTile( IBlockAccess arg0 ) {
        return tileEntity.get();
    }
}
