package com.fogclient.config;

import com.fogclient.module.Module;
import com.fogclient.module.ModuleManager;
import com.fogclient.setting.BooleanSetting;
import com.fogclient.setting.KeySetting;
import com.fogclient.setting.ModeSetting;
import com.fogclient.setting.NumberSetting;
import com.fogclient.setting.Setting;
import com.fogclient.setting.StringSetting;
import com.fogclient.ui.component.Panel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ConfigManager {
   private static final File CONFIG_DIR = new File(System.getProperty("user.home"), ".fogclient");
   private static final File CONFIG_FILE;
   private static final File GUI_CONFIG_FILE;
   private static final Gson gson;

   public static void saveConfig() {
      try {
         JsonObject var0 = new JsonObject();
         JsonObject var1 = new JsonObject();
         Iterator var2 = ModuleManager.getInstance().getModules().iterator();

         while(var2.hasNext()) {
            Module var3 = (Module)var2.next();
            JsonObject var4 = new JsonObject();
            var4.addProperty("enabled", var3.isEnabled());
            var4.addProperty("keybind", var3.getKeybind());
            var4.addProperty("actionKeybind", var3.getActionKeybind());
            JsonObject var5 = new JsonObject();
            Iterator var6 = var3.getSettings().iterator();

            while(var6.hasNext()) {
               Setting var7 = (Setting)var6.next();
               if (var7 instanceof BooleanSetting) {
                  var5.addProperty(var7.getName(), ((BooleanSetting)var7).getValue());
               } else if (var7 instanceof NumberSetting) {
                  var5.addProperty(var7.getName(), ((NumberSetting)var7).getValue());
               } else if (var7 instanceof ModeSetting) {
                  var5.addProperty(var7.getName(), ((ModeSetting)var7).getValue());
               } else if (var7 instanceof StringSetting) {
                  var5.addProperty(var7.getName(), ((StringSetting)var7).getValue());
               } else if (var7 instanceof KeySetting) {
                  var5.addProperty(var7.getName(), ((KeySetting)var7).getKeyCode());
               }
            }

            var4.add("settings", var5);
            var1.add(var3.getName(), var4);
         }

         var0.add("modules", var1);
         FileWriter var9 = new FileWriter(CONFIG_FILE);
         gson.toJson(var0, var9);
         var9.close();
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }

   public static void loadConfig() {
      if (CONFIG_FILE.exists()) {
         try {
            JsonParser var0 = new JsonParser();
            JsonObject var1 = var0.parse(new FileReader(CONFIG_FILE)).getAsJsonObject();
            if (var1.has("modules")) {
               JsonObject var2 = var1.getAsJsonObject("modules");
               Iterator var3 = ModuleManager.getInstance().getModules().iterator();

               while(true) {
                  Module var4;
                  JsonObject var5;
                  do {
                     do {
                        if (!var3.hasNext()) {
                           return;
                        }

                        var4 = (Module)var3.next();
                     } while(!var2.has(var4.getName()));

                     var5 = var2.getAsJsonObject(var4.getName());
                     if (var5.has("enabled")) {
                        boolean var6 = var5.get("enabled").getAsBoolean();
                        if (var6 != var4.isEnabled()) {
                           var4.setEnabled(var6);
                        }
                     }

                     if (var5.has("keybind")) {
                        var4.setKeybind(var5.get("keybind").getAsInt());
                     }

                     if (var5.has("actionKeybind")) {
                        var4.setActionKeybind(var5.get("actionKeybind").getAsInt());
                     }
                  } while(!var5.has("settings"));

                  JsonObject var10 = var5.getAsJsonObject("settings");
                  Iterator var7 = var4.getSettings().iterator();

                  while(var7.hasNext()) {
                     Setting var8 = (Setting)var7.next();
                     if (var10.has(var8.getName())) {
                        if (var8 instanceof BooleanSetting) {
                           ((BooleanSetting)var8).setEnabled(var10.get(var8.getName()).getAsBoolean());
                        } else if (var8 instanceof NumberSetting) {
                           ((NumberSetting)var8).setValue(var10.get(var8.getName()).getAsDouble());
                        } else if (var8 instanceof ModeSetting) {
                           ((ModeSetting)var8).setMode(var10.get(var8.getName()).getAsString());
                        } else if (var8 instanceof StringSetting) {
                           ((StringSetting)var8).setValue(var10.get(var8.getName()).getAsString());
                        } else if (var8 instanceof KeySetting) {
                           ((KeySetting)var8).setKeyCode(var10.get(var8.getName()).getAsInt());
                        }
                     }
                  }
               }
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }
   }

   public static void saveGUIConfig(Map<String, Panel> var0) {
      try {
         JsonObject var1 = new JsonObject();
         Iterator var2 = var0.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            JsonObject var4 = new JsonObject();
            var4.addProperty("x", ((Panel)var3.getValue()).getX());
            var4.addProperty("y", ((Panel)var3.getValue()).getY());
            var4.addProperty("open", ((Panel)var3.getValue()).isOpen());
            var1.add((String)var3.getKey(), var4);
         }

         FileWriter var6 = new FileWriter(GUI_CONFIG_FILE);
         gson.toJson(var1, var6);
         var6.close();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public static Map<String, ConfigManager.PanelConfig> loadGUIConfig() {
      HashMap var0 = new HashMap();
      if (!GUI_CONFIG_FILE.exists()) {
         return var0;
      } else {
         try {
            JsonParser var1 = new JsonParser();
            JsonObject var2 = var1.parse(new FileReader(GUI_CONFIG_FILE)).getAsJsonObject();
            Iterator var3 = var2.entrySet().iterator();

            while(var3.hasNext()) {
               Entry var4 = (Entry)var3.next();
               JsonObject var5 = ((JsonElement)var4.getValue()).getAsJsonObject();
               ConfigManager.PanelConfig var6 = new ConfigManager.PanelConfig();
               var6.x = var5.get("x").getAsInt();
               var6.y = var5.get("y").getAsInt();
               var6.open = var5.get("open").getAsBoolean();
               var0.put(var4.getKey(), var6);
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         return var0;
      }
   }

   static {
      CONFIG_FILE = new File(CONFIG_DIR, "config.json");
      GUI_CONFIG_FILE = new File(CONFIG_DIR, "gui.json");
      gson = (new GsonBuilder()).setPrettyPrinting().create();
      if (!CONFIG_DIR.exists()) {
         CONFIG_DIR.mkdirs();
      }

   }

   public static class PanelConfig {
      public int x;
      public int y;
      public boolean open;
   }
}
