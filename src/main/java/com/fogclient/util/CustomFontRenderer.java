package com.fogclient.util;

import com.fogclient.module.render.EscolheNickModule;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class CustomFontRenderer extends FontRenderer {
   public CustomFontRenderer(GameSettings var1, ResourceLocation var2, TextureManager var3, boolean var4) {
      super(var1, var2, var3, var4);
   }

   public int drawString(String var1, float var2, float var3, int var4, boolean var5) {
      return super.drawString(this.replaceNick(var1), var2, var3, var4, var5);
   }

   public int getStringWidth(String var1) {
      return super.getStringWidth(this.replaceNick(var1));
   }

   public void drawSplitString(String var1, int var2, int var3, int var4, int var5) {
      super.drawSplitString(this.replaceNick(var1), var2, var3, var4, var5);
   }

   public List<String> listFormattedStringToWidth(String var1, int var2) {
      return super.listFormattedStringToWidth(this.replaceNick(var1), var2);
   }

   private String replaceNick(String var1) {
      EscolheNickModule var2 = EscolheNickModule.getInstance();
      if (var2 != null && var2.isEnabled() && !EscolheNickModule.nickMap.isEmpty()) {
         if (var1 == null) {
            return null;
         } else {
            Iterator var3 = EscolheNickModule.nickMap.entrySet().iterator();

            while(var3.hasNext()) {
               Entry var4 = (Entry)var3.next();
               if (var1.contains((CharSequence)var4.getKey())) {
                  var1 = var1.replace((CharSequence)var4.getKey(), (CharSequence)var4.getValue());
               }
            }

            return var1;
         }
      } else {
         return var1;
      }
   }
}
