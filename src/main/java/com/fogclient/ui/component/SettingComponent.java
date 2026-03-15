package com.fogclient.ui.component;

import com.fogclient.setting.Setting;

public abstract class SettingComponent extends Component {
   protected Setting setting;
   protected ModuleButton parent;
   protected int offset;
   protected int x;
   protected int y;
   protected int width;
   protected int height;

   public SettingComponent(Setting var1, ModuleButton var2, int var3) {
      this.setting = var1;
      this.parent = var2;
      this.offset = var3;
   }

   public void setOffset(int var1) {
      this.offset = var1;
   }

   public int getHeight() {
      return 16;
   }

   public void updatePosition(int var1, int var2, int var3) {
      this.x = var1;
      this.y = var2 + this.offset;
      this.width = var3;
      this.height = this.getHeight();
   }
}
