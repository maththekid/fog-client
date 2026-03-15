package com.fogclient.module.player;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import java.lang.reflect.Field;
import java.util.Iterator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent.Pre;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Mouse;

public class JuntaPotesModule extends Module {
   private static Field guiLeftField;
   private static Field guiTopField;

   public JuntaPotesModule() {
      super("JuntaPotes", "Groups bowls in inventory with middle click (Button 5)", Category.PLAYER);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onEvent(Event var1) {
      if (var1 instanceof Pre) {
         Pre var2 = (Pre)var1;
         if (!Mouse.getEventButtonState()) {
            return;
         }

         int var3 = Mouse.getEventButton();
         if (var3 == 4 && var2.gui instanceof GuiContainer) {
            GuiContainer var4 = (GuiContainer)var2.gui;
            Slot var5 = this.getSlotAtMousePosition(var4);
            if (var5 != null && var5.getHasStack() && this.isValidItem(var5.getStack())) {
               try {
                  this.handleItemClick(var4, var5);
                  var2.setCanceled(true);
               } catch (Exception var7) {
                  var7.printStackTrace();
               }
            }
         }
      }

   }

   private Slot getSlotAtMousePosition(GuiContainer var1) {
      if (guiLeftField != null && guiTopField != null) {
         try {
            int var2 = guiLeftField.getInt(var1);
            int var3 = guiTopField.getInt(var1);
            int var4 = Mouse.getEventX() * var1.width / mc.displayWidth;
            int var5 = var1.height - Mouse.getEventY() * var1.height / mc.displayHeight - 1;
            Iterator var6 = var1.inventorySlots.inventorySlots.iterator();

            while(var6.hasNext()) {
               Slot var7 = (Slot)var6.next();
               if (this.isMouseOverSlot(var7, var4, var5, var2, var3)) {
                  return var7;
               }
            }
         } catch (Exception var8) {
            var8.printStackTrace();
         }

         return null;
      } else {
         return null;
      }
   }

   private boolean isMouseOverSlot(Slot var1, int var2, int var3, int var4, int var5) {
      int var6 = var4 + var1.xDisplayPosition;
      int var7 = var5 + var1.yDisplayPosition;
      return var2 >= var6 - 1 && var2 < var6 + 16 + 1 && var3 >= var7 - 1 && var3 < var7 + 16 + 1;
   }

   private boolean isValidItem(ItemStack var1) {
      if (var1 == null) {
         return false;
      } else {
         Item var2 = var1.getItem();
         return var2 == Items.bowl;
      }
   }

   private void handleItemClick(GuiContainer var1, Slot var2) {
      ItemStack var3 = mc.thePlayer.inventory.getItemStack();
      if (var3 == null) {
         mc.playerController.windowClick(var1.inventorySlots.windowId, var2.slotNumber, 0, 0, mc.thePlayer);
      }

      mc.playerController.windowClick(var1.inventorySlots.windowId, var2.slotNumber, 0, 6, mc.thePlayer);
      mc.playerController.windowClick(var1.inventorySlots.windowId, var2.slotNumber, 0, 0, mc.thePlayer);
   }

   static {
      try {
         guiLeftField = ReflectionHelper.findField(GuiContainer.class, new String[]{"guiLeft", "field_147003_i"});
         guiLeftField.setAccessible(true);
         guiTopField = ReflectionHelper.findField(GuiContainer.class, new String[]{"guiTop", "field_147009_r"});
         guiTopField.setAccessible(true);
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }
}
