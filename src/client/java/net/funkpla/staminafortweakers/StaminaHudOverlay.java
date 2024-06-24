package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;

public class StaminaHudOverlay implements HudRenderCallback {
    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();
    private static final int opaque = 0xFF000000;


    @Override
        public void onHudRender(GuiGraphics context, DeltaTracker tickCounter) {
        int width = context.guiWidth();
        int height = context.guiHeight();
        Minecraft client = Minecraft.getInstance();
        double scale = client.getWindow().getGuiScale();

        LocalPlayer player = Minecraft.getInstance().player;
        if (player.isCreative() || player.isSpectator()) return;
        float scaledStamina = (float) (player.getAttributeValue(StaminaForTweakers.STAMINA) / player.getAttributeValue(StaminaForTweakers.MAX_STAMINA));
        float pct = scaledStamina * 100;

        int color = getColor(pct);

        int x1 = 0;
        int y1 = 0;

        if (config.hudMode == StaminaConfig.HudMode.BAR) {
            int barWidth = 0;
            int barHeight = 0;

            switch (config.bar.orientation) {
                case VERTICAL -> {
                    barWidth = config.bar.shortSide;
                    barHeight = config.bar.longSide;
                }
                case HORIZONTAL -> {
                    barWidth = config.bar.longSide;
                    barHeight = config.bar.shortSide;
                }

            }

            switch (config.alignV) {
                case TOP -> y1 = config.bar.offsetY;
                case CENTER -> y1 = height / 2 - config.bar.offsetY - barHeight / 2;
                case BOTTOM -> y1 = height - config.bar.offsetY - barHeight;
            }

            switch (config.alignH) {
                case LEFT -> x1 = config.bar.offsetX;
                case CENTER -> x1 = width / 2 - config.bar.offsetX - barWidth / 2;
                case RIGHT -> x1 = width - config.bar.offsetX - barWidth;
            }
            int x2 = x1 + barWidth;
            int y2 = y1 + barHeight;

            context.fill(x1 - 1, y1 - 1, x2 + 1, y2 + 1, -1, opaque + config.staminaBarOutlineColor);
            context.fill(x1, y1, x2, y2, -1, opaque + config.staminaBarBackgroundColor);

            if (config.bar.orientation == StaminaConfig.Orientation.HORIZONTAL)
                context.fill(x1, y1, (int) (x1 + (barWidth * scaledStamina)), y2, -1, color);
            else context.fill(x1, y2, x2, y2 - (int) (barHeight * scaledStamina), -1, color);


            if (player.hasEffect(StaminaForTweakers.TIRELESSNESS)) {
                context.renderOutline(x1 - 2, y1 - 2, barWidth + 4, barHeight + 4, opaque + config.staminaBarTirelessColor);
            }

        } else {

            ResourceLocation icon;
            ResourceLocation bgIcon;
            if (player.isSprinting()) {
                icon = StaminaForTweakers.resourceLocationOf("textures/stamina/sprint.png");
                bgIcon = StaminaForTweakers.resourceLocationOf("textures/stamina/sprint_background.png");
            } else {
                icon = StaminaForTweakers.resourceLocationOf("textures/stamina/walk.png");
                bgIcon = StaminaForTweakers.resourceLocationOf("textures/stamina/walk_background.png");
            }
            Vector2i iconDimensions = StaminaForTweakersClient.getIconSize(icon);

            int iconHeight = config.icon.height;
            int imageWidth = iconDimensions.x;
            int imageHeight = iconDimensions.y;
            float iconProportions = ((float) imageWidth / (float) imageHeight);
            int iconWidth = (int) (iconHeight * iconProportions);

            switch (config.alignV) {
                case TOP -> y1 = (int) (config.icon.offsetY / scale);
                case CENTER -> y1 = height / 2 - config.icon.offsetY - iconHeight / 2;
                case BOTTOM -> y1 = height - config.icon.offsetY - iconHeight;
            }

            switch (config.alignH) {
                case LEFT -> x1 = (int) (config.icon.offsetX / scale);
                case CENTER -> x1 = width / 2 - config.icon.offsetX - iconWidth / 2;
                case RIGHT -> x1 = width - config.icon.offsetX - iconWidth;
            }

            RGBA bg = splitColor(config.staminaBarBackgroundColor);

            context.setColor(bg.red, bg.green, bg.blue, 1);
            context.blit(
                    bgIcon,
                    x1, y1,
                    0, 0,
                    iconWidth, iconHeight,
                    iconWidth, iconHeight
            );

            int cutout = (int) (iconHeight * scaledStamina);
            int remainder = iconHeight - cutout;
            if (player.hasEffect(StaminaForTweakers.TIRELESSNESS)) {
                color = config.staminaBarTirelessColor;
            }

            RGBA split = splitColor(color);
            context.setColor(split.red, split.green, split.blue, 1);
            context.blit(
                    icon,
                    x1, y1 + remainder,
                    0, remainder,
                    iconWidth, cutout,
                    iconWidth, iconHeight
            );
            context.setColor(1, 1, 1, 1);
        }
    }

    private static RGBA splitColor(int color) {
        float alpha = ((float) (color >> 24 & 0x0ff) / 255);
        float red = ((float) (color >> 16 & 0x0ff) / 255);
        float green = ((float) (color >> 8 & 0x0ff) / 255);
        float blue = ((float) (color & 0x0ff) / 255);
        return new RGBA(red, green, blue, 1.0F);
    }

    private record RGBA(float red, float green, float blue, float alpha) {
    }

    private int getColor(float pct) {
        int color = opaque;
        if (pct <= Math.max(config.windedPercentage / 2.0F, 10F)) {
            color += config.staminaBarNearlyExhaustedColor;
        } else if (pct <= config.windedPercentage) {
            color += config.staminaBarWindedColor;
        } else if (pct <= config.fatiguedPercentage) {
            color += config.staminaBarFatiguedColor;
        } else {
            color += config.staminaBarColor;
        }
        return color;
    }
}