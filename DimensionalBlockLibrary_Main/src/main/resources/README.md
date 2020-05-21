# Dimensional Block Library
Create custom serverside blocks simplified.

## What is this?
**DBL** helps developers create custom cross version compatible blocks with ease. Custom blocks can be used to store persistent data across server start/stops, have various material states, and interact with the server more naturally.

## Supported versions
- 1.15.2

## Commands
- `dimensionalblocks` - Dumps all currently registered block states into the `/plugins/DimensionalBlocks/dump.txt` file.

## Permissions
- `dimensionalblocklibrary.admin` - Allows execution of the command.

## How to get started
### Creating a custom Dimensional Block
The main class involved is the `DBlock` class. It contains all sorts of useful methods for you to manipulate and play around with to get your block to where you want it.
```java
public class CustomBlock extends DBlock {
    public CustomBlock() {
        super( new DInfo( NamespacedKey.minecraft( "custom_block" ), Material.STONE.createBlockData() ) );
    }
    
    @Override
    public DState< ? >[] getStates() {
        return new DState< ? >[ 0 ];
    }
}
```

You'll need to provide a `DInfo` object to describe your new block, but that's fairly straightforward as well. It comes with setters for you to change various attributes, like the explosion resistance, map color, or even piston reaction. However, some of the properties are inherited from the `BlockData` you provide, so you'll need to use the right Material.

As for the `DState` array, that's for creating custom blockstates, like which direction your block is facing, whether or not your block is waterlogged, and more.

Now that you have a custom class, the next step is to register it internally with the server. It's recommended that you keep your block instance somewhere safe, and easily accessible, since you should only ever need 1.
```java
CustomBlock CUSTOM_BLOCK = new CustomBlock();
DimensionalBlocks.register( CUSTOM_BLOCK );
```
That's all you'll need. Now you can go onto the server and set your block with the `/setblock ~ ~ ~ custom_block` command, or you can set it with the methods that `DimensionalBlocks` provides.
```java
Location someLoc;
DimensionalBlocks.setDBlockAt( CUSTOM_BLOCK, someLoc );
```
Likewise, you can get a custom `DBlockData` at a given location, if it exists.
```java
Location someLoc;
DBlockData blockData = DimensionalBlocks.getDBlockDataAt( someLoc );
```

### DState
A `DState` represents various configurations that your `DBlock` can be in. This is much like the `Rotatable` interface from Bukkit. You can create a custom `DState` for various states that your block can be represented as. Note that this is **not** for storing custom data or a large amount of trivial data. That's what `DTileEntity` is for. To add a `DState` to your block, you'll need to create a state of your own.
```java
public class CustomStateBoolean extends DState< Boolean > {
	public CustomStateBoolean( String id ) {
		super( id, Boolean.class );
	}

	@Override
	public String convertToString( Boolean value ) {
		return Boolean.toString( value );
	}

	@Override
	public Optional< Boolean > getFrom( String value ) {
		if ( value.equalsIgnoreCase( "true" ) || value.equalsIgnoreCase( "false" ) ) {
			return Optional.of( Boolean.valueOf( value.toUpperCase() ) );
		}
		return Optional.empty();
	}

	@Override
	public Collection< Boolean > getValues() {
		return Arrays.asList( true, false );
	}
}
```

### DBlockData
A `DBlockData` object is much like a `BlockState` from Bukkit. It's a representation of some possible configuration of all the states that your `DBlock` can be in. You can change values for various states, set individual blockdata representations, and change the default `DBlockData` of your block when it gets set somewhere. It's best to set up the default block and representations in the `DBlock#register` method. Our custom block will look like this:
```java
public class CustomBlock extends DBlock {
    CustomStateBoolean ROUGH_STONE = new CustomStateBoolean( "rough_stone" );

    public CustomBlock() {
        super( new DInfo( NamespacedKey.minecraft( "custom_block" ), Material.STONE.createBlockData() ) );
    }
    
	@Override
    public void onRegister() {
        DBlockData defData = getDefaultBlockData();
        defData.set( ROUGH_STONE, false );
        defData.setAsDefault();

        defData.set( ROUGH_STONE, true );
        defData.setClientBlock( Material.COBBLESTONE.createBlockData() );
	}
    
    @Override
    public DState< ? >[] getStates() {
        return new DState< ? >[] { ROUGH_STONE };
    }
}
```

### Interacting with the world
First we need a way to fetch a `DBlockData`. This can be done easily with the API.
```java
Location location;
DBlockData blockData = Dimensional.getDBlockDataAt( Location );
```
You can also get it directly from a block.
```java
Block block;
DBlockData blockData = Dimensional.getDBlockDataFrom( block );
```

Alternatively, if we wanted to do something on an event, `DBlock` provides various methods for us to override. For our example, we want our block to toggle states whenever a player right clicks our block.
```java
public class CustomBlock extends DBlock {
    CustomStateBoolean ROUGH_STONE = new CustomStateBoolean( "rough_stone" );

    public CustomBlock() {
        super( new DInfo( NamespacedKey.minecraft( "custom_block" ), Material.STONE.createBlockData() ) );
    }
    
	@Override
    public void onRegister() {
        DBlockData defData = getDefaultBlockData();
        defData.set( ROUGH_STONE, false );
        defData.setAsDefault();

        defData.set( ROUGH_STONE, true );
        defData.setClientBlock( Material.COBBLESTONE.createBlockData() );
	}

    @Override
    public InteractionResult interact( DBlockData data, Location location, HumanEntity entity,
            boolean mainhand, CollisionResultBlock ray ) {
        if ( mainhand ) {
            // Get the current state and reverse it
            boolean isCobble = data.get( ROUGH_STONE );
            data.set( ROUGH_STONE, !isCobble );

            // Update our block at the location
            update( data, location );
            
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }    

    @Override
    public DState< ? >[] getStates() {
        return new DState< ? >[] { ROUGH_STONE };
    }
}
```
It is important to note that you **must** update the `DBlockData` after you modify it, otherwise the changes will not appear in the world!

### Storing custom data
You can store large amounts of custom data with a `DTileEntity` object. These will persist over server stop and start, just like the custom states.
```java
public class TileEntityIntegerCounter extends DTileEntity {
    int counter = 0;

    public int increment() {
        return counter++;
    }

    @Override
	public void load( NBTCompound compound ) {
        counter = NBTEditor.getInt( compound, "current-count" );
    }

    @Override
    public void save( NBTCompound compound ) {
        compound.set( counter, "current-count" );
    }
}
```
You will need to register this alongside with your custom block and a tile entity id, if you want this to be associated with it.
```java
CustomBlock CUSTOM_BLOCK = new CustomBlock();
DimensionalBlocks.register( CUSTOM_BLOCK, TileEntityIntegerCounter::new, "integer_counter" );
```

### Manipulating a DTileEntity
Like the `DBlockData`, you can use methods provided by the API to fetch your `DTileEntity`. We'll make our block keep count of how many times it gets hit by snowballs. 
```java
Location location;
DTileEntity tileEntity = DimensionalBlocks.getDTileEntityAt( location);
```

### Putting it all together
Our final custom block class looks like this:
```java
public class CustomBlock extends DBlock {
    CustomStateBoolean ROUGH_STONE = new CustomStateBoolean( "rough_stone" );

    public CustomBlock() {
        super( new DInfo( NamespacedKey.minecraft( "custom_block" ), Material.STONE.createBlockData() ) );
    }
    
	@Override
    public void onRegister() {
        DBlockData defData = getDefaultBlockData();
        defData.set( ROUGH_STONE, false );
        defData.setAsDefault();

        defData.set( ROUGH_STONE, true );
        defData.setClientBlock( Material.COBBLESTONE.createBlockData() );
	}

    @Override
    public InteractionResult interact( DBlockData data, Location location, HumanEntity entity,
            boolean mainhand, CollisionResultBlock ray ) {
        if ( mainhand ) {
            // Get the current state and reverse it
            boolean isCobble = data.get( ROUGH_STONE );
            data.set( ROUGH_STONE, !isCobble );

            // Update our block at the location
            update( data, location );
            
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }    

    @Override
    public void onProjectileHit( DBlockData data, Entity projectile, CollisionResultBlock ray ) {
        Location blockLocation = ray.getBlock().getLocation();
        DTileEntity tileEntity = DimensionalBlocks.getDTileEntityAt( blockLocation );
        if ( tileEntity instanceof TileEntityIntegerCounter && projectile instanceof Snowball ) {
            TileEntityIntegerCounter counter = ( TileEntityIntegerCounter ) tileEntity;
            counter.increment();
        }
    }

    @Override
    public DState< ? >[] getStates() {
        return new DState< ? >[] { ROUGH_STONE };
    }
}
```
Our plugin's main class looks something like this:
```java
public class CustomBlockPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        CustomBlock CUSTOM_BLOCK = new CustomBlock();
        DimensionalBlocks.register( CUSTOM_BLOCK, TileEntityIntegerCounter::new, "integer_counter" );
    }
}
```
The `plugin.yml` must include `DimensionalBlocks` in its dependencies, and it **must** load at `STARTUP`.