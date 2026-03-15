package com.fogclient.module.render;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.ui.ClickGUI;

public class ClickGUIModule extends Module {
   public ClickGUIModule() {
      super("Menu Principal", "Displays the click GUI.", Category.CLIENT);
      this.keybind = 54;
   }

   public void onDisable() {
   }

   public void onEnable() {
      if (mc.currentScreen == null) {
         mc.displayGuiScreen(new ClickGUI());
      }

      this.toggle();
   }
}
