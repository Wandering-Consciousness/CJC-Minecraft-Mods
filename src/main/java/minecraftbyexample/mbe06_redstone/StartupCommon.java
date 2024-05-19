package minecraftbyexample.mbe06_redstone;

import minecraftbyexample.mbe06_redstone.input.BlockRedstoneColouredLamp;
import minecraftbyexample.mbe06_redstone.input_and_output.BlockRedstoneMeter;
import minecraftbyexample.mbe06_redstone.input_and_output.TileEntityRedstoneMeter;
import minecraftbyexample.mbe06_redstone.output_only.BlockRedstoneTarget;
import minecraftbyexample.mbe06_redstone.output_only.BlockRedstoneVariableSource;
import minecraftbyexample.mbe20_tileentity_data.TileEntityData;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * These methods are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupCommon
{
  public static BlockRedstoneColouredLamp blockRedstoneColouredLamp;
  public static BlockRedstoneTarget blockRedstoneTarget;
  public static BlockRedstoneMeter blockRedstoneMeter;
  public static BlockRedstoneVariableSource blockRedstoneVariableSource;

  public static BlockItem itemBlockRedstoneColouredLamp;
  public static BlockItem itemBlockRedstoneTarget;
  public static BlockItem itemBlockRedstoneMeter;
  public static BlockItem itemBlockRedstoneVariableSource;

  public static TileEntityType<TileEntityRedstoneMeter> tileEntityDataTypeMBE06;  // Holds the type of our tile entity; needed for the TileEntity constructor

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
    blockRedstoneVariableSource = (BlockRedstoneVariableSource) (new BlockRedstoneVariableSource()
            .setRegistryName("minecraftbyexample", "mbe06_block_redstone_variable_source_registry_name"));
    blockRegisterEvent.getRegistry().register(blockRedstoneVariableSource);

    blockRedstoneTarget = (BlockRedstoneTarget) (new BlockRedstoneTarget()
            .setRegistryName("minecraftbyexample", "mbe06b_block_redstone_target_registry_name"));
    blockRegisterEvent.getRegistry().register(blockRedstoneTarget);

    blockRedstoneMeter = (BlockRedstoneMeter) (new BlockRedstoneMeter()
            .setRegistryName("minecraftbyexample", "mbe06c_block_redstone_meter_registry_name"));
    blockRegisterEvent.getRegistry().register(blockRedstoneMeter);

    blockRedstoneColouredLamp = (BlockRedstoneColouredLamp) (new BlockRedstoneColouredLamp()
            .setRegistryName("minecraftbyexample", "mbe06d_block_redstone_coloured_lamp_registry_name"));
    blockRegisterEvent.getRegistry().register(blockRedstoneColouredLamp);

  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    // We need to create a BlockItem so the player can carry this block in their hand and it can appear in the inventory
    final int MAXIMUM_STACK_SIZE = 4;  // player can only hold 4 of this block in their hand at once

    Item.Properties itemProperties = new Item.Properties()
            .maxStackSize(MAXIMUM_STACK_SIZE)
            .group(ItemGroup.REDSTONE);  // which inventory tab?
    itemBlockRedstoneVariableSource = new BlockItem(blockRedstoneVariableSource, itemProperties);
    itemBlockRedstoneVariableSource.setRegistryName(blockRedstoneVariableSource.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockRedstoneVariableSource);

    itemBlockRedstoneTarget = new BlockItem(blockRedstoneTarget, itemProperties);
    itemBlockRedstoneTarget.setRegistryName(blockRedstoneTarget.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockRedstoneTarget);

    itemBlockRedstoneMeter = new BlockItem(blockRedstoneMeter, itemProperties);
    itemBlockRedstoneMeter.setRegistryName(blockRedstoneMeter.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockRedstoneMeter);

    itemBlockRedstoneColouredLamp = new BlockItem(blockRedstoneColouredLamp, itemProperties);
    itemBlockRedstoneColouredLamp.setRegistryName(blockRedstoneColouredLamp.getRegistryName());
    itemRegisterEvent.getRegistry().register(itemBlockRedstoneColouredLamp);
  }

  @SubscribeEvent
  public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) {
    tileEntityDataTypeMBE06 = TileEntityType.Builder
                    .create(TileEntityRedstoneMeter::new, blockRedstoneMeter).build(null);
                            // you probably don't need a datafixer --> null should be fine
    tileEntityDataTypeMBE06.setRegistryName("minecraftbyexample:mbe06_tile_entity_type_registry_name");
    event.getRegistry().register(tileEntityDataTypeMBE06);
  }

  @SubscribeEvent
  public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
    // not actually required for this example....
  }
}
