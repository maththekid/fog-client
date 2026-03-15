package com.fogclient;

import com.fogclient.command.CommandKillBot;
import com.fogclient.command.CommandSpawnBot;
import com.fogclient.config.ConfigManager;
import com.fogclient.entity.EntityPvPBot;
import com.fogclient.event.EventDispatcher;
import com.fogclient.module.ModuleManager;
import com.fogclient.render.entity.RenderBotAsPlayer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(
   modid = "fogclient",
   version = "1.0",
   name = "FogClient",
   acceptedMinecraftVersions = "[1.8.9]"
)
public class FogClient {

   @EventHandler
   public void preInit(FMLPreInitializationEvent var1) {
      EntityRegistry.registerModEntity(EntityPvPBot.class, "TreinoBot", 202, this, 64, 1, true);
   }

   @EventHandler
   public void init(FMLInitializationEvent var1) {
	  RenderingRegistry.registerEntityRenderingHandler(EntityPvPBot.class, new RenderBotAsPlayer(Minecraft.getMinecraft().getRenderManager()));
      MinecraftForge.EVENT_BUS.register(new EventDispatcher());
      ModuleManager.getInstance();
      ConfigManager.loadConfig();

	  ClientCommandHandler.instance.registerCommand(new CommandSpawnBot());
	  ClientCommandHandler.instance.registerCommand(new CommandKillBot());
   }

   @EventHandler
   public void serverStopped(FMLServerStoppedEvent var1) {
      ConfigManager.saveConfig();
   }
}
