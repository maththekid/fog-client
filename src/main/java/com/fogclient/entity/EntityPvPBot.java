package com.fogclient.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityPvPBot extends EntityZombie {
   private DificuldadeBot dificuldade;
   private int wTapCooldown;
   private int ticksDesdeUltimoHitRecebido;
   private String nickExibido;

   public EntityPvPBot(World var1) {
      this(var1, DificuldadeBot.FACIL);
   }

   public EntityPvPBot(World var1, DificuldadeBot var2) {
      super(var1);
      this.wTapCooldown = 0;
      this.ticksDesdeUltimoHitRecebido = 100;
      this.nickExibido = "TreinoBOT";
      this.dificuldade = var2;
      this.isImmuneToFire = true;
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10000.0D);
      this.setHealth(10000.0F);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(0.287D);
   }

   public void setNickExibido(String var1) {
      this.nickExibido = var1;
      this.setCustomNameTag("Â§c[BOT]Â§r " + this.nickExibido);
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      this.setHealth(this.getMaxHealth());
      ++this.ticksDesdeUltimoHitRecebido;
      EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 64.0D);
      if (var1 != null && !var1.capabilities.isCreativeMode && !var1.isDead) {
         double var2 = var1.posX - this.posX;
         double var4 = var1.posZ - this.posZ;
         this.rotationYaw = (float)(Math.atan2(var4, var2) * 180.0D / 3.141592653589793D) - 90.0F;
         this.rotationYawHead = this.rotationYaw;
         this.renderYawOffset = this.rotationYaw;
         if (this.ticksDesdeUltimoHitRecebido > 10) {
            if (this.wTapCooldown > 0) {
               this.getNavigator().clearPathEntity();
               --this.wTapCooldown;
            } else {
               this.getNavigator().tryMoveToEntityLiving(var1, 1.0D);
            }
         } else {
            this.getNavigator().tryMoveToEntityLiving(var1, 1.0D);
            this.wTapCooldown = 0;
         }

         double var6 = this.getDistanceToEntity(var1);
         double var8 = this.getAlcance();
         if (var6 <= var8) {
            this.swingItem();
            if (var6 <= 3.0D) {
               float var10 = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
               var1.attackEntityFrom(DamageSource.causeMobDamage(this), var10);
            }
         }
      }

   }

   private double getAlcance() {
      switch(this.dificuldade) {
      case FACIL:
         return 2.0D;
      case INTERMEDIARIO:
         return 2.5D;
      case DIFICIL:
         return 3.0D;
      case CHEATER:
         return 3.5D;
      default:
         return 2.0D;
      }
   }

   protected String getLivingSound() {
      return null;
   }

   protected String getHurtSound() {
      return null;
   }

   protected String getDeathSound() {
      return null;
   }

   public boolean attackEntityFrom(DamageSource var1, float var2) {
      boolean var3 = super.attackEntityFrom(var1, var2);
      if (!this.worldObj.isRemote && var3) {
         this.ticksDesdeUltimoHitRecebido = 0;
      }

      return var3;
   }

   public boolean canDespawn() {
      return false;
   }
}
