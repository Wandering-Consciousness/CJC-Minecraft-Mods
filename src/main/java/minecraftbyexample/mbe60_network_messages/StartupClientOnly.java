package minecraftbyexample.mbe60_network_messages;

import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    // not actually required for this example....
  }
}
