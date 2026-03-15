package com.fogclient.util.crafting;

public class RecraftRecipe {
   public RecipeType recipeType;
   public int bowlSlot;
   public int ingredient1Slot;
   public int ingredient2Slot;

   public RecraftRecipe(RecipeType var1, int var2, int var3, int var4) {
      this.recipeType = var1;
      this.bowlSlot = var2;
      this.ingredient1Slot = var3;
      this.ingredient2Slot = var4;
   }
}
