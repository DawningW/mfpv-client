package com.gluecode.fpvdrone.gui.screen;

import com.gluecode.fpvdrone.gui.widget.list.DroneBuildList;
import com.gluecode.fpvdrone.gui.screen.addon.DoneFooter;
import com.gluecode.fpvdrone.gui.screen.addon.ServerTitleWikiHeader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;

public class DroneBuildScreen extends FpvScreen {
  private DroneBuildList droneBuildList;

  public DroneBuildScreen(
    Screen previousScreen
  ) {
    super(
      previousScreen,
      new ServerTitleWikiHeader(I18n.get("fpvdrone.settings.fpvsettings")),
      new DoneFooter()
    );
  }

  // func_231160_c_ = init
  @Override
  protected void init() {
    super.init();
    this.droneBuildList = new DroneBuildList(this);
    this.addWidget(this.droneBuildList);
  }

  // func_230430_a_ = render
  @Override
  public void renderCustom(
    PoseStack matrixStack,
    int mouseX,
    int mouseY,
    float partialTicks
  ) {
    this.droneBuildList.render(matrixStack, mouseX, mouseY, partialTicks);
  }
}
