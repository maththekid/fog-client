package com.fogclient.module.combat;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class NoDefenderModule extends Module {
   public NoDefenderModule() {
      super("SemDefesa", "Impede o bloqueio com espada.", Category.COMBAT);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onEvent(Event var1) {
      if (var1 instanceof PlayerInteractEvent) {
         PlayerInteractEvent var2 = (PlayerInteractEvent)var1;
         if (var2.entityPlayer != null && var2.entityPlayer.getHeldItem() != null && var2.entityPlayer.getHeldItem().getItem() instanceof ItemSword) {
            if (var2.action == Action.RIGHT_CLICK_AIR) {
               var2.setCanceled(true);
            } else if (var2.action == Action.RIGHT_CLICK_BLOCK) {
               var2.useItem = Result.DENY;
            }
         }
      }

   }

   public void onLivingUpdate() {
      if (this.isEnabled()) {
         if (mc.thePlayer != null && mc.thePlayer.isBlocking()) {
            mc.thePlayer.clearItemInUse();
         }

      }
   }
}
