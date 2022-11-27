package com.gluecode.fpvdrone.gui.screen.wizard;

import com.gluecode.fpvdrone.gui.GuiEvents;
import com.gluecode.fpvdrone.gui.screen.EmptyListScreen;
import com.gluecode.fpvdrone.gui.screen.FpvScreen;
import com.gluecode.fpvdrone.gui.screen.addon.WizardHeader;
import com.gluecode.fpvdrone.util.SettingsLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nullable;

public class WelcomeScreen extends EmptyListScreen {
  // currentWelcomeScreen is set whenever a welcomeScreen is instantiated.
  // it is used to make the "close" button functional.
  @Nullable
  public static WelcomeScreen currentWelcomeScreen;
  
  public WelcomeScreen(
    Screen previousScreen
  ) {
    super(previousScreen, new WizardHeader(null, false), null);
    WelcomeScreen.currentWelcomeScreen = this;
    
    WizardHeader header = (WizardHeader) this.header;
    if (header != null) {
      header.setOverrideOnDone(this::handleDone);
    }
  }
  
  public void handleDone() {
    this.getMinecraft().setScreen(this.previousScreen);
  }
  
  @Override
  protected void init() {
    super.init();
  
    this.addRenderableWidget(new Button(
      this.width / 2 - WizardConfig.wideButtonWidth / 2,
      WizardConfig.headerHeight + WizardConfig.contentTop + WizardConfig.titleSpacing,
      WizardConfig.wideButtonWidth,
      20,
      new TextComponent(I18n.get("fpvdrone.wizard.welcome.new")),
      this::handleNew
    ));
  
    this.addRenderableWidget(new Button(
      this.width / 2 - WizardConfig.wideButtonWidth / 2,
      WizardConfig.headerHeight + WizardConfig.contentTop + WizardConfig.titleSpacing + 20 + WizardConfig.doubleButtonSpacing,
      WizardConfig.wideButtonWidth,
      20,
      new TextComponent(I18n.get("fpvdrone.wizard.welcome.returning")),
      this::handleReturning
    ));
  }
  
  public void handleNew(Button button) {
    GuiEvents.openDecideControllerScreen(this);
  }
  
  public void handleReturning(Button button) {
    SettingsLoader.firstTimeSetup = false;
    SettingsLoader.save();
    
    if (this.previousScreen instanceof FpvScreen) {
      // Entered via "Calibration Wizard" button.
      // The previous screen is where we are going to.
      this.handleDone();
    } else {
      // Entered as first timer.
      GuiEvents.openFpvSettingsScreen(this.previousScreen);
    }
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
    String welcomeString = I18n.get("fpvdrone.wizard.welcome.title");
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
