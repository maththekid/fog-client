package com.fogclient.ui.component;

import com.fogclient.setting.KeySetting;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

public class KeySettingComponent extends SettingComponent {
   private KeySetting setting;
   private ModuleButton parent;
   private int offset;
   private int x;
   private int y;
   private int width;
   private int height;
   private boolean binding;

   public KeySettingComponent(KeySetting var1, ModuleButton var2, int var3) {
      super(var1, var2, var3);
      this.setting = var1;
      this.parent = var2;
      this.offset = var3;
      this.height = 16;
      this.binding = false;
   }

   public void setOffset(int var1) {
      this.offset = var1;
   }

   public void updatePosition(int var1, int var2, int var3) {
      this.x = var1;
      this.y = var2 + this.offset;
      this.width = var3;
   }

   public void render(int var1, int var2, float var3) {
      Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -13421773);
      String var4 = this.getKeyName(this.setting.getKeyCode());
      String var5 = this.binding ? "Press Key/Mouse..." : this.setting.getName() + ": " + var4;
      mc.fontRendererObj.drawStringWithShadow(var5, (float)(this.x + 4), (float)(this.y + 4), this.binding ? -65536 : -1);
   }

   public void mouseClicked(int var1, int var2, int var3) {
      int var4;
      if (this.isHovered(var1, var2, this.x, this.y, this.width, this.height)) {
         if (var3 == 0) {
            this.binding = !this.binding;
         } else if (var3 == 1) {
            this.setting.setKeyCode(0);
            this.binding = false;
         } else if (this.binding) {
            var4 = -100 + var3;
            this.setting.setKeyCode(var4);
            this.binding = false;
         }
      } else if (this.binding) {
         var4 = -100 + var3;
         this.setting.setKeyCode(var4);
         this.binding = false;
      }

   }

   public void mouseReleased(int var1, int var2, int var3) {
   }

   public void keyTyped(char var1, int var2) {
      if (this.binding) {
         if (var2 != 1 && var2 != 211) {
            this.setting.setKeyCode(var2);
         } else {
            this.setting.setKeyCode(0);
         }

         this.binding = false;
      }

   }

   public int getHeight() {
      return this.height;
   }

   private String getKeyName(int var1) {
      if (var1 == 0) {
         return "NONE";
      } else if (var1 < 0) {
         int var3 = var1 + 100;
         switch(var3) {
         case 0:
            return "Mouse Left";
         case 1:
            return "Mouse Right";
         case 2:
            return "Mouse Middle";
         case 3:
            return "Mouse 4";
         case 4:
            return "Mouse 5";
         default:
            return "Mouse " + var3;
         }
      } else {
         String var2 = Keyboard.getKeyName(var1);
         return var2 != null ? var2 : "KEY_" + var1;
      }
   }
}
