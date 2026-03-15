package com.fogclient.command;

import com.fogclient.entity.EntityPvPBot;
import java.util.Iterator;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandKillBot extends CommandBase {
   public String getCommandName() {
      return "killbot";
   }

   public String getCommandUsage(ICommandSender var1) {
      return "/killbot";
   }

   public void processCommand(ICommandSender var1, String[] var2) {
      if (!(var1 instanceof EntityPlayer)) {
         var1.addChatMessage(new ChatComponentText("Apenas jogadores podem usar este comando."));
      } else {
         EntityPlayer var3 = (EntityPlayer)var1;
         World var4 = var3.worldObj;
         int var5 = 0;
         if (!var4.isRemote) {
            Iterator var6 = var4.loadedEntityList.iterator();

            while(var6.hasNext()) {
               Object var7 = var6.next();
               if (var7 instanceof EntityPvPBot) {
                  ((EntityPvPBot)var7).setDead();
                  ++var5;
               }
            }

            if (var5 == 1) {
               var1.addChatMessage(new ChatComponentText("Removido 1 bot do mundo."));
            } else {
               var1.addChatMessage(new ChatComponentText("Removidos " + var5 + " bots do mundo."));
            }
         }

      }
   }

   public int getRequiredPermissionLevel() {
      return 0;
   }
}
