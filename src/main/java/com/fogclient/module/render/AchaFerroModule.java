package com.fogclient.module.render;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.lwjgl.opengl.GL11;

public class AchaFerroModule extends Module {
   private final Map<ChunkCoordIntPair, Set<BlockPos>> chunkIrons = new HashMap();
   private int currentChunkIndex = 0;

   public AchaFerroModule() {
      super("AchaFerro", "Mostra minÃ©rios de ferro atravÃ©s das paredes (Alcance Ilimitado).", Category.RENDER);
      this.keybind = 0;
   }

   public void onEnable() {
      this.chunkIrons.clear();
      this.currentChunkIndex = 0;
      if (mc.thePlayer != null) {
      }

   }

   public void onDisable() {
      this.chunkIrons.clear();
   }

   public void onEvent(Event var1) {
      if (var1 instanceof RenderWorldLastEvent) {
         this.onRenderWorldLast((RenderWorldLastEvent)var1);
      }

   }

   public void onTick() {
      if (this.isEnabled()) {
         Minecraft var1 = Minecraft.getMinecraft();
         if (var1.thePlayer != null && var1.theWorld != null) {
            IChunkProvider var2 = var1.theWorld.getChunkProvider();
            byte var3 = 8;
            int var4 = var1.thePlayer.chunkCoordX;
            int var5 = var1.thePlayer.chunkCoordZ;

            for(int var6 = -var3; var6 <= var3; ++var6) {
               for(int var7 = -var3; var7 <= var3; ++var7) {
                  if (var2.chunkExists(var4 + var6, var5 + var7)) {
                     Chunk var8 = var2.provideChunk(var4 + var6, var5 + var7);
                     this.scanChunk(var8);
                  }
               }
            }

         }
      }
   }

   private void scanChunk(Chunk var1) {
      HashSet var2 = new HashSet();
      ExtendedBlockStorage[] var3 = var1.getBlockStorageArray();
      int var4 = var1.xPosition * 16;
      int var5 = var1.zPosition * 16;

      for(int var6 = 0; var6 < var3.length; ++var6) {
         ExtendedBlockStorage var7 = var3[var6];
         if (var7 != null && !var7.isEmpty()) {
            for(int var8 = 0; var8 < 16; ++var8) {
               for(int var9 = 0; var9 < 16; ++var9) {
                  for(int var10 = 0; var10 < 16; ++var10) {
                     if (var7.getBlockByExtId(var8, var9, var10) == Blocks.iron_ore) {
                        var2.add(new BlockPos(var4 + var8, var6 * 16 + var9, var5 + var10));
                     }
                  }
               }
            }
         }
      }

      if (!var2.isEmpty()) {
         this.chunkIrons.put(var1.getChunkCoordIntPair(), var2);
      } else {
         this.chunkIrons.remove(var1.getChunkCoordIntPair());
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
            Iterator var3 = this.chunkIrons.values().iterator();

            while(var3.hasNext()) {
               Set var4 = (Set)var3.next();
               Iterator var5 = var4.iterator();

               while(var5.hasNext()) {
                  BlockPos var6 = (BlockPos)var5.next();
                  this.renderIronESP(var6, var1.partialTicks);
               }
            }

            GlStateManager.enableCull();
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.popMatrix();
         }
      }
   }

   private void renderIronESP(BlockPos var1, float var2) {
      Minecraft var3 = Minecraft.getMinecraft();
      double var4 = var3.getRenderManager().viewerPosX;
      double var6 = var3.getRenderManager().viewerPosY;
      double var8 = var3.getRenderManager().viewerPosZ;
      double var10 = (double)var1.getX() - var4;
      double var12 = (double)var1.getY() - var6;
      double var14 = (double)var1.getZ() - var8;
      float var16 = 0.9F;
      float var17 = 0.7F;
      float var18 = 0.5F;
      GL11.glLineWidth(1.0F);
      GlStateManager.color(var16, var17, var18, 1.0F);
      AxisAlignedBB var19 = new AxisAlignedBB(var10, var12, var14, var10 + 1.0D, var12 + 1.0D, var14 + 1.0D);
      RenderGlobal.drawSelectionBoundingBox(var19);
      GlStateManager.color(var16, var17, var18, 0.2F);
      this.drawFilledBox(var19);
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
}
