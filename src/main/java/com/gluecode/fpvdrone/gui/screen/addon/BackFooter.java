package com.gluecode.fpvdrone.gui.screen.addon;

import com.gluecode.fpvdrone.Main;
import com.gluecode.fpvdrone.gui.screen.FpvScreen;
import com.gluecode.fpvdrone.gui.screen.wizard.WizardConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;

public class BackFooter extends ScreenAddon {
  public void handleDone(FpvScreen screen) {
    screen.getMinecraft().setScreen(screen.previousScreen);
  }
  
  @Override
  public void init(FpvScreen screen) {
    screen.addRenderableWidget(new Button(
      WizardConfig.left,
      screen.height - 20 - WizardConfig.footerBottom,
      WizardConfig.wideButtonWidth,
      20,
      new TextComponent(I18n.get("gui.back")),
      (Button button) -> this.handleDone(screen)
    ));
  }
  
  @Override
  public void render(
    FpvScreen screen, PoseStack matrixStack, int mouseX, int mouseY, float partialTicks
  ) {
  
  }
}
