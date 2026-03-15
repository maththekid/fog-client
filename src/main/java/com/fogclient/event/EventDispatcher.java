package com.fogclient.event;

import com.fogclient.config.ConfigManager;
import com.fogclient.module.Module;
import com.fogclient.module.ModuleManager;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent.Pre;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class EventDispatcher {
   private final Minecraft mc = Minecraft.getMinecraft();
   private int saveTimer = 0;

   public EventDispatcher() {
      MinecraftForge.EVENT_BUS.register(this);
      FMLCommonHandler.instance().bus().register(this);
   }

   @SubscribeEvent
   public void onKeyInput(KeyInputEvent var1) {
      if (this.mc.currentScreen == null) {
         int var2 = Keyboard.getEventKey();
         if (var2 != 0) {
            if (Keyboard.getEventKeyState()) {
               ModuleManager.getInstance().onKeyPressed(var2);
            }

         }
      }
   }

   @SubscribeEvent
   public void onMouseInput(MouseInputEvent var1) {
      if (this.mc.currentScreen == null) {
         int var2 = Mouse.getEventButton();
         if (var2 >= 0) {
            int var3 = -100 + var2;
            if (Mouse.getEventButtonState()) {
               ModuleManager.getInstance().onKeyPressed(var3);
            }

         }
      }
   }

   @SubscribeEvent
   public void onClientTick(ClientTickEvent var1) {
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         Iterator var2 = ModuleManager.getInstance().getModules().iterator();

         while(var2.hasNext()) {
            Module var3 = (Module)var2.next();
            if (var3.isEnabled()) {
               if (var1.phase == Phase.START) {
                  var3.onTickStart();
               } else {
                  var3.onTick();
               }
            }
         }

         if (var1.phase == Phase.END) {
            ++this.saveTimer;
            if (this.saveTimer >= 100) {
               this.saveTimer = 0;
               ConfigManager.saveConfig();
            }
         }

      }
   }

   @SubscribeEvent
   public void onLivingUpdate(LivingUpdateEvent var1) {
      if (var1.entity == this.mc.thePlayer) {
         Iterator var2 = ModuleManager.getInstance().getModules().iterator();

         while(var2.hasNext()) {
            Module var3 = (Module)var2.next();
            if (var3.isEnabled()) {
               var3.onLivingUpdate();
            }
         }

      }
   }

   @SubscribeEvent
   public void onRenderWorldLast(RenderWorldLastEvent var1) {
      Iterator var2 = ModuleManager.getInstance().getModules().iterator();

      while(var2.hasNext()) {
         Module var3 = (Module)var2.next();
         if (var3.isEnabled()) {
            var3.onRenderTick();
            var3.onEvent(var1);
         }
      }

   }

   @SubscribeEvent
   public void onAttack(AttackEntityEvent var1) {
      Iterator var2 = ModuleManager.getInstance().getModules().iterator();

      while(var2.hasNext()) {
         Module var3 = (Module)var2.next();
         if (var3.isEnabled()) {
            var3.onEvent(var1);
         }
      }

   }

   @SubscribeEvent
   public void onGuiOpen(GuiOpenEvent var1) {
      Iterator var2 = ModuleManager.getInstance().getModules().iterator();

      while(var2.hasNext()) {
         Module var3 = (Module)var2.next();
         if (var3.isEnabled()) {
            var3.onEvent(var1);
         }
      }

   }

   @SubscribeEvent
   public void onMouseInput(Pre var1) {
      Iterator var2 = ModuleManager.getInstance().getModules().iterator();

      while(var2.hasNext()) {
         Module var3 = (Module)var2.next();
         if (var3.isEnabled()) {
            var3.onEvent(var1);
         }
      }

   }

   @SubscribeEvent
   public void onPlayerLoggedIn(PlayerLoggedInEvent var1) {
      Iterator var2 = ModuleManager.getInstance().getModules().iterator();

      while(var2.hasNext()) {
         Module var3 = (Module)var2.next();
         if (var3.isEnabled()) {
            var3.onEvent(var1);
         }
      }

   }

   @SubscribeEvent
   public void onNameFormat(NameFormat var1) {
      Iterator var2 = ModuleManager.getInstance().getModules().iterator();

      while(var2.hasNext()) {
         Module var3 = (Module)var2.next();
         if (var3.isEnabled()) {
            var3.onEvent(var1);
         }
      }

   }

   @SubscribeEvent
   public void onWorldTick(WorldTickEvent var1) {
      Iterator var2 = ModuleManager.getInstance().getModules().iterator();

      while(var2.hasNext()) {
         Module var3 = (Module)var2.next();
         if (var3.isEnabled()) {
            var3.onEvent(var1);
         }
      }

   }

   @SubscribeEvent
   public void onNeighborNotify(NeighborNotifyEvent var1) {
      Iterator var2 = ModuleManager.getInstance().getModules().iterator();

      while(var2.hasNext()) {
         Module var3 = (Module)var2.next();
         if (var3.isEnabled()) {
            var3.onEvent(var1);
         }
      }

   }

   @SubscribeEvent
   public void onPlayerInteract(PlayerInteractEvent var1) {
      Iterator var2 = ModuleManager.getInstance().getModules().iterator();

      while(var2.hasNext()) {
         Module var3 = (Module)var2.next();
         if (var3.isEnabled()) {
            var3.onEvent(var1);
         }
      }

   }
}
