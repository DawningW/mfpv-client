package com.gluecode.fpvdrone.gui.widget.list;

import com.gluecode.fpvdrone.gui.entry.FPVEntry;
import com.gluecode.fpvdrone.gui.screen.wizard.WizardConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;

public class FPVList extends ContainerObjectSelectionList<FPVEntry> {
  public FPVEntry activeEntry = null;
  public Screen parentScreen;
  
  public FPVList(
    Screen parentScreen
  ) {
    super(
      Minecraft.getInstance(),
      parentScreen.width, // widthIn,
      parentScreen.height, // heightIn,
      WizardConfig.headerHeight, // topIn,
      parentScreen.height - WizardConfig.footerHeight, // bottomIn,
      20 // itemHeightIn
    );
    this.parentScreen = parentScreen;
  }
  
  public int getLeftPadding() {
    return 30;
  }
  
  public int getRightPadding() {
    return 30;
  }
  
  @Override
  protected int getScrollbarPosition() {
    return this.width - 6;
  }
  
  @Override
  public int getRowWidth() {
    return this.width;
  }
  
  @Override
  public int getRowLeft() {
    return 0;
  }
}
