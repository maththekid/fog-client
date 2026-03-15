package com.fogclient.module.combat;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class SoupaModule extends Module {
   private final Random random = new Random();
   private int stage = 0;
   private int previousSlot = -1;
   private int soupSlot = -1;

   public SoupaModule() {
      super("SopaFacil", "Automatically eats soup when health is low.", Category.COMBAT);
   }

   public void onEnable() {
      this.reset();
   }

   public void onDisable() {
      this.reset();
   }

   public boolean isAction() {
      return true;
   }

   public void onKeyPressed() {
   }

   public void onAction() {
      if (this.isEnabled() && this.stage == 0) {
         this.tryStartSoup(mc.thePlayer);
      }

   }

   public void onTickStart() {
      if (this.isEnabled()) {
         if (mc.thePlayer != null && mc.theWorld != null) {
            if (this.stage == 1) {
               this.finishSoup(mc.thePlayer);
               this.stage = 0;
            }

         }
      }
   }

   private void tryStartSoup(EntityPlayer var1) {
      if (var1 != null) {
         float var2 = var1.getMaxHealth();
         float var3 = var1.getHealth();
         if (!(var3 > var2 - 7.0F)) {
            ArrayList var4 = new ArrayList();

            for(int var5 = 0; var5 < 9; ++var5) {
               ItemStack var6 = var1.inventory.mainInventory[var5];
               if (var6 != null && var6.getItem() == Items.mushroom_stew) {
                  var4.add(var5);
               }
            }

            if (!var4.isEmpty()) {
               this.soupSlot = (Integer)var4.get(this.random.nextInt(var4.size()));
               this.previousSlot = var1.inventory.currentItem;
               var1.inventory.currentItem = this.soupSlot;
               ItemStack var7 = var1.inventory.getCurrentItem();
               if (var7 != null && var7.getItem() == Items.mushroom_stew) {
                  mc.playerController.sendUseItem(var1, mc.theWorld, var7);
               }

               this.stage = 1;
            }
         }
      }
   }

   private void finishSoup(EntityPlayer var1) {
      if (var1.inventory.currentItem == this.soupSlot) {
         mc.thePlayer.dropOneItem(false);
      }

      if (this.previousSlot != -1) {
         var1.inventory.currentItem = this.previousSlot;
      }

      this.reset();
   }

   private void reset() {
      this.stage = 0;
      this.previousSlot = -1;
      this.soupSlot = -1;
   }
}
