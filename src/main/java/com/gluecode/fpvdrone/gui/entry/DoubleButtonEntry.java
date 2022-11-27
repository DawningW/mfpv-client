package com.gluecode.fpvdrone.gui.entry;

import com.gluecode.fpvdrone.gui.widget.list.FPVList;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class DoubleButtonEntry extends FPVEntry {
  private Supplier<String> getName;
  private Button changeButton;
  private Button configureButton;
  private Supplier<Boolean> disabled;
  
  public DoubleButtonEntry(
    FPVList list,
    Supplier<String> getName,
    String leftLabel,
    Runnable onLeft,
    String rightLabel,
    Runnable onRight,
    Supplier<Boolean> disabled
  ) {
    super(list, "");
    this.disabled = disabled;
    this.getName = getName;
    this.configureButton = new Button(
      0,
      0,
      80,
      20,
      new TextComponent(leftLabel),
      (Button button) -> {
        onLeft.run();
      }
    );
    this.changeButton = new Button(
      0,
      0,
      80,
      20,
      new TextComponent(rightLabel),
      (Button button) -> {
        onRight.run();
      }
    );
  }
  
  @Override
  public boolean isLetterAcceptable(String letter) {
    return false;
  }
  
  @Override
  public void betterRender(
    PoseStack matrixStack,
    Font fontRenderer,
    int rowIndex,
    int rowTop,
    int rowLeft,
    int rowWidth,
    int rowHeight,
    int mouseX,
    int mouseY,
    boolean isMouseOver,
    float partialTicks
  ) {
    fontRenderer.draw(
      matrixStack,
      this.getName.get(),
      rowLeft,
      (float) (rowTop + 6),
      16777215
    );
  
    int right = rowLeft + rowWidth;
    int configureWidth = this.configureButton.getWidth();
    int changeWidth = this.changeButton.getWidth();
    
    this.configureButton.x = right - changeWidth - 1 - configureWidth;
    this.configureButton.y = rowTop;
    this.configureButton.active = !this.disabled.get();
    this.configureButton.render(
      matrixStack,
      mouseX,
      mouseY,
      partialTicks
    );
    
    this.changeButton.x = right - changeWidth;
    this.changeButton.y = rowTop;
    this.changeButton.active = !this.disabled.get();
    this.changeButton.render(matrixStack, mouseX, mouseY, partialTicks);
  }
  
  @Override
  public @NotNull List<? extends GuiEventListener> children() {
    return ImmutableList.of(this.changeButton, this.configureButton);
  }

  @Override
  public @NotNull List<? extends NarratableEntry> narratables() {
    return ImmutableList.of();
  }
}
