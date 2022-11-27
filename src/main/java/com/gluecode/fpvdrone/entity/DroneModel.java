package com.gluecode.fpvdrone.entity;

import com.gluecode.fpvdrone.Main;
import com.gluecode.fpvdrone.network.DroneState;
import com.jme3.math.FastMath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("AccessStaticViaInstance")
public class DroneModel<T extends AbstractClientPlayer> extends PlayerModel<T> {
  private static final float rads = (float) (Math.PI / 180.0);
  private static final float degs = (float) (180.0 / Math.PI);
  public static float scale = 32;
  public static float bladeThickness = 0.002f;
  
  private boolean propsLayer;
//  private boolean showBlur = false;
  private UUID playerUuid;

  private final ModelPart root;
  private final ModelPart frame;
  private final ModelPart splitCam;
  private final ModelPart splitCamWindow;
  private final ModelPart proCam;
  private final ModelPart proCamWindow;
  private final ModelPart txaStem;
  private final ModelPart txaTip;
  private final ModelPart standoffs;
  private final ModelPart battery;
  private final PropModelPart[] blades;
  private final ModelPart[] motors;
  
  private float lastAge = 0;
//  private float motorPosition = 0;
  public DroneBuild build;
  public DroneRenderer renderer;

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.ZERO);
    PartDefinition partdefinition2 = partdefinition1.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 64).addBox(0.0F, 0.0F, 0.0F, 10.0F, 2.0F, 10.0F), PartPose.offset(-5.0F, -10.03125F, -5.0F));
    PartDefinition partdefinition3 = partdefinition2.addOrReplaceChild("hat2", CubeListBuilder.create().texOffs(0, 76).addBox(0.0F, 0.0F, 0.0F, 7.0F, 4.0F, 7.0F), PartPose.offsetAndRotation(1.75F, -4.0F, 2.0F, -0.05235988F, 0.0F, 0.02617994F));
    PartDefinition partdefinition4 = partdefinition3.addOrReplaceChild("hat3", CubeListBuilder.create().texOffs(0, 87).addBox(0.0F, 0.0F, 0.0F, 4.0F, 4.0F, 4.0F), PartPose.offsetAndRotation(1.75F, -4.0F, 2.0F, -0.10471976F, 0.0F, 0.05235988F));
    partdefinition4.addOrReplaceChild("hat4", CubeListBuilder.create().texOffs(0, 95).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(1.75F, -2.0F, 2.0F, -0.20943952F, 0.0F, 0.10471976F));
    PartDefinition partdefinition5 = partdefinition1.getChild("nose");
    partdefinition5.addOrReplaceChild("mole", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 3.0F, -6.75F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.25F)), PartPose.offset(0.0F, -2.0F, 0.0F));
    return LayerDefinition.create(meshdefinition, 64, 128);
  }

  public static LayerDefinition createDroneLayer(DroneBuild build, boolean propsLayer) {
    float frameWidth = build.frameWidth;
    float frameHeight = build.frameHeight;
    float frameLength = build.frameLength;
    float motorWidth = build.motorWidth;
    float motorHeight = build.motorHeight;
    float batteryHeight = build.getBatteryHeight();
    float batteryWidth = build.getBatteryWidth();
    float batteryLength = build.getBatteryLength();
    int nBlades = build.nBlades;
    float bladeLength = build.bladeLength;
    float bladeWidth = build.bladeWidth;
    float armWidth = build.armWidth;
    float armThickness = build.armThickness;
    float txaLength = build.txaLength;
    boolean showProCam = build.showProCam;
    boolean isHeroCam = build.isHeroCam;
    boolean isToothpick = build.isToothpick;

    float armLength = build.getArmLength(bladeLength, frameWidth);

    float plateThickness = 0.005f;

    float standoffWidth = 0.005f;
    float standoffHeight = frameHeight - plateThickness * 2f;
    float nStandoffs = build.getNStandoffs(frameLength);

    Main.LOGGER.debug("nStandoffs: " + nStandoffs);

    boolean bottomBattery = nStandoffs < 8;

    if (bottomBattery) {
      showProCam = false;
    }

    float splitCamWidth = build.getSplitCamSize(frameWidth);
    float splitCamDepth = splitCamWidth / 2f;
    float splitCamTeleWidth = splitCamWidth * 0.5f;
    float splitCamTeleDepth = splitCamDepth * 0.5f;

    float proCamWidth = 0.038f;
    float proCamDepth = 0.036f;
    float proCamTeleWidth = 0.030f;
    float proCamTeleDepth = 0.001f;

    float heroWindowWidth = 0.030f;
    float heroWindowDepth = 0.008f;
    float heroCamWidth = 0.062f;
    float heroCamHeight = 0.0445f;
    float heroCamDepth = 0.032f - heroWindowDepth;

    float stackHeight = 0.015f;
    float stackWidth = build.getStackSize(frameLength);
    float stackLength = build.getStackSize(frameLength);

    float txaWidth = 0.005f;
    float txaHeight = 0.005f;

    float txaTipLength = 0.017f;
    float txaTipWidth = 0.013f;
    float txaTipHeight = 0.013f;

    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    PartDefinition partdefinition2 = partdefinition.addOrReplaceChild("frame", CubeListBuilder.create()
      // top plate
      .texOffs(0, 0).addBox(
        -frameWidth / 2f * scale,
        -plateThickness * scale,
        -frameLength / 2f * scale,
        frameWidth * scale,
        plateThickness * scale,
        frameLength * scale,
        false)
      // bottom plate
      .texOffs(0, 0).addBox(
        -frameWidth / 2f * scale,
        -frameHeight * scale,
        -frameLength / 2f * scale,
        frameWidth * scale,
        plateThickness * scale,
        frameLength * scale,
        false
      )
      // stack
      .texOffs(0, 0).addBox(
        -stackWidth / 2f * scale,
        -(stackHeight + armThickness) * scale,
        -stackLength / 2f * scale,
        stackWidth * scale,
        stackHeight * scale,
        stackLength * scale,
        false
      ),
      PartPose.offset(0.0F, 24.0F, 0.0F)
    );
    CubeListBuilder builder = CubeListBuilder.create()
      // front stand-offs
      .texOffs(0, 0).addBox(
        -(frameWidth / 2f) * scale,
        -(standoffHeight + plateThickness) * scale,
        -(frameLength / 2f) * scale,
        standoffWidth * scale,
        standoffHeight * scale,
        standoffWidth * scale,
        false
      )
      .texOffs(0, 0).addBox(
        -(-frameWidth / 2f + standoffWidth) * scale,
        -(standoffHeight + plateThickness) * scale,
        -(frameLength / 2f) * scale,
        standoffWidth * scale,
        standoffHeight * scale,
        standoffWidth * scale,
        false
      )
      // back stand-offs
      .texOffs(0, 0).addBox(
        -(frameWidth / 2f) * scale,
        -(standoffHeight + plateThickness) * scale,
        (frameLength / 2f - standoffWidth) * scale,
        standoffWidth * scale,
        standoffHeight * scale,
        standoffWidth * scale,
        false
      )
      .texOffs(0, 0).addBox(
        -(-frameWidth / 2f + standoffWidth) * scale,
        -(standoffHeight + plateThickness) * scale,
        (frameLength / 2f - standoffWidth) * scale,
        standoffWidth * scale,
        standoffHeight * scale,
        standoffWidth * scale,
        false
      );
    if (nStandoffs > 4) {
      // middle stand-offs
      builder.texOffs(0, 0).addBox(
          -(frameWidth / 2f) * scale,
          -(standoffHeight + plateThickness) * scale,
          -(frameLength / 4f) * scale,
          standoffWidth * scale,
          standoffHeight * scale,
          standoffWidth * scale,
          false
      ).texOffs(0, 0).addBox(
          -(-frameWidth / 2f + standoffWidth) * scale,
          -(standoffHeight + plateThickness) * scale,
          -(frameLength / 4f) * scale,
          standoffWidth * scale,
          standoffHeight * scale,
          standoffWidth * scale,
          false
      ).texOffs(0, 0).addBox(
          -(frameWidth / 2f) * scale,
          -(standoffHeight + plateThickness) * scale,
          (frameLength / 4f - standoffWidth) * scale,
          standoffWidth * scale,
          standoffHeight * scale,
          standoffWidth * scale,
          false
      ).texOffs(0, 0).addBox(
          -(-frameWidth / 2f + standoffWidth) * scale,
          -(standoffHeight + plateThickness) * scale,
          (frameLength / 4f - standoffWidth) * scale,
          standoffWidth * scale,
          standoffHeight * scale,
          standoffWidth * scale,
          false
      );
    }
    partdefinition.addOrReplaceChild("standoffs", builder, PartPose.offset(0.0F, 24.0F, 0.0F));
    builder = CubeListBuilder.create();
    if (bottomBattery) {
      builder.texOffs(0, 0).addBox(
        -batteryLength / 2f * scale,
        0,
        -batteryWidth / 2f * scale,
        batteryLength * scale,
        batteryHeight * scale,
        batteryWidth * scale,
        false
      );
    } else {
      builder.texOffs(0, 0).addBox(
        -batteryWidth / 2f * scale,
        -(batteryHeight + frameHeight) * scale,
        -(batteryLength - frameLength / 4f + standoffWidth) * scale,
        batteryWidth * scale,
        batteryHeight * scale,
        batteryLength * scale,
        false
      );
    }
    partdefinition.addOrReplaceChild("battery", builder, PartPose.offset(0.0F, 24.0F, 0.0F));
    partdefinition2.addOrReplaceChild("tx_antenna_stem", builder
      .texOffs(0, 0).addBox(
        -txaWidth / 2f * scale,
        -txaHeight * scale,
        -txaLength * scale,
        txaWidth * scale,
        txaHeight * scale,
        txaLength * scale,
        false
      ), PartPose.offset(
        0.0f,
        -(frameHeight - plateThickness) * scale,
        -frameLength / 2.0f * scale
      )
    );
    partdefinition.addOrReplaceChild("tx_antenna_tip", builder
      .texOffs(0, 0).addBox(
        -txaTipWidth / 2f * scale,
        -(txaTipHeight - txaTipHeight / 2f + txaWidth / 2f) * scale,
        -(txaTipLength + txaLength) * scale,
        txaTipWidth * scale,
        txaTipHeight * scale,
        txaTipLength * scale,
        false
      ), PartPose.offset(
        0,
        24 - (frameHeight - plateThickness) * scale,
        -frameLength / 2f * scale
      )
    );
    partdefinition.addOrReplaceChild("split_cam", builder
      .texOffs(0, 0).addBox(
        -splitCamWidth / 2f * scale,
        -(splitCamWidth - splitCamWidth / 2f) * scale,
        -splitCamDepth / 2f * scale,
        splitCamWidth * scale,
        splitCamWidth * scale,
        splitCamDepth * scale,
        false
      ), PartPose.offset(
        0.0F,
        24 - (frameHeight / 2f) * scale,
        (frameLength / 2f - standoffWidth / 2f) * scale
      )
    );
    partdefinition.addOrReplaceChild("split_cam_window", builder
      .texOffs(0, 0).addBox(
        -splitCamTeleWidth / 2f * scale,
        -(splitCamTeleWidth / 2f) * scale,
        (splitCamDepth / 2f) * scale,
        splitCamTeleWidth * scale,
        splitCamTeleWidth * scale,
        splitCamTeleDepth * scale,
        false
      ), PartPose.offset(
        0,
        24 - (frameHeight / 2f) * scale,
        (frameLength / 2f - standoffWidth / 2f) * scale
      )
    );
    if (showProCam) {
      if (!isHeroCam) {
        float cameraAngle = build.cameraAngle * rads;
        float proCamPivotNudge = FastMath.sin(cameraAngle) * proCamWidth;
        partdefinition.addOrReplaceChild("pro_cam", builder
          .texOffs(0, 0).addBox(
            -proCamWidth / 2f * scale,
            -(proCamWidth) * scale,
            0,
            proCamWidth * scale,
            proCamWidth * scale,
            proCamDepth * scale,
            false
          ), PartPose.offset(
            0.0F,
            24 - (frameHeight) * scale,
            (frameLength / 4f - standoffWidth / 2f + proCamPivotNudge) * scale
          )
        );
        partdefinition.addOrReplaceChild("pro_cam_window", builder
          .texOffs(0, 0).addBox(
            -proCamTeleWidth / 2f * scale,
            -(proCamWidth + proCamTeleWidth) / 2f * scale,
            (proCamDepth) * scale,
            proCamTeleWidth * scale,
            proCamTeleWidth * scale,
            proCamTeleDepth * scale,
            false
          ), PartPose.offset(
            0,
            24 - (frameHeight) * scale,
            (frameLength / 4f - standoffWidth / 2f + proCamPivotNudge) * scale
          )
        );
      } else {
        float cameraAngle = build.cameraAngle * rads;
        float proCamPivotNudge = FastMath.sin(cameraAngle) * heroCamHeight;
        partdefinition.addOrReplaceChild("pro_cam", builder
          .texOffs(0, 0).addBox(
            -heroCamWidth / 2f * scale,
            -(heroCamHeight) * scale,
            0,
            heroCamWidth * scale,
            heroCamHeight * scale,
            heroCamDepth * scale,
            false
          ), PartPose.offset(
            0.0F,
            24 - (frameHeight) * scale,
            (frameLength / 4f - standoffWidth / 2f + proCamPivotNudge) * scale
          )
        );
        partdefinition.addOrReplaceChild("pro_cam_window", builder
          .texOffs(0, 0).addBox(
            -heroCamWidth / 2f * scale,
            -(heroCamHeight) * scale,
            (heroCamDepth) * scale,
            heroWindowWidth * scale,
            heroWindowWidth * scale,
            heroWindowDepth * scale,
            false
          ), PartPose.offset(
            0,
            24 - (frameHeight) * scale,
            (frameLength / 4f - standoffWidth / 2f + proCamPivotNudge) * scale
          )
        );
      }
    }
    for (int motorNumber = 0; motorNumber < 4; motorNumber++) {
      float armAngle = -motorNumber * 1f / 4f * FastMath.PI * 2f;
      partdefinition2.addOrReplaceChild("arm_" + motorNumber, builder
        .texOffs(0, 0).addBox(
          -armWidth / 2f * scale,
          -armThickness * scale,
          0,
          armWidth * scale,
          armThickness * scale,
          armLength * scale,
          false
        ), PartPose.rotation(
          0.0F,
          armAngle + FastMath.HALF_PI + FastMath.QUARTER_PI,
          0.0F
        )
      );
    }
    return LayerDefinition.create(meshdefinition, 16, 16);
  }

  private DroneModel(ModelPart root, UUID uuid, DroneBuild build, boolean propsLayer) {
    super(root, false);
    this.playerUuid = uuid;
    this.build = build;
    this.propsLayer = propsLayer;
//  this.showBlur = blur;

    this.root = root;
    this.frame = root.getChild("frame");
    this.splitCam = root.getChild("split_cam");
    this.splitCamWindow = root.getChild("split_cam_window");
    this.proCam = root.getChild("pro_cam");
    this.proCamWindow = root.getChild("pro_cam_window");
    this.txaStem = frame.getChild("tx_antenna_stem");
    this.txaTip = root.getChild("tx_antenna_tip");
    this.standoffs = root.getChild("standoffs");
    this.battery = root.getChild("battery");
//  if (this.showProps && this.showBlur) {
//    blades = new PropModelPart[1 * 4];
//  } else if (this.showProps && !this.showBlur) {
    blades = new PropModelPart[build.nBlades * 4];
//  }
    motors = new ModelPart[4];

    float frameWidth = build.frameWidth;
    float motorWidth = build.motorWidth;
    float motorHeight = build.motorHeight;
    int nBlades = build.nBlades;
    float bladeLength = build.bladeLength;
    float bladeWidth = build.bladeWidth;
    float armWidth = build.armWidth;
    float armThickness = build.armThickness;
    float armLength = build.getArmLength(bladeLength, frameWidth);
    for (int motorNumber = 0; motorNumber < 4; motorNumber++) {
      motors[motorNumber] = frame.getChild("arm_" + motorNumber);

      float motorR = 0.75f;
      float motorG = 0.75f;
      float motorB = 0.75f;

      ColorModelPart motor = new ColorModelPart(
        List.of(),
        Map.of(),
        motorR,
        motorG,
        motorB,
        1f
      );
      motor.setPos(
        0.0F,
        -armThickness * scale,
        (armLength - armWidth / 2f) * scale
      );
      motors[motorNumber].children.put("motor", motor);
      setRotationAngle(motor, 0.0F, 0.0F, 0.0F);
      motor.cubes.add(new ModelPart.Cube(
        0,
        0,
        -motorWidth / 2f * scale,
        -motorHeight * scale,
        -motorWidth / 2f * scale,
        motorWidth * scale,
        motorHeight * scale,
        motorWidth * scale,
        0.0F,
        0.0F,
        0.0F,
        false,
        0 * 1.0f,
        0 * 1.0f
      ));
      float maxPitch = FastMath.atan2(
        bladeLength,
        2f * FastMath.PI * motorWidth
      ); // intentional motorWidth and not motorWidth / 2f
      float maxY = bladeWidth / 2f * FastMath.sin(maxPitch);
      float hubHeight = maxY * 2f;
      float hubWidth = bladeWidth;

      float accentR = build.red;
      float accentG = build.green;
      float accentB = build.blue;

      ColorModelPart propHub = new ColorModelPart(
        List.of(),
        Map.of(),
        accentR,
        accentG,
        accentB,
        1f
      );
      propHub.setPos(0, 0, 0);
      motor.children.put("prop_hub", propHub);
      setRotationAngle(propHub, 0.0F, 0, 0.0F);
      propHub.cubes.add(new ModelPart.Cube(
        0,
        0,
        -hubWidth / 2f * scale,
        -(motorHeight + hubHeight) * scale,
        -hubWidth / 2f * scale,
        hubWidth * scale,
        hubHeight * scale,
        hubWidth * scale,
        0.0F,
        0.0F,
        0.0F,
        false,
        0 * 1.0f,
        0 * 1.0f
      ));

//    if (this.showProps && this.showBlur) {
//      // Only render once because all blades have been baked into the texture.
//      for (int i = 0; i < 1; i++) {
//        float bladeAngle = i * 1f / nBlades * FastMath.PI * 2f;
//        PropModelPart blade = new PropModelPart(
//          this,
//          false,
//          build,
//          motorNumber
//        );
//        blades[motorNumber] = blade;
//
//        blade.setPos(0, -bladeThickness * scale, 0);
//        motor.addChild(blade);
//        setRotationAngle(blade, 0, bladeAngle + FastMath.PI / 4f, 0);
//        blade.texOffs(0, 0).addBox(
//          -bladeLength * scale,
//          (-motorHeight - bladeThickness) * scale,
//          -bladeLength * scale,
//          2f * bladeLength * scale,
//          bladeThickness * scale,
//          2f * bladeLength * scale,
//          0.0F,
//          false
//        );
//      }
      if (this.propsLayer) {
        for (int i = 0; i < nBlades; i++) {
          float bladeAngle = i * 1f / nBlades * FastMath.PI * 2f;
          PropModelPart blade = new PropModelPart(
            List.of(),
            Map.of(),
            build,
            motorNumber
          );
          blades[motorNumber * nBlades + i] = blade;

          blade.setPos(0, -bladeThickness * scale, 0);
          motor.children.put("blade", blade);
          setRotationAngle(blade, 0, bladeAngle + FastMath.PI / 4f, 0);
          blade.cubes.add(new ModelPart.Cube(
            0,
            0,
            -bladeWidth / 2f * scale,
            (-motorHeight - bladeThickness) * scale,
            0,
            bladeWidth * scale,
            bladeThickness * scale,
            bladeLength * scale,
            0.0F,
            0.0F,
            0.0F,
            false,
            0 * 1.0f,
            0 * 1.0f
          ));
        }
      }
    }
  }

  public DroneModel(UUID uuid, DroneBuild build, boolean propsLayer) {
    this(createDroneLayer(build, propsLayer).bakeRoot(), uuid, build, propsLayer);

    this.renderType = (resourceLocation) -> {
      if (propsLayer) {
        // For some reason, even when PropModelPart is imitating the RenderType.entityCutoutNoCull,
        // Props will render as black.
        // They need to be renderered using EntityTranslucent
        return RenderType.entityTranslucentCull(resourceLocation);
      } else {
        return RenderType.entityCutoutNoCull(resourceLocation);
      }
    };
  }

  @Override
  public void setupAnim(
    T entityIn,
    float limbSwing,
    float limbSwingAmount,
    float ageInTicks,
    float netHeadYaw,
    float headPitch
  ) {
    float age = ageInTicks / 20f;
    float elapsed = age - lastAge;
    lastAge = age;
  
    float[] motorPos = DroneState.getMotorPos(entityIn.getUUID(), elapsed);
    
    // yRot does not follow right-hand rule.
    motors[0].yRot = -motorPos[0] + FastMath.PI;
    motors[1].yRot = -motorPos[1]; // For some reason this doesn't need an offest
    motors[2].yRot = -motorPos[2] + FastMath.PI;
    motors[3].yRot = -motorPos[3] + FastMath.PI;
//    motors[0].yRot = 0 + FastMath.PI;
//    motors[1].yRot = 0;
//    motors[2].yRot = 0 + FastMath.PI;
//    motors[3].yRot = 0 + FastMath.PI;
    
    if (proCam != null) {
      proCam.xRot = build.cameraAngle * rads;
      proCamWindow.xRot = build.cameraAngle * rads;
    }
    splitCam.xRot = build.cameraAngle * rads;
    splitCamWindow.xRot = build.cameraAngle * rads;
    txaStem.xRot = -(90 - build.cameraAngle) * rads;
    txaTip.xRot = -(90 - build.cameraAngle) * rads;
    
//    scale = 32;
//    splitCam.xRot = FastMath.cos(age);
  }
  
  @Override
  public void renderToBuffer(
    PoseStack matrixStack,
    VertexConsumer buffer,
    int packedLight,
    int packedOverlay,
    float red,
    float green,
    float blue,
    float alpha
  ) {
//    boolean renderProps = this.renderer.renderProps;
    
    float r = 36f / 255f;
    float g = 36f / 255f;
    float b = 36f / 255f;
    
    float glassR = 10f / 255f;
    float glassG = 10f / 255f;
    float glassB = 10f / 255f;
    
    float accentR = build.red;
    float accentG = build.green;
    float accentB = build.blue;
    
    float batteryR = 0.75f;
    float batteryG = 0.75f;
    float batteryB = 0.75f;
    
    matrixStack.pushPose();
    
    matrixStack.translate(0, 24f / 16f - 24f / scale, 0);
    matrixStack.scale(16f / scale, 16f / scale, 16f / scale);
    
    if (this.propsLayer) {
      if (blades != null) {
        for (int i = 0; i < blades.length; i++) {
          int motorNumber = blades[i].motorNumber;
            if (blades[i] != null) {
              blades[i].alpha = (1 - getBlurAlpha(this.playerUuid, motorNumber));
            }
        }
      }

      // render with alpha 0 to hide frame.
      frame.render(
        matrixStack,
        buffer,
        packedLight,
        packedOverlay,
        accentR,
        accentG,
        accentB,
        0
      );
    } else {
      if (blades != null) {
        for (int i = 0; i < blades.length; i++) {
          if (blades[i] != null) {
            blades[i].alpha = 0;
          }
        }
      }
      
      frame.render(
        matrixStack,
        buffer,
        packedLight,
        packedOverlay,
        r,
        g,
        b,
        1f
      );
      splitCam.render(
        matrixStack,
        buffer,
        packedLight,
        packedOverlay,
        r,
        g,
        b,
        1f
      );
      splitCamWindow.render(
        matrixStack,
        buffer,
        packedLight,
        packedOverlay,
        glassR,
        glassG,
        glassB,
        1f
      );
      if (proCam != null) {
        proCam.render(
          matrixStack,
          buffer,
          packedLight,
          packedOverlay,
          r,
          g,
          b,
          1f
        );
        proCamWindow.render(
          matrixStack,
          buffer,
          packedLight,
          packedOverlay,
          glassR,
          glassG,
          glassB,
          1f
        );
      }
      txaTip.render(
        matrixStack,
        buffer,
        packedLight,
        packedOverlay,
        batteryR,
        batteryG,
        batteryB,
        1f
      );
      standoffs.render(
        matrixStack,
        buffer,
        packedLight,
        packedOverlay,
        accentR,
        accentG,
        accentB,
        1f
      );
      battery.render(
        matrixStack,
        buffer,
        packedLight,
        packedOverlay,
        batteryR,
        batteryG,
        batteryB,
        1f
      );
    }
    
    matrixStack.popPose();
  }
  
  public void setRotationAngle(
    ModelPart modelRenderer,
    float x,
    float y,
    float z
  ) {
    modelRenderer.xRot = x;
    modelRenderer.yRot = y;
    modelRenderer.zRot = z;
  }
  
  public static float getBlurAlpha(UUID uuid, int motorNumber) {
    DroneState last = DroneState.lastMap.getOrDefault(uuid, null);
    if (last == null) {
      return 0;
    }
    
    float[] motorVel = last.motorVel;
    
    if (motorNumber >= motorVel.length) {
      return 0;
    }
  
    float vel = FastMath.abs(motorVel[motorNumber]);
    if (vel < 30) {
      return 0;
    } else {
      return 1;
    }
  }
}
