package com.gluecode.fpvdrone.gui.screen.addon;

import com.gluecode.fpvdrone.gui.screen.FpvScreen;
import com.gluecode.fpvdrone.gui.screen.wizard.WizardConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;

public class BackHelpFooter extends ScreenAddon {
  
  public void handleBack(FpvScreen screen) {
    screen.getMinecraft().setScreen(screen.previousScreen);
  }
  
  public void handleDiscord(Screen screen) {
    screen.getMinecraft().setScreen(new ConfirmLinkScreen((p_244739_1_) -> {
      if (p_244739_1_) {
        Util.getPlatform().openUri(
          "https://discord.gg/WJfhXuz");
      }
      screen.getMinecraft().setScreen(screen);
    }, "https://discord.gg/WJfhXuz", true));
  }
  
  public void handleWiki(Screen screen) {
    screen.getMinecraft().setScreen(new ConfirmLinkScreen((p_244739_1_) -> {
      if (p_244739_1_) {
        Util.getPlatform().openUri(
          "https://minecraftfpv.com/wiki");
      }
      screen.getMinecraft().setScreen(screen);
    }, "https://minecraftfpv.com/wiki", true));
  }
  
  @Override
  public void init(FpvScreen screen) {
    int width = 310;
    int nButtons = 3;
    int padding = 8;
    int btnWidth = (width - (nButtons - 1) * padding) / nButtons;
    
    screen.addRenderableWidget(new Button(
      WizardConfig.left,
      screen.height - 20 - WizardConfig.footerBottom,
      WizardConfig.wideButtonWidth,
      20,
      new TextComponent(I18n.get("gui.back")),
      (Button button) -> this.handleBack(screen)
    ));
    
    screen.addRenderableWidget(new Button(
      screen.width - 2 * WizardConfig.shortButtonWidth - 2 * WizardConfig.right,
      screen.height - 20 - WizardConfig.footerBottom,
      WizardConfig.shortButtonWidth,
      20,
      new TextComponent("Discord"),
      (Button button) -> this.handleDiscord(screen)
    ));
    
    screen.addRenderableWidget(new Button(
      screen.width - WizardConfig.shortButtonWidth - WizardConfig.right,
      screen.height - 20 - WizardConfig.footerBottom,
      WizardConfig.shortButtonWidth,
      20,
      new TextComponent(I18n.get("fpvdrone.settings.wiki")),
      (Button button) -> this.handleWiki(screen)
    ));
  }
  
  @Override
  public void render(
    FpvScreen screen, PoseStack matrixStack, int mouseX, int mouseY, float partialTicks
  ) {
  
  }
}
