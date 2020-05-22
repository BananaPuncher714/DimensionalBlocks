package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.implementation.v1_15_R1;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DTileEntity;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.util.NBTEditor.NBTCompound;

import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.TileEntity;

public class BananaTileEntity extends TileEntity {
    protected static Map< DTileEntity, WeakReference< BananaTileEntity > > TILE_ENTITY_MAP = new HashMap< DTileEntity, WeakReference< BananaTileEntity > >();
    
    private DTileEntity tileEntity;
    
    public BananaTileEntity( String id, DTileEntity tileEntity ) {
        super( IRegistry.BLOCK_ENTITY_TYPE.get( new MinecraftKey( id ) ) );
        
        this.tileEntity = tileEntity;
        
        TILE_ENTITY_MAP.put( tileEntity, new WeakReference< BananaTileEntity >( this ) );
    }
    
    public DTileEntity getTileEntity() {
        return tileEntity;
    }

    @Override
    public void load( NBTTagCompound nbttagcompound ) {
        super.load( nbttagcompound );
        tileEntity.load( new NBTCompound( nbttagcompound ) );
    }

    @Override
    public NBTTagCompound save( NBTTagCompound nbttagcompound ) {
        super.save( nbttagcompound );
        tileEntity.save( new NBTCompound( nbttagcompound ) );
        return nbttagcompound;
    }
}
