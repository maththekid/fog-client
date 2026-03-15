package com.fogclient.setting;

import com.fogclient.module.Module;
import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {
   private int index;
   private List<String> modes;

   public ModeSetting(String var1, Module var2, String var3, String... var4) {
      super(var1, var2);
      this.modes = Arrays.asList(var4);
      this.index = this.modes.indexOf(var3);
   }

   public ModeSetting(String var1, String var2, String... var3) {
      super(var1);
      this.modes = Arrays.asList(var3);
      this.index = this.modes.indexOf(var2);
   }

   public String getMode() {
      if (this.index < 0 || this.index >= this.modes.size()) {
         this.index = 0;
      }

      return (String)this.modes.get(this.index);
   }

   public String getValue() {
      if (this.index < 0 || this.index >= this.modes.size()) {
         this.index = 0;
      }

      return (String)this.modes.get(this.index);
   }

   public boolean is(String var1) {
      return this.index == this.modes.indexOf(var1);
   }

   public void cycle() {
      if (this.index < this.modes.size() - 1) {
         ++this.index;
      } else {
         this.index = 0;
      }

   }

   public List<String> getModes() {
      return this.modes;
   }

   public void setMode(String var1) {
      int var2 = this.modes.indexOf(var1);
      this.index = var2 >= 0 ? var2 : 0;
   }
}
