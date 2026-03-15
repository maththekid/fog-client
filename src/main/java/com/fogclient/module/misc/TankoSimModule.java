package com.fogclient.module.misc;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.BooleanSetting;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class TankoSimModule extends Module {
   private final BooleanSetting woodShovel = new BooleanSetting("PÃ¡ de Madeira", this, true);
   private final BooleanSetting stoneShovel = new BooleanSetting("PÃ¡ de Pedra", this, false);
   private final BooleanSetting shears = new BooleanSetting("Tesouras", this, false);
   private final BooleanSetting armor = new BooleanSetting("Armaduras", this, false);

   public TankoSimModule() {
      super("TankoSIM", "Drops wooden shovels automatically from inventory", Category.MISC);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onTick() {
      if (this.isEnabled()) {
         if (mc.thePlayer != null && mc.thePlayer.inventoryContainer != null) {
            for(int var1 = 0; var1 < mc.thePlayer.inventoryContainer.inventorySlots.size(); ++var1) {
               Slot var2 = (Slot)mc.thePlayer.inventoryContainer.inventorySlots.get(var1);
               if (var2.getHasStack()) {
                  ItemStack var3 = var2.getStack();
                  Item var4 = var3.getItem();
                  boolean var5 = false;
                  if (this.woodShovel.getValue() && var4 == Items.wooden_shovel) {
                     var5 = true;
                  } else if (this.stoneShovel.getValue() && var4 == Items.stone_shovel) {
                     var5 = true;
                  } else if (this.shears.getValue() && var4 == Items.shears) {
                     var5 = true;
                  } else if (this.armor.getValue() && var4 instanceof ItemArmor) {
                     if (var1 >= 5 && var1 <= 8) {
                        continue;
                     }

                     var5 = true;
                  }

                  if (var5) {
                     mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, var1, 1, 4, mc.thePlayer);
                  }
               }
            }

         }
      }
   }
}
