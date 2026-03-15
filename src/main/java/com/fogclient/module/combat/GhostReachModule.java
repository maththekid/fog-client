package com.fogclient.module.combat;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.module.combat.ghostreach.GhostReachCore;
import com.fogclient.setting.NumberSetting;
import net.minecraft.entity.Entity;

public class GhostReachModule extends Module {
   private final NumberSetting minReach = new NumberSetting("Min Reach", this, 3.0D, 3.0D, 6.0D, 0.1D);
   private final NumberSetting maxReach = new NumberSetting("Max Reach", this, 4.0D, 3.0D, 6.0D, 0.1D);
   private final NumberSetting chance = new NumberSetting("Chance %", this, 100.0D, 0.0D, 100.0D, 1.0D);

   public GhostReachModule() {
      super("ReachDiferenciado", "Extends attack range legitimately.", Category.COMBAT);
   }

   public void onEnable() {
      this.updateSettings();
   }

   public void onDisable() {
      GhostReachCore.INSTANCE.targetEntityId = -1;
   }

   public void onTickStart() {
      if (this.isEnabled()) {
         this.updateSettings();
         GhostReachCore var1 = GhostReachCore.INSTANCE;
         Entity var2 = var1.findTarget(GhostReachCore.maxReach);
         if (var2 != null && var1.checkConditions(var2)) {
            var1.targetEntityId = var2.getEntityId();
         } else {
            var1.targetEntityId = -1;
         }

      }
   }

   private void updateSettings() {
      GhostReachCore.minReach = this.minReach.getValue();
      GhostReachCore.maxReach = this.maxReach.getValue();
      GhostReachCore.chance = (float)this.chance.getValue();
   }
}
