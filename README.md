MinecraftByExample [1.16.4]
==================
The purpose of MinecraftByExample is to give simple working examples of the important concepts in Minecraft and Forge. If you're anything like me, a good code example is worth several screens of waffling explanation, and can very quickly explain the key concepts.  I also find it much easier to adapt and debug something that already works, than to have to synthesize something from scratch and spend hours trying to discover the missing bit of information I didn't know about.

I've tried to keep the code simple and obvious and to resist the urge to be clever. The examples might not be the most efficient or succinct implementation, I've deliberately left the optimization to you.

Each example is split up to be totally independent of all the others.  The only part of the code which is common to more than one example is the MinecraftByExample class.

If you want more information and explanatory text about the concepts, the following links might be useful:

  - [The Official Forge documentation][forgedocs] (parts of it are rather outdated but on the whole still very useful starting reference)
  - [Guide to how Minecraft works][greyminecraftcoder] (explaining the key concepts for understanding vanilla code)
  - [Fabric Wiki][fabricwiki] (some of it is specific to the Fabric API, but a lot of useful general info too) 
  - [Forge modder support forum][Forge forum] ask for advice from the experts
  - [McJty tutorials][McJty] lots of step-by-step instructions / tutorial
  - [Mr Crayfish Furniture Mod (lots of examples)](https://github.com/MrCrayfish/MrCrayfishFurnitureMod)
  - [Cadiboo's example mod](https://github.com/Cadiboo/Example-Mod) some great tutorials for when you're starting out
  
#### For earlier versions, see the relevant GitHub branch:
 - MBE for Forge 1.8: [1-8final][version1-8]
 - MBE for Forge 1.8.9: [1-8-9final][version1-8-9]
 - MBE for Forge 1.10.2: [1-10-2final][version1-10-2]
 - MBE for Forge 1.11: [1-11final][version1-11]
 - MBE for Forge 1.11.2: [1-11-2final][version1-11-2]
 - MBE for Forge 1.12.2: [1-12-2final][version1-12-2]
 - MBE for Forge 1.14.4: [1-14-4partial][version1-14-4] (partially updated only)
 - MBE for Forge 1.15.2: [1-15-2][version1-15-2] 
 - MBE for Forge 1.16.3: [1-16-3][version1-16-3]

If you are updating from previous forge versions, you will probably find [this link][versionupdates] and [this link][versionupdates1-15] very helpful.  For better or for worse, MCP decided to rename a very large number of classes (eg all Blocks Blockxxx --> xxxxBlock, etc) so this might save you a stack of time.
If you use IntelliJ, you might find these [xml mapping files][mapfiles] useful too

## List of examples 
See [here](https://greyminecraftcoder.blogspot.com/2020/05/minecraft-by-example.html) for pictures of what each example looks like in-game.

### Blocks
  - [MBE01][01] - a simple cube
  - [MBE02][02] - a block with a more complicated shape
  - [MBE03][03] - two types of blocks which vary their appearance / shape:<br>
        a block (coloured signpost) with multiple variants- four colours, can be placed facing in four directions<br>
        a block (3D Web) with multiple parts (multipart) similar to a vanilla fence.
  - [MBE04][04] - dynamically created block models<br>
        a camouflage ("secret door") block which dynamically changes its appearance to match adjacent blocks - uses IBlockModel.getQuads(), ModelBakeEvent, IForgeBakedModel and IModelData<br>
        an "altimeter" block which shows the block altitude (y coordinate) on the side in digital display - as camouflage block but uses programmatic generation of quads  
  - [MBE05][05] - multilayer block (lantern block with transparent glass) with animated flame texture
  - [MBE06][06] - several different types of block which use redstone
  - [MBE08][08] - how to add a creative tab for organising your custom blocks / items

### Items
  - [MBE10][10] - a simple item
  - [MBE11][11] - an item with multiple variants - rendered using multiple models and multiple layers
  - [MBE12][12] - an item that stores extra information in NBT, also illustrates the "in use" animation similar to drawing a bow
  - [MBE15][15] - a chessboard item with 1 - 64 pieces; uses ItemOverrideList.getModelWithOverrides(), IBlockModel.getQuads() and onModelBakeEvent()

### TileEntities
  - [MBE20][20] - using a tile entity to store information about a block - also shows examples of using NBT storage
  - [MBE21][21] - using the TileEntityRenderer to render unusual shapes or animations

### Containers (Inventories)
  - [MBE30][30] - a simple container for storing items in the world - similar to a Chest
  - [MBE31][31] - a functional container such as a Furnace or Crafting Table
  - [MBE32][32] - an item (bag of flowers) which can store other items inside it.  Also shows how to use Capability

### Recipes (Crafting/Furnace)
  - [MBE35][35] - some typical example crafting recipes and furnace (smelting) recipes

### Commands
  - [MBE45][45] - custom commands

### Particles - particle effects
  - [MBE50][50] - shows how to use vanilla Particles; also how to generate your own custom Particles

### Network
  - [MBE60][60] - send network messages between client and server

### Capabilities
  - [MBE65][65] - define new Capabilities and attach them to vanilla objects 

### Testing tools
  - [MBE75][75] - a tool to help you automate testing of your classes in-game.

### Entities and Models
  - [MBE80][80] - Shows the basics of Models (eg PigModel), model parameters adjustable in real time using commands
  - [MBE81][81] - Projectile Entities (eg snowballs, arrows)
  
### Miscellaneous Debugging Tools
  - [DebuggingTools]- This package is a bunch of functions and tools that I use occasionally, mostly for debugging
  
## Usage
  - You can browse directly in GitHub, or alternatively, download it as a zip and browse it locally.

  - If you want to install it and compile it, the basic steps for beginners are:
    1. Download the project as a zip.
    2. Unzip it to an appropriate folder on your computer, such as My Documents.  (Or, if you know how to fork a project on GitHub and import it into a local git repository, you can do that instead)
    3. Look at Forge's README.txt file in this folder and follow the instructions to import it into Eclipse or IntelliJ IDEA.
    4. Use the gradle task runClient to run or debug the project.

### How to compile and run:
1) Execute gradle task runClient to test the client installation
or
2) Execute gradle task runServer to test the dedicated server installation.  (The first time you run this task it will exit without starting the server.  You then need to edit the eula.txt file in the run directory, and execute runServer again.)

    
#### If You're Still Confused
Head over [here][more_help] if this didn't make sense to you (check comments for differences with latest versions of IDEA).

[main_classes]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample
[greyminecraftcoder]: https://greyminecraftcoder.blogspot.com/p/list-of-topics-1144.html
[forgedocs]:https://mcforge.readthedocs.org/en/latest/
[more_help]:https://suppergerrie2.com/minecraft-1-14-modding-with-forge-1-setting-up-a-dev-environment/

[01]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe01_block_simple
[02]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe02_block_partial
[03]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe03_block_variants
[04]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe04_block_dynamic_block_models
[05]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe05_block_advanced_models
[06]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe06_redstone
[08]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe08_itemgroup

[10]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe10_item_simple
[11]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe11_item_variants
[12]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe12_item_nbt_animate
[13]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe14_item_camera_transforms
[14]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe14_item_camera_transforms
[15]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe15_item_dynamic_item_model

[20]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe20_tileentity_data
[21]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe21_tileentityrenderer

[30]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe30_inventory_basic
[31]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe31_inventory_furnace
[32]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe32_inventory_item

[35]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe35_recipes

[40]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe40_hud_overlay

[45]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe45_commands

[50]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe50_particle

[60]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe60_network_messages

[65]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe65_capability

[75]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe75_testing_framework

[80]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe80_model_renderer
[81]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe81_entity_projectile

[DebuggingTools]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/usefultools
 
[Forge forum]: https://www.minecraftforge.net/forum/forum/70-modder-support/
[fabricwiki]: https://fabricmc.net/wiki/start
[McJty]: https://wiki.mcjty.eu/modding/index.php?title=YouTube-1.14

[gradle_tool_window]: https://www.jetbrains.com/idea/help/gradle-tool-window.html

[version1-8]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-8final
[version1-8-9]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-8-9final
[version1-10-2]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-10-2final
[version1-11]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-11-final
[version1-11-2]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-11-2-final
[version1-12-2]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-12-2-final
[version1-14-4]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-14-4-partial
[version1-15-2]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-15-2-final
[version1-16-3]: https://github.com/TheGreyGhost/MinecraftByExample/tree/1-16-3-final
[versionupdates]: https://gist.github.com/williewillus/353c872bcf1a6ace9921189f6100d09a
[versionupdates1-15]:https://gist.github.com/williewillus/30d7e3f775fe93c503bddf054ef3f93e
[mapfiles]: https://github.com/TheGreyGhost/MinecraftByExample/tree/master/miscellaneous/name_remappings

With thanks to these helpful folks:
alvaropp, 
yooksi,
Brandon3035,
twrightsman (greekphysique),
Nephroid,
Herbix, and
Shadowfacts

## Licence Info:
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org/>
