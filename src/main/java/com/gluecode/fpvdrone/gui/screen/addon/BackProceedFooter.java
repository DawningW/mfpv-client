package com.gluecode.fpvdrone.gui.screen.addon;

import com.gluecode.fpvdrone.gui.screen.FpvScreen;
import com.gluecode.fpvdrone.gui.screen.wizard.WizardConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;

import java.util.function.Supplier;

public class BackProceedFooter extends ScreenAddon {
  private Button proceedButton;
  private Runnable onBack;
  private Runnable onProceed;
  private Supplier<String> proceedLabel;
  private Supplier<Boolean> isProceedVisible;
  
  public BackProceedFooter() {
  
  }
  
  public void completeConstructor(Runnable onProceed, Supplier<String> proceedLabel, Supplier<Boolean> isProceedVisible) {
    this.onProceed = onProceed;
    this.proceedLabel = proceedLabel;
    this.isProceedVisible = isProceedVisible;
  }
  
  public void overrideOnBack(Runnable onBack) {
    this.onBack = onBack;
  }
  
  public void handleBack(FpvScreen screen) {
    if (this.onBack != null) {
      this.onBack.run();
      return;
    }
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
      (Button button) -> this.handleBack(screen)
    ));
  
    this.proceedButton = new Button(
      screen.width - WizardConfig.wideButtonWidth - WizardConfig.right,
      screen.height - 20 - WizardConfig.footerBottom,
      WizardConfig.wideButtonWidth,
      20,
      new TextComponent(I18n.get("gui.proceed")),
      (Button button) -> {
        this.onProceed.run();
      }
    );
    screen.addRenderableWidget(this.proceedButton);
  }
  
  @Override
  public void render(
    FpvScreen screen, PoseStack matrixStack, int mouseX, int mouseY, float partialTicks
  ) {
    if (this.isProceedVisible != null) {
      if (!this.isProceedVisible.get()) {
        this.proceedButton.visible = false;
      } else {
        this.proceedButton.visible = true;
        if (this.proceedLabel != null) {
          this.proceedButton.setMessage(new TextComponent(this.proceedLabel.get()));
        }
      }
    }
  }
}
