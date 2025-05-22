package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.math.Color;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.funkpla.staminafortweakers.config.StaminaConfig;
import net.funkpla.staminafortweakers.registry.Attributes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import java.util.Objects;

public class StaminaHudOverlay implements HudRenderCallback {
    private static final int SmoothedTicks = 5;
    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();
    private double smoothed = 0;

    private float getDisplayStamina(LocalPlayer player) {
        double curStamina = player.getAttributeValue(Attributes.STAMINA);
        smoothed = ((smoothed * (SmoothedTicks - 1)) / SmoothedTicks) + curStamina / SmoothedTicks;
        return (float) (smoothed / player.getAttributeValue(Attributes.MAX_STAMINA));
    }

    @Override
    public void onHudRender(GuiGraphics context, float tickDelta) {
        int width = context.guiWidth();
        int height = context.guiHeight();
        Minecraft client = Minecraft.getInstance();
        double scale = client.getWindow().getGuiScale();

        LocalPlayer player = Objects.requireNonNull(Minecraft.getInstance().player);
        if (client.options.hideGui || player.isCreative() || player.isSpectator())
            return;

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

    private void renderStaminaIcon(GuiGraphics context, @NotNull LocalPlayer player, double scale, int height,
                                   int width, float displayStamina, Color color) {

        int y1;
        int x1;
        ResourceLocation icon;
        ResourceLocation bgIcon;
        if (player.isSprinting()) {
            icon = StaminaMod.locate("textures/stamina/sprint.png");
            bgIcon = StaminaMod.locate("textures/stamina/sprint_background.png");
        } else {
            icon = StaminaMod.locate("textures/stamina/walk.png");
            bgIcon = StaminaMod.locate("textures/stamina/walk_background.png");
        }
        Vector2i iconDimensions = StaminaClientMod.getIconSize(icon);

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
                int offset  = width / 2 - config.icon.offsetX - iconWidth / 2;
                if (hasOffhandItem && config.icon.offsetY < 27 && offset + 27 > 90) {
                    x1 = offset - 27;
                } else {
                    x1 = offset;
                }
            }
            default -> x1 = width - config.icon.offsetX - iconWidth;

        }


        Color bg = Color.ofOpaque(config.staminaBarBackgroundColor);

        context.setColor(bg.getRed(), bg.getGreen(), bg.getBlue(), bg.getAlpha());
        context.blit(bgIcon, x1, y1, 0, 0, iconWidth, iconHeight, iconWidth, iconHeight);

        int cutout = (int) Math.ceil(iconHeight * displayStamina);
        int remainder = iconHeight - cutout;


        context.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        context.blit(icon, x1, y1 + remainder, 0, remainder, iconWidth, cutout, iconWidth, iconHeight);
        context.setColor(1, 1, 1, 1);
    }

    private void renderStaminaBar(GuiGraphics context, int height, int width, float displayStamina, Color color) {
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

        context.fill(x1 - 1, y1 - 1, x2 + 1, y2 + 1, -1, Color.ofOpaque(config.staminaBarOutlineColor).getColor());
        context.fill(x1, y1, x2, y2, -1, Color.ofOpaque(config.staminaBarBackgroundColor).getColor());

        if (config.bar.orientation == StaminaConfig.Orientation.HORIZONTAL)
            context.fill(x1, y1, (int) Math.ceil(x1 + (barWidth * displayStamina)), y2, -1, color.getColor());
        else
            context.fill(x1, y2, x2, (int) Math.ceil(y2 - (barHeight * displayStamina)), -1, color.getColor());
    }

    private @NotNull Color getColor(float pct) {
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
}
