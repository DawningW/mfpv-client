package com.gluecode.fpvdrone.gui.screen;

import com.gluecode.fpvdrone.gui.widget.list.ControllerChoicesList;
import com.gluecode.fpvdrone.gui.screen.addon.DoneFooter;
import com.gluecode.fpvdrone.gui.screen.addon.ServerTitleWikiHeader;
import com.gluecode.fpvdrone.input.ControllerReader;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;

public class ControllerChoicesScreen extends FpvScreen {
  private ControllerChoicesList keyBindingList;
  
  public ControllerChoicesScreen(
    Screen previousScreen
  ) {
    super(
      previousScreen,
      new ServerTitleWikiHeader(I18n.get("fpvdrone.device.choose")),
      new DoneFooter()
    );
  }
  
  public void handleIdSet(int id) {
    ControllerReader.setControllerId(id);
    if (this.footer != null) {
      DoneFooter footer = (DoneFooter) this.footer;
      footer.handleDone(this);
    }
  }
  
  @Override
  protected void init() {
    super.init();
    this.keyBindingList = new ControllerChoicesList(
      this,
      this::handleIdSet
    );
    this.addWidget(this.keyBindingList);
  }
  
  @Override
  public void renderCustom(
    PoseStack matrixStack,
    int mouseX,
    int mouseY,
    float partialTicks
  ) {
    this.keyBindingList.render(matrixStack, mouseX, mouseY, partialTicks);
  }
}
