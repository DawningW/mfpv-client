package com.gluecode.fpvdrone.render;

import com.gluecode.fpvdrone.Main;
import com.gluecode.fpvdrone.input.ControllerReader;
import com.gluecode.fpvdrone.input.MouseManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawSelectionEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Main.MOD_ID)
public class StickOverlayRenderer {
  @SubscribeEvent
  public static void handleBlockOutlineRendering(DrawSelectionEvent event) {
    if (ControllerReader.getArm() && !CameraManager.getShowBlockOutline()) event.setCanceled(true);
  }
  
//  @SubscribeEvent // todo: StickOverlayRenderer can't work
  public static void handleStickOverlayRendering(RenderGameOverlayEvent.Pre event) {
    if (!ControllerReader.getArm()) return;
    if (!CameraManager.getShowStickOverlay()) return;
    
    float mouseYawDiff = MouseManager.yposDiff();
    float mousePitchDiff = MouseManager.getMousePitchDiff();
    float mouseRollDiff = MouseManager.getMouseRollDiff();
    
    Window mainWindow = Minecraft.getInstance().getWindow();
    int scaledWidth = mainWindow.getGuiScaledWidth();
    int scaledHeight = mainWindow.getGuiScaledHeight();
    
    PoseStack stack = event.getMatrixStack();
    stack.pushPose();
    stack.translate(
      (float) (scaledWidth / 2),
      (float) (scaledHeight / 2),
      0
    );
    stack.scale(1, -1, 1);
    
    Tesselator tesselator = Tesselator.getInstance();
    BufferBuilder buffer = tesselator.getBuilder();
    buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
    
    if (Minecraft.getInstance().player.isCreative() ||
        Minecraft.getInstance().player.isSpectator()) {
      stack.translate(0, -scaledHeight / 2 + 20, 0);
    } else {
      stack.translate(0, -scaledHeight / 2 + 60, 0);
    }

    /*
    * float mousePitchDiff = getMousePitchDiff();
    * float mouseRollDiff = getMouseRollDiff();
    */
    
    stack.pushPose();
    stack.translate(15f, 0, 0);
    renderAxis(
      stack,
      buffer,
      ControllerReader.getRoll() + MouseManager.getMouseRollDiff(),
      ControllerReader.getPitch() + MouseManager.getMousePitchDiff()
    );
    stack.popPose();
    
    stack.pushPose();
    stack.translate(-15f, 0, 0);
    renderAxis(stack, buffer, ControllerReader.getYaw(), ControllerReader.getThrottle());
    stack.popPose();
    
    applyLineMode();
    tesselator.end();
    cleanLineMode();
    stack.popPose();
  }
  
  public static void renderAxis(
    PoseStack stack,
    BufferBuilder buffer,
    float xValue,
    float yValue
  ) {
    stack.pushPose();
    float size = 10f;
    
    stack.pushPose();
    stack.scale(size, size, 1);
    Matrix4f matrix = stack.last().pose();
    buffer.vertex(matrix, 0, -1, 0).color(1f, 1f, 1f, 1f).endVertex();
    buffer.vertex(matrix, 0, 1, 0).color(1f, 1f, 1f, 1f).endVertex();
    buffer.vertex(matrix, -1, 0, 0).color(1f, 1f, 1f, 1f).endVertex();
    buffer.vertex(matrix, 1, 0, 0).color(1f, 1f, 1f, 1f).endVertex();
    stack.popPose();
    
    stack.pushPose();
    matrix = stack.last().pose();
    stack.translate(xValue * size, yValue * size, 0);
    buffer.vertex(matrix, 1, 1, 0).color(1f, 1f, 1f, 1f).endVertex();
    buffer.vertex(matrix, 1, -1, 0).color(1f, 1f, 1f, 1f).endVertex();
    buffer.vertex(matrix, 1, -1, 0).color(1f, 1f, 1f, 1f).endVertex();
    buffer.vertex(matrix, -1, -1, 0).color(1f, 1f, 1f, 1f).endVertex();
    buffer.vertex(matrix, -1, -1, 0).color(1f, 1f, 1f, 1f).endVertex();
    buffer.vertex(matrix, -1, 1, 0).color(1f, 1f, 1f, 1f).endVertex();
    buffer.vertex(matrix, -1, 1, 0).color(1f, 1f, 1f, 1f).endVertex();
    buffer.vertex(matrix, 1, 1, 0).color(1f, 1f, 1f, 1f).endVertex();
    stack.popPose();
    
    stack.popPose();
  }
  
  public static void applyLineMode() {
    RenderSystem.disableTexture();
    RenderSystem.depthMask(false);
//    GL11.glLineWidth(2.0F);
  }
  
  public static void cleanLineMode() {
//    GL11.glLineWidth(1.0F);
    RenderSystem.depthMask(true);
    RenderSystem.enableTexture();
  }
}
