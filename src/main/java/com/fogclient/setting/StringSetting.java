package com.fogclient.setting;

import com.fogclient.module.Module;

public class StringSetting extends Setting {
   private String value;
   private String text;

   public StringSetting(String var1, Module var2, String var3) {
      super(var1, var2);
      this.value = var3;
      this.text = var3;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String var1) {
      this.value = var1;
      if (this.text == null) {
         this.text = var1;
      }

   }

   public String getText() {
      return this.text;
   }

   public void setText(String var1) {
      this.text = var1;
   }
}
