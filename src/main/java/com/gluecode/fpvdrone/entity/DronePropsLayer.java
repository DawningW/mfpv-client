package com.gluecode.fpvdrone.entity;

import com.gluecode.fpvdrone.Main;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

public class DronePropsLayer<DroneEntity extends LivingEntity> extends RenderLayer<AbstractClientPlayer, DroneModel<AbstractClientPlayer>> {
  private final DroneModel<AbstractClientPlayer> droneModel;
  
  public DronePropsLayer(
    RenderLayerParent<AbstractClientPlayer, DroneModel<AbstractClientPlayer>> entityRendererIn,
    UUID uuid,
    DroneBuild build
  ) {
    super(entityRendererIn);
    droneModel = new DroneModel<>(
      uuid,
      build,
      true
    ); // make a new drone model with different params
    droneModel.renderer = (DroneRenderer) entityRendererIn;
  }
  
  @Override
  public void render(
    PoseStack matrixStackIn,
    MultiBufferSource bufferIn,
    int packedLightIn,
    AbstractClientPlayer entitylivingbaseIn,
    float limbSwing,
    float limbSwingAmount,
    float partialTicks,
    float ageInTicks,
    float netHeadYaw,
    float headPitch
  ) {
    if (!entitylivingbaseIn.isInvisible()) {
      this.getParentModel().copyPropertiesTo(this.droneModel);
      this.droneModel.prepareMobModel(
        entitylivingbaseIn,
        limbSwing,
        limbSwingAmount,
        partialTicks
      );
      this.droneModel.setupAnim(
        entitylivingbaseIn,
        limbSwing,
        limbSwingAmount,
        ageInTicks,
        netHeadYaw,
        headPitch
      );
      // VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucentCull(this.getEntityTexture(entitylivingbaseIn)));
      
      VertexConsumer ivertexbuilder;
//      if (this.blur) {
//        // What we set here doesn't actually matter because PropModelRenderer will override it.
//        ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucentCull(
//          new ResourceLocation(
//            Main.MOD_ID,
//            "textures/entity/props3.png"
//          )));
//      } else {
        ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucentCull(
          new ResourceLocation(
            Main.MOD_ID,
            "textures/entity/drone.png"
          )));
//      }
      
      this.droneModel.renderToBuffer(
        matrixStackIn,
        ivertexbuilder,
        packedLightIn,
        LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F),
        1.0F,
        1.0F,
        1.0F,
        1.0F
      );
    }
  }
}
