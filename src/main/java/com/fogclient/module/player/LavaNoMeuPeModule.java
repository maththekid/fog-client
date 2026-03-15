package com.fogclient.module.player;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import java.lang.reflect.Field;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class LavaNoMeuPeModule extends Module {
   private int state = 0;
   private int stateTicks = 0;
   private int oldSlot = -1;
   private float oldPitch = 0.0F;
   private float oldYaw = 0.0F;
   private BlockPos lavaPos;
   private static Field itemRendererField;

   public LavaNoMeuPeModule() {
      super("LavaNoMeuPe", "Places lava under your feet", Category.PLAYER);
      this.keybind = 0;

      try {
         itemRendererField = ReflectionHelper.findField(EntityRenderer.class, new String[]{"itemRenderer", "field_78516_c"});
         itemRendererField.setAccessible(true);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public boolean isAction() {
      return true;
   }

   public void onAction() {
      if (this.isEnabled() && this.state == 0) {
         this.state = 1;
         this.stateTicks = 0;
      }

   }

   public void onEnable() {
      this.state = 0;
      this.stateTicks = 0;
   }

   public void onDisable() {
      this.state = 0;
      this.stateTicks = 0;
   }

   public void onTick() {
      if (mc.thePlayer != null) {
         if (this.state > 0) {
            switch(this.state) {
            case 1:
               int var1 = -1;

               for(int var21 = 0; var21 < 9; ++var21) {
                  ItemStack var23 = mc.thePlayer.inventory.getStackInSlot(var21);
                  if (var23 != null && var23.getItem() == Items.lava_bucket) {
                     var1 = var21;
                     break;
                  }
               }

               if (var1 != -1) {
                  this.oldSlot = mc.thePlayer.inventory.currentItem;
                  this.oldPitch = mc.thePlayer.rotationPitch;
                  this.oldYaw = mc.thePlayer.rotationYaw;
                  mc.thePlayer.inventory.currentItem = var1;
                  mc.thePlayer.rotationPitch = 90.0F;
                  double var22 = mc.thePlayer.posX;
                  double var4 = mc.thePlayer.posY;
                  double var25 = mc.thePlayer.posZ;
                  this.lavaPos = new BlockPos(Math.floor(var22), Math.floor(var4) - 1.0D, Math.floor(var25));
                  this.state = 2;
                  this.stateTicks = 0;
               } else {
                  this.state = 0;
               }
               break;
            case 2:
               if (this.stateTicks == 0) {
                  if (mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem())) {
                     this.resetEquippedProgress();
                  }
               } else if (this.stateTicks >= 3 && this.stateTicks <= 5 && this.lavaPos != null) {
                  BlockPos var2 = this.lavaPos;
                  double var3 = mc.thePlayer.getDistanceSq((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D);
                  int var5 = -1;

                  while(true) {
                     double var9;
                     if (var5 > 1) {
                        double var24 = (double)var2.getX() + 0.5D;
                        double var26 = (double)var2.getY() + 0.5D;
                        var9 = (double)var2.getZ() + 0.5D;
                        double var11 = var24 - mc.thePlayer.posX;
                        double var13 = var9 - mc.thePlayer.posZ;
                        double var15 = var26 - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
                        double var17 = Math.sqrt(var11 * var11 + var13 * var13);
                        float var19 = (float)(Math.atan2(var13, var11) * 180.0D / 3.141592653589793D) - 90.0F;
                        float var20 = (float)(-(Math.atan2(var15, var17) * 180.0D / 3.141592653589793D));
                        mc.thePlayer.rotationYaw = var19;
                        mc.thePlayer.rotationPitch = var20;
                        if (mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem())) {
                           this.resetEquippedProgress();
                        }
                        break;
                     }

                     for(int var6 = -1; var6 <= 1; ++var6) {
                        for(int var7 = -1; var7 <= 1; ++var7) {
                           BlockPos var8 = var2.add(var5, var6, var7);
                           if (mc.theWorld.getBlockState(var8).getBlock() == Blocks.lava || mc.theWorld.getBlockState(var8).getBlock() == Blocks.flowing_lava) {
                              var9 = mc.thePlayer.getDistanceSq((double)var8.getX() + 0.5D, (double)var8.getY() + 0.5D, (double)var8.getZ() + 0.5D);
                              if (var9 < var3) {
                                 var3 = var9;
                                 var2 = var8;
                              }
                           }
                        }
                     }

                     ++var5;
                  }
               }

               ++this.stateTicks;
               if (this.stateTicks > 6) {
                  if (this.oldSlot != -1) {
                     mc.thePlayer.inventory.currentItem = this.oldSlot;
                  }

                  mc.thePlayer.rotationYaw = this.oldYaw;
                  mc.thePlayer.rotationPitch = this.oldPitch;
                  this.state = 0;
               }
            }
         }

      }
   }

   private void resetEquippedProgress() {
      try {
         if (itemRendererField != null) {
            ItemRenderer var1 = (ItemRenderer)itemRendererField.get(mc.entityRenderer);
            if (var1 != null) {
               var1.resetEquippedProgress();
            }
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
