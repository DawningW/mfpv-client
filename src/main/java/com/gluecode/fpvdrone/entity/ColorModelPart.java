package com.gluecode.fpvdrone.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;

import java.util.List;
import java.util.Map;

public class ColorModelPart extends ModelPart {
  public float red;
  public float green;
  public float blue;
  public float alpha;
  
  public ColorModelPart(
    List<ModelPart.Cube> cubes,
    Map<String, ModelPart> children,
    float red,
    float green,
    float blue,
    float alpha
  ) {
    super(cubes, children);
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.alpha = alpha;
  }
  
  @Override
  public void compile(
    PoseStack.Pose matrixEntryIn,
    VertexConsumer bufferIn,
    int packedLightIn,
    int packedOverlayIn,
    float red,
    float green,
    float blue,
    float alpha
  ) {
    super.compile(
      matrixEntryIn,
      bufferIn,
      packedLightIn,
      packedOverlayIn,
      this.red,
      this.green,
      this.blue,
      this.alpha
    );
  }
}
