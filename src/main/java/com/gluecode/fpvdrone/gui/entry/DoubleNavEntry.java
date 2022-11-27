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

public class DoubleNavEntry extends FPVEntry {
  private Button leftButton;
  private Button rightButton;
  
  public DoubleNavEntry(
    FPVList list,
    String leftName,
    Runnable onLeftSelect,
    String rightName,
    Runnable onRightSelect
  ) {
    super(list, "");
    this.leftButton = new Button(
      0,
      0,
      150,
      20,
      new TextComponent(leftName),
      (Button button) -> {
        onLeftSelect.run();
      }
    );
    this.rightButton = new Button(
      0,
      0,
      150,
      20,
      new TextComponent(rightName),
      (Button button) -> {
        onRightSelect.run();
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
    this.leftButton.x = rowLeft + rowWidth / 2 - 150 - 5;
    this.leftButton.y = rowTop;
    this.leftButton.render(matrixStack, mouseX, mouseY, partialTicks);
    
    this.rightButton.x = rowLeft + rowWidth / 2 + 5;
    this.rightButton.y = rowTop;
    this.rightButton.render(matrixStack, mouseX, mouseY, partialTicks);
  }
  
  @Override
  public @NotNull List<? extends GuiEventListener> children() {
    return ImmutableList.of(this.leftButton, this.rightButton);
  }

  @Override
  public @NotNull List<? extends NarratableEntry> narratables() {
    return ImmutableList.of();
  }
}
