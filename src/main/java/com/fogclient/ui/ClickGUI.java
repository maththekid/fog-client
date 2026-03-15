package com.fogclient.ui;

import com.fogclient.config.ConfigManager;
import com.fogclient.module.Category;
import com.fogclient.ui.component.Panel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends GuiScreen {
   private List<Panel> panels = new ArrayList();

   public ClickGUI() {
      int var1 = 10;
      Category[] var2 = Category.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Category var5 = var2[var4];
         this.panels.add(new Panel(var5.getName(), var1, 10, 100, 16, var5));
         var1 += 110;
      }

   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      super.initGui();
      Map var1 = ConfigManager.loadGUIConfig();
      byte var2 = 10;
      byte var3 = 10;
      int var4 = var2;
      int var5 = var3;
      byte var6 = 100;
      byte var7 = 10;
      Iterator var8 = this.panels.iterator();

      while(var8.hasNext()) {
         Panel var9 = (Panel)var8.next();
         String var10 = var9.getTitle();
         if (var1.containsKey(var10)) {
            ConfigManager.PanelConfig var11 = (ConfigManager.PanelConfig)var1.get(var10);
            var9.setX(var11.x);
            var9.setY(var11.y);
            var9.setOpen(var11.open);
         } else {
            boolean var12 = this.panels.size() * (var6 + var7) + var2 > this.width;
            if (var12) {
               if (var4 + var6 > this.width) {
                  var4 = var2;
                  var5 += 120;
               }

               var9.setX(var4);
               var9.setY(var5);
               var4 += var6 + var7;
            }
         }
      }

   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      HashMap var1 = new HashMap();
      Iterator var2 = this.panels.iterator();

      while(var2.hasNext()) {
         Panel var3 = (Panel)var2.next();
         var1.put(var3.getTitle(), var3);
      }

      ConfigManager.saveGUIConfig(var1);
      ConfigManager.saveConfig();
      super.onGuiClosed();
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      Iterator var4 = this.panels.iterator();

      while(var4.hasNext()) {
         Panel var5 = (Panel)var4.next();
         var5.render(var1, var2, var3);
      }

      GlStateManager.pushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      ResourceLocation var9 = new ResourceLocation("fogclient", "textures/gui/logo.png");
      this.mc.getTextureManager().bindTexture(var9);
      short var10 = 128;
      short var6 = 128;
      byte var7 = 4;
      int var8 = this.height - var6 - 4;
      Gui.drawModalRectWithCustomSizedTexture(var7, var8, 0.0F, 0.0F, var10, var6, (float)var10, (float)var6);
      GlStateManager.disableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
      super.drawScreen(var1, var2, var3);
   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      Iterator var4 = this.panels.iterator();

      while(var4.hasNext()) {
         Panel var5 = (Panel)var4.next();
         var5.mouseClicked(var1, var2, var3);
      }

      super.mouseClicked(var1, var2, var3);
   }

   protected void mouseReleased(int var1, int var2, int var3) {
      Iterator var4 = this.panels.iterator();

      while(var4.hasNext()) {
         Panel var5 = (Panel)var4.next();
         var5.mouseReleased(var1, var2, var3);
      }

      super.mouseReleased(var1, var2, var3);
   }

   protected void keyTyped(char var1, int var2) throws IOException {
      Iterator var3 = this.panels.iterator();

      while(var3.hasNext()) {
         Panel var4 = (Panel)var3.next();
         var4.keyTyped(var1, var2);
      }

      super.keyTyped(var1, var2);
   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
