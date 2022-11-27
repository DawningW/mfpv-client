package com.gluecode.fpvdrone.gui.screen;

import com.gluecode.fpvdrone.gui.widget.list.ModelSettingsList;
import com.gluecode.fpvdrone.gui.screen.addon.ServerTitleWikiHeader;
import com.gluecode.fpvdrone.gui.screen.addon.WizardDoneFooter;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;

public class ModelSettingsScreen extends FpvScreen {
  private ModelSettingsList list;

  public ModelSettingsScreen(
    Screen previousScreen
  ) {
    super(
      previousScreen,
      new ServerTitleWikiHeader(I18n.get("fpvdrone.settings.fpvsettings")),
      new WizardDoneFooter()
    );
  }

  // func_231160_c_ = init
  @Override
  protected void init() {
    super.init();
    this.list = new ModelSettingsList(this);
    this.addWidget(this.list);
  }

  // func_230430_a_ = render
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
