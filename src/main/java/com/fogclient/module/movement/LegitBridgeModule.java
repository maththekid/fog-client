package com.fogclient.module.movement;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.BooleanSetting;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class LegitBridgeModule extends Module {
   private long nextClickTime = 0L;
   private long nextReleaseTime = 0L;
   private boolean isHolding = false;
   private boolean wasOnEdge = false;
   private boolean bridgingActive = false;
   private final Random random = new Random();
   private BooleanSetting toggleMode;

   public LegitBridgeModule() {
      super("LegitBridge", "Ajuda a fazer pontes (simula clicks).", Category.MOVEMENT);
      this.keybind = 0;
      this.actionKeybind = 0;
      this.toggleMode = new BooleanSetting("Toggle Action", this, true);
   }

   public boolean isAction() {
      return true;
   }

   public void onAction() {
      if (this.isEnabled() && this.toggleMode.isEnabled()) {
         this.bridgingActive = !this.bridgingActive;
      }

   }

   public void onEnable() {
      this.isHolding = false;
      this.wasOnEdge = false;
      this.bridgingActive = false;
   }

   public void onDisable() {
      if (this.isHolding) {
         int var1 = Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode();
         KeyBinding.setKeyBindState(var1, false);
         this.isHolding = false;
      }

      this.bridgingActive = false;
   }

   public void onTick() {
      if (this.isEnabled()) {
         Minecraft var1 = Minecraft.getMinecraft();
         boolean var2 = false;
         if (this.toggleMode.isEnabled()) {
            var2 = this.actionKeybind == 0 || this.bridgingActive;
         } else if (this.actionKeybind == 0) {
            var2 = true;
         } else {
            var2 = Keyboard.isKeyDown(this.actionKeybind);
         }

         if (!var2) {
            if (this.isHolding) {
               int var5 = var1.gameSettings.keyBindUseItem.getKeyCode();
               KeyBinding.setKeyBindState(var5, false);
               this.isHolding = false;
            }

            this.wasOnEdge = false;
         } else if (var1.thePlayer != null && var1.theWorld != null) {
            if (var1.currentScreen == null) {
               if (var1.thePlayer.getHeldItem() != null && var1.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                  boolean var3 = this.shouldPlaceBlock(var1);
                  if (var3) {
                     if (!this.wasOnEdge) {
                        this.nextClickTime = System.currentTimeMillis() + (long)this.random.nextInt(20);
                        this.isHolding = false;
                     }

                     this.performHumanClick(var1);
                  } else if (this.isHolding) {
                     int var4 = var1.gameSettings.keyBindUseItem.getKeyCode();
                     KeyBinding.setKeyBindState(var4, false);
                     this.isHolding = false;
                  }

                  this.wasOnEdge = var3;
               }

            }
         }
      }
   }

   private void performHumanClick(Minecraft var1) {
      long var2 = System.currentTimeMillis();
      int var4 = var1.gameSettings.keyBindUseItem.getKeyCode();
      if (!this.isHolding) {
         if (var2 >= this.nextClickTime) {
            KeyBinding.setKeyBindState(var4, true);
            KeyBinding.onTick(var4);
            this.isHolding = true;
            this.updateHoldTime();
         }
      } else if (var2 >= this.nextReleaseTime) {
         KeyBinding.setKeyBindState(var4, false);
         this.isHolding = false;
         this.updateNextDelay(var1);
      }

   }

   private void updateHoldTime() {
      long var1 = 30L + (long)(Math.abs(this.random.nextGaussian()) * 15.0D);
      if (var1 < 15L) {
         var1 = 15L;
      }

      if (var1 > 80L) {
         var1 = 80L;
      }

      this.nextReleaseTime = System.currentTimeMillis() + var1;
   }

   private void updateNextDelay(Minecraft var1) {
      if (var1.thePlayer.motionY < -0.05D) {
         long var6 = (long)(10 + this.random.nextInt(20));
         this.nextClickTime = System.currentTimeMillis() + var6;
      } else {
         byte var2 = 35;
         int var3 = (int)(this.random.nextGaussian() * 10.0D);
         long var4 = (long)(var2 + var3);
         if (this.random.nextInt(50) == 0) {
            var4 += (long)(50 + this.random.nextInt(100));
         }

         if (var4 < 10L) {
            var4 = 10L;
         }

         if (var4 > 100L) {
            var4 = 100L;
         }

         this.nextClickTime = System.currentTimeMillis() + var4;
      }
   }

   private boolean shouldPlaceBlock(Minecraft var1) {
      BlockPos var2 = new BlockPos(var1.thePlayer.posX, var1.thePlayer.posY - 1.0D, var1.thePlayer.posZ);
      if (var1.theWorld.isAirBlock(var2)) {
         return true;
      } else {
         return var1.thePlayer.motionY < -0.05D;
      }
   }
}
