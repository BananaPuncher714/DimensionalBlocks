package com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.implementation.v1_15_R1;

import java.util.Collection;
import java.util.Optional;

import com.aaaaahhhhhhh.bananapuncher714.dimensional.block.library.api.DState;

import net.minecraft.server.v1_15_R1.BlockState;

public class BananaState< T extends Comparable< T > > extends BlockState< T > {
    private DState< T > state;
    
    protected BananaState( DState< T > state ) {
        super( state.getId(), state.getType() );
        this.state = state;
    }
    
    public DState< T > getState() {
        return state;
    }

    @Override
    public String a( T value ) {
        return state.convertToString( value );
    }

    @Override
    public Optional< T > b( String value ) {
        return state.getFrom( value );
    }

    @Override
    public Collection< T > getValues() {
        return state.getValues();
    }
}
