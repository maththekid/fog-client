package com.fogclient.module.combat;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DefaultSoupModule extends Module {
   public DefaultSoupModule() {
      super("SopaPadrao", "Consome sopa automaticamente.", Category.COMBAT);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onEvent(Event var1) {
      if (var1 instanceof PlayerInteractEvent) {
         this.onInteract((PlayerInteractEvent)var1);
      }

   }

   private void onInteract(PlayerInteractEvent var1) {
      if (this.isEnabled()) {
         EntityPlayer var2 = var1.entityPlayer;
         if (var2 != null) {
            if (var1.action == Action.RIGHT_CLICK_AIR || var1.action == Action.RIGHT_CLICK_BLOCK) {
               ItemStack var3 = var2.getHeldItem();
               if (var3 != null && var3.getItem() == Items.mushroom_stew) {
                  if (!(var2.getHealth() >= var2.getMaxHealth())) {
                     var1.setCanceled(true);
                     float var4;
                     if (var2.worldObj.isRemote) {
                        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(var3));
                        var4 = Math.min(7.0F, var2.getMaxHealth() - var2.getHealth());
                        if (var4 > 0.0F) {
                           var2.setHealth(var2.getHealth() + var4);
                        }

                        if (!var2.capabilities.isCreativeMode) {
                           var2.inventory.setInventorySlotContents(var2.inventory.currentItem, new ItemStack(Items.bowl));
                        }
                     } else {
                        var4 = Math.min(7.0F, var2.getMaxHealth() - var2.getHealth());
                        if (var4 > 0.0F) {
                           var2.setHealth(var2.getHealth() + var4);
                        }

                        if (!var2.capabilities.isCreativeMode) {
                           var2.inventory.setInventorySlotContents(var2.inventory.currentItem, new ItemStack(Items.bowl));
                           var2.inventoryContainer.detectAndSendChanges();
                        }
                     }

                  }
               }
            }
         }
      }
   }
}
