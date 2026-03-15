package com.fogclient.ui.component;

import com.fogclient.setting.Setting;
import com.fogclient.setting.StringSetting;
import net.minecraft.client.gui.Gui;

public class StringSettingComponent extends SettingComponent {
   private StringSetting stringSetting;
   private boolean isFocused;
   private int cursorTimer;

   public StringSettingComponent(Setting var1, ModuleButton var2, int var3) {
      super(var1, var2, var3);
      this.stringSetting = (StringSetting)var1;
   }

   public int getHeight() {
      return 30;
   }

   public void render(int var1, int var2, float var3) {
      Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -15658735);
      mc.fontRendererObj.drawStringWithShadow(this.stringSetting.getName(), (float)(this.x + 2), (float)(this.y + 2), -16711936);
      int var4 = this.x + 2;
      int var5 = this.y + 14;
      int var6 = this.width - 4;
      byte var7 = 12;
      Gui.drawRect(var4, var5, var4 + var6, var5 + var7, -16777216);
      String var8 = this.stringSetting.getText();
      boolean var9 = this.isFocused && System.currentTimeMillis() / 500L % 2L == 0L;
      String var10 = var8 + (var9 ? "_" : "");
      if (mc.fontRendererObj.getStringWidth(var10) > var6 - 4) {
         String var11;
         for(var11 = var10; mc.fontRendererObj.getStringWidth(var11) > var6 - 4 && var11.length() > 0; var11 = var11.substring(1)) {
         }

         var10 = var11;
      }

      mc.fontRendererObj.drawStringWithShadow(var10, (float)(var4 + 2), (float)(var5 + 2), -1);
   }

   public void mouseClicked(int var1, int var2, int var3) {
      if (this.isHovered(var1, var2, this.x, this.y, this.width, this.height)) {
         this.isFocused = true;
      } else {
         this.isFocused = false;
      }

   }

   public void mouseReleased(int var1, int var2, int var3) {
   }

   public void keyTyped(char var1, int var2) {
      if (this.isFocused) {
         String var3;
         if (var2 == 14) {
            var3 = this.stringSetting.getText();
            if (var3.length() > 0) {
               this.stringSetting.setText(var3.substring(0, var3.length() - 1));
               this.stringSetting.setValue(this.stringSetting.getText());
            }
         } else if (var2 == 28) {
            this.isFocused = false;
         } else if (this.isAllowedCharacter(var1)) {
            var3 = this.stringSetting.getText();
            this.stringSetting.setText(var3 + var1);
            this.stringSetting.setValue(this.stringSetting.getText());
         }
      }

   }

   private boolean isAllowedCharacter(char var1) {
      return var1 >= ' ' && var1 != 127;
   }
}
