package minecraftbyexample.mbe32_inventory_item;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * The Startup class for this example is called during startup of the mod
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{

  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    // we need to attach the fullness PropertyOverride to the Item, but there are two things to be careful of:
    // 1) We should do this on a client installation only, not on a DedicatedServer installation.  Hence we need to use
    //    FMLClientSetupEvent.
    // 2) FMLClientSetupEvent is multithreaded but ItemModelsProperties is not multithread-safe.  So we need to use the enqueueWork method,
    //    which lets us register a function for synchronous execution in the main thread after the parallel processing is completed
    event.enqueueWork(StartupClientOnly::registerPropertyOverride);

    // register the factory that is used on the client to generate a ContainerScreen corresponding to our Container
    ScreenManager.registerFactory(StartupCommon.containerTypeFlowerBag, ContainerScreenFlowerBag::new);
  }

  public static void registerPropertyOverride() {
    ItemModelsProperties.registerProperty(StartupCommon.itemFlowerBag, new ResourceLocation("fullness"), ItemFlowerBag::getFullnessPropertyOverride);
    // use lambda function to link the bag fullness to a suitable property override value
  }


}
