package com.gluecode.fpvdrone.gui.screen;

import com.gluecode.fpvdrone.gui.widget.list.ChannelMappingList;
import com.gluecode.fpvdrone.gui.screen.addon.DoneFooter;
import com.gluecode.fpvdrone.gui.screen.addon.ServerTitleWikiHeader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.language.I18n;

public class ChannelMappingScreen extends FpvScreen {
  public long time;
  private ChannelMappingList channelMappingList;

  public ChannelMappingScreen(
    ModelSettingsScreen previousScreen
  ) {
    super(
      previousScreen,
      new ServerTitleWikiHeader(I18n.get("fpvdrone.settings.fpvsettings")),
      new DoneFooter()
    );
  }

  @Override
  protected void init() {
    super.init();
    
    this.channelMappingList = new ChannelMappingList(this);
    this.addWidget(this.channelMappingList);
  }

  // func_230430_a_ = render
  @Override
  public void renderCustom(
    PoseStack matrixStack,
    int mouseX,
    int mouseY,
    float partialTicks
  ) {
    this.channelMappingList.render(
      matrixStack,
      mouseX,
      mouseY,
      partialTicks
    );
  }
}
