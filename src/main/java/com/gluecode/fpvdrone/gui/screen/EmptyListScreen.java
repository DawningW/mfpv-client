package com.gluecode.fpvdrone.gui.screen;

import com.gluecode.fpvdrone.gui.widget.list.FPVList;
import com.gluecode.fpvdrone.gui.screen.addon.ScreenAddon;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;

import javax.annotation.Nullable;

public class EmptyListScreen extends FpvScreen {
  FPVList list;
  
  public EmptyListScreen(
    Screen previousScreen,
    @Nullable ScreenAddon header,
    @Nullable ScreenAddon footer
  ) {
    super(
      previousScreen,
      header,
      footer
    );
  }
  
  @Override
  protected void init() {
    super.init();
    this.list = new FPVList(this);
    
    // Empty list must not be added as a child.
    // Children will capture mouse events.
    // Adding the list as a child will cause any buttons rendered on top to
    // to not receive mouse events.
    //    this.children.add(this.list);
  }
  
  @Override
  public void renderCustom(
    PoseStack matrixStack,
    int mouseX,
    int mouseY,
    float partialTicks
  ) {
    this.list.render(matrixStack, mouseX, mouseY, partialTicks);
  }
}
