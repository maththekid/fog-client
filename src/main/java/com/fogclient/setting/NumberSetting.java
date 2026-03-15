package com.fogclient.setting;

import com.fogclient.module.Module;

public class NumberSetting extends Setting {
   private double value;
   private double min;
   private double max;
   private double increment;

   public NumberSetting(String var1, Module var2, double var3, double var5, double var7, double var9) {
      super(var1, var2);
      this.value = var3;
      this.min = var5;
      this.max = var7;
      this.increment = var9;
   }

   public NumberSetting(String var1, double var2, double var4, double var6, double var8) {
      super(var1);
      this.value = var2;
      this.min = var4;
      this.max = var6;
      this.increment = var8;
   }

   public double getValue() {
      return this.value;
   }

   public void setValue(double var1) {
      double var3 = 1.0D / this.increment;
      this.value = (double)Math.round(Math.max(this.min, Math.min(this.max, var1)) * var3) / var3;
   }

   public double getMin() {
      return this.min;
   }

   public double getMax() {
      return this.max;
   }

   public double getIncrement() {
      return this.increment;
   }
}
