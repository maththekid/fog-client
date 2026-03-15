package com.fogclient.module.combat;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.BooleanSetting;
import com.fogclient.setting.ModeSetting;
import com.fogclient.setting.NumberSetting;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SuperKBModule extends Module {
   private final BooleanSetting superKbEnabled = new BooleanSetting("SuperKB", this, true);
   private final ModeSetting kbMode = new ModeSetting("KB Mode", this, "Packet", new String[]{"Packet", "WTap+ReSprint", "Legit"});
   private final BooleanSetting resprintJitter = new BooleanSetting("Jitter", this, false);
   private final NumberSetting resprintTicks = new NumberSetting("Resprint Ticks", this, 1.0D, 1.0D, 5.0D, 1.0D);
   private final BooleanSetting pingSpoofEnabled = new BooleanSetting("PingSpoof", this, false);
   private final NumberSetting pingDelay = new NumberSetting("Ping Delay", this, 100.0D, 0.0D, 1000.0D, 50.0D);
   private final Timer pingTimer = new Timer("FogClient-PingSpoof", true);
   private final Queue<EntityLivingBase> pendingAttacks = new ConcurrentLinkedQueue();
   private long clientTicks = 0L;
   private long resprintAtTick = -1L;

   public SuperKBModule() {
      super("EmpurraTudo", "Deals massive knockback to players.", Category.COMBAT);
   }

   public void onEnable() {
      this.pendingAttacks.clear();
      this.resprintAtTick = -1L;
   }

   public void onDisable() {
      this.pendingAttacks.clear();
      this.resprintAtTick = -1L;
   }

   public void onEvent(Event var1) {
      if (var1 instanceof AttackEntityEvent) {
         this.onAttack((AttackEntityEvent)var1);
      }

   }

   private void onAttack(AttackEntityEvent var1) {
      if (this.isEnabled()) {
         if (mc.thePlayer != null && mc.theWorld != null && var1.target != null) {
            if (var1.target instanceof EntityLivingBase) {
               final EntityLivingBase var2 = (EntityLivingBase)var1.target;
               if (this.pingSpoofEnabled.isEnabled()) {
                  this.pingTimer.schedule(new TimerTask() {
                     public void run() {
                        SuperKBModule.this.pendingAttacks.add(var2);
                     }
                  }, (long)this.pingDelay.getValue());
               } else {
                  this.applySuperKB(var2);
               }

            }
         }
      }
   }

   private void applySuperKB(EntityLivingBase var1) {
      if (this.superKbEnabled.isEnabled()) {
         EntityPlayerSP var2 = mc.thePlayer;
         if (var2 != null && var2.isSprinting() && var1.hurtTime >= 5) {
            String var3 = this.kbMode.getValue();
            if (var3.equals("Packet")) {
               var2.sendQueue.addToSendQueue(new C0BPacketEntityAction(var2, Action.STOP_SPRINTING));
               var2.sendQueue.addToSendQueue(new C0BPacketEntityAction(var2, Action.START_SPRINTING));
            } else if (var3.equals("WTap+ReSprint")) {
               var2.setSprinting(false);
               int var4 = 0;
               if (this.resprintJitter.isEnabled()) {
                  var4 = ThreadLocalRandom.current().nextInt(0, 2);
               }

               this.resprintAtTick = this.clientTicks + Math.max(1L, (long)this.resprintTicks.getValue()) + (long)var4;
            } else {
               var2.setSprinting(false);
            }
         }

      }
   }

   public void onTick() {
      if (this.isEnabled()) {
         if (mc.thePlayer != null && mc.theWorld != null) {
            ++this.clientTicks;

            EntityLivingBase var1;
            while((var1 = (EntityLivingBase)this.pendingAttacks.poll()) != null) {
               this.applySuperKB(var1);
            }

            if (this.resprintAtTick != -1L && this.clientTicks >= this.resprintAtTick && mc.thePlayer != null) {
               EntityPlayerSP var2 = mc.thePlayer;
               boolean var3 = var2.movementInput != null && var2.movementInput.moveForward > 0.0F;
               if (var3 && !var2.isSneaking() && !var2.isUsingItem()) {
                  var2.setSprinting(true);
               }

               this.resprintAtTick = -1L;
            }

         }
      }
   }
}
