package com.fogclient.setting;

import com.fogclient.module.Module;

public abstract class Setting {
   private String name;
   private Module parent;
   private boolean hidden = false;

   public Setting(String var1, Module var2) {
      this.name = var1;
      this.parent = var2;
      if (this.parent != null) {
         this.parent.addSetting(this);
      }

   }

   public Setting(String var1) {
      this.name = var1;
      this.parent = null;
   }

   public String getName() {
      return this.name;
   }

   public Module getParent() {
      return this.parent;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public void setHidden(boolean var1) {
      this.hidden = var1;
   }
}
