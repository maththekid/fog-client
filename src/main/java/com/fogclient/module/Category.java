package com.fogclient.module;

public enum Category {
   COMBAT("Guerra"),
   PLAYER("Player"),
   MOVEMENT("Mobilidade"),
   RENDER("Visual"),
   MISC("Utilidades"),
   CLIENT("Client"),
   MANUAL("Manual");

   private final String name;

   private Category(String var3) {
      this.name = var3;
   }

   public String getName() {
      return this.name;
   }
}
