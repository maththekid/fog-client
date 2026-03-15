package com.fogclient.setting;

import com.fogclient.module.Module;

public class KeySetting extends Setting {
   private int keyCode;

   public KeySetting(String var1, Module var2, int var3) {
      super(var1, var2);
      this.keyCode = var3;
   }

   public int getKeyCode() {
      return this.keyCode;
   }

   public void setKeyCode(int var1) {
      this.keyCode = var1;
   }
}
