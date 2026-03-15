package com.fogclient.ui.component;

import com.fogclient.setting.ModeSetting;
import com.fogclient.setting.Setting;
import net.minecraft.client.gui.Gui;

public class ModeBox extends SettingComponent {
   private ModeSetting modeSetting;

   public ModeBox(Setting var1, ModuleButton var2, int var3) {
      super(var1, var2, var3);
      this.modeSetting = (ModeSetting)var1;
   }

   public void render(int var1, int var2, float var3) {
      Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -15658735);
      mc.fontRendererObj.drawStringWithShadow(this.modeSetting.getName() + ": " + this.modeSetting.getMode(), (float)(this.x + 2), (float)(this.y + 4), -16711936);
   }

   public void mouseClicked(int var1, int var2, int var3) {
      if (this.isHovered(var1, var2, this.x, this.y, this.width, this.height) && var3 == 0) {
         this.modeSetting.cycle();
      }

   }

   public void mouseReleased(int var1, int var2, int var3) {
   }

   public void keyTyped(char var1, int var2) {
   }
}
