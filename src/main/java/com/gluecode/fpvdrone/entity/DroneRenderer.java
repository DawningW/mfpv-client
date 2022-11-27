package com.gluecode.fpvdrone.entity;

import com.gluecode.fpvdrone.Main;
import com.gluecode.fpvdrone.network.DroneState;
import com.gluecode.fpvdrone.render.BlurryPropRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class DroneRenderer extends LivingEntityRenderer<AbstractClientPlayer, DroneModel<AbstractClientPlayer>> {
  protected static final ResourceLocation TEXTURE = new ResourceLocation(
    Main.MOD_ID,
    "textures/entity/drone.png"
  );

//  public boolean renderProps = false;
  public DroneBuild build;

  public DroneRenderer(
    EntityRendererProvider.Context renderManagerIn,
    UUID uuid,
    DroneBuild build
  ) {
    super(renderManagerIn, new DroneModel<>(uuid, build, false), 0.2f);
    this.build = build;
    this.getModel().renderer = this;
    this.addLayer(new CustomHeadLayer<>(this, renderManagerIn.getModelSet()));
//    this.addLayer(new DronePropsLayer<>(this, uuid, build));
  }

  @Override
  public ResourceLocation getTextureLocation(@NotNull AbstractClientPlayer entity) {
    return TEXTURE;
  }

  @Override
  protected void setupRotations(
    AbstractClientPlayer player,
    PoseStack matrixStackIn,
    float ageInTicks,
    float rotationYaw,
    float partialTicks
  ) {
//    float rads = (float) (Math.PI / 180);

    DroneState droneOrientation = DroneState.getInterpolated(player.getUUID(), partialTicks);
    float[] angles = droneOrientation.getAngles();
    float droneYaw = angles[0];
    float dronePitch = angles[1];
    float droneRoll = angles[2];

    matrixStackIn.mulPose(new Quaternion(
      Vector3f.YP,
      -droneYaw,
      true
    ));
    matrixStackIn.mulPose(new Quaternion(
      Vector3f.XP,
      dronePitch,
      true
    ));
    matrixStackIn.mulPose(new Quaternion(
      Vector3f.ZP,
      droneRoll,
      true
    ));

//    matrixStackIn.mulPose(new Quaternion(Vector3f.YP, -player.yRot, true));
//    matrixStackIn.mulPose(new Quaternion(Vector3f.XP, player.xRot, true));
//    matrixStackIn.mulPose(new Quaternion(Vector3f.ZP, roll / rads, true));
  }

  public void renderBlurryProps(
    AbstractClientPlayer entityIn,
    float entityYaw,
    float partialTicks,
    PoseStack matrixStackIn
  ) {
    // Same logic found in InputHandler.setSpectateCamera
    Minecraft minecraft = Minecraft.getInstance();
    Entity cameraEntity = minecraft.getCameraEntity();
    Player self = minecraft.player;
    if (cameraEntity instanceof Player && !Objects.equals(self, cameraEntity)) {
      UUID uuid = cameraEntity.getUUID();
      boolean isArmed = Main.entityArmStates.getOrDefault(uuid, false);
      if (isArmed) {
        return;
      }
    }

    int light = this.entityRenderDispatcher.getPackedLightCoords(entityIn, partialTicks);
    int overlay = getOverlayCoords(entityIn, this.getWhiteOverlayProgress(entityIn, partialTicks));

    matrixStackIn.pushPose();
    Vec3 vector3d = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
    double camX = vector3d.x();
    double camY = vector3d.y();
    double camZ = vector3d.z();
    double d0 = Mth.lerp(
      (double) partialTicks,
      entityIn.xOld,
      entityIn.getX()
    );
    double d1 = Mth.lerp(
      (double) partialTicks,
      entityIn.yOld,
      entityIn.getY()
    );
    double d2 = Mth.lerp(
      (double) partialTicks,
      entityIn.zOld,
      entityIn.getZ()
    );
    double x = d0 - camX;
    double y = d1 - camY;
    double z = d2 - camZ;
    Vec3 vector3e = this.entityRenderDispatcher
      .getRenderer(entityIn)
      .getRenderOffset(entityIn, partialTicks);
    double e2 = x + vector3e.x();
    double e3 = y + vector3e.y();
    double e0 = z + vector3e.z();
    matrixStackIn.translate(e2, e3, e0);

    // copied from super.render:
    float f = Mth.rotLerp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot);
    float f7 = this.getBob(entityIn, partialTicks);
    this.setupRotations(entityIn, matrixStackIn, f7, f, partialTicks);
    matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
    matrixStackIn.translate(0.0D, (double)-1.501F, 0.0D);


    BlurryPropRenderer.render(entityIn, matrixStackIn, light, overlay, build);

    // can't call super.render because I think Optifine makes modifications to that code,
    // and it doesn't work in RenderWorldLastEvent.
//    super.render(
//      entityIn,
//      entityYaw,
//      partialTicks,
//      matrixStackIn,
//      buffer,
//      light
//    );
    matrixStackIn.popPose();
  }
}
