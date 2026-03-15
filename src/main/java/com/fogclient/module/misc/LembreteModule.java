package com.fogclient.module.misc;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.NumberSetting;
import com.fogclient.setting.StringSetting;
import java.time.LocalTime;
import net.minecraft.util.ChatComponentText;

public class LembreteModule extends Module {
   private final NumberSetting hourSetting = new NumberSetting("Hora (0-23)", this, 12.0D, 0.0D, 23.0D, 1.0D);
   private final NumberSetting minuteSetting = new NumberSetting("Minuto (0-59)", this, 0.0D, 0.0D, 59.0D, 1.0D);
   private final StringSetting messageSetting = new StringSetting("Mensagem", this, "Hora do Lembrete!");
   private boolean alarming = false;
   private long alarmStartTime = 0L;
   private long lastMessageTime = 0L;

   public LembreteModule() {
      super("Despertador", "Cria um lembrete com hora marcada.", Category.MISC);
   }

   public void onEnable() {
      this.alarming = false;
      this.alarmStartTime = 0L;
      this.lastMessageTime = 0L;
      if (mc.thePlayer != null) {
         mc.thePlayer.addChatMessage(new ChatComponentText("Â§a[Lembrete] Â§fMÃ³dulo ativado. O lembrete tocarÃ¡ Ã s " + String.format("%02d:%02d", (int)this.hourSetting.getValue(), (int)this.minuteSetting.getValue())));
      }

   }

   public void onDisable() {
      this.alarming = false;
   }

   public void onTick() {
      if (this.isEnabled()) {
         if (mc.thePlayer != null && mc.theWorld != null) {
            LocalTime var1 = LocalTime.now();
            int var2 = var1.getHour();
            int var3 = var1.getMinute();
            int var4 = (int)this.hourSetting.getValue();
            int var5 = (int)this.minuteSetting.getValue();
            if (!this.alarming && var2 == var4 && var3 == var5) {
               this.alarming = true;
               this.alarmStartTime = System.currentTimeMillis();
               this.lastMessageTime = 0L;
            }

            if (this.alarming) {
               long var6 = System.currentTimeMillis();
               if (var6 - this.alarmStartTime > 60000L) {
                  this.alarming = false;
                  return;
               }

               if (var6 - this.lastMessageTime >= 5000L) {
                  mc.thePlayer.addChatMessage(new ChatComponentText("Â§e[LEMBRETE] Â§f" + this.messageSetting.getValue()));
                  mc.thePlayer.playSound("random.orb", 1.0F, 1.0F);
                  this.lastMessageTime = var6;
               }
            }

         }
      }
   }
}
