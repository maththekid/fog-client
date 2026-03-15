package com.fogclient.ui.component;

import com.fogclient.module.Module;
import com.fogclient.setting.ActionSetting;
import com.fogclient.setting.BooleanSetting;
import com.fogclient.setting.KeySetting;
import com.fogclient.setting.ModeSetting;
import com.fogclient.setting.NumberSetting;
import com.fogclient.setting.Setting;
import com.fogclient.setting.StringSetting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;

public class ModuleButton extends Component {
   private Module module;
   private Panel parent;
   private int offset;
   private boolean extended;
   private List<SettingComponent> settings = new ArrayList();
   private int x;
   private int y;
   private int width;
   private int height;

   public ModuleButton(Module var1, Panel var2, int var3) {
      this.module = var1;
      this.parent = var2;
      this.offset = var3;
      this.extended = false;
      this.height = 16;
      this.settings.add(new KeybindComponent(var1, this, 0, false));
      if (var1.isAction()) {
         this.settings.add(new KeybindComponent(var1, this, 0, true));
      }

      Iterator var4 = var1.getSettings().iterator();

      while(var4.hasNext()) {
         Setting var5 = (Setting)var4.next();
         if (var5 instanceof BooleanSetting) {
            this.settings.add(new CheckBox(var5, this, 0));
         } else if (var5 instanceof NumberSetting) {
            this.settings.add(new Slider(var5, this, 0));
         } else if (var5 instanceof ModeSetting) {
            this.settings.add(new ModeBox(var5, this, 0));
         } else if (var5 instanceof KeySetting) {
            this.settings.add(new KeySettingComponent((KeySetting)var5, this, 0));
         } else if (var5 instanceof StringSetting) {
            this.settings.add(new StringSettingComponent(var5, this, 0));
         } else if (var5 instanceof ActionSetting) {
            this.settings.add(new ActionSettingComponent(var5, this, 0));
         }
      }

   }

   public void setOffset(int var1) {
      this.offset = var1;
   }

   public void render(int var1, int var2, float var3) {
      this.x = this.parent.getX();
      this.y = this.parent.getY() + this.offset;
      this.width = this.parent.getWidth();
      int var4 = this.module.isEnabled() ? -16711936 : -5592406;
      if (this.isHovered(var1, var2, this.x, this.y, this.width, 16)) {
         Gui.drawRect(this.x, this.y, this.x + this.width, this.y + 16, -12303292);
      } else {
         Gui.drawRect(this.x, this.y, this.x + this.width, this.y + 16, -14540254);
      }

      mc.fontRendererObj.drawStringWithShadow(this.module.getName(), (float)(this.x + 4), (float)(this.y + 4), var4);
      if (this.extended) {
         int var5 = 16;

         SettingComponent var7;
         for(Iterator var6 = this.settings.iterator(); var6.hasNext(); var5 += var7.getHeight()) {
            var7 = (SettingComponent)var6.next();
            var7.setOffset(var5);
            var7.updatePosition(this.x, this.y, this.width);
            var7.render(var1, var2, var3);
         }
      }

   }

   public void mouseClicked(int var1, int var2, int var3) {
      if (this.isHovered(var1, var2, this.x, this.y, this.width, 16)) {
         if (var3 == 0) {
            this.module.toggle();
         } else if (var3 == 1) {
            this.extended = !this.extended;
         }

      } else {
         if (this.extended) {
            Iterator var4 = this.settings.iterator();

            while(var4.hasNext()) {
               SettingComponent var5 = (SettingComponent)var4.next();
               var5.mouseClicked(var1, var2, var3);
            }
         }

      }
   }

   public void mouseReleased(int var1, int var2, int var3) {
      if (this.extended) {
         Iterator var4 = this.settings.iterator();

         while(var4.hasNext()) {
            SettingComponent var5 = (SettingComponent)var4.next();
            var5.mouseReleased(var1, var2, var3);
         }
      }

   }

   public void keyTyped(char var1, int var2) {
      if (this.extended) {
         Iterator var3 = this.settings.iterator();

         while(var3.hasNext()) {
            SettingComponent var4 = (SettingComponent)var3.next();
            var4.keyTyped(var1, var2);
         }
      }

   }

   public int getHeight() {
      if (!this.extended) {
         return 16;
      } else {
         int var1 = 16;

         SettingComponent var3;
         for(Iterator var2 = this.settings.iterator(); var2.hasNext(); var1 += var3.getHeight()) {
            var3 = (SettingComponent)var2.next();
         }

         return var1;
      }
   }
}
