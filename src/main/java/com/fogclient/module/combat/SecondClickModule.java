package com.fogclient.module.combat;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.KeySetting;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class SecondClickModule extends Module {
   private final KeySetting leftClickKey = new KeySetting("Left Click Key", this, 0);
   private final KeySetting rightClickKey = new KeySetting("Right Click Key", this, 0);
   private boolean lastLeftDown = false;
   private boolean lastRightDown = false;
   private int leftDownTicks = 0;
   private int rightDownTicks = 0;

   public SecondClickModule() {
      super("DuploClique", "Simulates a double click.", Category.COMBAT);
   }

   public void onEnable() {
      this.lastLeftDown = false;
      this.lastRightDown = false;
      this.leftDownTicks = 0;
      this.rightDownTicks = 0;
   }

   public void onDisable() {
      if (this.leftClickKey.getKeyCode() != 0) {
         this.resetKey(mc.gameSettings.keyBindAttack);
      }

      if (this.rightClickKey.getKeyCode() != 0) {
         this.resetKey(mc.gameSettings.keyBindUseItem);
      }

      this.lastLeftDown = false;
      this.lastRightDown = false;
      this.leftDownTicks = 0;
      this.rightDownTicks = 0;
   }

   public void onTick() {
      if (this.isEnabled()) {
         if (mc.currentScreen == null) {
            int var1 = this.leftClickKey.getKeyCode();
            boolean var3;
            boolean var4;
            if (var1 != 0) {
               boolean var2 = this.isPhysicalKeyDown(var1);
               if (var2) {
                  if (!this.lastLeftDown) {
                     KeyBinding.onTick(mc.gameSettings.keyBindAttack.getKeyCode());
                     this.leftDownTicks = 0;
                  }

                  ++this.leftDownTicks;
               } else {
                  this.leftDownTicks = 0;
               }

               var3 = this.isPhysicalKeyDown(mc.gameSettings.keyBindAttack.getKeyCode());
               var4 = var3 || var2 && this.leftDownTicks > 2;
               KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), var4);
               this.lastLeftDown = var2;
            }

            int var6 = this.rightClickKey.getKeyCode();
            if (var6 != 0) {
               var3 = this.isPhysicalKeyDown(var6);
               if (var3) {
                  if (!this.lastRightDown) {
                     KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
                     this.rightDownTicks = 0;
                  }

                  ++this.rightDownTicks;
               } else {
                  this.rightDownTicks = 0;
               }

               var4 = this.isPhysicalKeyDown(mc.gameSettings.keyBindUseItem.getKeyCode());
               boolean var5 = var4 || var3 && this.rightDownTicks > 2;
               KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), var5);
               this.lastRightDown = var3;
            }

         }
      }
   }

   private boolean isPhysicalKeyDown(int var1) {
      return var1 < 0 ? Mouse.isButtonDown(var1 + 100) : Keyboard.isKeyDown(var1);
   }

   private void resetKey(KeyBinding var1) {
      boolean var2 = this.isPhysicalKeyDown(var1.getKeyCode());
      if (!var2) {
         KeyBinding.setKeyBindState(var1.getKeyCode(), false);
      }

   }
}
