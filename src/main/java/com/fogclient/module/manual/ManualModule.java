package com.fogclient.module.manual;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.ui.GuiManual;

public class ManualModule extends Module {
   public ManualModule() {
      super("Abrir Manual", "Abre o manual do usuÃ¡rio in-game.", Category.MANUAL);
   }

   public void onEnable() {
      if (mc.thePlayer != null) {
         mc.displayGuiScreen(new GuiManual());
      }

      this.toggle();
   }

   public void onDisable() {
   }
}
