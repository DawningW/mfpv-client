package com.gluecode.fpvdrone.gui;

import com.gluecode.fpvdrone.render.CameraManager;
import com.gluecode.fpvdrone.Main;
import com.gluecode.fpvdrone.gui.screen.wizard.*;
import com.gluecode.fpvdrone.gui.widget.FpvSettingsButton;
import com.gluecode.fpvdrone.gui.screen.*;
import com.gluecode.fpvdrone.input.ControllerReader;
import com.gluecode.fpvdrone.util.SettingsLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Main.MOD_ID)
public class GuiEvents {
  public static final Field gameSettings = ObfuscationReflectionHelper.findField(
    OptionsSubScreen.class,
    "f_96282_"
  );

  @SubscribeEvent
  public static void onOpenGui(ScreenEvent.InitScreenEvent.Post event) {
    if (event.getScreen() instanceof ControlsScreen) {
      ControlsScreen screen = (ControlsScreen) event.getScreen();
      int screenWidth = screen.width;
      event.addListener(new FpvSettingsButton((screenWidth / 2) +
                                            5 +
                                            150 +
                                            4, 18,
        button -> GuiEvents.enterFpvSettings(screen)
      ));
    }
  }

  @SubscribeEvent
  public static void onRenderHand(RenderHandEvent event) {
    if (ControllerReader.getArm()) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public static void onRenderGameOverlay(RenderGameOverlayEvent.PreLayer event) {
    if (ControllerReader.getArm()) {
      if (
        event.getOverlay() == ForgeIngameGui.HOTBAR_ELEMENT
        // event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE
        // event.getType() == RenderGameOverlayEvent.ElementType.FOOD
      ) {
        event.setCanceled(true);
      } else if (event.getOverlay() == ForgeIngameGui.CROSSHAIR_ELEMENT &&
                 !CameraManager.getShowCrosshairs()) {
        event.setCanceled(true);
      }
    }
  }
  
  public static void enterFpvSettings(ControlsScreen screen) {
    if (SettingsLoader.firstTimeSetup) {
      openWelcomeScreen(screen);
    } else {
      openFpvSettingsScreen(screen);
    }
  }
  
  public static void openWelcomeScreen(Screen screen) {
    Minecraft.getInstance().setScreen(new WelcomeScreen(
      screen
    ));
  }
  
  public static void openDecideControllerScreen(WelcomeScreen screen) {
    Minecraft.getInstance().setScreen(new DecideControllerScreen(
      screen
    ));
  }
  
  public static void openChooseControllerScreen(DecideControllerScreen screen) {
    Minecraft.getInstance().setScreen(new ChooseControllerScreen(
      screen
    ));
  }
  
  public static void openCalibrateControllerStickScreen(ChooseControllerScreen screen) {
    Minecraft.getInstance().setScreen(new CalibrateControllerStickScreen(
      screen
    ));
  }
  
  public static void openCalibrateControllerArmScreen(CalibrateControllerStickScreen screen) {
    Minecraft.getInstance().setScreen(new CalibrateControllerArmScreen(
      screen
    ));
  }
  
  public static void openCompleteSwitchScreen(Screen screen) {
    Minecraft.getInstance().setScreen(new CompleteScreen(
      screen,
      CompleteScreen.Mode.SWITCH
    ));
  }
  
  public static void openCompleteKeyScreen(Screen screen) {
    Minecraft.getInstance().setScreen(new CompleteScreen(
      screen,
      CompleteScreen.Mode.KEY
    ));
  }
  
  public static void openCalibrateKeyboardScreen(DecideControllerScreen screen) {
    Minecraft.getInstance().setScreen(new CalibrateKeyboardScreen(
      screen
    ));
  }

  public static void openFpvSettingsScreen(Screen screen) {
    openModelSettingsScreen(screen);
//    Minecraft.getInstance().setScreen(new MainSettingsScreen(
//      screen
//    ));
  }

  public static void openControllerChoicesScreen(Screen screen) {
    Minecraft.getInstance().setScreen(new ControllerChoicesScreen(
      screen
    ));
  }

  public static void openChannelMappingScreen(ModelSettingsScreen screen) {
    Minecraft.getInstance().setScreen(new ChannelMappingScreen(
      screen
    ));
  }

  public static void openRatesScreen(ModelSettingsScreen screen) {
    Minecraft.getInstance().setScreen(new RatesScreen(
      screen
    ));
  }

  public static void openDroneBuildScreen(ModelSettingsScreen screen) {
    Minecraft.getInstance().setScreen(new DroneBuildScreen(
      screen
    ));
  }

  public static void openModelChoicesScreen(Screen screen) {
    Minecraft.getInstance().setScreen(new ModelChoicesScreen(
      screen
    ));
  }

  public static void openModelSettingsScreen(Screen screen) {
    Minecraft.getInstance().setScreen(new ModelSettingsScreen(
      screen
    ));
  }

  public static void openMiscSettingsScreen(ModelSettingsScreen screen) {
    Minecraft.getInstance().setScreen(new OtherSettingsScreen(
      screen
    ));
  }
}
