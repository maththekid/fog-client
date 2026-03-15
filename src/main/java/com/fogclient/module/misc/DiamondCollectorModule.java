package com.fogclient.module.misc;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.NumberSetting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemArmor.ArmorMaterial;

public class DiamondCollectorModule extends Module {
   private final NumberSetting delay = new NumberSetting("Delay (ms)", this, 100.0D, 0.0D, 1000.0D, 10.0D);
   private long lastClickTime = 0L;

   public DiamondCollectorModule() {
      super("DiamondCollector", "Automatically steals diamond items from chests.", Category.MISC);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onTick() {
      if (this.isEnabled()) {
         if (mc.thePlayer != null && mc.playerController != null) {
            if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
               ContainerChest var1 = (ContainerChest)mc.thePlayer.openContainer;
               int var2 = var1.getLowerChestInventory().getSizeInventory();
               if ((double)(System.currentTimeMillis() - this.lastClickTime) < this.delay.getValue()) {
                  return;
               }

               for(int var3 = 0; var3 < var2; ++var3) {
                  ItemStack var4 = var1.getLowerChestInventory().getStackInSlot(var3);
                  if (var4 != null && this.isDiamond(var4.getItem())) {
                     mc.playerController.windowClick(var1.windowId, var3, 0, 1, mc.thePlayer);
                     this.lastClickTime = System.currentTimeMillis();
                     break;
                  }
               }
            }

         }
      }
   }

   private boolean isDiamond(Item var1) {
      if (var1 == Items.diamond) {
         return true;
      } else if (var1 instanceof ItemTool && ((ItemTool)var1).getToolMaterialName().equals("EMERALD")) {
         return true;
      } else if (var1 instanceof ItemSword && ((ItemSword)var1).getToolMaterialName().equals("EMERALD")) {
         return true;
      } else {
         return var1 instanceof ItemArmor && ((ItemArmor)var1).getArmorMaterial() == ArmorMaterial.DIAMOND;
      }
   }
}
