package com.fogclient.module.render;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.lwjgl.opengl.GL11;

public class DestakBauModule extends Module {
   public DestakBauModule() {
      super("EncontraBau", "Mostra baÃºs e ender chests atravÃ©s das paredes.", Category.RENDER);
      this.keybind = 48;
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onEvent(Event var1) {
      if (var1 instanceof RenderWorldLastEvent) {
         this.onRenderWorldLast((RenderWorldLastEvent)var1);
      }

   }

   private void onRenderWorldLast(RenderWorldLastEvent var1) {
      if (this.isEnabled()) {
         Minecraft var2 = Minecraft.getMinecraft();
         if (var2.theWorld != null && var2.thePlayer != null) {
            GlStateManager.pushMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.disableDepth();
            GlStateManager.disableCull();
            Iterator var3 = var2.theWorld.loadedTileEntityList.iterator();

            while(true) {
               TileEntity var4;
               do {
                  if (!var3.hasNext()) {
                     GlStateManager.enableCull();
                     GlStateManager.enableDepth();
                     GlStateManager.disableBlend();
                     GlStateManager.enableLighting();
                     GlStateManager.enableTexture2D();
                     GlStateManager.popMatrix();
                     return;
                  }

                  var4 = (TileEntity)var3.next();
               } while(!(var4 instanceof TileEntityChest) && !(var4 instanceof TileEntityEnderChest));

               this.renderChestESP(var4, var1.partialTicks);
            }
         }
      }
   }

   private void renderChestESP(TileEntity var1, float var2) {
      Minecraft var3 = Minecraft.getMinecraft();
      double var4 = var3.getRenderManager().viewerPosX;
      double var6 = var3.getRenderManager().viewerPosY;
      double var8 = var3.getRenderManager().viewerPosZ;
      BlockPos var10 = var1.getPos();
      double var11 = (double)var10.getX() - var4;
      double var13 = (double)var10.getY() - var6;
      double var15 = (double)var10.getZ() - var8;
      float var17 = 1.0F;
      float var18 = 0.8F;
      float var19 = 0.0F;
      float var20 = 0.5F;
      GL11.glLineWidth(2.0F);
      GlStateManager.color(var17, var18, var19, 1.0F);
      AxisAlignedBB var21 = new AxisAlignedBB(var11, var13, var15, var11 + 1.0D, var13 + 1.0D, var15 + 1.0D);
      RenderGlobal.drawSelectionBoundingBox(var21);
      GlStateManager.color(var17, var18, var19, 0.2F);
      this.drawFilledBox(var21);
      this.renderBeam(var11 + 0.5D, var13 + 0.5D, var15 + 0.5D, var17, var18, var19);
      GL11.glLineWidth(1.0F);
   }

   private void drawFilledBox(AxisAlignedBB var1) {
      Tessellator var2 = Tessellator.getInstance();
      WorldRenderer var3 = var2.getWorldRenderer();
      var3.begin(7, DefaultVertexFormats.POSITION);
      var3.pos(var1.minX, var1.minY, var1.minZ).endVertex();
      var3.pos(var1.maxX, var1.minY, var1.minZ).endVertex();
      var3.pos(var1.maxX, var1.minY, var1.maxZ).endVertex();
      var3.pos(var1.minX, var1.minY, var1.maxZ).endVertex();
      var3.pos(var1.minX, var1.maxY, var1.minZ).endVertex();
      var3.pos(var1.minX, var1.maxY, var1.maxZ).endVertex();
      var3.pos(var1.maxX, var1.maxY, var1.maxZ).endVertex();
      var3.pos(var1.maxX, var1.maxY, var1.minZ).endVertex();
      var3.pos(var1.minX, var1.minY, var1.minZ).endVertex();
      var3.pos(var1.minX, var1.maxY, var1.minZ).endVertex();
      var3.pos(var1.maxX, var1.maxY, var1.minZ).endVertex();
      var3.pos(var1.maxX, var1.minY, var1.minZ).endVertex();
      var3.pos(var1.minX, var1.minY, var1.maxZ).endVertex();
      var3.pos(var1.maxX, var1.minY, var1.maxZ).endVertex();
      var3.pos(var1.maxX, var1.maxY, var1.maxZ).endVertex();
      var3.pos(var1.minX, var1.maxY, var1.maxZ).endVertex();
      var3.pos(var1.minX, var1.minY, var1.maxZ).endVertex();
      var3.pos(var1.minX, var1.maxY, var1.maxZ).endVertex();
      var3.pos(var1.minX, var1.maxY, var1.minZ).endVertex();
      var3.pos(var1.minX, var1.minY, var1.minZ).endVertex();
      var3.pos(var1.maxX, var1.minY, var1.minZ).endVertex();
      var3.pos(var1.maxX, var1.maxY, var1.minZ).endVertex();
      var3.pos(var1.maxX, var1.maxY, var1.maxZ).endVertex();
      var3.pos(var1.maxX, var1.minY, var1.maxZ).endVertex();
      var2.draw();
   }

   private void renderBeam(double var1, double var3, double var5, float var7, float var8, float var9) {
      Tessellator var10 = Tessellator.getInstance();
      WorldRenderer var11 = var10.getWorldRenderer();
      GlStateManager.color(var7, var8, var9, 0.4F);
      double var12 = 0.2D;
      double var14 = 256.0D;
      var11.begin(7, DefaultVertexFormats.POSITION);
      var11.pos(var1 + var12, var3, var5 - var12).endVertex();
      var11.pos(var1 + var12, var3, var5 + var12).endVertex();
      var11.pos(var1 + var12, var3 + var14, var5 + var12).endVertex();
      var11.pos(var1 + var12, var3 + var14, var5 - var12).endVertex();
      var11.pos(var1 - var12, var3, var5 + var12).endVertex();
      var11.pos(var1 - var12, var3, var5 - var12).endVertex();
      var11.pos(var1 - var12, var3 + var14, var5 - var12).endVertex();
      var11.pos(var1 - var12, var3 + var14, var5 + var12).endVertex();
      var11.pos(var1 + var12, var3, var5 + var12).endVertex();
      var11.pos(var1 - var12, var3, var5 + var12).endVertex();
      var11.pos(var1 - var12, var3 + var14, var5 + var12).endVertex();
      var11.pos(var1 + var12, var3 + var14, var5 + var12).endVertex();
      var11.pos(var1 - var12, var3, var5 - var12).endVertex();
      var11.pos(var1 + var12, var3, var5 - var12).endVertex();
      var11.pos(var1 + var12, var3 + var14, var5 - var12).endVertex();
      var11.pos(var1 - var12, var3 + var14, var5 - var12).endVertex();
      var10.draw();
   }
}
