package com.fogclient.setting;

import com.fogclient.module.Module;

public class BooleanSetting extends Setting {
   private boolean enabled;

   public BooleanSetting(String var1, Module var2, boolean var3) {
      super(var1, var2);
      this.enabled = var3;
   }

   public BooleanSetting(String var1, boolean var2) {
      super(var1);
      this.enabled = var2;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean getValue() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public void toggle() {
      this.enabled = !this.enabled;
   }
}
