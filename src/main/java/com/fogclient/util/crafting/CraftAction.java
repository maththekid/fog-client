package com.fogclient.util.crafting;

public class CraftAction {
   public CraftActionType type;
   public int fromSlot;
   public int toSlot;

   public CraftAction(CraftActionType var1, int var2, int var3) {
      this.type = var1;
      this.fromSlot = var2;
      this.toSlot = var3;
   }
}
