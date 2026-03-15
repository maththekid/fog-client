package com.fogclient.ui.component;

import net.minecraft.client.Minecraft;

public abstract class Component {
   protected static final Minecraft mc = Minecraft.getMinecraft();

   public abstract void render(int var1, int var2, float var3);

   public abstract void mouseClicked(int var1, int var2, int var3);

   public abstract void mouseReleased(int var1, int var2, int var3);

   public abstract void keyTyped(char var1, int var2);

   public boolean isHovered(int var1, int var2, int var3, int var4, int var5, int var6) {
      return var1 >= var3 && var1 <= var3 + var5 && var2 >= var4 && var2 <= var4 + var6;
   }
}
