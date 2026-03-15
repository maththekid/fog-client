package com.fogclient.module;

import com.fogclient.module.combat.AutoRecraftModule;
import com.fogclient.module.combat.BlockHitProModule;
import com.fogclient.module.combat.BowAimbotModule;
import com.fogclient.module.combat.DefaultSoupModule;
import com.fogclient.module.combat.GhostReachModule;
import com.fogclient.module.combat.NoDefenderModule;
import com.fogclient.module.combat.PerfectAimModule;
import com.fogclient.module.combat.RecraftAuxModule;
import com.fogclient.module.combat.SecondClickModule;
import com.fogclient.module.combat.SemDelayHitsModule;
import com.fogclient.module.combat.SoupaModule;
import com.fogclient.module.combat.SuperKBModule;
import com.fogclient.module.manual.ManualModule;
import com.fogclient.module.misc.DiamondCollectorModule;
import com.fogclient.module.misc.LembreteModule;
import com.fogclient.module.misc.TankoSimModule;
import com.fogclient.module.misc.TreinoBotModule;
import com.fogclient.module.misc.TrollModule;
import com.fogclient.module.movement.JumpResetModule;
import com.fogclient.module.movement.LegitBridgeModule;
import com.fogclient.module.player.HelperMLGModule;
import com.fogclient.module.player.JuntaPotesModule;
import com.fogclient.module.player.LavaNoMeuPeModule;
import com.fogclient.module.player.RoubaLavaModule;
import com.fogclient.module.render.AchaFerroModule;
import com.fogclient.module.render.ClickGUIModule;
import com.fogclient.module.render.DestakBauModule;
import com.fogclient.module.render.DestakBigornaModule;
import com.fogclient.module.render.DestakCamaModule;
import com.fogclient.module.render.EscolheNickModule;
import com.fogclient.module.render.InventoryScaleLiteModule;
import com.fogclient.module.render.UltraF5Module;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
   private static ModuleManager instance;
   private List<Module> modules = new ArrayList();

   public ModuleManager() {
      this.registerModules();
   }

   public static ModuleManager getInstance() {
      if (instance == null) {
         instance = new ModuleManager();
      }

      return instance;
   }

   private void registerModules() {
      this.modules.add(new SuperKBModule());
      this.modules.add(new BowAimbotModule());
      this.modules.add(new GhostReachModule());
      this.modules.add(new AutoRecraftModule());
      this.modules.add(new SecondClickModule());
      this.modules.add(new RecraftAuxModule());
      this.modules.add(new DefaultSoupModule());
      this.modules.add(new NoDefenderModule());
      this.modules.add(new SemDelayHitsModule());
      this.modules.add(new BlockHitProModule());
      this.modules.add(new PerfectAimModule());
      this.modules.add(new SoupaModule());
      this.modules.add(new DestakBauModule());
      this.modules.add(new DestakBigornaModule());
      this.modules.add(new DestakCamaModule());
      this.modules.add(new AchaFerroModule());
      this.modules.add(new InventoryScaleLiteModule());
      this.modules.add(new EscolheNickModule());
      this.modules.add(new UltraF5Module());
      this.modules.add(new JumpResetModule());
      this.modules.add(new LegitBridgeModule());
      this.modules.add(new RoubaLavaModule());
      this.modules.add(new JuntaPotesModule());
      this.modules.add(new LavaNoMeuPeModule());
      this.modules.add(new HelperMLGModule());
      this.modules.add(new DiamondCollectorModule());
      this.modules.add(new TankoSimModule());
      this.modules.add(new TreinoBotModule());
      this.modules.add(new LembreteModule());
      this.modules.add(new ClickGUIModule());
      this.modules.add(new TrollModule());
      this.modules.add(new ManualModule());
   }

   public List<Module> getModules() {
      return this.modules;
   }

   public List<Module> getModulesByCategory(Category var1) {
      return (List)this.modules.stream().filter((var1x) -> {
         return var1x.getCategory() == var1;
      }).collect(Collectors.toList());
   }

   public Module getModule(Class<? extends Module> var1) {
      Iterator var2 = this.modules.iterator();

      Module var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Module)var2.next();
      } while(var3.getClass() != var1);

      return var3;
   }

   public Module getModuleByName(String var1) {
      Iterator var2 = this.modules.iterator();

      Module var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (Module)var2.next();
      } while(!var3.getName().equalsIgnoreCase(var1));

      return var3;
   }

   public void onKeyPressed(int var1) {
      if (var1 != 0) {
         Iterator var2 = this.modules.iterator();

         while(var2.hasNext()) {
            Module var3 = (Module)var2.next();
            if (var3.getKeybind() == var1) {
               boolean var4 = var3.isAction() && var3.getActionKeybind() == var1;
               if (!var4) {
                  var3.toggle();
               }
            }

            if (var3.isAction() && var3.isEnabled() && var3.getActionKeybind() == var1) {
               var3.onAction();
            }
         }

      }
   }
}
