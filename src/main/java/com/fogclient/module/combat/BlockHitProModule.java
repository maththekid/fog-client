package com.fogclient.module.combat;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BlockHitProModule extends Module {
   private int blockTicks = 0;

   public BlockHitProModule() {
      super("BlockHitPro", "Automatically blocks with sword after attacking", Category.COMBAT);
   }

   public void onEnable() {
      this.blockTicks = 0;
   }

   public void onDisable() {
      this.blockTicks = 0;
      if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
         KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
      }

   }

   public void onTick() {
      if (mc.thePlayer != null) {
         if (this.blockTicks > 0 && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            if (this.blockTicks == 2) {
               KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
               KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
            }

            --this.blockTicks;
            if (this.blockTicks == 0) {
               KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            }
         }

      }
   }

   public void onEvent(Event var1) {
      if (var1 instanceof AttackEntityEvent) {
         AttackEntityEvent var2 = (AttackEntityEvent)var1;
         if (var2.entityPlayer == mc.thePlayer && var2.entityPlayer.getHeldItem() != null && var2.entityPlayer.getHeldItem().getItem() instanceof ItemSword) {
            this.blockTicks = 2;
         }
      }

   }
}
