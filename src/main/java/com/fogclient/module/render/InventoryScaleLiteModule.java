package com.fogclient.module.render;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.NumberSetting;
import com.fogclient.ui.ClickGUI;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class InventoryScaleLiteModule extends Module {
   private final NumberSetting normalScale = new NumberSetting("Normal Scale", this, 2.0D, 0.0D, 3.0D, 1.0D);
   private final NumberSetting inventoryScale = new NumberSetting("Inv Scale", this, 2.0D, 0.0D, 3.0D, 1.0D);

   public InventoryScaleLiteModule() {
      super("EscalaCustomizada", "Altera a escala da GUI no inventÃ¡rio", Category.RENDER);
   }

   public void onEnable() {
   }

   public void onDisable() {
      mc.gameSettings.guiScale = (int)this.normalScale.getValue();
   }

   public void onEvent(Event var1) {
      if (var1 instanceof GuiOpenEvent) {
         this.onGuiOpen((GuiOpenEvent)var1);
      }

   }

   private void onGuiOpen(GuiOpenEvent var1) {
      if (this.isEnabled()) {
         if (var1.gui instanceof GuiContainer) {
            mc.gameSettings.guiScale = (int)this.inventoryScale.getValue();
         } else if (var1.gui == null || var1.gui instanceof ClickGUI) {
            mc.gameSettings.guiScale = (int)this.normalScale.getValue();
         }

      }
   }
}
