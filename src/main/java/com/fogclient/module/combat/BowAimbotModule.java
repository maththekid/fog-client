package com.fogclient.module.combat;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.BooleanSetting;
import com.fogclient.setting.ModeSetting;
import com.fogclient.setting.NumberSetting;
import com.fogclient.util.Ballistics;
import com.fogclient.util.MathUtils;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

public class BowAimbotModule extends Module {
   private final BooleanSetting silent = new BooleanSetting("Silent", this, true);
   private final BooleanSetting ignoreWalls = new BooleanSetting("Ignore Walls", this, false);
   private final BooleanSetting ignoreInvisible = new BooleanSetting("Ignore Invisible", this, true);
   private final NumberSetting fov = new NumberSetting("FOV", this, 180.0D, 10.0D, 360.0D, 10.0D);
   private final NumberSetting maxDistance = new NumberSetting("Max Distance", this, 60.0D, 10.0D, 100.0D, 5.0D);
   private final NumberSetting minDistance = new NumberSetting("Min Distance", this, 0.0D, 0.0D, 20.0D, 1.0D);
   private final NumberSetting predictIntensity = new NumberSetting("Predict", this, 1.0D, 0.0D, 5.0D, 0.1D);
   private final ModeSetting targetPriority = new ModeSetting("Priority", this, "Closest", new String[]{"Closest", "Health", "Angle"});
   private EntityLivingBase currentTarget;
   private float[] aimAngles;
   private float originalYaw;
   private float originalPitch;
   private float originalPrevYaw;
   private float originalPrevPitch;
   private float originalHeadYaw;
   private float originalPrevHeadYaw;
   private float originalRenderYawOffset;
   private float originalPrevRenderYawOffset;
   private float yawDelta;
   private float pitchDelta;
   private boolean isAiming;

   public BowAimbotModule() {
      super("FlechaCerteira", "Automatically aims at players with bow.", Category.COMBAT);
   }

   public void onEnable() {
   }

   public void onDisable() {
      this.isAiming = false;
      this.currentTarget = null;
   }

   public void onTickStart() {
      if (this.isEnabled()) {
         this.isAiming = false;
         this.yawDelta = 0.0F;
         this.pitchDelta = 0.0F;
         if (mc.thePlayer != null && mc.theWorld != null) {
            ItemStack var1 = mc.thePlayer.getHeldItem();
            if (var1 != null && var1.getItem() instanceof ItemBow) {
               if (mc.thePlayer.isUsingItem()) {
                  this.currentTarget = this.getTarget();
                  if (this.currentTarget != null) {
                     this.aimAngles = Ballistics.calculateAngles(this.currentTarget, this.predictIntensity.getValue());
                     if (this.aimAngles != null) {
                        if (this.silent.isEnabled()) {
                           this.originalYaw = mc.thePlayer.rotationYaw;
                           this.originalPitch = mc.thePlayer.rotationPitch;
                           this.originalPrevYaw = mc.thePlayer.prevRotationYaw;
                           this.originalPrevPitch = mc.thePlayer.prevRotationPitch;
                           this.originalHeadYaw = mc.thePlayer.rotationYawHead;
                           this.originalPrevHeadYaw = mc.thePlayer.prevRotationYawHead;
                           this.originalRenderYawOffset = mc.thePlayer.renderYawOffset;
                           this.originalPrevRenderYawOffset = mc.thePlayer.prevRenderYawOffset;
                           mc.thePlayer.rotationYaw = this.aimAngles[0];
                           mc.thePlayer.rotationPitch = this.aimAngles[1];
                           mc.thePlayer.rotationYawHead = this.aimAngles[0];
                           mc.thePlayer.renderYawOffset = this.aimAngles[0];
                           this.isAiming = true;
                        } else {
                           mc.thePlayer.rotationYaw = this.aimAngles[0];
                           mc.thePlayer.rotationPitch = this.aimAngles[1];
                        }

                     }
                  }
               }
            }
         }
      }
   }

   public void onTick() {
      if (this.isEnabled()) {
         if (this.isAiming && this.silent.isEnabled()) {
            mc.thePlayer.rotationYaw = this.originalYaw + this.yawDelta;
            mc.thePlayer.rotationPitch = this.originalPitch + this.pitchDelta;
            mc.thePlayer.prevRotationYaw = this.originalYaw;
            mc.thePlayer.prevRotationPitch = this.originalPitch;
            mc.thePlayer.rotationYawHead = this.originalHeadYaw;
            mc.thePlayer.prevRotationYawHead = this.originalHeadYaw;
            mc.thePlayer.renderYawOffset = this.originalRenderYawOffset;
            mc.thePlayer.prevRenderYawOffset = this.originalRenderYawOffset;
            this.isAiming = false;
         }

      }
   }

   public void onLivingUpdate() {
      if (mc.thePlayer != null && this.isAiming && this.silent.isEnabled()) {
         float var1 = mc.thePlayer.rotationYaw;
         float var2 = mc.thePlayer.rotationPitch;
         float var3 = var1 - this.aimAngles[0];

         float var4;
         for(var4 = var2 - this.aimAngles[1]; var3 <= -180.0F; var3 += 360.0F) {
         }

         while(var3 > 180.0F) {
            var3 -= 360.0F;
         }

         this.yawDelta += var3;
         this.pitchDelta += var4;
         mc.thePlayer.rotationYaw = this.aimAngles[0];
         mc.thePlayer.rotationPitch = this.aimAngles[1];
         MovementInput var5 = mc.thePlayer.movementInput;
         float var6 = var5.moveForward;
         float var7 = var5.moveStrafe;
         float var8 = this.originalYaw;
         float var9 = this.aimAngles[0];
         float var10 = var8 - var9;
         float var11 = (float)Math.toRadians((double)var10);
         float var12 = MathHelper.cos(var11);
         float var13 = MathHelper.sin(var11);
         var5.moveStrafe = var7 * var12 - var6 * var13;
         var5.moveForward = var6 * var12 + var7 * var13;
      }

   }

   private EntityLivingBase getTarget() {
      if (mc.theWorld == null) {
         return null;
      } else {
         EntityLivingBase var1 = null;
         double var2 = Double.MAX_VALUE;
         List var4 = mc.theWorld.getLoadedEntityList();
         Iterator var5 = var4.iterator();

         while(true) {
            EntityLivingBase var7;
            do {
               do {
                  do {
                     do {
                        do {
                           Entity var6;
                           do {
                              if (!var5.hasNext()) {
                                 return var1;
                              }

                              var6 = (Entity)var5.next();
                           } while(!(var6 instanceof EntityLivingBase));

                           var7 = (EntityLivingBase)var6;
                        } while(var7 == mc.thePlayer);
                     } while(var7.isDead);
                  } while(var7.getHealth() <= 0.0F);
               } while(this.ignoreInvisible.isEnabled() && var7.isInvisible());
            } while(this.ignoreWalls.isEnabled() && !mc.thePlayer.canEntityBeSeen(var7));

            double var8 = (double)mc.thePlayer.getDistanceToEntity(var7);
            if (!(var8 < this.minDistance.getValue()) && !(var8 > this.maxDistance.getValue()) && this.isInFOV(var7, (float)this.fov.getValue())) {
               double var10 = this.getScore(var7, var8);
               if (var10 < var2) {
                  var2 = var10;
                  var1 = var7;
               }
            }
         }
      }
   }

   private boolean isInFOV(Entity var1, float var2) {
      float[] var3 = MathUtils.getRotations(Minecraft.getMinecraft().thePlayer.getPositionVector(), var1.getPositionVector());
      float var4 = MathUtils.getAngleDifference(Minecraft.getMinecraft().thePlayer.rotationYaw, var3[0]);
      return Math.abs(var4) < var2 / 2.0F;
   }

   private double getScore(EntityLivingBase var1, double var2) {
      String var4 = this.targetPriority.getValue();
      if (var4.equals("Health")) {
         return (double)var1.getHealth();
      } else if (var4.equals("Angle")) {
         float[] var5 = MathUtils.getRotations(Minecraft.getMinecraft().thePlayer.getPositionVector(), var1.getPositionVector());
         float var6 = MathUtils.getAngleDifference(Minecraft.getMinecraft().thePlayer.rotationYaw, var5[0]);
         return (double)Math.abs(var6);
      } else {
         return var2;
      }
   }
}
