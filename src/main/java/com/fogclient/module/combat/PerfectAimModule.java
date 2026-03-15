package com.fogclient.module.combat;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.BooleanSetting;
import com.fogclient.setting.NumberSetting;
import com.fogclient.util.MathUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class PerfectAimModule extends Module {
   private final BooleanSetting enablePrediction = new BooleanSetting("Prediction", this, true);
   private final NumberSetting aimbotSpeed = new NumberSetting("Speed", this, 3.0D, 0.1D, 10.0D, 0.1D);
   private final NumberSetting hitboxPixels = new NumberSetting("Hitbox Pixels", this, 5.0D, 0.0D, 50.0D, 1.0D);
   private final BooleanSetting enableHitboxCorrection = new BooleanSetting("Hitbox Correction", this, true);
   private final BooleanSetting enableSensitivityAdjustment = new BooleanSetting("Sensitivity Adjust", this, false);
   private final NumberSetting sensitivityReduction = new NumberSetting("Sensitivity Reduction", this, 20.0D, 0.0D, 100.0D, 5.0D);
   private EntityLivingBase currentTarget;
   private Vec3 predictedPosition;
   private final Random random = new Random();
   private float storedSensitivity = -1.0F;
   private boolean isSensitivityModified = false;

   public PerfectAimModule() {
      super("MiraCerteira", "Aims at the closest player.", Category.COMBAT);
   }

   public void onEnable() {
      this.storedSensitivity = -1.0F;
      this.isSensitivityModified = false;
   }

   public void onDisable() {
      if (this.isSensitivityModified && this.storedSensitivity != -1.0F) {
         mc.gameSettings.mouseSensitivity = this.storedSensitivity;
         this.isSensitivityModified = false;
         this.storedSensitivity = -1.0F;
      }

   }

   public void onTickStart() {
      if (this.isEnabled()) {
         if (mc.thePlayer != null && mc.theWorld != null) {
            this.updateTargetAndPrediction();
         }
      }
   }

   public void onRenderTick() {
      if (this.isEnabled()) {
         if (mc.thePlayer != null) {
            if (this.currentTarget != null && this.isAimingConditionMet()) {
               this.applyAimbotRotation();
            }

            this.adjustSensitivity();
         }
      }
   }

   private void updateTargetAndPrediction() {
      Entity var1 = mc.objectMouseOver != null ? mc.objectMouseOver.entityHit : null;
      if (var1 instanceof EntityPlayer) {
         this.currentTarget = (EntityLivingBase)var1;
      } else {
         this.currentTarget = this.findClosestTarget();
      }

      if (this.currentTarget != null) {
         if (this.enablePrediction.isEnabled()) {
            this.predictPosition(this.currentTarget);
         } else {
            this.predictedPosition = this.currentTarget.getPositionVector();
         }
      } else {
         this.predictedPosition = null;
      }

   }

   private EntityLivingBase findClosestTarget() {
      EntityPlayerSP var1 = mc.thePlayer;
      double var2 = 100.0D;
      EntityLivingBase var4 = null;
      List var5 = mc.theWorld.getLoadedEntityList();
      Iterator var6 = var5.iterator();

      while(var6.hasNext()) {
         Entity var7 = (Entity)var6.next();
         if (var7 instanceof EntityPlayer && var7 != var1 && !var7.isInvisible()) {
            double var8 = (double)var7.getDistanceToEntity(var1);
            if (var8 < 6.0D) {
               float[] var10 = MathUtils.getRotations(var1.getPositionEyes(1.0F), var7.getPositionVector().addVector(0.0D, (double)var7.height / 2.0D, 0.0D));
               float var11 = Math.abs(MathUtils.wrapAngleTo180(var10[0] - var1.rotationYaw));
               float var12 = Math.abs(var10[1] - var1.rotationPitch);
               double var13 = (double)(var11 + var12);
               if (var13 < var2) {
                  var2 = var13;
                  var4 = (EntityLivingBase)var7;
               }
            }
         }
      }

      return var4;
   }

   private void predictPosition(EntityLivingBase var1) {
      int var2 = 0;
      if (var1 instanceof EntityPlayer) {
         try {
            var2 = mc.getNetHandler().getPlayerInfo(var1.getUniqueID()).getResponseTime();
         } catch (Exception var17) {
         }
      }

      int var3 = Math.round((float)var2 / 50.0F) + 2;
      double var4 = var1.posX;
      double var6 = var1.posY;
      double var8 = var1.posZ;
      double var10 = var1.posX - var1.lastTickPosX;
      double var12 = var1.posY - var1.lastTickPosY;
      double var14 = var1.posZ - var1.lastTickPosZ;

      for(int var16 = 0; var16 < var3; ++var16) {
         var12 -= 0.08D;
         var10 *= 0.91D;
         var12 *= 0.98D;
         var14 *= 0.91D;
         if (!mc.theWorld.getCollidingBoundingBoxes(var1, var1.getEntityBoundingBox().offset(var10, var12, var14)).isEmpty()) {
            break;
         }

         var4 += var10;
         var6 += var12;
         var8 += var14;
      }

      this.predictedPosition = new Vec3(var4, var6, var8);
   }

   private boolean isAimingConditionMet() {
      boolean var1 = Math.abs(mc.mouseHelper.deltaX) > 0 || Math.abs(mc.mouseHelper.deltaY) > 0;
      boolean var2 = mc.gameSettings.keyBindAttack.isKeyDown();
      return var1 && var2;
   }

   private void applyAimbotRotation() {
      if (this.predictedPosition != null) {
         EntityPlayerSP var1 = mc.thePlayer;
         float var2 = this.currentTarget.width;
         float var3 = this.currentTarget.height;
         Vec3 var4 = new Vec3(this.predictedPosition.xCoord, this.predictedPosition.yCoord + (double)(var3 / 2.0F), this.predictedPosition.zCoord);
         float[] var5 = MathUtils.getRotations(var1.getPositionEyes(1.0F), var4);
         double var6 = var1.getDistance(this.predictedPosition.xCoord, this.predictedPosition.yCoord, this.predictedPosition.zCoord);
         double var8 = Math.toDegrees(Math.atan((double)var2 / var6));
         double var10 = Math.toDegrees(Math.atan((double)var3 / var6));
         float var12 = MathUtils.wrapAngleTo180(var1.rotationYaw);
         float var13 = var1.rotationPitch;
         float var14 = MathUtils.wrapAngleTo180(var5[0]);
         float var15 = var5[1];
         float var16 = MathUtils.wrapAngleTo180(var14 - var12);
         float var17 = var15 - var13;
         float var18 = (float)(var8 / 2.0D);
         float var19 = (float)(var10 / 2.0D);
         float var20 = (float)(this.hitboxPixels.getValue() * 0.18000000715255737D);
         boolean var21 = Math.abs(var16) < var18 + var20 && Math.abs(var17) < var19 + var20;
         boolean var22 = Math.abs(var16) < var18 && Math.abs(var17) < var19;
         if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit == this.currentTarget) {
            var22 = true;
         }

         if (this.enableHitboxCorrection.isEnabled() && var21 && !var22) {
            float var23;
            float var24;
            float var25;
            float var26;
            if (var22) {
               var23 = var14;
               var24 = var15;
            } else {
               var25 = var16 > 0.0F ? var18 : -var18;
               var26 = var17 > 0.0F ? var19 : -var19;
               var23 = var14 - var25;
               var24 = var15 - var26;
            }

            var25 = (float)Math.sqrt((double)(mc.mouseHelper.deltaX * mc.mouseHelper.deltaX + mc.mouseHelper.deltaY * mc.mouseHelper.deltaY));
            var26 = (float)(this.aimbotSpeed.getValue() / 100.0D);
            float var27 = var26 / 12.0F;
            float var28 = var26 * 4.0F;
            float var29 = var26 + var25 * var27;
            var29 = MathHelper.clamp_float(var29, var26 * 0.35F, var28);
            float var30 = MathUtils.wrapAngleTo180(var23 - var12) * var29;
            float var31 = (var24 - var13) * var29;
            if (this.enableHitboxCorrection.isEnabled()) {
               float var32 = (this.random.nextFloat() - 0.5F) * 0.05F;
               var30 += var32;
               var31 += var32;
            }

            var1.rotationYaw += var30;
            var1.rotationPitch += var31;
         }

      }
   }

   private void adjustSensitivity() {
      if (!this.enableSensitivityAdjustment.isEnabled()) {
         if (this.isSensitivityModified && this.storedSensitivity != -1.0F) {
            mc.gameSettings.mouseSensitivity = this.storedSensitivity;
            this.isSensitivityModified = false;
            this.storedSensitivity = -1.0F;
         }

      } else {
         boolean var1 = false;
         if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            var1 = true;
         }

         if (var1) {
            if (!this.isSensitivityModified) {
               this.storedSensitivity = mc.gameSettings.mouseSensitivity;
               this.isSensitivityModified = true;
            }

            float var2 = (float)(this.sensitivityReduction.getValue() / 100.0D);
            float var3 = this.storedSensitivity * (1.0F - var2);
            mc.gameSettings.mouseSensitivity = this.lerp(mc.gameSettings.mouseSensitivity, var3, 0.25F);
         } else if (this.isSensitivityModified) {
            mc.gameSettings.mouseSensitivity = this.lerp(mc.gameSettings.mouseSensitivity, this.storedSensitivity, 0.25F);
            if ((double)Math.abs(mc.gameSettings.mouseSensitivity - this.storedSensitivity) < 0.001D) {
               mc.gameSettings.mouseSensitivity = this.storedSensitivity;
               this.isSensitivityModified = false;
               this.storedSensitivity = -1.0F;
            }
         }

      }
   }

   private float lerp(float var1, float var2, float var3) {
      return var1 + var3 * (var2 - var1);
   }
}
