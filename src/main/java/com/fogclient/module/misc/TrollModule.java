package com.fogclient.module.misc;

import com.fogclient.module.Category;
import com.fogclient.module.Module;

public class TrollModule extends Module {
   public TrollModule() {
      super("Troll", "Crasha o jogo instantaneamente.", Category.CLIENT);
   }

   public void onEnable() {
      if (mc.thePlayer != null) {
         throw new RuntimeException("Troll Module Activated! Game Crashed.");
      }
   }

   public void onDisable() {
   }
}
