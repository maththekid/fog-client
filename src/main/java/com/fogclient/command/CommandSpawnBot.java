package com.fogclient.command;

import com.fogclient.entity.DificuldadeBot;
import com.fogclient.entity.EntityPvPBot;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandSpawnBot extends CommandBase {
   public String getCommandName() {
      return "spawnbot";
   }

   public String getCommandUsage(ICommandSender var1) {
      return "/spawnbot <dano_em_coracoes> <dificuldade (facil|intermediario|dificil|cheater)>\nExemplo: /spawnbot 4 facil";
   }

   public void processCommand(ICommandSender var1, String[] var2) {
      if (!(var1 instanceof EntityPlayer)) {
         var1.addChatMessage(new ChatComponentText("Apenas jogadores podem usar este comando."));
      } else if (var2.length == 0) {
         var1.addChatMessage(new ChatComponentText(this.getCommandUsage(var1)));
      } else {
         EntityPlayer var3 = (EntityPlayer)var1;
         World var4 = var3.worldObj;
         String var5 = var3.getName();
         float var6 = 8.0F;

         try {
            if (var2.length >= 1) {
               var6 = Float.parseFloat(var2[0]) * 2.0F;
            }
         } catch (NumberFormatException var10) {
            var1.addChatMessage(new ChatComponentText("Dano invÃ¡lido, usando padrÃ£o de 4 coraÃ§Ãµes."));
         }

         String var7 = var2.length >= 2 ? var2[1].toLowerCase() : "facil";
         DificuldadeBot var8 = DificuldadeBot.FACIL;
         if (!var7.startsWith("fac") && !var7.startsWith("eas")) {
            if (!var7.startsWith("int") && !var7.startsWith("med")) {
               if (!var7.startsWith("dif") && !var7.startsWith("har")) {
                  if (!var7.startsWith("che")) {
                     var1.addChatMessage(new ChatComponentText("Dificuldade invÃ¡lida. Use: facil, intermediario, dificil ou cheater."));
                     return;
                  }

                  var8 = DificuldadeBot.CHEATER;
               } else {
                  var8 = DificuldadeBot.DIFICIL;
               }
            } else {
               var8 = DificuldadeBot.INTERMEDIARIO;
            }
         } else {
            var8 = DificuldadeBot.FACIL;
         }

         EntityPvPBot var9 = new EntityPvPBot(var4, var8);
         var9.setPosition(var3.posX, var3.posY, var3.posZ);
         var9.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue((double)var6);
         var9.setNickExibido(var5);
         if (!var4.isRemote) {
            var4.spawnEntityInWorld(var9);
            var1.addChatMessage(new ChatComponentText("Bot de PvP invocado com dano de " + var6 / 2.0F + " coraÃ§Ãµes! Dificuldade: " + var7));
         }

      }
   }

   public int getRequiredPermissionLevel() {
      return 0;
   }
}
