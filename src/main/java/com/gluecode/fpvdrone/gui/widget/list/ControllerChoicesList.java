package com.gluecode.fpvdrone.gui.widget.list;

import com.gluecode.fpvdrone.gui.entry.SingleButtonEntry;
import com.gluecode.fpvdrone.input.ControllerReader;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class ControllerChoicesList extends FPVList {
  public ControllerChoicesList(
    Screen parentScreen,
    Consumer<Integer> setId
  ) {
    super(parentScreen);
    
    for (int id = 0; id < 16; id++) {
      String name = ControllerReader.getControllerName(id);
      final int innerId = id;
      this.addEntry(
        new SingleButtonEntry(this, () -> {
          return ControllerReader.getControllerName(innerId);
        }, () -> {
          return !GLFW.glfwJoystickPresent(innerId);
        }, () -> {
          setId.accept(innerId);
        })
      );
    }
  }
}
