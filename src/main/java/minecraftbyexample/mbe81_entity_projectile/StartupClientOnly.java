package minecraftbyexample.mbe81_entity_projectile;

import minecraftbyexample.mbe11_item_variants.ItemVariants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * The Startup classes for this example are called during startup
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  /**
   * @param event
   */
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
    // we need to attach the fullness PropertyOverride to the Item, but there are two things to be careful of:
    // 1) We should do this on a client installation only, not on a DedicatedServer installation.  Hence we need to use
    //    FMLClientSetupEvent.
    // 2) FMLClientSetupEvent is multithreaded but ItemModelsProperties is not multithread-safe.  So we need to use the enqueueWork method,
    //    which lets us register a function for synchronous execution in the main thread after the parallel processing is completed
    event.enqueueWork(StartupClientOnly::registerPropertyOverride);

    // Register the custom renderer for each entity
    // I've written out the factory as an explicit implementation to make it clearer, but most folks would just use a lambda instead, eg
    //    RenderingRegistry.registerEntityRenderingHandler(StartupCommon.emojiEntityType,
    //            erm -> new SpriteRenderer<>(erm, Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(StartupCommon.emojiEntityType, new emojiRenderFactory());
    RenderingRegistry.registerEntityRenderingHandler(StartupCommon.boomerangEntityType, new BoomerangRenderFactory());
  }

  public static void registerPropertyOverride() {
    // We use a PropertyOverride for this item to change the appearance depending on the state of the property;
    // In this case, the boomerang moves gradually higher when the player holds the right button for longer.
    // getChargeUpTime() is used as a lambda function to calculate the current chargefraction during rendering
    // See the mbe81b_boomerang_charge_0.json, mbe81b_boomerang_charge_1.json, etc to see how this is done.
    //  See also mbe12 for a more-detailed explanation
    ItemModelsProperties.registerProperty(StartupCommon.boomerangItem, new ResourceLocation("chargefraction"), BoomerangItem::getChargeUpTime);
  }

  // I've written this out as an explicit implementation to make it clearer, but most folks would just use a lambda instead
  private static class emojiRenderFactory implements IRenderFactory<EmojiEntity> {
    @Override
    public EntityRenderer<? super EmojiEntity> createRenderFor(EntityRendererManager manager) {
      ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
      return new SpriteRenderer<>(manager, itemRenderer);
    }
  }

  // I've written this out as an explicit implementation to make it clearer, but most folks would just use a lambda instead
  private static class BoomerangRenderFactory implements IRenderFactory<BoomerangEntity> {
    @Override
    public EntityRenderer<? super BoomerangEntity> createRenderFor(EntityRendererManager manager) {
      return new BoomerangRenderer(manager);
    }
  }

  // manually add our Boomerang's model to the model registry so that vanilla will load and bake it.
  //  (It is already loaded for rendering our item, but that is an inconvenient format for entity rendering)
  // We need to wrap our model in a json in order for it to load properly.
  // Alternatively, you can manually keep track of your entity models yourself, by subscribing to ModelRegistryEvent
  //  event and ModelBakeEvent
  //  for example here: https://github.com/Cadiboo/WIPTech/blob/fb5883e9d76ef0361ec1ebbcb9c508611dd2ef6b/src/main/java/cadiboo/wiptech/client/ClientEventSubscriber.java#L189
  @SubscribeEvent
  public static void onModelRegistryEvent(ModelRegistryEvent event) {
    ModelLoader.addSpecialModel(BoomerangRenderer.BOOMERANG_MODEL_RESOURCE_LOCATION);
  }

  // the commented-out code below is useful if you want to manually track your entity models yourself instead of treating them as blocks

//  /**
//   * Indicate to vanilla that it should load and bake the given model, even if no blocks or
//   * items use it. This is useful if e.g. you have baked models only for entity renderers.
//   * Call during {@link net.minecraftforge.client.event.ModelRegistryEvent}
//   * @param rl The model, either {@link ModelResourceLocation} to point to a blockstate variant,
//   *           or plain {@link ResourceLocation} to point directly to a json in the models folder.
//   */
//  public static void addSpecialModel(ResourceLocation rl) {
//    specialModels.add(rl);
//  }
//
//  @SubscribeEvent
//  public static void onModelBakeEvent(final ModelBakeEvent event) {
//    final Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();
//
//    injectModels(registry);
//    WIPTech.info("Injected models");
//
//  }
//
//  private static void injectModels(final IRegistry<ModelResourceLocation, IBakedModel> registry) {
//    final HashSet<ModelResourceLocation> modelLocations = new HashSet<>();
//
//    modelLocations.add(new ModelResourceLocation("minecraftbyexample:entity/mbe81b_boomerang.obj"));
//
//    for (final ModelResourceLocation modelLocation : modelLocations) {
//      try {
//        /* modified from code made by Draco18s */
//        final ModelResourceLocation location = new ModelResourceLocation(modelLocation.toString());
//
//        final IBakedModel bakedModel = ModelsCache.INSTANCE.getBakedModel(modelLocation);
//
//        registry.putObject(location, bakedModel);
//        WIPTech.debug("Sucessfully injected " + modelLocation.toString() + " into Model Registry");
//      } catch (final Exception e) {
//        WIPTech.error("Error injecting model " + modelLocation.toString() + " into Model Registry");
//      }
//    }
//  }

}


