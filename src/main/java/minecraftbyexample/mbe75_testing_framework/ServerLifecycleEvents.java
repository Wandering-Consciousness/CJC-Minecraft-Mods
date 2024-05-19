package minecraftbyexample.mbe75_testing_framework;

import com.mojang.brigadier.CommandDispatcher;
import minecraftbyexample.usefultools.debugging.commands.DebugTriggerWatcher;
import minecraftbyexample.usefultools.debugging.commands.MBEdebugCommand;
import net.minecraft.command.CommandSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * User: The Grey Ghost
 * Date: 24/12/2014
 *
 * Don't forget to register this class on the MinecraftForge.EVENT_BUS.
 */
public class ServerLifecycleEvents
{
  @SubscribeEvent
  public static void onServerStartingEvent(FMLServerStartingEvent event) {
    MinecraftForge.EVENT_BUS.register(DebugTestWatcher.class);
  }
}
