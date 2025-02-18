package com.gluecode.fpvdrone.gui.screen.wizard;

import com.gluecode.fpvdrone.gui.GuiEvents;
import com.gluecode.fpvdrone.gui.screen.EmptyListScreen;
import com.gluecode.fpvdrone.gui.screen.addon.WizardHeader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;

public class DecideControllerScreen extends EmptyListScreen {
  
  public DecideControllerScreen(
    Screen previousScreen
  ) {
    super(previousScreen, new WizardHeader(null, false), null);
  }
  
  @Override
  protected void init() {
    super.init();
  
    this.addRenderableWidget(new Button(
      this.width / 2 - WizardConfig.wideButtonWidth / 2,
      WizardConfig.headerHeight + WizardConfig.contentTop + WizardConfig.titleSpacing,
      WizardConfig.wideButtonWidth,
      20,
      new TextComponent(I18n.get("gui.yes")),
      this::handleYes
    ));
  
    this.addRenderableWidget(new Button(
      this.width / 2 - WizardConfig.wideButtonWidth / 2,
      WizardConfig.headerHeight + WizardConfig.contentTop + WizardConfig.titleSpacing + 20 + WizardConfig.doubleButtonSpacing,
      WizardConfig.wideButtonWidth,
      20,
      new TextComponent(I18n.get("gui.no")),
      this::handleNo
    ));
  }
  
  public void handleYes(Button button) {
    GuiEvents.openChooseControllerScreen(this);
  }
  
  public void handleNo(Button button) {
    GuiEvents.openCalibrateKeyboardScreen(this);
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
    String welcomeString = I18n.get("fpvdrone.wizard.decideController.title");
    int welcomeWidth = minecraft.font.width(welcomeString);
    minecraft.font.draw(
      matrixStack,
      welcomeString,
      this.width / 2f - welcomeWidth / 2f,
      WizardConfig.headerHeight + WizardConfig.contentTop,
      0xFFFFFF
    );
  }
}
