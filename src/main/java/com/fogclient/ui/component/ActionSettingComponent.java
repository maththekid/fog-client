package com.fogclient.ui.component;

import com.fogclient.setting.ActionSetting;
import com.fogclient.setting.Setting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class ActionSettingComponent extends SettingComponent {
   private ActionSetting actionSetting;

   public ActionSettingComponent(Setting var1, ModuleButton var2, int var3) {
      super(var1, var2, var3);
      this.actionSetting = (ActionSetting)var1;
      this.height = 16;
   }

   public void render(int var1, int var2, float var3) {
      Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -15658735);
      boolean var4 = this.isHovered(var1, var2, this.x + 2, this.y + 2, this.width - 4, this.height - 4);
      int var5 = var4 ? -12303292 : -14540254;
      Gui.drawRect(this.x + 2, this.y + 2, this.x + this.width - 2, this.y + this.height - 2, var5);
      String var6 = this.actionSetting.getName();
      int var7 = mc.fontRendererObj.getStringWidth(var6);
      mc.fontRendererObj.drawStringWithShadow(var6, (float)(this.x + this.width / 2 - var7 / 2), (float)(this.y + 4), -16711936);
   }

   public void mouseClicked(int var1, int var2, int var3) {
      if (this.isHovered(var1, var2, this.x + 2, this.y + 2, this.width - 4, this.height - 4) && var3 == 0) {
         mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
         this.actionSetting.perform();
      }

   }

   public void mouseReleased(int var1, int var2, int var3) {
   }

   public void keyTyped(char var1, int var2) {
   }
}
