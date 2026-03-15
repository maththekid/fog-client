package com.fogclient.module.combat.ghostreach;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class GhostReachCore {
   public static final GhostReachCore INSTANCE = new GhostReachCore();
   public static double minReach = 3.0D;
   public static double maxReach = 4.0D;
   public static float chance = 100.0F;
   public static boolean debug = false;
   public int targetEntityId = -1;
   private final Minecraft mc = Minecraft.getMinecraft();
   private long lastReachHitTime = 0L;
   private int consecutiveReachHits = 0;
   private final Random random = new Random();
   private long lastPacketTime = -1L;
   private double tps = 20.0D;

   public boolean isTarget(int var1) {
      return this.targetEntityId == var1;
   }

   public boolean checkConditions(Entity var1) {
      if (var1 != null && this.mc.thePlayer != null) {
         double var2 = (double)this.mc.thePlayer.getDistanceToEntity(var1);
         long var4 = System.currentTimeMillis();
         if (var4 - this.lastReachHitTime < 500L && (double)this.random.nextFloat() > 0.3D) {
            return false;
         } else if (this.consecutiveReachHits >= 2 && (double)this.random.nextFloat() > 0.1D) {
            this.consecutiveReachHits = 0;
            return false;
         } else {
            boolean var6 = this.mc.thePlayer.isSprinting() && this.mc.gameSettings.keyBindAttack.isKeyDown() && var2 >= minReach && var2 <= maxReach && this.random.nextFloat() * 100.0F < chance && this.isWithinAngle(var1, 45.0F) && this.getTPS() >= 18.0D;
            if (var6) {
               this.lastReachHitTime = var4;
               ++this.consecutiveReachHits;
            } else if (var2 < minReach) {
               this.consecutiveReachHits = 0;
            }

            return var6;
         }
      } else {
         return false;
      }
   }

   private boolean isWithinAngle(Entity var1, float var2) {
      Vec3 var3 = this.mc.thePlayer.getLook(1.0F);
      Vec3 var4 = (new Vec3(var1.posX - this.mc.thePlayer.posX, var1.posY - this.mc.thePlayer.posY, var1.posZ - this.mc.thePlayer.posZ)).normalize();
      double var5 = var3.dotProduct(var4);
      if (var5 > 1.0D) {
         var5 = 1.0D;
      } else if (var5 < -1.0D) {
         var5 = -1.0D;
      }

      return Math.toDegrees(Math.acos(var5)) <= (double)var2;
   }

   public Entity findTarget(double var1) {
      if (this.mc.theWorld != null && this.mc.thePlayer != null) {
         Entity var3 = null;
         Vec3 var4 = this.mc.thePlayer.getPositionEyes(1.0F);
         Vec3 var5 = this.mc.thePlayer.getLook(1.0F);
         double var6 = 0.02D;
         double var8 = (this.random.nextDouble() - 0.5D) * var6;
         double var10 = (this.random.nextDouble() - 0.5D) * var6;
         double var12 = (this.random.nextDouble() - 0.5D) * var6;
         Vec3 var14 = var4.addVector(var5.xCoord * var1 + var8, var5.yCoord * var1 + var10, var5.zCoord * var1 + var12);
         List var15 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().addCoord(var5.xCoord * var1, var5.yCoord * var1, var5.zCoord * var1).expand(1.0D, 1.0D, 1.0D));
         double var16 = var1;
         Iterator var18 = var15.iterator();

         while(true) {
            while(true) {
               Entity var19;
               do {
                  if (!var18.hasNext()) {
                     return var3;
                  }

                  var19 = (Entity)var18.next();
               } while(!var19.canBeCollidedWith());

               AxisAlignedBB var20 = var19.getEntityBoundingBox().expand(0.1D, 0.1D, 0.1D);
               MovingObjectPosition var21 = var20.calculateIntercept(var4, var14);
               if (var20.isVecInside(var4)) {
                  if (var16 >= 0.0D) {
                     var3 = var19;
                     var16 = 0.0D;
                  }
               } else if (var21 != null) {
                  double var22 = var4.distanceTo(var21.hitVec);
                  if (var22 < var16 || var16 == 0.0D) {
                     var3 = var19;
                     var16 = var22;
                  }
               }
            }
         }
      } else {
         return null;
      }
   }

   public void updateTPS() {
      long var1 = System.currentTimeMillis();
      if (this.lastPacketTime != -1L) {
         long var3 = var1 - this.lastPacketTime;
         if (var3 > 0L) {
            double var5 = 20.0D * (1000.0D / (double)var3);
            if (var5 > 20.0D) {
               var5 = 20.0D;
            }

            if (var5 < 0.0D) {
               var5 = 0.0D;
            }

            this.tps = this.tps * 0.7D + var5 * 0.3D;
         }
      }

      this.lastPacketTime = var1;
   }

   private double getTPS() {
      return this.tps;
   }
}
