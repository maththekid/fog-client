package com.fogclient.module.render;

import com.fogclient.module.Category;
import com.fogclient.module.Module;
import com.fogclient.setting.ActionSetting;
import com.fogclient.setting.StringSetting;
import com.fogclient.util.CustomFontRenderer;
import com.mojang.authlib.GameProfile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EscolheNickModule extends Module {
   public static Map<String, String> nickMap = new HashMap();
   private static EscolheNickModule instance;
   private boolean fontRendererInjected = false;
   private StringSetting realNick;
   private StringSetting newNick;
   private ActionSetting applyButton;
   private ActionSetting resetButton;
   private boolean commandsRegistered = false;

   public EscolheNickModule() {
      super("EscolheNick", "Change players' nicknames locally", Category.RENDER);
      instance = this;
      this.realNick = new StringSetting("Nick Real", this, "");
      this.newNick = new StringSetting("Novo Nick", this, "");
      this.applyButton = new ActionSetting("Aplicar", this, new Runnable() {
         public void run() {
            if (!EscolheNickModule.this.realNick.getValue().isEmpty() && !EscolheNickModule.this.newNick.getValue().isEmpty()) {
               EscolheNickModule.nickMap.put(EscolheNickModule.this.realNick.getValue(), EscolheNickModule.this.newNick.getValue());
               if (EscolheNickModule.mc.thePlayer != null) {
                  EscolheNickModule.mc.thePlayer.addChatMessage(new ChatComponentText("Â§aNick alterado: " + EscolheNickModule.this.realNick.getValue() + " -> " + EscolheNickModule.this.newNick.getValue()));
                  EscolheNickModule.mc.thePlayer.addChatMessage(new ChatComponentText("Â§7Relogue no servidor para aplicar completamente."));
               }

               EscolheNickModule.this.applyTabNames();
            } else {
               if (EscolheNickModule.mc.thePlayer != null) {
                  EscolheNickModule.mc.thePlayer.addChatMessage(new ChatComponentText("Â§cPreencha os campos de nick!"));
               }

            }
         }
      });
      this.resetButton = new ActionSetting("Resetar", this, new Runnable() {
         public void run() {
            EscolheNickModule.nickMap.clear();
            if (EscolheNickModule.mc.thePlayer != null) {
               EscolheNickModule.mc.thePlayer.addChatMessage(new ChatComponentText("Â§aNicks resetados."));
            }

            EscolheNickModule.this.applyTabNames();
         }
      });
   }

   public static EscolheNickModule getInstance() {
      return instance;
   }

   public void onEnable() {
      this.injectFontRenderer();
      this.registerCommands();
      this.applyTabNames();
   }

   public void onDisable() {
      this.applyTabNames();
   }

   private void injectFontRenderer() {
      if (!this.fontRendererInjected) {
         if (!(mc.fontRendererObj instanceof CustomFontRenderer)) {
            CustomFontRenderer var1 = new CustomFontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
            if (mc.gameSettings.language != null) {
               var1.setUnicodeFlag(mc.isUnicode());
               var1.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
            }

            mc.fontRendererObj = var1;
            if (mc.getResourceManager() instanceof IReloadableResourceManager) {
               ((IReloadableResourceManager)mc.getResourceManager()).registerReloadListener(mc.fontRendererObj);
            }

            this.fontRendererInjected = true;
         }

      }
   }

   private void registerCommands() {
      if (!this.commandsRegistered) {
         try {
            ClientCommandHandler.instance.registerCommand(new EscolheNickModule.CommandNickEscolher());
            ClientCommandHandler.instance.registerCommand(new EscolheNickModule.CommandNickReset());
            this.commandsRegistered = true;
         } catch (Exception var2) {
            var2.printStackTrace();
         }

      }
   }

   public void onEvent(Event var1) {
      if (var1 instanceof NameFormat) {
         NameFormat var2 = (NameFormat)var1;
         if (!this.isEnabled() || nickMap.isEmpty()) {
            return;
         }

         String var3 = var2.username;
         Iterator var4 = nickMap.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            if (var3.equals(var5.getKey())) {
               var2.displayname = (String)var5.getValue();
            }
         }
      }

   }

   public void onTick() {
      this.applyTabNames();
   }

   private void applyTabNames() {
      if (mc.theWorld != null && mc.getNetHandler() != null) {
         Iterator var1;
         NetworkPlayerInfo var2;
         if (this.isEnabled() && !nickMap.isEmpty()) {
            var1 = mc.getNetHandler().getPlayerInfoMap().iterator();

            while(var1.hasNext()) {
               var2 = (NetworkPlayerInfo)var1.next();
               GameProfile var3 = var2.getGameProfile();
               String var4 = var3.getName();
               if (nickMap.containsKey(var4)) {
                  var2.setDisplayName(new ChatComponentText((String)nickMap.get(var4)));
               } else if (var2.getDisplayName() != null && !var2.getDisplayName().getFormattedText().equals(var4)) {
                  var2.setDisplayName(null);
               }
            }

         } else {
            var1 = mc.getNetHandler().getPlayerInfoMap().iterator();

            while(var1.hasNext()) {
               var2 = (NetworkPlayerInfo)var1.next();
               if (var2.getDisplayName() != null) {
                  var2.setDisplayName(null);
               }
            }

         }
      }
   }

   public static class CommandNickReset extends CommandBase {
      public String getCommandName() {
         return "nickreset";
      }

      public String getCommandUsage(ICommandSender var1) {
         return "/nickreset";
      }

      public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
         EscolheNickModule.nickMap.clear();
         var1.addChatMessage(new ChatComponentText("Â§aNicks resetados com sucesso. Â§7(" + EscolheNickModule.nickMap.size() + " ativos)"));
         if (EscolheNickModule.instance != null) {
            EscolheNickModule.instance.applyTabNames();
         }

      }

      public int getRequiredPermissionLevel() {
         return 0;
      }
   }

   public static class CommandNickEscolher extends CommandBase {
      public String getCommandName() {
         return "nickescolher";
      }

      public String getCommandUsage(ICommandSender var1) {
         return "/nickescolher <real> <novo>";
      }

      public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
         if (var2.length >= 2) {
            EscolheNickModule.nickMap.put(var2[0], var2[1]);
            var1.addChatMessage(new ChatComponentText("Nick set: " + var2[0] + " -> " + var2[1]));
            var1.addChatMessage(new ChatComponentText("Relogue no servidor para a alteraÃ§Ã£o de nick ser aplicada corretamente"));
            if (EscolheNickModule.instance != null) {
               EscolheNickModule.instance.applyTabNames();
            }

         }
      }

      public int getRequiredPermissionLevel() {
         return 0;
      }
   }
}
