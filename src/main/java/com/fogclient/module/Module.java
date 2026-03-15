package com.fogclient.module;

import com.fogclient.setting.Setting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class Module {
   protected String name;
   protected String description;
   protected boolean enabled;
   protected int keybind;
   protected int actionKeybind;
   protected Category category;
   protected List<Setting> settings = new ArrayList();
   protected static final Minecraft mc = Minecraft.getMinecraft();

   public Module(String var1, String var2, Category var3) {
      this.name = var1;
      this.description = var2;
      this.category = var3;
      this.enabled = false;
      this.keybind = 0;
      this.actionKeybind = 0;
   }

   public abstract void onEnable();

   public abstract void onDisable();

   public void onEvent(Event var1) {
   }

   public void onTick() {
   }

   public void onTickStart() {
   }

   public void onRenderTick() {
   }

   public void onLivingUpdate() {
   }

   public void onKeyPressed() {
   }

   public void onAction() {
   }

   public boolean isKeyJustPressed() {
      return false;
   }

   public boolean isAction() {
      return false;
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
      if (var1) {
         this.onEnable();
      } else {
         this.onDisable();
      }

   }

   public void toggle() {
	   this.setEnabled(!this.enabled);
   }

   public int getKeybind() {
      return this.keybind;
   }

   public void setKeybind(int var1) {
      this.keybind = var1;
   }

   public int getActionKeybind() {
      return this.actionKeybind;
   }

   public void setActionKeybind(int var1) {
      this.actionKeybind = var1;
   }

   public Category getCategory() {
      return this.category;
   }

   public void addSetting(Setting var1) {
      this.settings.add(var1);
   }

   public List<Setting> getSettings() {
      return this.settings;
   }
}
