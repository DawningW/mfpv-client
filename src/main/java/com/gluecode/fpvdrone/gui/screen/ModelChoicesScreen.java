package com.gluecode.fpvdrone.gui.screen;

import com.gluecode.fpvdrone.gui.widget.list.ModelChoiceList;
import com.gluecode.fpvdrone.gui.screen.addon.DoneFooter;
import com.gluecode.fpvdrone.gui.screen.addon.ServerTitleWikiHeader;
import com.gluecode.fpvdrone.gui.screen.wizard.WizardConfig;
import com.gluecode.fpvdrone.util.SettingsLoader;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

public class ModelChoicesScreen extends FpvScreen {
  private ModelChoiceList list = null;
  private String initName = I18n.get("fpvdrone.model.new");
  private String editValue = initName;
  private boolean editMode = false;
  //  private Button createButton;
  private Button nameButton;
  
  public ModelChoicesScreen(
    Screen previousScreen
  ) {
    super(
      previousScreen,
      new ServerTitleWikiHeader(I18n.get("fpvdrone.model.active")),
      new DoneFooter()
    );
    MinecraftForge.EVENT_BUS.register(this);
  }
  
  @SubscribeEvent
  public void charTyped(ScreenEvent.KeyboardCharTypedEvent.Pre event) {
    if (editMode) {
      if (this.editValue.length() < 24) {
        this.editValue = this.editValue + event.getCodePoint();
      }
    }
  }
  
  @Override
  protected void init() {
    super.init();
    this.generateList();
    
    this.nameButton = this.addRenderableWidget(new Button(
      WizardConfig.left,
      this.height - 20 - WizardConfig.footerBottom,
      WizardConfig.wideButtonWidth * 2,
      20,
      new TextComponent(this.editValue),
      this::handleNewPress
    ));
  }
  
  public void generateList() {
    if (this.list != null) {
      this.removeWidget(this.list);
    }
    
    this.list = new ModelChoiceList(this);
    this.addWidget(this.list);
  }
  
  public boolean keyPressed(
    int p_keyPressed_1_,
    int p_keyPressed_2_,
    int p_keyPressed_3_
  ) {
    InputConstants.Key input = InputConstants.getKey(
      p_keyPressed_1_,
      p_keyPressed_2_
    );
    if (editMode) {
      if (input.toString().equals("key.keyboard.backspace")) {
        if (this.editValue.length() > 0) {
          this.editValue = this.editValue.substring(
            0,
            this.editValue.length() - 1
          );
        }
        return true;
      } else if (input.toString().equals("key.keyboard.space")) {
        return true;
      }
    }
    return super.keyPressed(
      p_keyPressed_1_,
      p_keyPressed_2_,
      p_keyPressed_3_
    );
  }
  
  public void handleNewPress(@Nullable Widget button) {
    this.editMode = !this.editMode;
    if (editMode) {
      this.editValue = "";
    } else {
      String attemptName = this.editValue.trim();
      if (!attemptName.equals("")) {
        Object existing = SettingsLoader.models.get(attemptName);
        if (existing == null) {
          SettingsLoader.loadModel(SettingsLoader.defaultModelName);
          SettingsLoader.currentModel = attemptName;
          SettingsLoader.save();
          this.generateList();
        }
      }
      this.editValue = initName;
    }
  }
  
  @Override
  public void renderCustom(
    PoseStack matrixStack,
    int mouseX,
    int mouseY,
    float partialTicks
  ) {
    this.list.render(matrixStack, mouseX, mouseY, partialTicks);
    this.nameButton.setMessage(
      new TextComponent(this.editMode ? "> " +
                                              this.editValue +
                                              "_ <" : this.editValue));
    this.nameButton.render(matrixStack, mouseX, mouseY, partialTicks);
  }
}
