package com.fogclient.setting;

import com.fogclient.module.Module;

public class ActionSetting extends Setting {
   private Runnable action;

   public ActionSetting(String var1, Module var2, Runnable var3) {
      super(var1, var2);
      this.action = var3;
   }

   public void perform() {
      if (this.action != null) {
         this.action.run();
      }

   }
}
