package net.funkpla.staminafortweakers;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;

public class StaminaHudOverlay implements HudRenderCallback {
    private final StaminaConfig config = AutoConfig.getConfigHolder(StaminaConfig.class).getConfig();
    private static final int opaque = 0xFF000000;


    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
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

        int x1 = 0;
        int y1 = 0;
        int barWidth = 0;
        int barHeight = 0;

        switch (config.orientation) {
            case VERTICAL -> {
                barWidth = config.hudShortSide;
                barHeight = config.hudLongSide;
            }
            case HORIZONTAL -> {
                barWidth = config.hudLongSide;
                barHeight = config.hudShortSide;
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

        if (config.orientation == StaminaConfig.Orientation.HORIZONTAL)
            context.fill(x1, y1, (int) (x1 + (barWidth * scaledStamina)), y2, color);
        else context.fill(x1, y2, x2, y2 - (int) (barHeight * scaledStamina), color);

        context.drawBorder(x1, y1, barWidth, barHeight, opaque + config.staminaBarOutlineColor);

        //context.drawTexture(FILLED_STAMINA, x, y1, 0, 0, (int) (BAR_LENGTH * scaledStamina), BAR_HEIGHT, BAR_LENGTH, BAR_HEIGHT);
    }
}