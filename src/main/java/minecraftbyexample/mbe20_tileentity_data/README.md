# MBE20_TILEENTITY_DATA

This is an example of how you can use a `TileEntity` to store extra information about a block. 

In this case, it stores a timer value. When the block is placed, the timer starts counting down. When the timer reaches zero, the block randomly changes into something else.

This example will show you:

1. how to create a `Block` class with an associated `TileEntity`
1. how to create a `TileEntity` class and register it
1. how to save and load the state of the `TileEntity` using NBT: `write()` and `read()`
1. an overview of how to use NBT for a variety of different data types--see `write()` and `read()`
1. how to send the server `TileEntity` information to the client using packets:
    1. `getUpdatePacket()` and `onDataPacket()` -- for single `TileEntity` updates
    1. `getUpdateTag()` and `handleUpdateTag()` -- for sending as part of a chunk update packet
1. how to get your `TileEntity` updated every tick: implementing `ITickableTileEntity`

The pieces you need to understand are located in:

* `StartupCommon` and `StartupClient` class
* `BlockTileEntityData` class
* `TileEntityData` class

Various other resources associated with the block rendering (see mbe01 for more details) are located in:

* `resources\assets\minecraftbyexample\lang\en_US.lang`
* `resources\assets\minecraftbyexample\blockstates\`
* `resources\assets\minecraftbyexample\models\block\`
* `resources\assets\minecraftbyexample\models\item\`
* `resources\assets\minecraftbyexample\textures\block\`
* `resources\data\minecraftbyexample\loot_tables\blocks\` 

The block will appear in the Blocks tab in the creative inventory.

Further information:

NBTexplorer is a very useful tool to examine the structure of your NBT saved data and make sure it's correct: [http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-tools/1262665-nbtexplorer-nbt-editor-for-windows-and-mac](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-tools/1262665-nbtexplorer-nbt-editor-for-windows-and-mac)<br>
[Forge info on TileEntities](https://mcforge.readthedocs.io/en/latest/tileentities/tileentity/)


## Common errors

* `TileEntity` doesn't properly restore after loading a saved game--your NBT read/write code is wrong / mismatched. Did you forget the `super.write()` and/or `super.read()`?
* The `TileEntity` on the client doesn't synchronise properly--`getUpdatePacket()`, `getUpdateTag()`, `onDataPacket()`, and/or `handleUpdateTag()` are probably wrong
* "{your tileentity class} is missing a mapping! This is a bug!"--you forgot to `GameRegistry.registerTileEntity()`


