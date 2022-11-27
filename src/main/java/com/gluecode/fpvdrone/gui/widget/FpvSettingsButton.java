package com.gluecode.fpvdrone.gui.widget;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;

public class FpvSettingsButton extends Button {
  public FpvSettingsButton(int x, int y, OnPress onPress) {
    super(
      x,
      y,
      30,
      20,
      new TextComponent(I18n.get("fpvdrone.settings.fpv")),
      onPress
    );
  }
}
