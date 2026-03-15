package com.fogclient.render.entity;

import com.fogclient.entity.EntityPvPBot;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBotAsPlayer extends RenderLiving<EntityPvPBot> {
   private final ModelPlayer model;

   public RenderBotAsPlayer(RenderManager var1) {
      super(var1, new ModelPlayer(0.0F, false), 0.5F);
      this.model = (ModelPlayer)this.mainModel;
   }

   protected ResourceLocation getEntityTexture(EntityPvPBot var1) {
      return DefaultPlayerSkin.getDefaultSkinLegacy();
   }

   // $FF: synthetic method
   protected ResourceLocation func_110775_a(Entity var1) {
      return this.getEntityTexture((EntityPvPBot)var1);
   }
}
