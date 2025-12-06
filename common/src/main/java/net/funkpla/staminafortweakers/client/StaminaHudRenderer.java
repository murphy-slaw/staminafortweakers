package net.funkpla.staminafortweakers.client;

import java.util.Objects;
import me.shedaniel.math.Color;
import net.funkpla.staminafortweakers.Common;
import net.funkpla.staminafortweakers.Exhaustible;
import net.funkpla.staminafortweakers.IconManager;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class StaminaHudRenderer {
  private static final int SmoothedTicks = 5;
  private double smoothed;

  public StaminaHudRenderer() {
    this.smoothed = 0;
  }

  private float getDisplayStamina(LocalPlayer player) {
    double curStamina = player.getAttributeValue(Services.REGISTRY.getStaminaAttribute());
    double maxStamina = player.getAttributeValue(Services.REGISTRY.getMaxStaminaAttribute());
    if (Double.compare(curStamina, 1d) <= 0) smoothed = 0d;
    else if (Double.compare(curStamina, maxStamina) >= 0) smoothed = curStamina;
    else
      this.smoothed =
          (this.smoothed * (double) (SmoothedTicks - 1) / SmoothedTicks)
              + curStamina / SmoothedTicks;
    return (float) (this.smoothed / maxStamina);
  }

  public void renderHud(GuiGraphics context, float ignoredTickDelta, Minecraft client) {
    StaminaConfig config = Common.getConfig();
    int width = context.guiWidth();
    int height = context.guiHeight();
    double scale = client.getWindow().getGuiScale();

    LocalPlayer player = Objects.requireNonNull(Minecraft.getInstance().player);
    if (client.options.hideGui || player.isCreative() || player.isSpectator()) return;

    float displayStamina = getDisplayStamina(player);
    Color color;

    if (((Exhaustible) player).shouldExhaust()) {
      color = getColor(displayStamina * 100);
    } else {
      color = Color.ofOpaque(config.staminaBarTirelessColor);
      displayStamina = 1.0f;
    }

    if (config.hudMode == StaminaConfig.HudMode.BAR) {
      renderStaminaBar(context, height, width, displayStamina, color);
    } else {
      renderStaminaIcon(context, player, scale, height, width, displayStamina, color);
    }
  }

  private void renderStaminaIcon(
      GuiGraphics context,
      @NotNull LocalPlayer player,
      double scale,
      int height,
      int width,
      float displayStamina,
      Color color) {

    StaminaConfig config = Common.getConfig();
    int y1;
    int x1;
    ResourceLocation icon;
    ResourceLocation bgIcon;
    if (player.isSprinting()) {
      icon = Common.locate("textures/stamina/sprint.png");
      bgIcon = Common.locate("textures/stamina/sprint_background.png");
    } else {
      icon = Common.locate("textures/stamina/walk.png");
      bgIcon = Common.locate("textures/stamina/walk_background.png");
    }
    Vector2i iconDimensions = IconManager.getIconSize(icon);

    int iconHeight = config.icon.height;
    int imageWidth = iconDimensions.x;
    int imageHeight = iconDimensions.y;
    float iconProportions = ((float) imageWidth / (float) imageHeight);
    int iconWidth = (int) (iconHeight * iconProportions);

    switch (config.alignV) {
      case TOP -> y1 = (int) (config.icon.offsetY / scale);
      case CENTER -> y1 = height / 2 - config.icon.offsetY - iconHeight / 2;
      default -> y1 = height - config.icon.offsetY - iconHeight;
    }

    switch (config.alignH) {
      case LEFT -> x1 = (int) (config.icon.offsetX / scale);
      case CENTER -> {
        boolean hasOffhandItem = !player.getOffhandItem().isEmpty();
        int offset = width / 2 - config.icon.offsetX - iconWidth / 2;
        if (hasOffhandItem && config.icon.offsetY < 27 && offset + 27 > 90) {
          x1 = offset - 27;
        } else {
          x1 = offset;
        }
      }
      default -> x1 = width - config.icon.offsetX - iconWidth;
    }

    Color bg = Color.ofOpaque(config.staminaBarBackgroundColor);

    setRenderColor(context, bg);
    context.blit(bgIcon, x1, y1, 0, 0, iconWidth, iconHeight, iconWidth, iconHeight);
    context.setColor(1f, 1f, 1f, 1f);

    int cutout = (int) Math.ceil(iconHeight * displayStamina);
    int remainder = iconHeight - cutout;

    setRenderColor(context, color);
    context.blit(icon, x1, y1 + remainder, 0, remainder, iconWidth, cutout, iconWidth, iconHeight);
    context.setColor(1f, 1f, 1f, 1f);
  }

  private void renderStaminaBar(
      GuiGraphics context, int height, int width, float displayStamina, Color color) {
    StaminaConfig config = Common.getConfig();
    int x1;
    int y1;
    int barWidth;
    int barHeight;

    if (Objects.requireNonNull(config.bar.orientation) == StaminaConfig.Orientation.VERTICAL) {
      barWidth = config.bar.shortSide;
      barHeight = config.bar.longSide;
    } else {
      barWidth = config.bar.longSide;
      barHeight = config.bar.shortSide;
    }

    switch (config.alignV) {
      case TOP -> y1 = config.bar.offsetY;
      case CENTER -> y1 = height / 2 - config.bar.offsetY - barHeight / 2;
      default -> y1 = height - config.bar.offsetY - barHeight;
    }

    switch (config.alignH) {
      case LEFT -> x1 = config.bar.offsetX;
      case CENTER -> x1 = width / 2 - config.bar.offsetX - barWidth / 2;
      default -> x1 = width - config.bar.offsetX - barWidth;
    }
    int x2 = x1 + barWidth;
    int y2 = y1 + barHeight;

    context.fill(
        x1 - 1,
        y1 - 1,
        x2 + 1,
        y2 + 1,
        -1,
        Color.ofOpaque(config.staminaBarOutlineColor).getColor());
    context.fill(
        x1,
        y1,
        x2,
        y2,
        -1,
        me.shedaniel.math.Color.ofOpaque(config.staminaBarBackgroundColor).getColor());

    if (config.bar.orientation == StaminaConfig.Orientation.HORIZONTAL)
      context.fill(
          x1, y1, (int) Math.floor(x1 + (barWidth * displayStamina)), y2, -1, color.getColor());
    else
      context.fill(
          x1, y2, x2, (int) Math.floor(y2 - (barHeight * displayStamina)), -1, color.getColor());
  }

  private @NotNull Color getColor(float pct) {
    StaminaConfig config = Common.getConfig();
    if (pct <= Math.max(config.windedPercentage / 2.0F, 10F)) {
      return Color.ofOpaque(config.staminaBarNearlyExhaustedColor);
    }
    if (pct <= config.windedPercentage) {
      return Color.ofOpaque(config.staminaBarWindedColor);
    }
    if (pct <= config.fatiguedPercentage) {
      return Color.ofOpaque(config.staminaBarFatiguedColor);
    }
    return Color.ofOpaque(config.staminaBarColor);
  }

  private float[] colorToRGBA(Color color) {
    return new float[] {
      color.getRed() / 255f,
      color.getGreen() / 255f,
      color.getBlue() / 255f,
      color.getAlpha() / 255f
    };
  }

  private void setRenderColor(GuiGraphics context, Color color) {
    float[] rgba = colorToRGBA(color);
    context.setColor(rgba[0], rgba[1], rgba[2], rgba[3]);
  }
}
