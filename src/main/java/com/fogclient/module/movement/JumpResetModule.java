package com.fogclient.module.movement;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import java.lang.reflect.Method;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class JumpResetModule extends Module {
   private Method jumpMethod;
   private boolean hasJumped = false;

   public JumpResetModule() {
      super("PuloDoGato0KB", "Reduces knockback by jumping.", Category.MOVEMENT);

      try {
		  this.jumpMethod = ReflectionHelper.findMethod(EntityLivingBase.class, null, new String[]{"func_70664_aZ", "jump"});
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void onEnable() {
      this.hasJumped = false;
   }

   public void onDisable() {
   }

   public void onTick() {
      if (mc.thePlayer != null && mc.theWorld != null) {
         if (mc.thePlayer.hurtTime == 0) {
            this.hasJumped = false;
         } else if (!this.hasJumped) {
            if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.onGround && mc.gameSettings.keyBindAttack.isKeyDown()) {
               try {
                  if (this.jumpMethod != null) {
                     this.jumpMethod.invoke(mc.thePlayer);
                     this.hasJumped = true;
                  }
               } catch (Exception var2) {
                  var2.printStackTrace();
               }
            }

         }
      }
   }
}
