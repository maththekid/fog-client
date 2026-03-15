package com.fogclient.module.misc;

import com.fogclient.entity.EntityPvPBot;
import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.ActionSetting;
import com.fogclient.setting.ModeSetting;
import com.fogclient.setting.NumberSetting;
import java.util.Iterator;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class TreinoBotModule extends Module {
   private NumberSetting damageSetting = new NumberSetting("Dano (CoraÃ§Ãµes)", this, 4.0D, 0.5D, 20.0D, 0.5D);
   private ModeSetting difficultySetting = new ModeSetting("Dificuldade", this, "FÃ¡cil", new String[]{"FÃ¡cil", "MÃ©dio", "DifÃ\u00adcil", "Cheater"});
   private ActionSetting spawnButton = new ActionSetting("Spawnar Bot", this, new Runnable() {
      public void run() {
         TreinoBotModule.this.spawnBot();
      }
   });
   private ActionSetting killButton = new ActionSetting("Remover Bots", this, new Runnable() {
      public void run() {
         TreinoBotModule.this.killBots();
      }
   });

   public TreinoBotModule() {
      super("BotPvP", "Spawns a bot to train PvP.", Category.MISC);
   }

   private void spawnBot() {
      if (mc.thePlayer != null && mc.theWorld != null) {
         double var1 = this.damageSetting.getValue();
         String var3 = this.difficultySetting.getValue();
         String var4 = "facil";
         if (var3.equals("MÃ©dio")) {
            var4 = "intermediario";
         } else if (var3.equals("DifÃ\u00adcil")) {
            var4 = "dificil";
         } else if (var3.equals("Cheater")) {
            var4 = "cheater";
         }

         mc.thePlayer.sendChatMessage("/spawnbot " + var1 + " " + var4);
      }
   }

   private void killBots() {
      if (mc.thePlayer != null) {
         mc.thePlayer.sendChatMessage("/killbot");
      }
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onEvent(Event var1) {
      if (var1 instanceof PlayerLoggedInEvent) {
         PlayerLoggedInEvent var2 = (PlayerLoggedInEvent)var1;
         if (!var2.player.worldObj.isRemote) {
            int var3 = 0;
            Iterator var4 = var2.player.worldObj.loadedEntityList.iterator();

            while(var4.hasNext()) {
               Object var5 = var4.next();
               if (var5 instanceof EntityPvPBot) {
                  ((EntityPvPBot)var5).setDead();
                  ++var3;
               }
            }
         }
      }

   }
}
