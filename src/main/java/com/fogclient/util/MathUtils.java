package com.fogclient.util;

import net.minecraft.util.Vec3;

public class MathUtils {
   public static float clamp(float var0, float var1, float var2) {
      return Math.max(var1, Math.min(var2, var0));
   }

   public static Vec3 getBezierPoint(Vec3 var0, Vec3 var1, Vec3 var2, float var3) {
      double var4 = Math.pow(1.0F - var3, 2.0D) * var0.xCoord + (double)(2.0F * (1.0F - var3) * var3) * var1.xCoord + Math.pow(var3, 2.0D) * var2.xCoord;
      double var6 = Math.pow(1.0F - var3, 2.0D) * var0.yCoord + (double)(2.0F * (1.0F - var3) * var3) * var1.yCoord + Math.pow(var3, 2.0D) * var2.yCoord;
      double var8 = Math.pow(1.0F - var3, 2.0D) * var0.zCoord + (double)(2.0F * (1.0F - var3) * var3) * var1.zCoord + Math.pow(var3, 2.0D) * var2.zCoord;
      return new Vec3(var4, var6, var8);
   }

   public static float[] getRotations(Vec3 var0, Vec3 var1) {
      double var2 = var1.xCoord - var0.xCoord;
      double var4 = var1.yCoord - var0.yCoord;
      double var6 = var1.zCoord - var0.zCoord;
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      float var10 = (float)(Math.atan2(var6, var2) * 180.0D / 3.141592653589793D) - 90.0F;
      float var11 = (float)(-(Math.atan2(var4, var8) * 180.0D / 3.141592653589793D));
      return new float[]{var10, var11};
   }

   public static float wrapAngleTo180(float var0) {
      var0 %= 360.0F;
      if (var0 >= 180.0F) {
         var0 -= 360.0F;
      }

      if (var0 < -180.0F) {
         var0 += 360.0F;
      }

      return var0;
   }

   public static float getAngleDifference(float var0, float var1) {
      return wrapAngleTo180(var0 - var1);
   }
}
