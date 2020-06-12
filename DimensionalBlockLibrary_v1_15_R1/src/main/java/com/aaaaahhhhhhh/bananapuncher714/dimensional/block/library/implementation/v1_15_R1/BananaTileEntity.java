package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.implementation.v1_15_R1;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Material;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DBlockData;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DTileEntity;
import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.util.NBTEditor;

import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.IBlockData;
import net.minecraft.server.v1_15_R1.IRegistry;
import net.minecraft.server.v1_15_R1.ITickable;
import net.minecraft.server.v1_15_R1.MinecraftKey;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.PacketPlayOutTileEntityData;
import net.minecraft.server.v1_15_R1.TileEntity;

public class BananaTileEntity extends TileEntity implements ITickable {
    protected static Map< DTileEntity, WeakReference< BananaTileEntity > > TILE_ENTITY_MAP = new HashMap< DTileEntity, WeakReference< BananaTileEntity > >();
    
    private DTileEntity tileEntity;
    
    private NBTTagCompound saveBeforeTheCrash;
    
    public BananaTileEntity( String id, DTileEntity tileEntity ) {
        super( IRegistry.BLOCK_ENTITY_TYPE.get( new MinecraftKey( id ) ) );
        
        this.tileEntity = tileEntity;
        
        TILE_ENTITY_MAP.put( tileEntity, new WeakReference< BananaTileEntity >( this ) );
    }
    
    public DTileEntity getTileEntity() {
        return tileEntity;
    }
    
    public void saveTheCurrentCompoundBecauseSomeNoobDecidedToReloadTheServer() {
        saveBeforeTheCrash = save( new NBTTagCompound() );
    }
    
    public Optional< NBTTagCompound > getSavedNBTTagCompound() {
        return Optional.ofNullable( saveBeforeTheCrash );
    }
    
    @Override
    public void load( NBTTagCompound nbttagcompound ) {
        super.load( nbttagcompound );
        tileEntity.load( NBTEditor.getNBTCompound( nbttagcompound ) );
    }

    @Override
    public NBTTagCompound save( NBTTagCompound nbttagcompound ) {
        super.save( nbttagcompound );
        tileEntity.save( NBTEditor.getNBTCompound( nbttagcompound ) );
        return nbttagcompound;
    }

    @Override
    public PacketPlayOutTileEntityData getUpdatePacket() {
        NBTTagCompound compound = save( new NBTTagCompound() );

        IBlockData data = getBlock();
        BlockPosition blockpos = getPosition();
        DBlockData bData = new BananaBlockData( data );
        Material mat = bData.getBlockData().getMaterial();

        int val = getUpdatePacketAction( mat );

        return new PacketPlayOutTileEntityData( blockpos, val, compound );
    }
    
    @Override
    public NBTTagCompound b() {
        return save( new NBTTagCompound() );
    }
    
    @Override
    public void tick() {
        tileEntity.tick();
    }
    
    protected static int getUpdatePacketAction( Material material ) {
        switch ( material ) {
        case SPAWNER:
            return 1;
        case COMMAND_BLOCK:
        case CHAIN_COMMAND_BLOCK:
        case REPEATING_COMMAND_BLOCK:
            return 2;
        case BEACON:
            return 3;
        case PLAYER_HEAD:
        case PLAYER_WALL_HEAD:
            return 4;
        case CONDUIT:
            return 5;
        case WHITE_BANNER:
        case ORANGE_BANNER:
        case MAGENTA_BANNER:
        case LIGHT_BLUE_BANNER:
        case YELLOW_BANNER:
        case LIME_BANNER:
        case PINK_BANNER:
        case GRAY_BANNER:
        case LIGHT_GRAY_BANNER:
        case CYAN_BANNER:
        case PURPLE_BANNER:
        case BLUE_BANNER:
        case BROWN_BANNER:
        case GREEN_BANNER:
        case RED_BANNER:
        case BLACK_BANNER:
            return 6;
        case STRUCTURE_BLOCK:
            return 7;
        case END_GATEWAY:
            return 8;
        case OAK_SIGN:
        case OAK_WALL_SIGN:
        case SPRUCE_SIGN:
        case SPRUCE_WALL_SIGN:
        case BIRCH_SIGN:
        case BIRCH_WALL_SIGN:
        case JUNGLE_SIGN:
        case JUNGLE_WALL_SIGN:
        case ACACIA_SIGN:
        case ACACIA_WALL_SIGN:
        case DARK_OAK_SIGN:
        case DARK_OAK_WALL_SIGN:
            return 9;
        case WHITE_BED:
        case ORANGE_BED:
        case MAGENTA_BED:
        case LIGHT_BLUE_BED:
        case YELLOW_BED:
        case LIME_BED:
        case PINK_BED:
        case GRAY_BED:
        case LIGHT_GRAY_BED:
        case CYAN_BED:
        case PURPLE_BED:
        case BLUE_BED:
        case BROWN_BED:
        case GREEN_BED:
        case RED_BED:
        case BLACK_BED:
            return 11;
        case JIGSAW:
            return 12;
        case CAMPFIRE:
            return 13;
        case BEEHIVE:
        case BEE_NEST:
            return 14;
        default: return 10;
        }
    }
}
