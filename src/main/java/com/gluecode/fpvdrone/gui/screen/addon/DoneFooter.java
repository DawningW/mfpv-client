package com.gluecode.fpvdrone.gui.screen.addon;

import com.gluecode.fpvdrone.gui.screen.FpvScreen;
import com.gluecode.fpvdrone.gui.screen.wizard.WizardConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;

public class DoneFooter extends ScreenAddon {
  private Runnable onDone;
  
  public void overrideDone(Runnable onDone) {
    this.onDone = onDone;
  }
  
  public void handleDone(FpvScreen screen) {
    if (this.onDone != null) {
      this.onDone.run();
      return;
    }
    screen.getMinecraft().setScreen(screen.previousScreen);
  }
  
  @Override
  public void init(FpvScreen screen) {
    screen.addRenderableWidget(new Button(
      screen.width - WizardConfig.wideButtonWidth - WizardConfig.right,
      screen.height - 20 - WizardConfig.footerBottom,
      WizardConfig.wideButtonWidth,
      20,
      new TextComponent(I18n.get("gui.done")),
      (Button button) -> this.handleDone(screen)
    ));
  }
  
  @Override
  public void render(
    FpvScreen screen, PoseStack matrixStack, int mouseX, int mouseY, float partialTicks
  ) {
  
  }
}
