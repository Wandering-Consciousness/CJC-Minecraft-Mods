package minecraftbyexample.mbe05_block_advanced_models;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * These methods are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{

  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    RenderTypeLookup.setRenderLayer(StartupCommon.blockGlassLantern, StartupClientOnly::isGlassLanternValidLayer);
  }

  // does the Glass Lantern render in the given layer (RenderType) - used as Predicate<RenderType> lambda for setRenderLayer
  public static boolean isGlassLanternValidLayer(RenderType layerToCheck) {
    return layerToCheck == RenderType.getCutout() || layerToCheck == RenderType.getTranslucent();
  }
}