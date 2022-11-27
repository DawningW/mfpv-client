package com.gluecode.fpvdrone.gui.screen.wizard;

import com.gluecode.fpvdrone.gui.screen.EmptyListScreen;
import com.gluecode.fpvdrone.gui.screen.addon.BackFooter;
import com.gluecode.fpvdrone.gui.screen.addon.WizardHeader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;

public class HelpScreen extends EmptyListScreen {
  
  public HelpScreen(
    Screen previousScreen
  ) {
    super(previousScreen, null, new BackFooter());
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
  protected void init() {
    super.init();
  
    this.addRenderableWidget(new Button(
      this.width / 2 - WizardConfig.shortButtonWidth / 2,
      WizardConfig.headerHeight + WizardConfig.contentTop + WizardConfig.titleSpacing,
      WizardConfig.shortButtonWidth,
      20,
      new TextComponent("Discord"),
      (button) -> this.handleDiscord(this)
    ));
  
    this.addRenderableWidget(new Button(
      this.width / 2 - WizardConfig.shortButtonWidth / 2,
      WizardConfig.headerHeight + WizardConfig.contentTop + WizardConfig.titleSpacing + 20 + WizardConfig.doubleButtonSpacing,
      WizardConfig.shortButtonWidth,
      20,
      new TextComponent(I18n.get("fpvdrone.settings.wiki")),
      (button) -> this.handleWiki(this)
    ));
  }
  
  @Override
  public void renderCustom(
    PoseStack matrixStack,
    int mouseX,
    int mouseY,
    float partialTicks
  ) {
    super.renderCustom(matrixStack, mouseX, mouseY, partialTicks);
  
    Minecraft minecraft = Minecraft.getInstance();
    String title = I18n.get("fpvdrone.wizard.help.title");
    int welcomeWidth = minecraft.font.width(title);
    minecraft.font.draw(
      matrixStack,
      title,
      this.width / 2f - welcomeWidth / 2f,
      WizardConfig.headerHeight + WizardConfig.contentTop,
      0xFFFFFF
    );
  }
}
