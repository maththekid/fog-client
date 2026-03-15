package com.fogclient.mixin;

import com.fogclient.module.combat.ghostreach.GhostNettyHandler;
import com.fogclient.module.combat.ghostreach.GhostReachCore;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetworkManager.class})
public class MixinNetworkManager {
   @Inject(
      method = {"channelActive"},
      at = {@At("RETURN")},
      remap = false
   )
   private void onChannelActive(ChannelHandlerContext var1, CallbackInfo var2) {
      ChannelPipeline var3 = var1.pipeline();
      if (var3.get("ghost_handler") == null) {
         var3.addBefore("packet_handler", "ghost_handler", new GhostNettyHandler(GhostReachCore.INSTANCE));
      }

   }
}
