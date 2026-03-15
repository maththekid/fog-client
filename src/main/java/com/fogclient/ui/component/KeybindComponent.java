package com.fogclient.ui.component;

import com.fogclient.module.Module;
import com.fogclient.setting.Setting;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

public class KeybindComponent extends SettingComponent {
   private boolean binding;
   private Module module;
   private boolean isActionBind;

   public KeybindComponent(Module var1, ModuleButton var2, int var3, boolean var4) {
      super((Setting)null, var2, var3);
      this.module = var1;
      this.binding = false;
      this.isActionBind = var4;
   }

   public void render(int var1, int var2, float var3) {
      Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -15658735);
      boolean var4 = this.isHovered(var1, var2, this.x + 2, this.y + 2, this.width - 4, this.height - 4);
      int var5 = var4 ? -11184811 : -13421773;
      Gui.drawRect(this.x + 2, this.y + 2, this.x + this.width - 2, this.y + this.height - 2, var5);
      int var6 = this.isActionBind ? this.module.getActionKeybind() : this.module.getKeybind();
      String var7 = this.getKeyName(var6);
      String var8 = this.isActionBind ? "Action: " : "Bind: ";
      String var9 = this.binding ? "Press Key/Mouse..." : var8 + var7;
      int var10 = mc.fontRendererObj.getStringWidth(var9);
      mc.fontRendererObj.drawStringWithShadow(var9, (float)(this.x + (this.width - var10) / 2), (float)(this.y + (this.height - 8) / 2), this.binding ? -43691 : -16711936);
   }

   public void mouseClicked(int var1, int var2, int var3) {
      int var4;
      if (this.isHovered(var1, var2, this.x + 2, this.y + 2, this.width - 4, this.height - 4)) {
         if (var3 == 0) {
            this.binding = !this.binding;
         } else if (this.binding) {
            var4 = -100 + var3;
            if (this.isActionBind) {
               this.module.setActionKeybind(var4);
            } else {
               this.module.setKeybind(var4);
            }

            this.binding = false;
         }
      } else if (this.binding) {
         var4 = -100 + var3;
         if (this.isActionBind) {
            this.module.setActionKeybind(var4);
         } else {
            this.module.setKeybind(var4);
         }

         this.binding = false;
      }

   }

   public void keyTyped(char var1, int var2) {
      if (this.binding) {
         int var3 = var2 != 1 && var2 != 211 ? var2 : 0;
         if (this.isActionBind) {
            this.module.setActionKeybind(var3);
         } else {
            this.module.setKeybind(var3);
         }

         this.binding = false;
      }

   }

   public void mouseReleased(int var1, int var2, int var3) {
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
