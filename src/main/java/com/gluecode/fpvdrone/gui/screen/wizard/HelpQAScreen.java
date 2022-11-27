package com.gluecode.fpvdrone.gui.screen.wizard;

import com.gluecode.fpvdrone.gui.screen.EmptyListScreen;
import com.gluecode.fpvdrone.gui.screen.addon.BackHelpFooter;
import com.gluecode.fpvdrone.gui.screen.addon.WizardHeader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;


import javax.annotation.Nullable;
import java.util.LinkedHashMap;

public class HelpQAScreen extends EmptyListScreen {
  private LinkedHashMap<String, Component> qa;
  
  public HelpQAScreen(
    Screen previousScreen,
    LinkedHashMap<String, Component> qa
  ) {
    super(previousScreen, null, new BackHelpFooter());
    this.qa = qa;
  }
  
  @Override
  protected void init() {
    super.init();
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
    float lineMult = WizardConfig.lineHeight;
    int lineHeight = (int) (lineMult * minecraft.font.lineHeight);
    
    String[] questions = this.qa.keySet().toArray(new String[0]);
    for (int i = 0; i < questions.length; i++) {
      int y = 3 * lineHeight * i;
      String q = questions[i];
      Component a = this.qa.get(q);
      TextComponent answer = new TextComponent("A: ");
      answer.append(a);
      
      minecraft.font.draw(
        matrixStack,
        "Q: " + q,
        3 * WizardConfig.left,
        WizardConfig.headerHeight + WizardConfig.contentTop + y,
        0xFFFFFF
      );
      
      minecraft.font.draw(
        matrixStack,
        answer,
        4 * WizardConfig.left,
        WizardConfig.headerHeight + WizardConfig.contentTop + y + lineHeight,
        0xFFFFFF
      );
    }
  }
  
  public @Nullable
  Style getStyleAt(double mouseX, double mouseY) {
    // Find if any links were clicked.
    String[] questions = this.qa.keySet().toArray(new String[0]);
    
    Minecraft minecraft = Minecraft.getInstance();
    float lineMult = WizardConfig.lineHeight;
    int lineHeight = (int) (lineMult * minecraft.font.lineHeight);
    
    for (int i = 0; i < questions.length; i++) {
      int y = 3 * lineHeight * i;
      String q = questions[i];
      Component a = this.qa.get(q);
      TextComponent answer = new TextComponent("A: ");
      answer.append(a);
      
      int answerY = WizardConfig.headerHeight +
                    WizardConfig.contentTop +
                    y +
                    lineHeight;
      
      if (answerY <= mouseY && mouseY <= answerY + minecraft.font.lineHeight) {
        // This answer is on the same line as the mouseClick.
        // Do not worry about line wrapping. It will not be supported.
        
        int answerX = 4 * WizardConfig.left;
        Style style = minecraft.font.getSplitter().componentStyleAtWidth(
          answer,
          (int) (mouseX - answerX)
        );
        
        if (style != null) {
          return style;
        }
      }
    }
    return null;
  }
  
  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int p_231044_5_) {
    Style style = this.getStyleAt(mouseX, mouseY);
    
    if (
      style != null &&
      style.getClickEvent() != null &&
      style.getClickEvent().getAction().equals(ClickEvent.Action.OPEN_URL)
    ) {
      String url = style.getClickEvent().getValue();
      this.handleLinkClick(url);
      return true;
    }
    
    return super.mouseClicked(mouseX, mouseY, p_231044_5_);
  }
  
  public void handleLinkClick(String url) {
    this.getMinecraft().setScreen(new ConfirmLinkScreen((p_244739_1_) -> {
      if (p_244739_1_) {
        Util.getPlatform().openUri(
          url);
      }
      this.getMinecraft().setScreen(this);
    }, url, true));
  }
}
