package com.fogclient.module.player;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.BooleanSetting;
import com.fogclient.setting.NumberSetting;
import java.lang.reflect.Field;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class RoubaLavaModule extends Module {
   private BooleanSetting enableEmptyBucketLava = new BooleanSetting("Lava Pickup", true);
   private BooleanSetting enableEmptyBucketWater = new BooleanSetting("Water Pickup", true);
   private BooleanSetting enableLavaReset = new BooleanSetting("Lava Reset", true);
   private BooleanSetting enableWaterReset = new BooleanSetting("Water Reset", true);
   private BooleanSetting enableAntiDrop = new BooleanSetting("Anti Drop", true);
   private NumberSetting detectionRange = new NumberSetting("Range", 0.0D, 0.0D, 6.0D, 1.0D);
   private boolean wasActive = false;
   private int originalSlot = -1;
   private int actionTimer = 0;
   private Field rightClickDelayTimerField;

   public RoubaLavaModule() {
      super("RoubaTudo", "Steals lava/water from nearby blocks", Category.PLAYER);
      this.addSetting(this.enableEmptyBucketLava);
      this.addSetting(this.enableEmptyBucketWater);
      this.addSetting(this.enableLavaReset);
      this.addSetting(this.enableWaterReset);
      this.addSetting(this.enableAntiDrop);
      this.addSetting(this.detectionRange);

      try {
         this.rightClickDelayTimerField = ReflectionHelper.findField(Minecraft.class, new String[]{"field_71467_ac", "rightClickDelayTimer"});
         this.rightClickDelayTimerField.setAccessible(true);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void onEnable() {
      this.wasActive = false;
      this.originalSlot = -1;
      this.actionTimer = 0;
   }

   public void onDisable() {
      this.wasActive = false;
      if (this.originalSlot != -1) {
         mc.thePlayer.inventory.currentItem = this.originalSlot;
         this.originalSlot = -1;
      }

   }

   public void onTickStart() {
      if (mc.thePlayer != null && mc.theWorld != null) {
         if (this.actionTimer > 0) {
            --this.actionTimer;
         }

         if (this.enableAntiDrop.isEnabled() && this.actionTimer > 0 && mc.gameSettings.keyBindDrop.isPressed()) {
            ItemStack var1 = mc.thePlayer.getHeldItem();
            if (var1 != null && (var1.getItem() == Items.bucket || var1.getItem() == Items.lava_bucket || var1.getItem() == Items.water_bucket)) {
               KeyBinding.setKeyBindState(mc.gameSettings.keyBindDrop.getKeyCode(), false);
            }
         }

         boolean var14 = false;
         boolean var2 = false;
         int var3 = 5;
         if (this.detectionRange.getValue() == 0.0D) {
            if (this.isPlayerInDanger()) {
               var2 = true;
               var3 = 5;
            }
         } else {
            var2 = true;
            var3 = (int)this.detectionRange.getValue();
         }

         if (var2) {
            float var4 = mc.playerController.getBlockReachDistance();
            BlockPos var5 = this.findNearestLiquid(var3, var4);
            if (var5 != null) {
               Material var6 = mc.theWorld.getBlockState(var5).getBlock().getMaterial();
               int var7 = this.findBucketSlot(mc.thePlayer.inventory, var6);
               if (var7 != -1) {
                  var14 = true;
                  this.lookAtBlock(var5);
                  mc.entityRenderer.getMouseOver(1.0F);
                  if (!this.wasActive) {
                     this.originalSlot = mc.thePlayer.inventory.currentItem;
                  }

                  mc.thePlayer.inventory.currentItem = var7;
                  this.actionTimer = 10;
                  ItemStack var8 = mc.thePlayer.inventory.getStackInSlot(var7);
                  boolean var9;
                  if (var8 != null) {
                     var9 = var6 == Material.lava && var8.getItem() == Items.lava_bucket;
                     boolean var10 = var6 == Material.water && var8.getItem() == Items.water_bucket;
                     if (var9 && !this.enableLavaReset.isEnabled()) {
                        var14 = false;
                        mc.thePlayer.inventory.currentItem = this.originalSlot != -1 ? this.originalSlot : mc.thePlayer.inventory.currentItem;
                        return;
                     }

                     if (var10 && !this.enableWaterReset.isEnabled()) {
                        var14 = false;
                        mc.thePlayer.inventory.currentItem = this.originalSlot != -1 ? this.originalSlot : mc.thePlayer.inventory.currentItem;
                        return;
                     }
                  }

                  if (this.rightClickDelayTimerField != null) {
                     try {
                        this.rightClickDelayTimerField.setInt(mc, 0);
                     } catch (Exception var13) {
                     }
                  }

                  var9 = false;
                  if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
                     BlockPos var15 = mc.objectMouseOver.getBlockPos();
                     if (var15.equals(var5) && mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), var15, mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec)) {
                        mc.thePlayer.swingItem();
                        var9 = true;
                     }
                  }

                  if (!var9 && mc.thePlayer.getDistanceSq(var5) < (double)(var4 * var4)) {
                     EnumFacing var16 = EnumFacing.UP;
                     Vec3 var11 = new Vec3((double)var5.getX() + 0.5D, (double)var5.getY() + 0.5D, (double)var5.getZ() + 0.5D);
                     if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), var5, var16, var11)) {
                        mc.thePlayer.swingItem();
                        var9 = true;
                     }
                  }

                  if (!var9) {
                     KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                     KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
                  }

                  if (this.rightClickDelayTimerField != null) {
                     try {
                        this.rightClickDelayTimerField.setInt(mc, 0);
                     } catch (Exception var12) {
                     }
                  }
               }
            }
         }

         if (this.wasActive && !var14) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            if (this.originalSlot != -1) {
               mc.thePlayer.inventory.currentItem = this.originalSlot;
               this.originalSlot = -1;
            }
         }

         this.wasActive = var14;
      }
   }

   private boolean isPlayerInDanger() {
      if (!mc.thePlayer.isInsideOfMaterial(Material.lava) && !mc.thePlayer.isInsideOfMaterial(Material.water)) {
         AxisAlignedBB var1 = mc.thePlayer.getEntityBoundingBox().expand(0.1D, 0.5D, 0.1D);
         int var2 = MathHelper.floor_double(var1.minX);
         int var3 = MathHelper.floor_double(var1.maxX + 1.0D);
         int var4 = MathHelper.floor_double(var1.minY);
         int var5 = MathHelper.floor_double(var1.maxY + 1.0D);
         int var6 = MathHelper.floor_double(var1.minZ);
         int var7 = MathHelper.floor_double(var1.maxZ + 1.0D);

         for(int var8 = var2; var8 < var3; ++var8) {
            for(int var9 = var4; var9 < var5; ++var9) {
               for(int var10 = var6; var10 < var7; ++var10) {
                  BlockPos var11 = new BlockPos(var8, var9, var10);
                  IBlockState var12 = mc.theWorld.getBlockState(var11);
                  Material var13 = var12.getBlock().getMaterial();
                  if (var13 == Material.lava || var13 == Material.water) {
                     return true;
                  }
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private int findBucketSlot(InventoryPlayer var1, Material var2) {
      int var3 = -1;
      int var4 = -1;
      boolean var5 = var2 == Material.lava && this.enableEmptyBucketLava.isEnabled() || var2 == Material.water && this.enableEmptyBucketWater.isEnabled();
      boolean var6 = var2 == Material.lava && this.enableLavaReset.isEnabled() || var2 == Material.water && this.enableWaterReset.isEnabled();

      for(int var7 = 0; var7 < 9; ++var7) {
         ItemStack var8 = var1.getStackInSlot(var7);
         if (var8 != null) {
            if (var5 && var8.getItem() == Items.bucket) {
               var3 = var7;
               break;
            }

            if (var6) {
               if (var2 == Material.lava && var8.getItem() == Items.lava_bucket) {
                  if (var4 == -1) {
                     var4 = var7;
                  }
               } else if (var2 == Material.water && var8.getItem() == Items.water_bucket && var4 == -1) {
                  var4 = var7;
               }
            }
         }
      }

      return var3 != -1 ? var3 : var4;
   }

   private BlockPos findNearestLiquid(int var1, float var2) {
      BlockPos var3 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
      BlockPos var4 = null;
      double var5 = Double.MAX_VALUE;
      double var7 = (double)(var2 * var2);
      int var9 = var1;

      for(int var10 = -var1; var10 <= var9; ++var10) {
         for(int var11 = -var9; var11 <= var9; ++var11) {
            for(int var12 = -var9; var12 <= var9; ++var12) {
               BlockPos var13 = var3.add(var10, var11, var12);
               double var14 = mc.thePlayer.getDistanceSq(var13);
               if (!(var14 > var7)) {
                  IBlockState var16 = mc.theWorld.getBlockState(var13);
                  Block var17 = var16.getBlock();
                  Material var18 = var17.getMaterial();
                  if (var18 == Material.lava || var18 == Material.water) {
                     int var19 = var17.getMetaFromState(var16);
                     if (var19 == 0 && var14 < var5) {
                        var5 = var14;
                        var4 = var13;
                     }
                  }
               }
            }
         }
      }

      return var4;
   }

   private void lookAtBlock(BlockPos var1) {
      double var2 = (double)var1.getX() + 0.5D - mc.thePlayer.posX;
      double var4 = (double)var1.getY() + 0.5D - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
      double var6 = (double)var1.getZ() + 0.5D - mc.thePlayer.posZ;
      double var8 = (double)MathHelper.sqrt_double(var2 * var2 + var6 * var6);
      float var10 = (float)(MathHelper.atan2(var6, var2) * 180.0D / 3.141592653589793D) - 90.0F;
      float var11 = (float)(-(MathHelper.atan2(var4, var8) * 180.0D / 3.141592653589793D));
      mc.thePlayer.rotationPitch = var11;
      mc.thePlayer.rotationYaw = var10;
   }
}
