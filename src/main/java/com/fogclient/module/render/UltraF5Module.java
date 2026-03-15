package com.fogclient.module.render;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.NumberSetting;
import java.lang.reflect.Field;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class UltraF5Module extends Module {
   private final NumberSetting distance = new NumberSetting("DistÃ¢ncia", this, 10.0D, 2.0D, 100.0D, 1.0D);
   private Field thirdPersonDistanceField;
   private Field thirdPersonDistanceTempField;
   private float defaultDistance = 4.0F;

   public UltraF5Module() {
      super("UltraF5", "Aumenta a distÃ¢ncia da cÃ¢mera em terceira pessoa (F5).", Category.RENDER);

      try {
         this.thirdPersonDistanceField = ReflectionHelper.findField(EntityRenderer.class, new String[]{"field_78490_B", "thirdPersonDistance"});
         this.thirdPersonDistanceField.setAccessible(true);
         this.thirdPersonDistanceTempField = ReflectionHelper.findField(EntityRenderer.class, new String[]{"field_78491_C", "thirdPersonDistanceTemp"});
         this.thirdPersonDistanceTempField.setAccessible(true);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void onEnable() {
   }

   public void onDisable() {
      if (mc.entityRenderer != null) {
         try {
            if (this.thirdPersonDistanceField != null) {
               this.thirdPersonDistanceField.setFloat(mc.entityRenderer, this.defaultDistance);
            }

            if (this.thirdPersonDistanceTempField != null) {
               this.thirdPersonDistanceTempField.setFloat(mc.entityRenderer, this.defaultDistance);
            }
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

   }

   public void onTick() {
      if (this.isEnabled()) {
         if (mc.entityRenderer != null) {
            float var1 = (float)this.distance.getValue();

            try {
               if (this.thirdPersonDistanceField != null) {
                  this.thirdPersonDistanceField.setFloat(mc.entityRenderer, var1);
               }

               if (this.thirdPersonDistanceTempField != null) {
                  this.thirdPersonDistanceTempField.setFloat(mc.entityRenderer, var1);
               }
            } catch (Exception var3) {
               var3.printStackTrace();
            }

         }
      }
   }
}
