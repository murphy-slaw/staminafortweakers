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

        if (config.orientation == StaminaConfig.Orientation.Horizontal) {
            int x1 = width / 2 - config.hudOffsetX;
            int y1 = height - config.hudOffsetY - config.hudHeight;
            int x2 = x1 + config.hudWidth;
            int y2 = y1 + config.hudHeight;
            context.fill(x1, y1, x2, y2, opaque + config.staminaBarBackgroundColor);
            context.fill(x1, y1, (int) (x1 + (config.hudWidth * scaledStamina)), y2, color);
            context.drawBorder(x1, y1, config.hudWidth, config.hudHeight, opaque + config.staminaBarOutlineColor);
        } else {
            int x1 = width / 2 - config.hudOffsetX;
            int y1 = height - config.hudOffsetY - config.hudWidth;
            int x2 = x1 + config.hudHeight;
            int y2 = y1 + config.hudWidth;
            context.fill(x1, y1, x2, y2, opaque + config.staminaBarBackgroundColor);
            context.fill(x1, y2, x2, y2 - (int) (config.hudWidth * scaledStamina), color);
            context.drawBorder(x1, y1, config.hudHeight, config.hudWidth, opaque + config.staminaBarOutlineColor);
        }

        //context.drawTexture(FILLED_STAMINA, x, y1, 0, 0, (int) (BAR_LENGTH * scaledStamina), BAR_HEIGHT, BAR_LENGTH, BAR_HEIGHT);
    }
}