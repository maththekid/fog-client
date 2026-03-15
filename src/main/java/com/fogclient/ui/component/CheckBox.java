package com.fogclient.ui.component;

import com.fogclient.setting.BooleanSetting;
import com.fogclient.setting.Setting;
import net.minecraft.client.gui.Gui;

public class CheckBox extends SettingComponent {
   private BooleanSetting boolSetting;

   public CheckBox(Setting var1, ModuleButton var2, int var3) {
      super(var1, var2, var3);
      this.boolSetting = (BooleanSetting)var1;
   }

   public void render(int var1, int var2, float var3) {
      Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -15658735);
      Gui.drawRect(this.x + this.width - 15, this.y + 1, this.x + this.width - 1, this.y + 15, -11184811);
      Gui.drawRect(this.x + this.width - 14, this.y + 2, this.x + this.width - 2, this.y + 14, -16777216);
      if (this.boolSetting.isEnabled()) {
         Gui.drawRect(this.x + this.width - 12, this.y + 4, this.x + this.width - 4, this.y + 12, -16711936);
      }

      mc.fontRendererObj.drawStringWithShadow(this.boolSetting.getName(), (float)(this.x + 2), (float)(this.y + 4), -16711936);
   }

   public void mouseClicked(int var1, int var2, int var3) {
      if (this.isHovered(var1, var2, this.x + this.width - 14, this.y + 2, 12, 12) && var3 == 0) {
         this.boolSetting.toggle();
      }

   }

   public void mouseReleased(int var1, int var2, int var3) {
   }

   public void keyTyped(char var1, int var2) {
   }
}
