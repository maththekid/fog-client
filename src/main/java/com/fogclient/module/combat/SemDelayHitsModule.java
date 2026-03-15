package com.fogclient.module.combat;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class SemDelayHitsModule extends Module {
   private Field leftClickCounterField;

   public SemDelayHitsModule() {
      super("HitInfinito", "Remove o delay de hit (1.8).", Category.COMBAT);

      try {
         this.leftClickCounterField = ReflectionHelper.findField(Minecraft.class, new String[]{"field_71429_W", "leftClickCounter"});
         this.leftClickCounterField.setAccessible(true);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onTick() {
      if (this.isEnabled()) {
         if (mc.thePlayer != null) {
            try {
               if (this.leftClickCounterField != null) {
                  this.leftClickCounterField.setInt(mc, 0);
               }
            } catch (Exception var2) {
            }

         }
      }
   }
}
