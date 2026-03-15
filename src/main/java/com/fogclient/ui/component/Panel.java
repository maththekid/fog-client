package com.fogclient.ui.component;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.module.ModuleManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;

public class Panel extends Component {
   private String title;
   private int x;
   private int y;
   private int width;
   private int height;
   private boolean dragging;
   private int dragX;
   private int dragY;
   private boolean open;
   private List<ModuleButton> components = new ArrayList();

   public Panel(String var1, int var2, int var3, int var4, int var5, Category var6) {
      this.title = var1;
      this.x = var2;
      this.y = var3;
      this.width = var4;
      this.height = var5;
      this.open = true;
      this.dragging = false;
      int var7 = var5;

      for(Iterator var8 = ModuleManager.getInstance().getModulesByCategory(var6).iterator(); var8.hasNext(); var7 += 16) {
         Module var9 = (Module)var8.next();
         ModuleButton var10 = new ModuleButton(var9, this, var7);
         this.components.add(var10);
      }

   }

   public void render(int var1, int var2, float var3) {
      if (this.dragging) {
         this.x = var1 - this.dragX;
         this.y = var2 - this.dragY;
      }

      Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -14540254);
      mc.fontRendererObj.drawStringWithShadow(this.title, (float)(this.x + (this.width - mc.fontRendererObj.getStringWidth(this.title)) / 2), (float)(this.y + this.height / 2 - 4), -16711936);
      if (this.open) {
         int var4 = this.height;

         ModuleButton var6;
         for(Iterator var5 = this.components.iterator(); var5.hasNext(); var4 += var6.getHeight()) {
            var6 = (ModuleButton)var5.next();
            var6.setOffset(var4);
            var6.render(var1, var2, var3);
         }

         Gui.drawRect(this.x, this.y + var4, this.x + this.width, this.y + var4 + 2, -15658735);
      }

   }

   public void mouseClicked(int var1, int var2, int var3) {
      if (this.isHovered(var1, var2, this.x, this.y, this.width, this.height)) {
         if (var3 == 0) {
            this.dragging = true;
            this.dragX = var1 - this.x;
            this.dragY = var2 - this.y;
         } else if (var3 == 1) {
            this.open = !this.open;
         }

      } else {
         if (this.open) {
            int var4 = this.height;

            ModuleButton var6;
            for(Iterator var5 = this.components.iterator(); var5.hasNext(); var4 += var6.getHeight()) {
               var6 = (ModuleButton)var5.next();
               var6.setOffset(var4);
               var6.mouseClicked(var1, var2, var3);
            }
         }

      }
   }

   public void mouseReleased(int var1, int var2, int var3) {
      this.dragging = false;
      if (this.open) {
         Iterator var4 = this.components.iterator();

         while(var4.hasNext()) {
            ModuleButton var5 = (ModuleButton)var4.next();
            var5.mouseReleased(var1, var2, var3);
         }
      }

   }

   public void keyTyped(char var1, int var2) {
      if (this.open) {
         Iterator var3 = this.components.iterator();

         while(var3.hasNext()) {
            ModuleButton var4 = (ModuleButton)var3.next();
            var4.keyTyped(var1, var2);
         }
      }

   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public void setX(int var1) {
      this.x = var1;
   }

   public void setY(int var1) {
      this.y = var1;
   }

   public int getWidth() {
      return this.width;
   }

   public boolean isOpen() {
      return this.open;
   }

   public void setOpen(boolean var1) {
      this.open = var1;
   }

   public String getTitle() {
      return this.title;
   }
}
