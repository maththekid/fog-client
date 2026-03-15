package com.fogclient.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

public class Ballistics {
   public static float[] calculateAngles(EntityLivingBase var0, double var1) {
      Minecraft var3 = Minecraft.getMinecraft();
      EntityPlayerSP var4 = var3.thePlayer;
      float var5 = getArrowVelocity(var3.thePlayer.getItemInUseDuration());
      if ((double)var5 < 0.1D) {
         return null;
      } else {
         double var6 = var4.getDistanceToEntity(var0);
         double var8 = var6 / (double)var5;
         double var10 = var0.posX - var0.lastTickPosX;
         double var12 = var0.posY - var0.lastTickPosY;
         double var14 = var0.posZ - var0.lastTickPosZ;
         double var16 = var10 * var8 * var1;
         double var18 = var12 * var8 * var1;
         double var20 = var14 * var8 * var1;
         double var22 = var0.posX + var16;
         double var24 = var0.posY + (double)var0.getEyeHeight() / 2.0D + var18;
         double var26 = var0.posZ + var20;
         return solveTrajectory(var4.posX, var4.posY + (double)var4.getEyeHeight(), var4.posZ, var22, var24, var26, var5);
      }
   }

   private static float getArrowVelocity(int var0) {
      float var1 = (float)var0 / 20.0F;
      var1 = (var1 * var1 + var1 * 2.0F) / 3.0F;
      if (var1 > 1.0F) {
         var1 = 1.0F;
      }

      return var1 * 3.0F;
   }

   private static float[] solveTrajectory(double var0, double var2, double var4, double var6, double var8, double var10, float var12) {
      double var13 = var6 - var0;
      double var15 = var8 - var2;
      double var17 = var10 - var4;
      double var19 = Math.sqrt(var13 * var13 + var17 * var17);
      double var21 = Math.atan2(var17, var13) * 180.0D / 3.141592653589793D - 90.0D;
      double var23 = Math.atan2(var15, var19);
      double var25 = var23 + Math.toRadians(5.0D);
      double var27 = getSimulationError(var0, var2, var4, var21, var23, var12, var19, var8);
      if (Double.isNaN(var27)) {
         return null;
      } else {
         for(int var29 = 0; var29 < 20; ++var29) {
            double var30 = getSimulationError(var0, var2, var4, var21, var25, var12, var19, var8);
            if (Double.isNaN(var30)) {
               var25 = (var23 + var25) / 2.0D;
            } else {
               if (Math.abs(var30) < 0.05D) {
                  return new float[]{(float)var21, (float)(-(var25 * 180.0D / 3.141592653589793D))};
               }

               double var32 = var25 - var23;
               double var34 = var30 - var27;
               if (Math.abs(var34) < 1.0E-5D) {
                  break;
               }

               double var36 = var25 - var30 * (var32 / var34);
               if (var36 < -1.5707963267948966D) {
                  var36 = -1.5707963267948966D;
               }

               if (var36 > 1.5707963267948966D) {
                  var36 = 1.5707963267948966D;
               }

               var23 = var25;
               var27 = var30;
               var25 = var36;
            }
         }

         return new float[]{(float)var21, (float)(-(var25 * 180.0D / 3.141592653589793D))};
      }
   }

   private static double getSimulationError(double var0, double var2, double var4, double var6, double var8, float var10, double var11, double var13) {
      Vec3 var15 = simulateArrow(var0, var2, var4, var6, var8, var10, var11);
      return var15 == null ? Double.NaN : var15.yCoord - var13;
   }

   private static Vec3 simulateArrow(double var0, double var2, double var4, double var6, double var8, float var10, double var11) {
      double var13 = (var6 + 90.0D) * 3.141592653589793D / 180.0D;
      double var15 = (double)var10 * Math.cos(var13) * Math.cos(var8);
      double var17 = (double)var10 * Math.sin(var13) * Math.cos(var8);
      double var19 = (double)var10 * Math.sin(var8);
      double var21 = var0;
      double var23 = var2;
      double var25 = var4;
      double var27 = 0.05000000074505806D;
      double var29 = 0.99D;

      for(int var31 = 0; var31 < 300; ++var31) {
         var21 += var15;
         var23 += var19;
         var25 += var17;
         double var32 = (var21 - var0) * (var21 - var0) + (var25 - var4) * (var25 - var4);
         if (var32 >= var11 * var11) {
            return new Vec3(var21, var23, var25);
         }

         var15 *= var29;
         var19 *= var29;
         var17 *= var29;
         var19 -= var27;
      }

      return null;
   }
}
