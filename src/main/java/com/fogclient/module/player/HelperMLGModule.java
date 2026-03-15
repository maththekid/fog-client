package com.fogclient.module.player;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class HelperMLGModule extends Module {
   private Method rightClickMethod;
   private boolean executing = false;

   public HelperMLGModule() {
      super("HelperMLG", "Place water automatically when falling.", Category.PLAYER);

      try {
         this.rightClickMethod = ReflectionHelper.findMethod(Minecraft.class, mc, new String[]{"func_147121_ag", "rightClickMouse"}, new Class[0]);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void onEnable() {
      this.executing = false;
   }

   public void onDisable() {
   }

   public void onTick() {
      if (mc.thePlayer != null && mc.theWorld != null) {
         if (this.isEnabled()) {
            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() == Items.water_bucket && mc.thePlayer.fallDistance > 1.5F) {
               MovingObjectPosition var1 = mc.objectMouseOver;
               if (var1 != null && var1.typeOfHit == MovingObjectType.BLOCK) {
                  double var2 = mc.thePlayer.posY - (double)(var1.getBlockPos().getY() + 1);
                  if (var2 > 0.0D && var2 < 2.0D && !this.executing) {
                     this.forceClick();
                     this.executing = true;
                  }
               }
            } else if (mc.thePlayer.onGround) {
               this.executing = false;
            }

         }
      }
   }

   private void forceClick() {
      try {
         if (this.rightClickMethod != null) {
            this.rightClickMethod.invoke(mc);
         } else {
            int var1 = mc.gameSettings.keyBindUseItem.getKeyCode();
            KeyBinding.onTick(var1);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
