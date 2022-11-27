package com.gluecode.fpvdrone.gui.screen;

import com.gluecode.fpvdrone.gui.screen.addon.DoneFooter;
import com.gluecode.fpvdrone.gui.screen.addon.ServerTitleWikiHeader;
import com.gluecode.fpvdrone.gui.screen.wizard.WizardConfig;
import com.gluecode.fpvdrone.gui.widget.RateChart;
import com.gluecode.fpvdrone.gui.widget.list.RatesList;
import com.gluecode.fpvdrone.util.SettingsLoader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;

public class RatesScreen extends FpvScreen {
  private RatesList ratesList;
  private RateChart rateChart;

  public RatesScreen(
    Screen previousScreen
  ) {
    super(
      previousScreen,
      new ServerTitleWikiHeader(I18n.get("fpvdrone.settings.fpvsettings")),
      new DoneFooter()
    );
  }

  // func_231160_c_ = init
  @Override
  protected void init() {
    super.init();
    
    this.ratesList = new RatesList(this);
    this.addWidget(this.ratesList);

    int chartSize = lastScreen.height - 32 - 43 - 12 * 2;
    this.rateChart = new RateChart(30, 43 + 12, chartSize, chartSize);
    this.addWidget(this.rateChart);

    this.addRenderableOnly(new Button(
      WizardConfig.left,
      this.height - 20 - WizardConfig.footerBottom,
      WizardConfig.wideButtonWidth,
      20,
      new TextComponent(I18n.get("fpvdrone.settings.reset.xbox")),
      this::handleXboxReset
    ));
    this.addRenderableOnly(new Button(
      WizardConfig.left + WizardConfig.wideButtonWidth + WizardConfig.doubleButtonSpacing,
      this.height - 20 - WizardConfig.footerBottom,
      WizardConfig.wideButtonWidth,
      20,
      new TextComponent(I18n.get("fpvdrone.settings.reset.radio")),
      this::handleRadioReset
    ));
  }

  private void handleXboxReset(Button button) {
    SettingsLoader.resetXboxRates();
  }

  private void handleRadioReset(Button button) {
    SettingsLoader.resetRadioRates();
  }

  @Override
  public void renderCustom(
    PoseStack matrixStack,
    int mouseX,
    int mouseY,
    float partialTicks
  ) {
    this.ratesList.render(matrixStack, mouseX, mouseY, partialTicks);
//    this.rateChart.render(matrixStack, mouseX, mouseY, partialTicks); // todo: Rate chart can't be rendered
    Minecraft minecraft = Minecraft.getInstance();
    String chartLabel = I18n.get("fpvdrone.rates.sticks");
    int labelWidth = minecraft.font.width(chartLabel);
    int chartSize = lastScreen.height - 32 - 43 - 12 * 2;
    minecraft.font.draw(
      matrixStack,
      I18n.get("fpvdrone.rates.sticks"),
      (float) (30 + chartSize / 2 - labelWidth / 2),
      60,
      16777215
    );
  }
}
