package com.gluecode.fpvdrone.gui.screen;

import com.gluecode.fpvdrone.gui.screen.addon.ScreenAddon;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class FpvScreen extends OptionsSubScreen {
  @Nullable
  public Screen previousScreen;
  @Nullable
  public ScreenAddon header;
  @Nullable
  public ScreenAddon footer;
  
  public FpvScreen(
    Screen previousScreen,
    @Nullable ScreenAddon header,
    @Nullable ScreenAddon footer
  ) {
    super(
      previousScreen,
      Minecraft.getInstance().options,
      new TextComponent("")
    );
    this.previousScreen = previousScreen;
    this.header = header;
    this.footer = footer;
  }
  
  @Override
  protected void init() {
    if (this.header != null) {
      this.header.init(this);
    }
    if (this.footer != null) {
      this.footer.init(this);
    }
  }

  @Override
  public <T extends GuiEventListener & Widget & NarratableEntry> @NotNull T addRenderableWidget(@NotNull T widget) { // Access widener
    return super.addRenderableWidget(widget);
  }

  abstract public void renderCustom(
    PoseStack matrixStack,
    int mouseX,
    int mouseY,
    float partialTicks
  );
  
  @Override
  public void render(
    @NotNull PoseStack matrixStack,
    int mouseX,
    int mouseY,
    float partialTicks
  ) {
    this.renderBackground(matrixStack);
    this.renderCustom(matrixStack, mouseX, mouseY, partialTicks);

    super.render(matrixStack, mouseX, mouseY, partialTicks);

    // These need to render after super.render because of text that might be drawn on top of "x" button.
    if (this.header != null) {
      this.header.render(this, matrixStack, mouseX, mouseY, partialTicks);
    }
    if (this.footer != null) {
      this.footer.render(this, matrixStack, mouseX, mouseY, partialTicks);
    }
  }
}
