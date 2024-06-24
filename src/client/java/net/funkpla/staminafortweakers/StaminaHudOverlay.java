package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;

public class StaminaHudOverlay implements HudRenderCallback {
    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();
    private static final int opaque = 0xFF000000;


    @Override
    public void onHudRender(GuiGraphics context, float tickDelta) {
        int width = context.guiWidth();
        int height = context.guiHeight();
        Minecraft client = Minecraft.getInstance();
        double scale = client.getWindow().getGuiScale();

        LocalPlayer player = Minecraft.getInstance().player;
        if (player.isCreative() || player.isSpectator()) return;
        float scaledStamina = (float) (player.getAttributeValue(StaminaForTweakers.STAMINA) / player.getAttributeValue(StaminaForTweakers.MAX_STAMINA));
        float pct = scaledStamina * 100;

        int color = opaque;
        if (pct <= 10) {
            color += config.staminaBarNearlyExhaustedColor;
        } else if (pct <= config.windedPercentage) {
            color += config.staminaBarWindedColor;
        } else if (pct <= config.fatiguedPercentage) {
            color += config.staminaBarFatiguedColor;
        } else {
            color += config.staminaBarColor;
        }

        float red = (float) ((color >> 16 & 0x0ff) / 255);
        float green = (float) ((color >> 8 & 0x0ff) / 255);
        float blue = (float) ((color & 0x0ff) / 255);

        int x1 = 0;
        int y1 = 0;

        if (config.hudMode == StaminaConfig.HudMode.BAR) {
            int barWidth = 0;
            int barHeight = 0;

            switch (config.staminaBarConfig.orientation) {
                case VERTICAL -> {
                    barWidth = config.staminaBarConfig.hudShortSide;
                    barHeight = config.staminaBarConfig.hudLongSide;
                }
                case HORIZONTAL -> {
                    barWidth = config.staminaBarConfig.hudLongSide;
                    barHeight = config.staminaBarConfig.hudShortSide;
                }

            }

            switch (config.alignV) {
                case TOP -> y1 = config.hudOffsetY;
                case CENTER -> y1 = height / 2 - config.hudOffsetY - barHeight / 2;
                case BOTTOM -> y1 = height - config.hudOffsetY - barHeight;
            }

            switch (config.alignH) {
                case LEFT -> x1 = config.hudOffsetX;
                case CENTER -> x1 = width / 2 - config.hudOffsetX - barWidth / 2;
                case RIGHT -> x1 = width - config.hudOffsetX - barWidth;
            }
            int x2 = x1 + barWidth;
            int y2 = y1 + barHeight;

            context.fill(x1, y1, x2, y2, opaque + config.staminaBarBackgroundColor);

            if (config.staminaBarConfig.orientation == StaminaConfig.Orientation.HORIZONTAL)
                context.fill(x1, y1, (int) (x1 + (barWidth * scaledStamina)), y2, color);
            else context.fill(x1, y2, x2, y2 - (int) (barHeight * scaledStamina), color);

            context.renderOutline(x1, y1, barWidth, barHeight, opaque + config.staminaBarOutlineColor);

            if (player.hasEffect(StaminaForTweakers.TIRELESSNESS)) {
                context.renderOutline(x1 - 1, y1 - 1, barWidth + 2, barHeight + 2, opaque + config.staminaBarTirelessColor);
            }
        } else {

            int iconWidth = StaminaForTweakersClient.getCustomWidth();
            int iconHeight = StaminaForTweakersClient.getCustomHeight();

            switch (config.alignV) {
                case TOP -> y1 = config.hudOffsetY;
                case CENTER -> y1 = height / 2 - config.hudOffsetY - iconHeight / 2;
                case BOTTOM -> y1 = height - config.hudOffsetY - iconHeight;
            }

            switch (config.alignH) {
                case LEFT -> x1 = config.hudOffsetX;
                case CENTER -> x1 = width / 2 - config.hudOffsetX - iconWidth / 2;
                case RIGHT -> x1 = width - config.hudOffsetX - iconWidth;
            }

            context.setColor(0, 0, 0, 0.5F);
            context.blit(
                    StaminaForTweakersClient.CUSTOM_TEXTURE,
                    x1, y1,
                    0, 0,
                    iconWidth, iconHeight,
                    iconWidth, iconHeight
            );
            context.setColor(red, green, blue, 1);
            int cutout = (int) (iconHeight * scaledStamina);
            int remainder = iconHeight - cutout;

            context.blit(
                    StaminaForTweakersClient.CUSTOM_TEXTURE,
                    x1, y1 + remainder,
                    0, remainder,
                    iconWidth, cutout,
                    iconWidth, iconHeight
            );
            context.setColor(1, 1, 1, 1);
        }
    }
}