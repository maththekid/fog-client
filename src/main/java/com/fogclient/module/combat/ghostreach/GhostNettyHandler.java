package com.fogclient.module.combat.ghostreach;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class GhostNettyHandler extends ChannelInboundHandlerAdapter {
   private final GhostReachCore core;
   private final Map<Integer, GhostNettyHandler.ServerPos> realServerPositions = new HashMap();
   private final Map<Integer, GhostNettyHandler.ServerPos> accumulatedDesync = new HashMap();
   private static Field s14_entityId;
   private static Field s14_posX;
   private static Field s14_posY;
   private static Field s14_posZ;
   private static Field s18_entityId;
   private static Field s18_posX;
   private static Field s18_posY;
   private static Field s18_posZ;

   private static Field getField(Class<?> var0, String... var1) throws NoSuchFieldException {
      String[] var2 = var1;
      int var3 = var1.length;
      int var4 = 0;

      while(var4 < var3) {
         String var5 = var2[var4];

         try {
            Field var6 = var0.getDeclaredField(var5);
            var6.setAccessible(true);
            return var6;
         } catch (NoSuchFieldException var7) {
            ++var4;
         }
      }

      throw new NoSuchFieldException("Could not find field for " + var0.getSimpleName());
   }

   public GhostNettyHandler(GhostReachCore var1) {
      this.core = var1;
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      if (var2 instanceof S03PacketTimeUpdate) {
         this.core.updateTPS();
      } else if (var2 instanceof S14PacketEntity || var2 instanceof S18PacketEntityTeleport) {
         this.handleEntityPacket(var2);
      }

      super.channelRead(var1, var2);
   }

   private void handleEntityPacket(Object var1) {
      try {
         boolean var3 = var1 instanceof S18PacketEntityTeleport;
         boolean var4 = var1 instanceof S14PacketEntity;
         int var2;
         if (var4) {
            var2 = s14_entityId.getInt(var1);
         } else {
            var2 = s18_entityId.getInt(var1);
         }

         GhostNettyHandler.ServerPos var5 = (GhostNettyHandler.ServerPos)this.realServerPositions.get(var2);
         if (var5 == null) {
            if (!var3) {
               return;
            }

            var5 = new GhostNettyHandler.ServerPos(0, 0, 0);
            this.realServerPositions.put(var2, var5);
         }

         if (var3) {
            var5.x = s18_posX.getInt(var1);
            var5.y = s18_posY.getInt(var1);
            var5.z = s18_posZ.getInt(var1);
            this.accumulatedDesync.remove(var2);
         } else {
            var5.x += s14_posX.getByte(var1);
            var5.y += s14_posY.getByte(var1);
            var5.z += s14_posZ.getByte(var1);
         }

         boolean var6 = this.core.isTarget(var2);
         GhostNettyHandler.ServerPos var7 = (GhostNettyHandler.ServerPos)this.accumulatedDesync.get(var2);
         Minecraft var8 = Minecraft.getMinecraft();
         if (var6) {
            if (var8.thePlayer != null) {
               Vec3 var9 = var8.thePlayer.getPositionEyes(1.0F);
               double var10 = (double)var5.x / 32.0D;
               double var12 = (double)var5.y / 32.0D;
               double var14 = (double)var5.z / 32.0D;
               double var16 = var10 - var9.xCoord;
               double var18 = var12 - var9.yCoord;
               double var20 = var14 - var9.zCoord;
               double var22 = Math.sqrt(var16 * var16 + var18 * var18 + var20 * var20);
               if (var22 > 0.1D) {
                  var16 /= var22;
                  var18 /= var22;
                  var20 /= var22;
                  double var24 = 2.85D;
                  double var26 = var9.xCoord + var16 * var24;
                  double var28 = var9.yCoord + var18 * var24;
                  double var30 = var9.zCoord + var20 * var24;
                  int var32 = MathHelper.floor_double(var26 * 32.0D);
                  int var33 = MathHelper.floor_double(var28 * 32.0D);
                  int var34 = MathHelper.floor_double(var30 * 32.0D);
                  if (var3) {
                     s18_posX.setInt(var1, var32);
                     s18_posY.setInt(var1, var33);
                     s18_posZ.setInt(var1, var34);
                     this.accumulatedDesync.put(var2, new GhostNettyHandler.ServerPos(var32 - var5.x, var33 - var5.y, var34 - var5.z));
                  } else {
                     byte var35 = s14_posX.getByte(var1);
                     byte var36 = s14_posY.getByte(var1);
                     byte var37 = s14_posZ.getByte(var1);
                     int var38 = var7 != null ? var7.x : 0;
                     int var39 = var7 != null ? var7.y : 0;
                     int var40 = var7 != null ? var7.z : 0;
                     int var41 = var32 - (var5.x - var35 + var38);
                     int var42 = var33 - (var5.y - var36 + var39);
                     int var43 = var34 - (var5.z - var37 + var40);
                     byte var44 = this.clampToByte(var41);
                     byte var45 = this.clampToByte(var42);
                     byte var46 = this.clampToByte(var43);
                     s14_posX.setByte(var1, var44);
                     s14_posY.setByte(var1, var45);
                     s14_posZ.setByte(var1, var46);
                     int var47 = var44 + var38 - var35;
                     int var48 = var45 + var39 - var36;
                     int var49 = var46 + var40 - var37;
                     this.accumulatedDesync.put(var2, new GhostNettyHandler.ServerPos(var47, var48, var49));
                  }

                  if (GhostReachCore.debug) {
                     var8.addScheduledTask(() -> {
                        var8.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("Â§7[GhostReach] Â§aRelocating entity " + var2));
                     });
                  }
               }
            }
         } else if (var7 != null && !var3) {
            byte var51 = s14_posX.getByte(var1);
            byte var52 = s14_posY.getByte(var1);
            byte var11 = s14_posZ.getByte(var1);
            int var53 = var51 - var7.x;
            int var13 = var52 - var7.y;
            int var54 = var11 - var7.z;
            byte var15 = this.clampToByte(var53);
            byte var55 = this.clampToByte(var13);
            byte var17 = this.clampToByte(var54);
            s14_posX.setByte(var1, var15);
            s14_posY.setByte(var1, var55);
            s14_posZ.setByte(var1, var17);
            int var56 = var7.x + var15 - var51;
            int var19 = var7.y + var55 - var52;
            int var57 = var7.z + var17 - var11;
            if (var56 == 0 && var19 == 0 && var57 == 0) {
               this.accumulatedDesync.remove(var2);
            } else {
               this.accumulatedDesync.put(var2, new GhostNettyHandler.ServerPos(var56, var19, var57));
            }
         }
      } catch (Exception var50) {
         var50.printStackTrace();
      }

   }

   private byte clampToByte(int var1) {
      if (var1 > 127) {
         return 127;
      } else {
         return var1 < -128 ? -128 : (byte)var1;
      }
   }

   static {
      try {
         s14_entityId = getField(S14PacketEntity.class, "entityId", "field_149074_a");
         s14_posX = getField(S14PacketEntity.class, "posX", "field_149072_b");
         s14_posY = getField(S14PacketEntity.class, "posY", "field_149073_c");
         s14_posZ = getField(S14PacketEntity.class, "posZ", "field_149070_d");
         s18_entityId = getField(S18PacketEntityTeleport.class, "entityId", "field_149458_a");
         s18_posX = getField(S18PacketEntityTeleport.class, "posX", "field_149456_b");
         s18_posY = getField(S18PacketEntityTeleport.class, "posY", "field_149457_c");
         s18_posZ = getField(S18PacketEntityTeleport.class, "posZ", "field_149454_d");
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }

   private static class ServerPos {
      int x;
      int y;
      int z;

      ServerPos(int var1, int var2, int var3) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
      }
   }
}
