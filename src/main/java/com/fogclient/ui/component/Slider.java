package com.fogclient.ui.component;

import com.fogclient.setting.NumberSetting;
import com.fogclient.setting.Setting;
import net.minecraft.client.gui.Gui;

public class Slider extends SettingComponent {
   private NumberSetting numSetting;
   private boolean dragging = false;

   public Slider(Setting var1, ModuleButton var2, int var3) {
      super(var1, var2, var3);
      this.numSetting = (NumberSetting)var1;
   }

   public void render(int var1, int var2, float var3) {
      Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -15658735);
      double var4 = this.numSetting.getMin();
      double var6 = this.numSetting.getMax();
      double var8 = this.numSetting.getValue();
      double var10 = (var8 - var4) / (var6 - var4);
      int var12 = (int)((double)(this.width - 4) * var10);
      Gui.drawRect(this.x + 2, this.y + 12, this.x + this.width - 2, this.y + 14, -11184811);
      Gui.drawRect(this.x + 2, this.y + 12, this.x + 2 + var12, this.y + 14, -16711936);
      String var13 = String.format("%.1f", var8);
      if (this.numSetting.getIncrement() == 1.0D) {
         var13 = String.format("%d", (int)var8);
      }

      mc.fontRendererObj.drawStringWithShadow(this.numSetting.getName() + ": " + var13, (float)(this.x + 2), (float)(this.y + 2), -16711936);
      if (this.dragging) {
         double var14 = (double)Math.min(this.width - 4, Math.max(0, var1 - (this.x + 2)));
         double var16 = var14 / (double)(this.width - 4);
         double var18 = var4 + (var6 - var4) * var16;
         this.numSetting.setValue(var18);
      }

   }

   public void mouseClicked(int var1, int var2, int var3) {
      if (this.isHovered(var1, var2, this.x + 2, this.y + 10, this.width - 4, 6) && var3 == 0) {
         this.dragging = true;
      } else if (this.isHovered(var1, var2, this.x, this.y, this.width, this.height) && var3 == 0) {
         this.dragging = true;
      }

   }

   public void mouseReleased(int var1, int var2, int var3) {
      this.dragging = false;
   }

   public void keyTyped(char var1, int var2) {
   }
}
