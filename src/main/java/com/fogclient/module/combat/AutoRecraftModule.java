package com.fogclient.module.combat;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.NumberSetting;
import com.fogclient.util.crafting.CraftAction;
import com.fogclient.util.crafting.CraftActionType;
import com.fogclient.util.crafting.RecipeType;
import com.fogclient.util.crafting.RecraftRecipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AutoRecraftModule extends Module {
   private NumberSetting speedSetting;
   private static final int MIN_DELAY_TICKS = 0;
   private static final int MAX_DELAY_TICKS = 2;
   private boolean isCrafting = false;
   private int tickCounter = 0;
   private int currentDelay = 0;
   private List<CraftAction> actionQueue = new ArrayList();
   private int currentActionIndex = 0;
   private static final Random random = new Random();
   private static final Item BOWL;
   private static final Item RED_MUSHROOM;
   private static final Item BROWN_MUSHROOM;
   private static final Item COCOA_BEANS;
   private static final Item CACTUS;
   private static final Item MUSHROOM_SOUP;
   private static final int CRAFT_SLOT_1 = 1;
   private static final int CRAFT_SLOT_2 = 2;
   private static final int CRAFT_SLOT_3 = 3;
   private static final int CRAFT_SLOT_4 = 4;
   private static final int RESULT_SLOT = 0;

   public AutoRecraftModule() {
      super("RecraftPerfeito", "Recrafta sopas automaticamente.", Category.COMBAT);
      this.keybind = 0;
      this.actionKeybind = 19;
      this.speedSetting = new NumberSetting("Speed", this, 100.0D, 1.0D, 100.0D, 1.0D);
   }

   public boolean isAction() {
      return true;
   }

   public void onEnable() {
   }

   public void onDisable() {
      this.isCrafting = false;
      this.actionQueue.clear();
      this.currentActionIndex = 0;
   }

   public void onKeyPressed() {
   }

   public void onAction() {
      if (this.isEnabled() && !this.isCrafting) {
         this.handleAutoRecraft();
      }

   }

   public int getSpeed() {
      return (int)this.speedSetting.getValue();
   }

   public void onTick() {
      if (this.isCrafting) {
         Minecraft var1 = Minecraft.getMinecraft();
         if (var1.currentScreen != null && var1.currentScreen instanceof GuiInventory) {
            int var2 = this.getSpeed();
            if (var2 < 0) {
               var2 = 0;
            }

            if (var2 > 100) {
               var2 = 100;
            }

            int var3 = 75 + var2 * 25 / 100;
            if (var3 < 0) {
               var3 = 0;
            }

            if (var3 > 100) {
               var3 = 100;
            }

            int var4;
            if (var3 >= 100) {
               var4 = 5;
            } else {
               var4 = 1 + var3 * 3 / 100;
               if (var4 < 1) {
                  var4 = 1;
               }

               if (var4 > 4) {
                  var4 = 4;
               }
            }

            byte var5 = 20;
            int var6 = var5 * (100 - var3) / 100;
            if (var6 < 0) {
               var6 = 0;
            }

            if (var6 > 0) {
               ++this.tickCounter;
               if (this.tickCounter <= var6) {
                  return;
               }

               this.tickCounter = 0;
            }

            int var7 = 0;

            while(this.currentActionIndex < this.actionQueue.size() && var7 < var4) {
               CraftAction var8 = (CraftAction)this.actionQueue.get(this.currentActionIndex);
               if (this.currentActionIndex == 0 && var8.type == CraftActionType.WAIT && this.actionQueue.size() == 1) {
                  if (this.executeAction(var8)) {
                     ++this.currentActionIndex;
                     this.prepareCraftActions();
                  }
                  break;
               }

               if (var8.type == CraftActionType.PICKUP && this.currentActionIndex + 1 < this.actionQueue.size()) {
                  CraftAction var9 = (CraftAction)this.actionQueue.get(this.currentActionIndex + 1);
                  if (var9.type == CraftActionType.PLACE) {
                     if (!this.executeAction(var8)) {
                        break;
                     }

                     ++this.currentActionIndex;
                     ++var7;
                     if (this.executeAction(var9)) {
                        ++this.currentActionIndex;
                        ++var7;
                     }
                  } else {
                     if (!this.executeAction(var8)) {
                        break;
                     }

                     ++this.currentActionIndex;
                     ++var7;
                  }
               } else {
                  if (!this.executeAction(var8)) {
                     break;
                  }

                  ++this.currentActionIndex;
                  ++var7;
               }
            }

            if (this.currentActionIndex >= this.actionQueue.size()) {
               this.isCrafting = false;
               this.actionQueue.clear();
               this.currentActionIndex = 0;
            }

         } else {
            this.isCrafting = false;
            this.actionQueue.clear();
         }
      }
   }

   private void handleAutoRecraft() {
      Minecraft var1 = Minecraft.getMinecraft();
      EntityPlayerSP var2 = var1.thePlayer;
      if (var2 != null) {
         if (var1.currentScreen == null || var1.currentScreen instanceof GuiInventory) {
            if (var1.currentScreen instanceof GuiInventory) {
               this.isCrafting = true;
               this.tickCounter = 0;
               this.currentActionIndex = 0;
               this.actionQueue.clear();
               this.prepareCraftActions();
            } else {
               var1.displayGuiScreen(new GuiInventory(var2));
               this.isCrafting = true;
               this.tickCounter = 0;
               this.currentActionIndex = 0;
               this.actionQueue.clear();
               this.actionQueue.add(new CraftAction(CraftActionType.WAIT, -1, -1));
            }
         }
      }
   }

   private void prepareCraftActions() {
      Minecraft var1 = Minecraft.getMinecraft();
      EntityPlayerSP var2 = var1.thePlayer;
      if (var2 != null && var1.currentScreen instanceof GuiInventory) {
         ContainerPlayer var3 = (ContainerPlayer)((GuiInventory)var1.currentScreen).inventorySlots;
         RecraftRecipe var4 = this.findRecraftRecipe(var3);
         if (var4 == null) {
            this.isCrafting = false;
            this.setEnabled(false);
         } else {
            ArrayList var5 = new ArrayList();
            var5.add(1);
            var5.add(2);
            var5.add(3);
            var5.add(4);
            Collections.shuffle(var5, random);
            int var6;
            int var7;
            if (var4.recipeType == RecipeType.MUSHROOMS) {
               var6 = (Integer)var5.get(0);
               var7 = (Integer)var5.get(1);
               int var8 = (Integer)var5.get(2);
               this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, var4.bowlSlot, -1));
               this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, var6));
               this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, var4.ingredient1Slot, -1));
               this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, var7));
               this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, var4.ingredient2Slot, -1));
               this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, var8));
            } else if (var4.recipeType == RecipeType.COCOA || var4.recipeType == RecipeType.CACTUS) {
               var6 = (Integer)var5.get(0);
               var7 = (Integer)var5.get(1);
               this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, var4.bowlSlot, -1));
               this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, var6));
               this.actionQueue.add(new CraftAction(CraftActionType.PICKUP, var4.ingredient1Slot, -1));
               this.actionQueue.add(new CraftAction(CraftActionType.PLACE, -1, var7));
            }

            this.actionQueue.add(new CraftAction(CraftActionType.WAIT, -1, -1));
            this.actionQueue.add(new CraftAction(CraftActionType.SHIFT_CLICK_RESULT, 0, -1));
            this.actionQueue.add(new CraftAction(CraftActionType.WAIT, -1, -1));
            this.actionQueue.add(new CraftAction(CraftActionType.PULL_REMAINING_ITEMS, -1, -1));
            this.actionQueue.add(new CraftAction(CraftActionType.CLOSE_INVENTORY, -1, -1));
         }
      }
   }

   private RecraftRecipe findRecraftRecipe(ContainerPlayer var1) {
      int var2 = -1;
      boolean var3 = true;
      boolean var4 = true;

      int var5;
      for(var5 = 9; var5 < 45; ++var5) {
         Slot var6 = var1.getSlot(var5);
         if (var6.getHasStack()) {
            ItemStack var7 = var6.getStack();
            if (var7.getItem() == BOWL && var2 == -1) {
               var2 = var5;
            }
         }
      }

      if (var2 == -1) {
         return null;
      } else {
         var5 = -1;
         int var10 = -1;

         Slot var8;
         ItemStack var9;
         int var11;
         for(var11 = 9; var11 < 45; ++var11) {
            var8 = var1.getSlot(var11);
            if (var8.getHasStack()) {
               var9 = var8.getStack();
               if (var9.getItem() == RED_MUSHROOM && var5 == -1) {
                  var5 = var11;
               } else if (var9.getItem() == BROWN_MUSHROOM && var10 == -1) {
                  var10 = var11;
               }
            }
         }

         if (var5 != -1 && var10 != -1) {
            return new RecraftRecipe(RecipeType.MUSHROOMS, var2, var5, var10);
         } else {
            for(var11 = 9; var11 < 45; ++var11) {
               var8 = var1.getSlot(var11);
               if (var8.getHasStack()) {
                  var9 = var8.getStack();
                  if (var9.getItem() == COCOA_BEANS && var9.getMetadata() == 3) {
                     return new RecraftRecipe(RecipeType.COCOA, var2, var11, -1);
                  }
               }
            }

            for(var11 = 9; var11 < 45; ++var11) {
               var8 = var1.getSlot(var11);
               if (var8.getHasStack()) {
                  var9 = var8.getStack();
                  if (var9.getItem() == CACTUS) {
                     return new RecraftRecipe(RecipeType.CACTUS, var2, var11, -1);
                  }
               }
            }

            return null;
         }
      }
   }

   private boolean executeAction(CraftAction var1) {
      Minecraft var2 = Minecraft.getMinecraft();
      if (var2.currentScreen != null && var2.currentScreen instanceof GuiInventory) {
         ContainerPlayer var3 = (ContainerPlayer)((GuiInventory)var2.currentScreen).inventorySlots;
         if (var1.type == CraftActionType.WAIT) {
            return true;
         } else {
            Slot var8;
            ItemStack var9;
            if (var1.type == CraftActionType.SHIFT_CLICK_RESULT) {
               var8 = var3.getSlot(0);
               if (var8.getHasStack()) {
                  var9 = var8.getStack();
                  if (var9.getItem() == MUSHROOM_SOUP) {
                     var2.playerController.windowClick(var3.windowId, 0, 0, 1, var2.thePlayer);
                     return true;
                  }
               }

               return false;
            } else if (var1.type != CraftActionType.PULL_REMAINING_ITEMS) {
               if (var1.type == CraftActionType.CLOSE_INVENTORY) {
                  var2.displayGuiScreen((GuiScreen)null);
                  this.isCrafting = false;
                  return true;
               } else if (var1.type == CraftActionType.PICKUP) {
                  var8 = var3.getSlot(var1.fromSlot);
                  if (!var8.getHasStack()) {
                     return true;
                  } else {
                     var9 = var2.thePlayer.inventory.getItemStack();
                     if (var9 != null) {
                        return false;
                     } else {
                        var2.playerController.windowClick(var3.windowId, var1.fromSlot, 1, 0, var2.thePlayer);
                        return true;
                     }
                  }
               } else if (var1.type == CraftActionType.PLACE) {
                  ItemStack var7 = var2.thePlayer.inventory.getItemStack();
                  if (var7 == null) {
                     return false;
                  } else {
                     var2.playerController.windowClick(var3.windowId, var1.toSlot, 0, 0, var2.thePlayer);
                     return true;
                  }
               } else {
                  return false;
               }
            } else {
               boolean var4 = false;

               for(int var5 = 1; var5 <= 4; ++var5) {
                  Slot var6 = var3.getSlot(var5);
                  if (var6.getHasStack()) {
                     var4 = true;
                     var2.playerController.windowClick(var3.windowId, var5, 0, 1, var2.thePlayer);
                     break;
                  }
               }

               return !var4;
            }
         }
      } else {
         return false;
      }
   }

   static {
      BOWL = Items.bowl;
      RED_MUSHROOM = Item.getItemFromBlock(Blocks.red_mushroom);
      BROWN_MUSHROOM = Item.getItemFromBlock(Blocks.brown_mushroom);
      COCOA_BEANS = Items.dye;
      CACTUS = Item.getItemFromBlock(Blocks.cactus);
      MUSHROOM_SOUP = Items.mushroom_stew;
   }
}
