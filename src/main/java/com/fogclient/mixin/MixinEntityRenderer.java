package com.fogclient.mixin;

import com.fogclient.module.ModuleManager;
import com.fogclient.module.combat.GhostReachModule;
import com.fogclient.module.combat.ghostreach.GhostReachCore;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EntityRenderer.class})
public abstract class MixinEntityRenderer {
   @Inject(
      method = {"getMouseOver"},
      at = {@At("HEAD")},
      remap = false
   )
   private void onGetMouseOver(float var1, CallbackInfo var2) {
      GhostReachModule var3 = (GhostReachModule)ModuleManager.getInstance().getModule(GhostReachModule.class);
      if (var3 != null && var3.isEnabled()) {
         GhostReachCore var4 = GhostReachCore.INSTANCE;
         Entity var5 = var4.findTarget(GhostReachCore.maxReach);
         if (var5 != null && var4.checkConditions(var5)) {
            var4.targetEntityId = var5.getEntityId();
         } else {
            var4.targetEntityId = -1;
         }
      }

   }
}
